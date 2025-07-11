package com.example.book.controller.request;

import com.example.book.entity.Book;

public class RegisterBookWithAuthorizationRequest {
    private String title;
    private String content;
    private String author;
    private String isbn;

    public Book toBook(Long accountId) {
        return new Book(title, content, author, isbn, accountId);
    }

    public RegisterBookWithAuthorizationRequest() {
    }

    public RegisterBookWithAuthorizationRequest(String title, String content, String author, String isbn) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.isbn = isbn;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
