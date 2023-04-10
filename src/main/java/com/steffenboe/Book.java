package com.steffenboe;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class Book {

    private final String title;
    private final String author;
    private final String bookId;
    private List<Hold> holds = new LinkedList<>();

    private Member borrower;
    private LocalDate dueDate;

    Book(String title, String author, String bookId){
        this.title = title;
        this.author = author;
        this.bookId = bookId;
    }

    String getTitle() {
        return title;
    }

    String getAuthor() {
        return author;
    }

    String getBookId() {
        return bookId;
    }

    LocalDate getDueDate() {
        return dueDate;
    }

    Member getBorrower() {
        return borrower;
    }

    boolean issue(Member member) {
        this.borrower = member;
        dueDate = LocalDate.now().plusMonths(1);
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
