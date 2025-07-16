package com.example.monoproj.board.service;

import com.example.monoproj.account.entity.*;
import com.example.monoproj.account.repository.AccountRepository;
import com.example.monoproj.account_profile.entity.AccountProfile;
import com.example.monoproj.account_profile.repository.AccountProfileRepository;
import com.example.monoproj.board.entity.Board;
import com.example.monoproj.board.repository.BoardRepository;
import com.example.monoproj.board.service.request.CreateBoardRequest;
import com.example.monoproj.board.service.response.CreateBoardResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BoardServiceTests {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountProfileRepository accountProfileRepository;

    @InjectMocks
    private BoardServiceImpl boardService;

    private Account account;
    private AccountProfile accountProfile;
    private Board board;

    public static Account createAccountWithId(Long id) {
        AccountRoleType roleType = new AccountRoleType(RoleType.NORMAL);
        AccountLoginType loginType = new AccountLoginType(LoginType.KAKAO);
        Account account = new Account(roleType, loginType);
        ReflectionTestUtils.setField(account, "id", id);
        return account;
    }

    public static AccountProfile createProfileWithId(Long id, Account account) {
        AccountProfile profile = new AccountProfile(account, "tester", "tester@email.com");
        ReflectionTestUtils.setField(profile, "id", id);
        return profile;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = createAccountWithId(100L);
        accountProfile = createProfileWithId(100L, account);

        board = new Board("기존 제목", accountProfile, "기존 내용");
        ReflectionTestUtils.setField(board, "boardId", 1L);
    }

    @Test
    void 게시글_생성_성공() {
        CreateBoardRequest request = new CreateBoardRequest("제목", 100L, "내용");

        when(accountRepository.findById(100L)).thenReturn(Optional.of(account));
        when(accountProfileRepository.findByAccount(account)).thenReturn(Optional.of(accountProfile));
        when(boardRepository.save(any(Board.class))).thenReturn(board);

        CreateBoardResponse response = boardService.register(request);

        assertEquals(board.getBoardId(), response.getBoardId());
        assertEquals(board.getTitle(), response.getTitle());
        assertEquals(board.getContent(), response.getContent());

        verify(boardRepository).save(any(Board.class));
    }
}
