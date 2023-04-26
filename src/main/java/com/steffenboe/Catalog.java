package com.steffenboe;

import java.io.Serializable;

class Catalog extends ItemList<LoanableItem, String> implements Serializable {

    private static Catalog instance;

    static Catalog instance() {
        if (instance == null) {
            return new Catalog();
        }
        return instance;
    }

    boolean insertBook(LoanableItem book) {
        return add(book);
    }

    boolean removeBook(String id) {
        LoanableItem loanableItem = search(id);
        if(loanableItem != null){
            remove(loanableItem);
            return true;
        }
        return false;
    }

}
