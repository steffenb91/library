package com.steffenboe;

import java.time.LocalDate;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class Book implements Matchable<String>{

    private String title;
    private String author;
    private String bookId;
    private List<Hold> holds = new LinkedList<>();
    private LocalDate acquisitionDate;

    private Member borrower;
    private LocalDate dueDate;
    private int bookType;

    public static final int BOOK = 1;
    public static final int PERIODICAL = 2;

    Book(String title, String author, String bookId){
        this.title = title;
        this.author = author;
        this.bookId = bookId;
        this.bookType = BOOK;
    }

    Book(String title, String bookId){
        this.title = title;
        this.bookId = bookId;
        this.bookType = PERIODICAL;
        this.acquisitionDate = LocalDate.now();
    }

    String getTitle() {
        return title;
    }

    String getAuthor() {
        if(bookType == PERIODICAL){
            return "";
        }
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
        switch(bookType){
            case PERIODICAL: 
                LocalDate cutoffDate = LocalDate.now().minusMonths(3);
                if(cutoffDate.isAfter(acquisitionDate)){
                    dueDate = dueDate.plusMonths(1);
                } else {
                    return false;
                }
                break;
            default:
                dueDate = dueDate.plusMonths(1);
                break;
        }
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
