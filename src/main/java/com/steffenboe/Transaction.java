package com.steffenboe;

import java.io.Serializable;
import java.time.LocalDate;

class Transaction implements Serializable {
    
    private LocalDate date;
    private String type;
    private String title;

    Transaction(String type, String title){
        this.type = type;
        this.title = title;
        date = LocalDate.now();
    }

    boolean onDate(LocalDate date){
        return true;
    }

    String getType() {
        return type;
    }

    String getTitle() {
        return title;
    }

    LocalDate getDate() {
        return date;
    }

}
