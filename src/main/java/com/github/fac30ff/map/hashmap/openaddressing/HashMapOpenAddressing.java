package com.github.fac30ff.map.hashmap.openaddressing;

import java.util.Arrays;
import java.util.Objects;

import static com.github.fac30ff.map.hashmap.openaddressing.TypeOfOpenAddressing.DOUBLE_HASHING;

public class HashMapOpenAddressing {
  private static final int INITIAL_CAPACITY = 16;
  private static final float LOAD_FACTOR = 0.75f;
  private static final int PRIME = 3;
  private int primeFactor;
  private int capacity;
  private int size;
  private final TypeOfOpenAddressing type;
  private Entry[] table;

  public HashMapOpenAddressing() {
    this(INITIAL_CAPACITY);
  }

  public HashMapOpenAddressing(int capacity) {
    this(capacity, DOUBLE_HASHING);
  }

  public HashMapOpenAddressing(int capacity, TypeOfOpenAddressing type) {
    this.capacity = capacity;
    table = new Entry[capacity];
    this.type = type;
    primeFactor = getPrimeFactor(capacity);
  }

  public HashMapOpenAddressing(TypeOfOpenAddressing type) {
    this(INITIAL_CAPACITY, type);
  }

  public int size() {
    return size;
  }

  public Long get(int key) {
    return get(key, this.type);
  }

  private Long get(int key, TypeOfOpenAddressing type) {
    switch (type) {
      case LINEAR_PROBING:
        return getLinearProbing(key);
      case QUADRATIC_PROBING:
        return getQuadraticProbing(key);
      case DOUBLE_HASHING:
      default:
        return getDoubleHashing(key);
    }
  }

  private Long getQuadraticProbing(int key) {
    int hash = hash(key);
    while (table[hash] != null) {
      if (table[hash].getKey() == key && table[hash].getHash() == hash) {
        return table[hash].getValue();
      }
      if (hash == capacity - 1) {
        hash = 0;
      } else {
        hash++;
        hash %= capacity;
      }
    }
    return null;
  }

  private Long getLinearProbing(int key) {
    int hash = hash(key);
    int factor = 1;
    while (table[hash] != null) {
      if (table[hash].getKey() == key && table[hash].getHash() == hash) {
        return table[hash].getValue();
      }
      if (hash == capacity - 1) {
        hash = 0;
      } else {
        hash = (int) (hash + Math.pow(factor, factor));
        factor++;
        hash %= capacity;
      }
    }
    return null;
  }

  private Long getDoubleHashing(int key) {
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
    put(key, value, this.type);
  }

  private void put(int key, long value, TypeOfOpenAddressing type) {
    if (size > (int) (capacity * LOAD_FACTOR)) {
      rearrange(Math.round((1 + LOAD_FACTOR) * capacity));
    }
    switch (type) {
      case LINEAR_PROBING:
        addLinearProbing(key, value);
        break;
      case QUADRATIC_PROBING:
        addQuadraticProbing(key, value);
        break;
      case DOUBLE_HASHING:
      default:
        addDoubleHashing(key, value);
        break;
    }
  }

  private void addDoubleHashing(int key, long value) {
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

  private void addLinearProbing(int key, long value) {
    int hash = hash(key);
    while (table[hash] != null) {
      if (table[hash].getKey() == key) {
        table[hash].setValue(value);
        return;
      }
      if (hash == capacity - 1) {
        hash = 0;
      } else {
        hash++;
        hash %= capacity;
      }
    }
    table[hash] = new Entry(hash, key, value);
    size++;
  }

  private void addQuadraticProbing(int key, long value) {
    int hash = hash(key);
    int factor = 1;
    while (table[hash] != null) {
      if (table[hash].getKey() == key) {
        table[hash].setValue(value);
        return;
      }
      if (hash == capacity - 1) {
        hash = 0;
      } else {
        hash = (int) (hash + Math.pow(factor, factor));
        factor++;
        hash %= capacity;
      }
    }
    table[hash] = new Entry(hash, key, value);
    size++;
  }

  private void rearrange(int newCap) {
    Entry[] old = table;
    table = new Entry[newCap];
    size = 0;
    this.capacity = newCap;
    for (Entry entry : old) {
      if (entry != null) {
        switch (type) {
          case LINEAR_PROBING:
            addLinearProbing(entry.getKey(), entry.getValue());
            break;
          case QUADRATIC_PROBING:
            addQuadraticProbing(entry.getKey(), entry.getValue());
            break;
          case DOUBLE_HASHING:
          default:
            addDoubleHashing(entry.getKey(), entry.getValue());
            break;
        }
      }
    }
  }

  private int getPrimeFactor(int capacity) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HashMapOpenAddressing that = (HashMapOpenAddressing) o;
    return Arrays.equals(table, that.table);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(table);
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
