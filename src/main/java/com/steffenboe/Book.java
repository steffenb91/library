package com.steffenboe;

import java.time.LocalDate;
import java.util.Iterator;

class Book extends LoanableItem {

    private String author;

    Book(String title, String author, String bookId){
        super(title, bookId);
        this.author = author;
    }

    public boolean issue(Member member) {
        this.borrower = member;
        dueDate = LocalDate.now().plusMonths(1);
        acquisitionDate = LocalDate.now();
        return true;
    }

    void placeHold(Hold hold){
        holds.add(hold);
    }

    Hold getNextHold(){
        Iterator<Hold> iterator = holds.iterator();
        while(iterator.hasNext()){
            Hold hold = iterator.next();
            iterator.remove();
            if(hold.isValid()){
                return hold;
            }
        }
        return null;
    }
}
