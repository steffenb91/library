package com.steffenboe;

import java.time.LocalDate;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

abstract class LoanableItem implements Matchable<String> {

    protected String title;
    protected String itemId;
    protected List<Hold> holds = new LinkedList<>();
    protected LocalDate acquisitionDate;

    protected Member borrower;
    protected LocalDate dueDate;

    LoanableItem(String title, String itemId){
        this.title = title;
        this.itemId = itemId;
        this.acquisitionDate = LocalDate.now();
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

    Member getBorrower() {
        return borrower;
    }

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

    boolean hasHold() {
        return !holds.isEmpty();
    }

    abstract boolean issue(Member member);

    String getItemId() {
        return itemId;
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
        return this.itemId.equals(itemId);
    }
    
}
