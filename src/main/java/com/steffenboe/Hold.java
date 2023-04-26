package com.steffenboe;

import java.time.LocalDate;

class Hold {

    private Member member;
    private LoanableItem item;
    private LocalDate date;

    Hold(Member member, LoanableItem book, LocalDate date) {
        this.member = member;
        this.item = book;
        this.date = date;
    }

    Member getMember() {
        return member;
    }

    LoanableItem getItem() {
        return item;
    }

    LocalDate getDate() {
        return date;
    }

    boolean isValid() {
        return LocalDate.now().isBefore(date);
    }


}
