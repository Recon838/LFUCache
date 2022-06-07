package com.raliev;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LFUCacheTest {

    private final Integer key1 = 1;
    private final String value1 = "a";

    private final Integer key2 = 2;
    private final String value2 = "bb";

    private final Integer key3 = 3;
    private final String value3 = "ccc";

    @Test
    void shouldReturnOptionalValue() {
        Cache<Integer, String> cache = new LFUCache<>();

        cache.put(key1, value1);
        assertEquals(value1, cache.get(key1).get());
        assertTrue(cache.contains(key1));
    }

    @Test
    void shouldReturnEmpty() {
        Cache<Integer, String> cache = new LFUCache<>();

        assertEquals(Optional.empty(), cache.get(key1));
        assertFalse(cache.contains(key1));
    }

    @Test
    void shouldReturnAccessCount() {
        Cache<Integer, String> cache = new LFUCache<>();

        assertEquals(Optional.empty(), cache.getAccessCountForKey(key1));

        cache.put(key1, value1);
        assertEquals(1, cache.getAccessCountForKey(key1).get());
    }

    @Test
    void shouldCalculateAccessCount() {
        Cache<Integer, String> cache = new LFUCache<>();

        cache.put(key1, value1);
        assertEquals(1, cache.getAccessCountForKey(key1).get());

        cache.get(key1);
        cache.get(key1);
        assertEquals(3, cache.getAccessCountForKey(key1).get());

        cache.put(key2, value2);
        cache.put(key3, value3);

        assertEquals(3, cache.size());
        assertEquals(1, cache.getAccessCountForKey(key2).get());
        assertEquals(1, cache.getAccessCountForKey(key3).get());

        cache.get(key2);

        assertEquals(2, cache.size());
        assertEquals(2, cache.getAccessCountForKey(key1).get());
        assertEquals(2, cache.getAccessCountForKey(key2).get());
        assertEquals(Optional.empty(), cache.getAccessCountForKey(key3));
    }

    @Test
    void shouldClear() {
        Cache<Integer, String> cache = new LFUCache<>();

        cache.put(key3, value3);

        assertTrue(cache.contains(key3));

        cache.clear();

        assertEquals(Optional.empty(), cache.get(key3));
        assertEquals(Optional.empty(), cache.getAccessCountForKey(key3));
    }
}
