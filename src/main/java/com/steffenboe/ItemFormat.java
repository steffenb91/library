package com.steffenboe;

class ItemFormat implements LoanableItemVisitor {

    @Override
    public void visit(LoanableItem loanableItem) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit' for loanable items");
    }

    @Override
    public void visit(Book book) {
        System.out.println(book.toString());
    }

    @Override
    public void visit(Periodical periodical) {
        System.out.println(periodical.toString());
    }

    

}
