package com.steffenboe;

import java.time.LocalDate;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class Book implements Matchable<String>{

    private final String title;
    private final String author;
    private final String bookId;
    private List<Hold> holds = new LinkedList<>();
    private LocalDate acquisitionDate;

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

    LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public boolean hasHold() {
        return !holds.isEmpty();
    }

    double computeFine() {
        double fine = 0.0;
        LocalDate dueDate = getDueDate();
        if (LocalDate.now().isAfter(dueDate)) {
            LocalDate acquisitionDate = getAcquisitionDate();
            if (yearApart(acquisitionDate, dueDate)) {
                fine = 0.15 * 0.05 * daysElapsedSince(dueDate);
            } else {
                fine = 0.25 * 0.1 * daysElapsedSince(dueDate);
            }
            if (hasHold()) {
                fine *= 2;
            }
        }
        return fine;
    }

    double daysElapsedSince(LocalDate dueDate) {
        return ChronoPeriod.between(LocalDate.now(), dueDate).get(ChronoUnit.DAYS);
    }

    boolean yearApart(LocalDate acquisitionDate, LocalDate dueDate) {
        return ChronoPeriod.between(acquisitionDate, dueDate).get(ChronoUnit.YEARS) > 0L;
    }

    int checkRemovability() {
        if (hasHold()) {
            return Library.BOOK_HAS_HOLD;
        }
        if (getBorrower() != null) {
            return Library.BOOK_ISSUED;
        }
        return Library.OPERATION_COMPLETED;
    }

    @Override
    public boolean matches(String other) {
        return this.bookId.equals(bookId);
    }
    

}
