package com.raliev;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

public class LFUCache<K, V> implements Cache<K, V> {

    private static final Integer DEFAULT_SIZE = 1000;
    private static final Integer ACCESS_UNIT = 1; // just a value in Integer pool
    private final Map<K, V> data;
    private final Map<K, LinkedList<Integer>> keyToAccessCount;

    public LFUCache() {
        this(DEFAULT_SIZE);
    }

    public LFUCache(int size) {
        data = new HashMap<>(size);
        keyToAccessCount = new HashMap<>(size);
    }

    @Override
    public V put(K key, V value) {
        data.put(key, value);
        keyToAccessCount.computeIfAbsent(key, e -> new LinkedList<>()).addLast(ACCESS_UNIT);
        return value;
    }

    @Override
    public Optional<V> get(K key) {
        V value = data.get(key);

        if (value != null) {
            incrementAccessCountForKey(key);
            decrementAccessCountForAllExceptKey(key);
            cleanIrrelevantData();
        }

        return Optional.ofNullable(value);
    }

    private void incrementAccessCountForKey(K key) {
        keyToAccessCount.get(key).addLast(ACCESS_UNIT);
    }

    private void decrementAccessCountForAllExceptKey(K key) {
        keyToAccessCount.entrySet().stream()
                .filter(e -> !e.getKey().equals(key))
                .forEach(e -> e.getValue().removeLast());
    }

    private void cleanIrrelevantData() {
        keyToAccessCount.entrySet()
                .removeIf(e -> e.getValue().size() == 0);
        data.entrySet().removeIf(e -> !keyToAccessCount.containsKey(e.getKey()));
    }

    @Override
    public void clear() {
        data.clear();
        keyToAccessCount.clear();
    }

    @Override
    public Optional<Integer> getAccessCountForKey(K key) {
        return Optional.ofNullable(keyToAccessCount.get(key))
                .map(List::size);
    }

    @Override
    public boolean contains(K key) {
        return data.containsKey(key);
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[count] : [key] : [value]");
        sb.append(System.getProperty("line.separator"));

        data.forEach((key, value) -> {
            sb.append(String.format("[%d] : %s : %s", getAccessCountForKey(key).get(), key, value));
            sb.append(System.getProperty("line.separator"));
        });

        return sb.toString();
    }
}
