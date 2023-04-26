package com.steffenboe;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Iterator;

class Library implements Serializable {

    private static final int BOOK_NOT_FOUND = 99;
    private static final int MEMBER_NOT_FOUND = 98;
    private static final int BOOK_HAS_HOLD_FINE = 96;
    private static final int BOOK_HAS_FINE = 95;
    static final int BOOK_HAS_HOLD = 94;
    private static final int OPERATION_FAILED = 0;
    static final int OPERATION_COMPLETED = 1;
    static final int BOOK_ISSUED = 0;

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

    Book addBook(String title, String author, String bookId) {
        Book book = new Book(title, author, bookId);
        if (catalog.insertBook(book)) {
            return book;
        }
        return null;
    }

    Member searchMembership(String memberId) {
        return null;
    }

    Book issueBook(String memberId, String bookId) {
        Book book = catalog.search(bookId);
        if (book == null) {
            return null;
        }
        if (book.getBorrower() != null) {
            return null;
        }

        Member member = memberList.search(memberId);
        if (member == null) {
            return null;
        }
        if (!book.issue(member) && member.issue(book)) {
            return null;
        }
        return null;
    }

    Iterator<Transaction> getTransactions(String memberId, LocalDate date) {
        Member member = memberList.search(memberId);
        Iterator<Transaction> result = null;
        if (member != null) {
            result = member.getTransactions(date);
        }
        return result;
    }

    Hold holdBook(String memberId, String bookId, LocalDate date) {
        Member member = memberList.search(memberId);
        Book book = catalog.search(bookId);
        if (member != null && book != null) {
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

    int returnBook(String bookId) {
        Book book = catalog.search(bookId);
        if (book == null) {
            return BOOK_NOT_FOUND;
        }
        Member member = book.getBorrower();
        if (member == null) {
            return MEMBER_NOT_FOUND;
        }

        double fine = book.computeFine();

        if (!member.returnBook(book)) {
            return OPERATION_FAILED;
        }
        if (fine > 0.0) {
            member.addFine(fine, book.getTitle());
            if (book.hasHold()) {
                return BOOK_HAS_HOLD_FINE;
            } else {
                return BOOK_HAS_FINE;
            }
        }
        if (book.hasHold()) {
            return BOOK_HAS_HOLD;
        }

        return OPERATION_COMPLETED;
    }

    int removeBook(String bookId) {
        Book book = catalog.search(bookId);
        if (book == null) {
            return BOOK_NOT_FOUND;
        }
        int returnCode = book.checkRemovability();
        if (returnCode != OPERATION_COMPLETED) {
            return returnCode;
        }
        if (catalog.removeBook(bookId)) {
            return OPERATION_COMPLETED;
        }
        return OPERATION_FAILED;
    }

    Book addPeriodical(String title, String bookId) {
        Book book = new Book(title, bookId);
        if(catalog.insertBook(book)){
            return book;
        }
        return null;
    }

}
