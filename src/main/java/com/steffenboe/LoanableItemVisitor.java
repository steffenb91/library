package com.steffenboe;

interface LoanableItemVisitor {

    void visit(LoanableItem loanableItem);

    void visit(Book book);

    void visit(Periodical periodical);
}
