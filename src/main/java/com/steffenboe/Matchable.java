package com.steffenboe;

public interface Matchable<K> {

    boolean matches(K other);
}
