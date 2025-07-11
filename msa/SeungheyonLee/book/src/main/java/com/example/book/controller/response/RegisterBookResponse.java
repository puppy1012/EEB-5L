package com.example.book.controller.response;

import com.example.book.entity.Book;
import lombok.Getter;


public class RegisterBookResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String isbn;

    public RegisterBookResponse() {
    }

    public RegisterBookResponse(Long id, String title, String content, String author, String isbn) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.isbn = isbn;
    }

    public static RegisterBookResponse from(Book book) {
        return new RegisterBookResponse(
                book.getId(),
                book.getTitle(),
                book.getContent(),
                book.getAuthor(),
                book.getIsbn()
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
}
