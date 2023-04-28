package com.steffenboe;

import java.time.LocalDate;

class Book extends LoanableItem {

    private String author;

    Book(String title, String author, String bookId) {
        super(title, bookId);
        this.author = author;
    }

    @Override
    boolean issue(Member member) {
        if (super.issue(member)) {
            dueDate = LocalDate.now().plusMonths(1);
            return true;
        } else {
            return false;
        }
    }

    String getAuthor() {
        return author;
    }

    @Override
    void accept(LoanableItemVisitor visitor) {
        visitor.visit(this);
    }
}
