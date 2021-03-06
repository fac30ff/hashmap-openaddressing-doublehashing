package com.github.fac30ff.map.hashmap.openaddressing;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestHashMapOpenAddressingDoubleHashing {
  private HashMapOpenAddressing map;
  private HashMapOpenAddressing expected;

  @Nested
  class LinearProbing {
    @BeforeEach
    void setUp() {
      map = new HashMapOpenAddressing(TypeOfOpenAddressing.LINEAR_PROBING);
      expected = new HashMapOpenAddressing(TypeOfOpenAddressing.LINEAR_PROBING);
      for (int i = 0; i < 9; i++) {
        map.put(i, i * 2);
        expected.put(i, i * 2);
      }
    }
    @Test
    void putMethodShouldWorkCorrectly() {
      HashMapOpenAddressing m = new HashMapOpenAddressing();
      m.put(Integer.MAX_VALUE, Long.MIN_VALUE);
      assertThat(m.get(Integer.MAX_VALUE), is(Long.MIN_VALUE));
      assertEquals(m.get(Integer.MAX_VALUE), Long.MIN_VALUE);
    }

    @Test
    void putMethodChangeValueBySameKeyAndReturnExpectedResult() {
      HashMapOpenAddressing m = new HashMapOpenAddressing();
      m.put(20, 20L);
      m.put(20, 30L);
      assertEquals(m.get(20), 30L);
    }

    @Test
    @Order(Integer.MIN_VALUE)
    void sizeMethodShouldReturnCorrectSize() {
      assertEquals(map.size(), 9);
      assertThat(expected.size(), is(9));
    }

    @Test
    @Order(Integer.MIN_VALUE + 1)
    void sizeMethodReturnExpectedValueAfterResizing() {
      for (int i = 9; i < 22; i++) {
        map.put(i, ThreadLocalRandom.current().nextInt(0, 100));
      }
      assertEquals(map.size(), 22);
    }

    @Test
    void getMethodShouldReturnExpectedValueByKey() {
      assertEquals(map.get(1), 2);
    }

    @Test
    void getMethodReturnExpectedValueAfterResizing() {
      for (int i = 9; i < 22; i++) {
        map.put(i, ThreadLocalRandom.current().nextInt(0, 100));
      }
      assertEquals(map.get(8), 16);
    }

    @Test
    void getMethodShouldReturnNullWhenDidNotFindEntry() {
      assertNull(map.get(50));
    }

    @Test
    void getMethodCauseNPEWhenDidNotFindValueByKey() {
      assertThrows(NullPointerException.class, this::getLongFromMap);
    }

    @Test
    void mapsNotEqualByEntries() {
      expected.put(20, 100);
      assertThat(map, not(expected));
    }

    @Test
    @Disabled
    void mapsIsEqualByEntries() {
      assertThat(map, is(expected));
    }

    private Long getLongFromMap() {
      return Long.valueOf(map.get(50));
    }
  }

  @Nested
  class QuadraticProbing {
    @BeforeEach
    void setUp() {
      map = new HashMapOpenAddressing(TypeOfOpenAddressing.QUADRATIC_PROBING);
      expected = new HashMapOpenAddressing(TypeOfOpenAddressing.QUADRATIC_PROBING);
      for (int i = 0; i < 9; i++) {
        map.put(i, i * 2);
        expected.put(i, i * 2);
      }
    }
    @Test
    void putMethodShouldWorkCorrectly() {
      HashMapOpenAddressing m = new HashMapOpenAddressing();
      m.put(Integer.MAX_VALUE, Long.MIN_VALUE);
      assertThat(m.get(Integer.MAX_VALUE), is(Long.MIN_VALUE));
      assertEquals(m.get(Integer.MAX_VALUE), Long.MIN_VALUE);
    }

    @Test
    void putMethodChangeValueBySameKeyAndReturnExpectedResult() {
      HashMapOpenAddressing m = new HashMapOpenAddressing();
      m.put(20, 20L);
      m.put(20, 30L);
      assertEquals(m.get(20), 30L);
    }

    @Test
    @Order(Integer.MIN_VALUE)
    void sizeMethodShouldReturnCorrectSize() {
      assertEquals(map.size(), 9);
      assertThat(expected.size(), is(9));
    }

    @Test
    @Order(Integer.MIN_VALUE + 1)
    void sizeMethodReturnExpectedValueAfterResizing() {
      for (int i = 9; i < 22; i++) {
        map.put(i, ThreadLocalRandom.current().nextInt(0, 100));
      }
      assertEquals(map.size(), 22);
    }

    @Test
    void getMethodShouldReturnExpectedValueByKey() {
      assertEquals(map.get(1), 2);
    }

    @Test
    void getMethodReturnExpectedValueAfterResizing() {
      for (int i = 9; i < 22; i++) {
        map.put(i, ThreadLocalRandom.current().nextInt(0, 100));
      }
      assertEquals(map.get(8), 16);
    }

    @Test
    void getMethodShouldReturnNullWhenDidNotFindEntry() {
      assertNull(map.get(50));
    }

    @Test
    void getMethodCauseNPEWhenDidNotFindValueByKey() {
      assertThrows(NullPointerException.class, this::getLongFromMap);
    }

    @Test
    void mapsNotEqualByEntries() {
      expected.put(20, 100);
      assertThat(map, not(expected));
    }

    @Test
    @Disabled
    void mapsIsEqualByEntries() {
      assertThat(map, is(expected));
    }

    private Long getLongFromMap() {
      return Long.valueOf(map.get(50));
    }
  }

  @Nested
  class DoubleHashing {
    @BeforeEach
    void setUp() {
      map = new HashMapOpenAddressing(TypeOfOpenAddressing.DOUBLE_HASHING);
      expected = new HashMapOpenAddressing(TypeOfOpenAddressing.DOUBLE_HASHING);
      for (int i = 0; i < 9; i++) {
        map.put(i, i * 2);
        expected.put(i, i * 2);
      }
    }

    @Test
    void putMethodShouldWorkCorrectly() {
      HashMapOpenAddressing m = new HashMapOpenAddressing();
      m.put(Integer.MAX_VALUE, Long.MIN_VALUE);
      assertThat(m.get(Integer.MAX_VALUE), is(Long.MIN_VALUE));
      assertEquals(m.get(Integer.MAX_VALUE), Long.MIN_VALUE);
    }

    @Test
    void putMethodChangeValueBySameKeyAndReturnExpectedResult() {
      HashMapOpenAddressing m = new HashMapOpenAddressing();
      m.put(20, 20L);
      m.put(20, 30L);
      assertEquals(m.get(20), 30L);
    }

    @Test
    @Order(Integer.MIN_VALUE)
    void sizeMethodShouldReturnCorrectSize() {
      assertEquals(map.size(), 9);
      assertThat(expected.size(), is(9));
    }

    @Test
    @Order(Integer.MIN_VALUE + 1)
    void sizeMethodReturnExpectedValueAfterResizing() {
      for (int i = 9; i < 22; i++) {
        map.put(i, ThreadLocalRandom.current().nextInt(0, 100));
      }
      assertEquals(map.size(), 22);
    }

    @Test
    void getMethodShouldReturnExpectedValueByKey() {
      assertEquals(map.get(1), 2);
    }

    @Test
    void getMethodReturnExpectedValueAfterResizing() {
      for (int i = 9; i < 22; i++) {
        map.put(i, ThreadLocalRandom.current().nextInt(0, 100));
      }
      assertEquals(map.get(8), 16);
    }

    @Test
    void getMethodShouldReturnNullWhenDidNotFindEntry() {
      assertNull(map.get(50));
    }

    @Test
    void getMethodCauseNPEWhenDidNotFindValueByKey() {
      assertThrows(NullPointerException.class, this::getLongFromMap);
    }

    @Test
    void mapsNotEqualByEntries() {
      expected.put(20, 100);
      assertThat(map, not(expected));
    }

    @Test
    @Disabled
    void mapsIsEqualByEntries() {
      assertThat(map, is(expected));
    }

    private Long getLongFromMap() {
      return Long.valueOf(map.get(50));
    }
  }

  @AfterAll
  void tearDown() {
    map = null;
    expected = null;
  }

}
