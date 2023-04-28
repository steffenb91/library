package com.steffenboe;

import java.time.LocalDate;

class Periodical extends LoanableItem {

    Periodical(String title, String itemId) {
        super(title, itemId);
    }

    @Override
    boolean issue(Member member) {
        LocalDate cutoffDate = LocalDate.now().minusMonths(3);
        if (cutoffDate.isAfter(acquisitionDate) && (super.issue(member))) {
            dueDate = dueDate.plusMonths(1);
            return true;
        }
        return false;
    }

    @Override
    void accept(LoanableItemVisitor visitor) {
        visitor.visit(this);
    }

}
