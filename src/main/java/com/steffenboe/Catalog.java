package com.steffenboe;

import java.io.Serializable;

class Catalog extends ItemList<Book, String> implements Serializable {

    private static Catalog instance;

    static Catalog instance() {
        if (instance == null) {
            return new Catalog();
        }
        return instance;
    }

    boolean insertBook(Book book) {
        return add(book);
    }

    boolean removeBook(String bookId) {
        Book book = search(bookId);
        if(book != null){
            remove(book);
            return true;
        }
        return false;
    }

}
