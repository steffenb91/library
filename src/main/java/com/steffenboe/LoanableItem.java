package com.steffenboe;

import java.time.LocalDate;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

abstract class LoanableItem implements Matchable<String> {

    private String title;
    private String id;
    protected Member borrowedBy;
    protected LocalDate dueDate;
    
    protected List<Hold> holds = new LinkedList<>();
    protected LocalDate acquisitionDate;

    LoanableItem(String title, String itemId){
        this.title = title;
        this.id = itemId;
        this.acquisitionDate = LocalDate.now();
    }

    int checkRemovability() {
        if (hasHold()) {
            return Library.BOOK_HAS_HOLD;
        }
        if (getBorrowedBy() != null) {
            return Library.BOOK_ISSUED;
        }
        return Library.OPERATION_COMPLETED;
    }

    Member getBorrowedBy() {
        return borrowedBy;
    }

    abstract void accept(LoanableItemVisitor visitor);

    double computeFine() {
        double fine = 0.0;
        if (LocalDate.now().isAfter(dueDate)) {
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

    String getTitle() {
        return title;
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

    boolean hasHold() {
        return !holds.isEmpty();
    }

    boolean issue(Member member){
        if(borrowedBy != null){
            return false;
        }
        dueDate = LocalDate.now();
        borrowedBy = member;
        return true;
    }

    String getId() {
        return id;
    }

    LocalDate getDueDate() {
        return dueDate;
    }

    LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    double daysElapsedSince(LocalDate dueDate) {
        return ChronoPeriod.between(LocalDate.now(), dueDate).get(ChronoUnit.DAYS);
    }

    boolean yearApart(LocalDate acquisitionDate, LocalDate dueDate) {
        return ChronoPeriod.between(acquisitionDate, dueDate).get(ChronoUnit.YEARS) > 0L;
    }

    @Override
    public boolean matches(String other) {
        return this.id.equals(id);
    }
    
}
