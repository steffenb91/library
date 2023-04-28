package com.steffenboe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class ItemList<T extends Matchable<K>, K> {
    
    private List<T> elements = new LinkedList<>();

    T search(K id){
        return elements.stream().filter(element -> element.matches(id)).findFirst().orElse(null);
    }

    boolean add(T item){
        return elements.add(item);
    }

    public boolean remove(T item) {
        return elements.remove(item);
    }

    List<T> findAll(){
        return new ArrayList<>(elements); 
    }



}
