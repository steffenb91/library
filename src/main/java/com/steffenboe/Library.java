package com.steffenboe;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Iterator;

class Library implements Serializable{

    private static Library instance;
    private Catalog catalog;
    private MemberList memberList;

    private Library(Catalog catalog, MemberList memberList) {
        this.catalog = catalog;
        this.memberList = memberList;
    }

    static Library instance() {
        if (instance == null) {
            return instance = new Library(Catalog.instance(), MemberList.instance());
        }
        return instance;
    }

    public Book addBook(String title, String author, String bookId) {
        Book book = new Book(title, author, bookId);
        if (catalog.insertBook(book)) {
            return book;
        }
        return null;
    }

    public Member searchMembership(String memberId) {
        return null;
    }

    public Book issueBook(String memberId, String bookId) {
        Book book = catalog.search(bookId);
        if(book == null){
            return null;
        }
        if(book.getBorrower() != null){
            return null;
        }

        Member member = memberList.search(memberId);
        if(member == null){
            return null;
        }
        if(!book.issue(member) && member.issue(book)){
            return null;    
        }
        return null;
    }

    public Iterator<Transaction> getTransactions(String memberId, LocalDate date) {
        Member member = memberList.search(memberId);
        Iterator<Transaction> result = null;
        if(member != null){
            result = member.getTransactions(date);
        }
        return result;
    }

    public Hold holdBook(String memberId, String bookId, LocalDate date) {
        Member member = memberList.search(memberId);
        Book book = catalog.search(bookId);
        if(member != null && book != null){
            return new Hold(member, book, date);
        }
        return null;
    }

    boolean save() {
        try (FileOutputStream fos = new FileOutputStream("LibraryData")) {
            ObjectOutputStream output = new ObjectOutputStream(fos);
            try {
                output.writeObject(this);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

}
