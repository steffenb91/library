package com.steffenboe;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

class Member implements Matchable<String>{

    private String id = UUID.randomUUID().toString();
    private List<Book> booksBorrowed = new LinkedList<>();
    private List<Hold> booksOnHold = new LinkedList<>();
    private List<Transaction> transactions = new LinkedList<>();

    boolean issue(Book book) {
        if (booksBorrowed.add(book)) {
            transactions.add(new Transaction("Book issued", book.getTitle()));
            return true;
        }
        return false;
    }

    Iterator<Transaction> getTransactions(LocalDate date) {
        List<Transaction> result = new LinkedList<>();
        Iterator<Transaction> iterator = transactions.iterator();
        while (iterator.hasNext()) {
            Transaction tx = iterator.next();
            if (tx.onDate(date)) {
                result.add(tx);
            }
        }
        return result.iterator();
    }

    void placeHold(Hold hold) {
        transactions.add(new Transaction("Hold placed", hold.getBook().getTitle()));
        booksOnHold.add(hold);
    }

    boolean removeHold(String bookId) {
        boolean removed = false;
        Iterator<Hold> iterator = booksOnHold.iterator();
        while (iterator.hasNext()) {
            Hold hold = iterator.next();
            String id = hold.getBook().getBookId();
            if (bookId.equals(id)) {
                transactions.add(new Transaction("Hold removed", hold.getBook().getTitle()));
                removed = true;
                iterator.remove();
            }
        }
        return removed;
    }

    public boolean returnBook(Book book) {
        return false;
    }

    public void addFine(double fine, String title) {
    }

    @Override
    public boolean matches(String other) {
        return this.id.equals(other);
    }

}
