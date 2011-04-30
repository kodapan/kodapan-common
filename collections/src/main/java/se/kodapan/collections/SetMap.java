/*
 * Copyright 2010 Kodapan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.kodapan.collections;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * does not accept null values
 *
 * @author kalle
 * @since 2010-mar-16 19:04:56
 */
public class SetMap<K, V> extends MapDecorator<K, Set<V>>
    implements Serializable {

  private static final long serialVersionUID = 1l;

  public static <K, V> SetMap<K, V> unmodifiableMapSet(SetMap<K, V> mapSet) {
    return new SetMap<K, V>(mapSet) {
      @Override
      public void putAll(Map<? extends K, ? extends Set<V>> map) {
        throw new UnsupportedOperationException("Immutable");
      }

      @Override
      public boolean add(K k, V v) {
        throw new UnsupportedOperationException("Immutable");
      }

      @Override
      public boolean removeSetValue(V v) {
        throw new UnsupportedOperationException("Immutable");
      }

      @Override
      public Set<V> put(K k, Set<V> vs) {
        throw new UnsupportedOperationException("Immutable");
      }

      @Override
      public Set<V> remove(Object o) {
        throw new UnsupportedOperationException("Immutable");
      }

      @Override
      public void clear() {
        throw new UnsupportedOperationException("Immutable");
      }
    };
  }

  private Map<K, Set<V>> map;

  public SetMap() {
    this(new HashMap<K, Set<V>>());
  }

  public SetMap(Map<K, Set<V>> map) {
    this.map = map;
  }

  @Override
  protected Map<K, Set<V>> getDecoratedMap() {
    return map;
  }

  @Override
  public void putAll(Map<? extends K, ? extends Set<V>> map) {
    for (Map.Entry<? extends K, ? extends Set<V>> e : map.entrySet()) {
      for (V v : e.getValue()) {
        add(e.getKey(), v);
      }
    }
  }
  
  public boolean add(K k, V v) {
    if (v == null) {
      throw new NullPointerException("No null values");
    }
    Set<V> values = get(k);
    if (values == null) {
      values = valuesFactory(k);
    }
    return values.add(v);    
  }

  protected synchronized Set<V> valuesFactory(K k) {
    Set<V> values = get(k);
    if (values != null) {
      return values;
    }
    values = setFactory();
    put(k, values);
    return values;
  }

  public Set<V> setFactory() {
    return new HashSet<V>();
  }

  public boolean containsSetValue(V v) {
    for (Set<V> set : values()) {
      if (set.contains(v)) {
        return true;
      }
    }
    return false;
  }


  public boolean removeSetValue(V v) {
    boolean removed = false;
    for (Set<V> set : values()) {
      if (set.remove(v)) {
        removed = true;
      }
    }
    return removed;
  }


}
