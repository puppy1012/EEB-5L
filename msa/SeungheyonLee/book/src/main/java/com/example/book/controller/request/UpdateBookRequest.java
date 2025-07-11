package com.example.book.controller.request;

import com.example.book.entity.Book;

public class UpdateBookRequest {
    private Long bookId;
    private String title;
    private String content;

    public Book toBook(Long accountId) {
        return new Book(bookId,title, content,accountId);
    }

    public UpdateBookRequest() {
    }

    public UpdateBookRequest(Long bookId, String title, String content) {
        this.bookId = bookId;
        this.title = title;
        this.content = content;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
