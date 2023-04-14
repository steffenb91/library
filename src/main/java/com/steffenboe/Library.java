package com.steffenboe;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

class Library implements Serializable {

    private static final int BOOK_NOT_FOUND = 99;
    private static final int MEMBER_NOT_FOUND = 98;
    private static final int BOOK_HAS_HOLD_FINE = 96;
    private static final int BOOK_HAS_FINE = 95;
    private static final int BOOK_HAS_HOLD = 94;
    private static final int OPERATION_FAILED = 0;
    private static final int OPERATION_COMPLETED = 1;

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
        double fine = 0.0;
        LocalDate dueDate = book.getDueDate();
        if (LocalDate.now().isAfter(dueDate)) {
            LocalDate acquisitionDate = book.getAcquisitionDate();
            if (yearApart(acquisitionDate, dueDate)) {
                fine = 0.15 * 0.05 * daysElapsedSince(dueDate);
            } else {
                fine = 0.25 * 0.1 * daysElapsedSince(dueDate);
            }
            if (book.hasHold()) {
                fine *= 2;
            }
        }
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

    private double daysElapsedSince(LocalDate dueDate) {
        return ChronoPeriod.between(LocalDate.now(), dueDate).get(ChronoUnit.DAYS);
    }

    private boolean yearApart(LocalDate acquisitionDate, LocalDate dueDate) {
        return ChronoPeriod.between(acquisitionDate, dueDate).get(ChronoUnit.YEARS) > 0L;
    }

}
