package com.github.fac30ff.map.hashmap.openaddressing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestHashMapOpenAddressing {
  private HashMapOpenAddressing map = new HashMapOpenAddressing();
  private HashMapOpenAddressing expected = new HashMapOpenAddressing();

  @BeforeAll
  void setUp() {
    for (int i = 0; i < 9; i++) {
      map.put(i, i*2);
      expected.put(i, i*3);
    }
  }

  @Test
  void shouldReturnSize() {
    assertEquals(map.size(), 9);
    assertThat(map.size(), is(9));
  }

  @Test
  void shouldReturnNullWhenDidNotFindEntry() {
    assertNull(map.get(50));
  }

  @Test
  void mapIsEqual() {
    assertThat(map, is(expected));
  }
}
