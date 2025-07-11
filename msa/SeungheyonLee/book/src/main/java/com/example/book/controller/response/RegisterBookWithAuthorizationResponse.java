package com.example.book.controller.response;

import com.example.book.entity.Book;
import lombok.Getter;


public class RegisterBookWithAuthorizationResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String isbn;

    private Long accountId;

    public RegisterBookWithAuthorizationResponse() {
    }

    public RegisterBookWithAuthorizationResponse(Long id, String title, String content, String author, String isbn, Long accountId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.isbn = isbn;

        this.accountId = accountId;
    }

    public static RegisterBookWithAuthorizationResponse from(Book book) {
        return new RegisterBookWithAuthorizationResponse(
                book.getId(),
                book.getTitle(),
                book.getContent(),
                book.getAuthor(),
                book.getIsbn(),
                book.getAccountId()
        );
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public Long getAccountId() {
        return accountId;
    }
}
