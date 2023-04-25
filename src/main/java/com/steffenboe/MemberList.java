package com.steffenboe;

import java.io.Serializable;

public class MemberList extends ItemList<Member, String> implements Serializable{

    private static MemberList instance;

    static MemberList instance() {
        if (instance == null) {
            return new MemberList();
        }
        return instance;
    }

}
