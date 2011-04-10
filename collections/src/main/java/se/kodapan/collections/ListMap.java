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
import java.util.*;

/**
 * @author kalle
 * @since 2010-apr-13 06:38:54
 */
public class ListMap<K, V> implements Map<K, List<V>>, Serializable {

  private static final long serialVersionUID = 1l;
  private Map<K, List<V>> map = new HashMap<K, List<V>>();

  public ListMap() {
  }

  public boolean add(K key, V value) {
    List<V> list = map.get(key);
    if (list == null) {
      list = new ArrayList<V>();
      map.put(key, list);
    }
    return list.add(value);
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(Object o) {
    return map.containsKey(o);
  }

  @Override
  public boolean containsValue(Object o) {
    return map.containsValue(o);
  }

  @Override
  public List<V> get(Object o) {
    return map.get(o);
  }

  @Override
  public List<V> put(K k, List<V> c) {
    return map.put(k, c);
  }

  @Override
  public List<V> remove(Object o) {
    return map.remove(o);
  }

  @Override
  public void putAll(Map<? extends K, ? extends List<V>> map) {
    this.map.putAll(map);
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Set<K> keySet() {
    return map.keySet();
  }

  @Override
  public Collection<List<V>> values() {
    return map.values();
  }

  @Override
  public Set<Entry<K, List<V>>> entrySet() {
    return map.entrySet();
  }
}
