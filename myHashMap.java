/*
 * *** Joel Mesa / 271 001 ***
 *
 * This hashMap object represents an over simplification of Java's implementation of HashMap within
 * Java's Collection Framework Library. You are to complete the following methods:
 *  - remove(K)
 *  - replace(K,V)
 *  - replace(K,V,V)
 *
 * In addition to the documentation below, you can read the online Java documentation for HashMap for
 * the expected behavior / return values of each method below. This object follows the same behavior
 * of those methods implemented in this Java library.
 *
 */


/**
 *
 *  This sample code is illustrating a hash table using separate chaining. To illustrate this,
 *  the code is building a Hash Map implementation that emulates Java's HashMap class. This class
 *  implements many of the java library's class's methods and emulates the behavior of the Map
 *  interface which is what the Java Library does.
 *
 *  This class is demonstrating the use of separate chaining hashing, which is also used by
 *  Java's library class. This class is not intended to be a full-blown Hash Map / Hash Table
 *  implementation, nor does it implement all methods in Java's HashMap class. But the ones that
 *  are implemented emulate how those methods work in Java's HashMap.
 *
 *  CAVEAT: as indicated, Java provides a HashMap class that is implemented on the Map Interface
 *  that is more robust, and is more expansive than this implementation. But what is implemented
 *  operates the same way. This coding example is illustrating sample coding for how hash tables
 *  using separate chaining (versus open addressing techniques). And the behavior emulates the Map
 *  interface that Java's HashMap follows.
 *
 *  PUBLIC METHODS:
 *  ---------------
 *
 *     void  clear()               - Removes all of the mappings from this map.
 *  boolean  containsValue(V)      - Returns true if this map maps one or more keys to the specified value
 *  boolean  containsKey(K)        - Returns true if this map contains a mapping for the specified key.
 *       V   get(K)                - Returns the value to which the specified key is mapped, or null
 *                                   if this map contains no mapping for the key
 *       V   put(K, V)             - Associates the specified value with the specified key in this map
 *       V   putIfAbsent(K, V)     - If the specified key is not already associated with a value (or
 *                                   is mapped to null) associates it with the given value and returns
 *                                   null, else returns the current value
 *       V   remove(K)             - Removes the entry for the specified key only if it is currently
 *                                   mapped to the specified value
 *  boolean  remove(K, V)          - Removes the entry for the specified key only if it is currently
 *                                   mapped to the specified value.
 *  boolean  replace(K, V)         - Replaces the entry for the specified key only if it is currently
 *                                   mapped to some value
 *        V  replace(K, V1, V2)    - Replaces the entry for the specified key only if currently mapped
 *                                   to the specified value.
 *  Set<K>   keySet()              - Returns a 'Set' view of the keys contained in the map.
 *  Set<Map.Entry<K,V>> entrySet() - Returns a 'Set' view of the mappings contains in the map.
 *      int  size()                - returns the number of <k,v> pairs in hashmap
 *      boolean isEmpty()          - returns true if this map contains no key-value mappings.
 *
 *
 *  Methods *NOT* implemented to fully emulate the behavior 
 *  of Java's HashMap Class
 *      - clone()
 *      - compute()
 *      - computeIfAbsent()
 *      - computeIfPresent()
 *      - foreach()
 *      - merge(()
 *      - putAll()
 *      - replaceAll()
 *      - values()
 *
 ****************************************/

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;


/**
 * Class HashNode
 *
 * Node object representing a <Key, Value> pair stored in the Hash Map.
 * Elements hashed to the same bucket slot will be chained through a linked list.
 */
class HashNode<K, V> {
    K key;
    V value;
    HashNode<K, V> next;

    public HashNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }
}

/**
 * Implementation of a HashMap using Separate Chaining.
 * Uses an ArrayList of linked lists (chains) to store key-value pairs.
 */
class myHashMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.7f;
    private static final int INITIAL_NUM_BUCKETS = 10;

    private ArrayList<HashNode<K, V>> bucket;
    private int numBuckets;
    private int size;

    // Constructor
    public myHashMap() {
        numBuckets = INITIAL_NUM_BUCKETS;
        size = 0;
        bucket = new ArrayList<>();

        for (int i = 0; i < numBuckets; i++) {
            bucket.add(null);
        }
    }

    public int Size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the hash map by resetting the buckets.
     */
    public void clear() {
        size = 0;
        numBuckets = INITIAL_NUM_BUCKETS;
        bucket = new ArrayList<>();
        for (int i = 0; i < numBuckets; i++) {
            bucket.add(null);
        }
    }

    /**
     * Hash function to get the index of a key.
     */
    private int getBucketIndex(K key) {
        return (key.hashCode() & 0x7fffffff) % numBuckets;
    }

    /**
     * Inserts a key-value pair into the hash map.
     * Updates value if key already exists.
     */
    public V put(K key, V value) {
        int index = getBucketIndex(key);
        HashNode<K, V> head = bucket.get(index);

        // Check if key already exists, update value if found
        while (head != null) {
            if (head.key.equals(key)) {
                V oldValue = head.value;
                head.value = value;
                return oldValue;
            }
            head = head.next;
        }

        // Insert new key-value pair
        HashNode<K, V> newNode = new HashNode<>(key, value);
        newNode.next = bucket.get(index);
        bucket.set(index, newNode);
        size++;

        // Check if rehashing is needed
        if ((1.0 * size) / numBuckets > DEFAULT_LOAD_FACTOR) {
            rehash();
        }

        return null;
    }

    /**
     * Doubles the bucket size and rehashes all entries.
     */
    private void rehash() {
        ArrayList<HashNode<K, V>> oldBucket = bucket;
        numBuckets *= 2;
        size = 0;
        bucket = new ArrayList<>();

        for (int i = 0; i < numBuckets; i++) {
            bucket.add(null);
        }

        for (HashNode<K, V> headNode : oldBucket) {
            while (headNode != null) {
                put(headNode.key, headNode.value);
                headNode = headNode.next;
            }
        }
    }

    /**
     * Retrieves a value based on its key.
     */
    public V get(K key) {
        int index = getBucketIndex(key);
        HashNode<K, V> head = bucket.get(index);

        while (head != null) {
            if (head.key.equals(key)) {
                return head.value;
            }
            head = head.next;
        }
        return null;
    }

    /**
     * Removes a key-value pair from the hash map.
     */
    public V remove(K key) {
        int index = getBucketIndex(key);
        HashNode<K, V> head = bucket.get(index);
        HashNode<K, V> prev = null;

        while (head != null) {
            if (head.key.equals(key)) {
                if (prev == null) {
                    bucket.set(index, head.next);
                } else {
                    prev.next = head.next;
                }
                size--;
                return head.value;
            }
            prev = head;
            head = head.next;
        }
        return null;
    }

    /**
     * Removes a key-value pair only if it is currently mapped to the specified value.
     */
    public boolean remove(K key, V value) {
        V currentValue = get(key);
        if (currentValue == null || !currentValue.equals(value)) {
            return false;
        }
        remove(key);
        return true;
    }

    /**
     * Replaces the value of a key if it exists.
     */
    public V replace(K key, V value) {
        int index = getBucketIndex(key);
        HashNode<K, V> head = bucket.get(index);

        while (head != null) {
            if (head.key.equals(key)) {
                V oldValue = head.value;
                head.value = value;
                return oldValue;
            }
            head = head.next;
        }
        return null;
    }

    /**
     * Replaces the value of a key only if it is currently mapped to a specified old value.
     */
    public boolean replace(K key, V oldValue, V newValue) {
        int index = getBucketIndex(key);
        HashNode<K, V> head = bucket.get(index);

        while (head != null) {
            if (head.key.equals(key) && head.value.equals(oldValue)) {
                head.value = newValue;
                return true;
            }
            head = head.next;
        }
        return false;
    }

    /**
     * Checks if the hash map contains a value.
     */
    public boolean containsValue(V value) {
        for (HashNode<K, V> headNode : bucket) {
            while (headNode != null) {
                if (headNode.value.equals(value)) {
                    return true;
                }
                headNode = headNode.next;
            }
        }
        return false;
    }

    /**
     * Checks if the hash map contains a key.
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Returns a set of all key-value pairs.
     */
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> returnSet = new HashSet<>();

        for (HashNode<K, V> headNode : bucket) {
            while (headNode != null) {
                returnSet.add(Map.entry(headNode.key, headNode.value));
                headNode = headNode.next;
            }
        }

        return returnSet;
    }

    /**
     * Returns a set of all keys in the hash map.
     */
    public Set<K> keySet() {
        Set<K> returnSet = new HashSet<>();
        for (HashNode<K, V> headNode : bucket) {
            while (headNode != null) {
                returnSet.add(headNode.key);
                headNode = headNode.next;
            }
        }
        return returnSet;
    }
}


        return returnSet;
    }

} /* end class myHashMap */
