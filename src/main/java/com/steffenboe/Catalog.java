package com.steffenboe;

import java.io.Serializable;
import java.util.LinkedList;

class Catalog implements Serializable{

    private static Catalog instance;
    private LinkedList<Book> bookCatalog = new LinkedList<>();

    static Catalog instance() {
        if(instance == null){
            return new Catalog();
        }
        return instance;
    }

    boolean insertBook(Book book) {
        return bookCatalog.add(book);
    }

    public Book search(String bookId) {
        return null;
    }

}
