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

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author kalle
 * @since 2010-jul-10 01:12:32
 */
public abstract class MapDecorator<K, V> implements Map<K, V>, Serializable {

  private static final long serialVersionUID = 1l;

  @Override
  public int size() {
    return getDecoratedMap().size();
  }

  @Override
  public boolean isEmpty() {
    return getDecoratedMap().isEmpty();
  }

  @Override
  public boolean containsKey(Object o) {
    return getDecoratedMap().containsKey(o);
  }

  @Override
  public boolean containsValue(Object o) {
    return getDecoratedMap().containsValue(o);
  }

  @Override
  public V get(Object o) {
    return getDecoratedMap().get(o);
  }

  @Override
  public V put(K k, V v) {
    return getDecoratedMap().put(k, v);
  }

  @Override
  public V remove(Object o) {
    return getDecoratedMap().remove(o);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> map) {
    getDecoratedMap().putAll(map);
  }

  @Override
  public void clear() {
    getDecoratedMap().clear();
  }

  @Override
  public Set<K> keySet() {
    return getDecoratedMap().keySet();
  }

  @Override
  public Collection<V> values() {
    return getDecoratedMap().values();
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return getDecoratedMap().entrySet();
  }

  @Override
  public boolean equals(Object o) {
    return getDecoratedMap().equals(o);
  }

  @Override
  public int hashCode() {
    return getDecoratedMap().hashCode();
  }

  protected abstract Map<K, V> getDecoratedMap();
}
