package com.raliev;

import java.util.Optional;

public interface Cache<K, V> {
    V put(K key, V value);

    Optional<V> get(K key);

    void clear();

    Optional<Integer> getAccessCountForKey(K key);

    boolean contains(K key);

    int size();
}
