package com.steffenboe;

import java.io.File;
import java.time.LocalDate;
import java.util.Iterator;

/**
 * Hello world!
 *
 */
public class App {

    private static UserInterface userInterface;

    public static void main(String[] args) {
        UserInterface.instance().process();
    }

    private static class UserInterface {

        private static final int EXIT = 0;
        private static final int ADD_MEMBER = 1;
        private static final int ADD_BOOKS = 2;
        private static final int ISSUE_BOOKS = 3;
        private static final int HELP = 4;
        private static final int PRINT_TRANSACTIONS = 5;
        private static final int HOLD_BOOK = 0;

        private Library library;

        private UserInterface() {
            File file = new File("LibraryData");
            if (file.exists() && file.canRead()) {
                if (yesOrNo("Saved data exists. Use it?")) {
                    retrieve();
                }
            }
            library = Library.instance();
        }

        private void retrieve() {
        }

        private boolean yesOrNo(String string) {
            return false;
        }

        private static UserInterface instance() {
            if (userInterface == null) {
                return userInterface = new UserInterface();
            } else {
                return userInterface;
            }
        }

        private void process() {
            int command;
            help();
            while ((command = getCommand()) != EXIT) {
                executeCommand(command);
            }
        }

        private void executeCommand(int command) {
            switch (command) {
                case ADD_MEMBER:
                    addMember();
                    break;
                case ADD_BOOKS:
                    addBooks();
                    break;
                case ISSUE_BOOKS:
                    issueBooks();
                    break;
                case PRINT_TRANSACTIONS:
                    getTransactions();
                    break;
                case HOLD_BOOK:
                    holdBook();
                    break;
                case HELP:
                    help();
                    break;
                default:
                    help();
            }
        }

        private void holdBook() {
            String memberId = getToken("Enter memberId:");
            String bookId = getToken("Enter book id:");
            LocalDate date = getDate("Enter date for hold.");
            Hold hold = library.holdBook(memberId, bookId,  date);
            if(hold != null){
                System.out.println("Hold placed successfully.");
            }
        }

        private void getTransactions() {
            Iterator<Transaction> result;
            String memberId = getToken("Enter memberId: ");
            LocalDate date = getDate("Please enter the date:");
            result = library.getTransactions(memberId, date);
            if(result == null){
                System.out.println("Invalid memberId");
            } else {
                while(result.hasNext()){
                    Transaction tx = result.next();
                    System.out.println(tx.getType() + " " + tx.getTitle() + "\\n");
                }
                System.out.println("There are noe more transactions.");
            }
        }

        private LocalDate getDate(String string) {
            return null;
        }

        private void issueBooks() {
            Book result;
            String memberId = getToken("Enter member id:");
            if(library.searchMembership(memberId) == null){
                System.out.println("No such member");
                return;
            }
            do {
                String bookId = getToken("Enter book id:");
                result = library.issueBook(memberId, bookId);
                if(result != null){
                    System.out.println(result.getTitle() + " " + result.getDueDate());
                } else {
                    System.out.println("Book could not be issued.");
                }

                if(!yesOrNo("Issue more books?")){
                    break;
                }
            } while (true);
        }

        private void addBooks() {
            Book result;
            do {
                String title = getToken("Enter book title:");
                String bookId = getToken("Enter book id:");
                if(yesOrNo("Is this a book?")){
                    String author = getToken("Enter author:");
                    result = library.addBook(title, author, bookId);
                } else {
                    result = library.addPeriodical(title, bookId);
                }
                if (result != null) {
                    System.out.println(result);
                } else {
                    System.out.println("Book could not be added.");
                }
                if (!yesOrNo("Add more books?")) {
                    break;
                }
            } while (true);
        }

        private String getToken(String string) {
            return null;
        }

        private void addMember() {
        }

        private void help() {
        }

        private int getCommand() {
            return 0;
        }

        private void save(){
            if(library.save()){
                System.out.println("Library saved.");
            } else {
                System.out.println("Saving of library failed.");
            }
        }
    }
}
