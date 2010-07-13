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

import se.kodapan.io.UnsupportedLocalVersion;

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
public class MapSet<K, V> extends MapDecorator<K, Set<V>>
    implements Serializable, Externalizable {

  private static final long serialVersionUID = 1l;

  private Map<K, Set<V>> map;

  public MapSet() {
    this(new HashMap<K, Set<V>>());
  }

  public MapSet(Map<K, Set<V>> map) {
    this.map = map;
  }

  @Override
  protected Map<K, Set<V>> getDecoratedMap() {
    return map;
  }

  @Override
  public void writeExternal(ObjectOutput objectOutput) throws IOException {
    objectOutput.writeInt(1); // local object version
    objectOutput.writeObject(map);
  }


  @Override
  @SuppressWarnings("unchecked")
  public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
    int version = objectInput.readInt();
    if (version == 1) {
      map = (Map) objectInput.readObject();
    } else {
      throw new UnsupportedLocalVersion(version, 1);
    }
  }


  public boolean add(K k, V v) {
    if (v == null) {
      throw new NullPointerException("No null values");
    }
    Set<V> values = get(k);
    if (values == null) {
      values = new HashSet<V>();
      put(k, values);
    }
    return values.add(v);    
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
