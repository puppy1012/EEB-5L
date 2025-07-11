package com.example.book.controller;

import com.example.book.controller.request.RegisterBookRequest;
import com.example.book.controller.request.RegisterBookWithAuthorizationRequest;
import com.example.book.controller.request.UpdateBookRequest;
import com.example.book.controller.response.*;
import com.example.book.entity.Book;
import com.example.book.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.book.client.AccountClient;

@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    private final AccountClient accountClient;

    public BookController(AccountClient accountClient) {
        this.accountClient = accountClient;
    }


    @GetMapping("/test")
    public String test() {
        log.info("return String from test()");
        Book book =  new Book("제목", "내용", "저자", "isbn");
        log.info("book = {}", book);
        return "Book Service";
    }

    @GetMapping("/test-book")
    public BookResponse testBook() {
        log.info("return Book from testBook()");
        BookResponse bookResponse =  new BookResponse("제목", "내용", "저자", "isbn");
        log.info("bookResponse = {}", bookResponse);
        return bookResponse;
    }

    @GetMapping("/test-book2")
    public RegisterBookResponse testBook2() {
        log.info("return Book from testBook2()");
        Book createdBook =  new Book("제목", "내용", "저자", "isbn");
        return RegisterBookResponse.from(createdBook);
    }

    @PostMapping("/register")
    public RegisterBookResponse register(@RequestBody RegisterBookRequest request) {
        log.info("register() -> request = {}", request);
        Book book = bookRepository.save(request.toBook());
        return RegisterBookResponse.from(book);
    }

    // 책 등록 API 엔드포인트 (POST /publication)
    @PostMapping("/publication")
    public RegisterBookWithAuthorizationResponse register(
            @RequestHeader("Authorization") String token,  // 클라이언트에서 전달된 Authorization 헤더 (Bearer 토큰 형식)
            @RequestBody RegisterBookWithAuthorizationRequest request) {  // 책 등록 요청에 필요한 데이터(JSON 형식)

        // 1. 요청 로그 출력 (디버깅용)
        log.info("register() -> request = {}", request);

        // 2. Authorization 헤더에서 Bearer 접두어를 제거한 실제 accessToken 추출
        String pureToken = extractToken(token);

        // 3. FeignClient를 이용해 account 서비스에 현재 로그인한 사용자의 계정 ID 요청
        //    - Header에 다시 Bearer 접두어 붙여서 전달해야 함 (ex: Bearer abc.def.ghi)
        IdAccountResponse response = accountClient.getAccountId("Bearer " + pureToken);

        // 4. 응답 객체에서 accountId 추출
        Long accountId = response.getAccountId();

        // 5. 로그 출력 (확인용)
        log.info("accountId = {}", accountId);

        // 6. 등록 요청 객체를 실제 Book 엔티티로 변환하면서 accountId도 함께 포함
        Book requestedBook = request.toBook(accountId);

        // 7. BookRepository(JPA)를 사용하여 DB에 저장
        Book registeredBook = bookRepository.save(requestedBook);

        // 8. 저장된 Book 객체를 기반으로 응답 객체 생성 후 반환
        return RegisterBookWithAuthorizationResponse.from(registeredBook);
    }
    @PostMapping("/update")
    public UpdateBookResponse register(
            @RequestHeader("Authorization")String token,
            @RequestBody UpdateBookRequest request){
        log.info("register() -> request = {}", request);
        String pureToken = extractToken(token);
        IdAccountResponse response = accountClient.getAccountId("Bearer " + pureToken);
        Long accountId = response.getAccountId();

        Long requestBookId=request.getBookId();
        Book foundBook=bookRepository.findById(requestBookId)
                .orElseThrow( ()-> new RuntimeException("이런 책은 존재하지 않습니다: " + requestBookId));
        if(!foundBook.getAccountId().equals(accountId)){
            throw new RuntimeException("책을 등록한 사람이 아닙니다");
        }
        foundBook.setTitle(request.getTitle());
        foundBook.setContent(request.getContent());

        Book updatedBook = bookRepository.save(foundBook);
        return UpdateBookResponse.from(updatedBook);
    }
    // Authorization 헤더에서 Bearer 접두어를 제거하고 토큰만 반환하는 헬퍼 메서드
    private String extractToken(String token) {
        // 예: "Bearer abc.def.ghi" → "abc.def.ghi"
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);  // "Bearer " 이후 문자열 자르기
        }
        return token;  // 접두어가 없다면 그대로 반환
    }
}
