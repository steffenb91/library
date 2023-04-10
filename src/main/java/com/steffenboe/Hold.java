package com.steffenboe;

import java.time.LocalDate;

class Hold {

    private Member member;
    private Book book;
    private LocalDate date;

    Hold(Member member, Book book, LocalDate date) {
        this.member = member;
        this.book = book;
        this.date = date;
    }

    Member getMember() {
        return member;
    }

    Book getBook() {
        return book;
    }

    LocalDate getDate() {
        return date;
    }

    boolean isValid() {
        return LocalDate.now().isBefore(date);
    }


}
