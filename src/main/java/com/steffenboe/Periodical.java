package com.steffenboe;

import java.time.LocalDate;

class Periodical extends LoanableItem {

    Periodical(String title, String itemId){
        super(title, itemId);
    }

    @Override
    public boolean issue(Member member) {
        LocalDate cutoffDate = LocalDate.now().minusMonths(3);
        if (cutoffDate.isAfter(acquisitionDate)) {
            dueDate = dueDate.plusMonths(1);
            return true;
        } else {
            return false;
        }
    }

}
