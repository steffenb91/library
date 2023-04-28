package com.steffenboe;

class LoanableItemFactory {

    private static final int BOOK = 1;
    private static final int PERIODICAL = 2;

    private static LoanableItemFactory factory;

    private LoanableItemFactory(){}

    public static LoanableItemFactory instance(){
        if(factory == null){
            factory = new LoanableItemFactory();
            return factory;
        } else {
            return factory;
        }
    }

    public LoanableItem createLoanableItem(int type, String title, String author, String id) {
        switch(type){
            case BOOK: 
                return new Book(title, author, id);
            case PERIODICAL:
                return new Periodical(title, id);
            default:
                return null;
        }
    }

}
