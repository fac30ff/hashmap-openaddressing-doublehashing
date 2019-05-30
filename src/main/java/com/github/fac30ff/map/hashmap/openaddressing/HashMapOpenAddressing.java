package com.github.fac30ff.map.hashmap.openaddressing;

import java.util.Objects;

public class HashMapOpenAddressing {
  private static final int INITIAL_CAPACITY = 16;
  private static final float LOAD_FACTOR = 0.75f;
  private static final int PRIME = 3;
  private int primeFactor;
  private int capacity;
  private int size;
  private Entry[] table;

  public HashMapOpenAddressing() {
    capacity = INITIAL_CAPACITY;
    table = new Entry[capacity];
    primeFactor = PRIME;
  }

  public HashMapOpenAddressing(int capacity) {
    this.capacity = capacity;
    table = new Entry[capacity];
    primeFactor = getPrimeFactor();
  }

  public int size() {
    return size;
  }

  public Long get(int key) {
    int hash1 = hash(key);
    int hash2 = hash2(key);
    while (table[hash1] != null) {
      if (table[hash1].getKey() == key && table[hash1].getHash() == hash1) {
        return table[hash1].getValue();
      }
      if (hash1 == capacity - 1) {
        hash1 = 0;
      } else {
        hash1 += hash2;
        hash1 %= capacity;
      }
    }
    return null;
  }

  public void put(int key, long value) {
    if (size > capacity / LOAD_FACTOR ) {
      rearrange(Math.round((1 + LOAD_FACTOR) * capacity));
    }
    add(key, value);
  }

  private void add(int key, long value) {
    int hash1 = hash(key);
    int hash2 = hash2(key);
    while (table[hash1] != null) {
      if (table[hash1].getKey() == key) {
        table[hash1].setValue(value);
        return;
      }
      if (hash1 == capacity - 1) {
        hash1 = 0;
      } else {
        hash1 += hash2;
        hash1 %= capacity;
      }
    }
    table[hash1] = new Entry(hash1, key, value);
    size++;
  }

  private void rearrange(int newCap) {
    Entry[] old = table;
    table = new Entry[newCap];
    size = 0;
    this.capacity = newCap;
    for (Entry entry : old) {
      if (entry != null) {
        add(entry.getKey(), entry.getValue());
      }
    }
  }

  private int getPrimeFactor() {
    for (int i = capacity - 1; i >= 1; i--) {
      int fact = 0;
      for (int j = 2; j <= (int) Math.sqrt(i); j++)
        if (i % j == 0)
          fact++;
      if (fact == 0)
        return i;
    }
    return PRIME;
  }

  private int hash(int key) {
    return key % capacity < 0 ? key + capacity : key % capacity;
  }

  private int hash2(int key) {
    return primeFactor - hash(key) % primeFactor;
  }

  static class Entry {
    private final int hash;
    private final int key;
    private long value;

    public Entry(int hash, int key, long value) {
      this.hash = hash;
      this.key = key;
      this.value = value;
    }

    public final int getHash() {
      return hash;
    }

    public final int getKey() {
      return key;
    }

    public final long getValue() {
      return value;
    }

    public final long setValue(long newValue) {
      long oldValue = value;
      value = newValue;
      return oldValue;
    }

    @Override
    public final String toString() {
      return "Entry{" +
              "key=" + key +
              ", value=" + value +
              '}';
    }

    @Override
    public final boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Entry entry = (Entry) o;
      return key == entry.key &&
              value == entry.value;
    }

    @Override
    public final int hashCode() {
      return Objects.hashCode(key) ^ Objects.hashCode(value);
    }
  }
}
