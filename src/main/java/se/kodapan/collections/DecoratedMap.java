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

import java.util.Map;

/**
 * @author kalle
 * @since 2010-jul-13 23:56:45
 */
public class DecoratedMap<K, V> extends MapDecorator<K, V>{

  private Map<K, V> decoratedMap;

  public DecoratedMap(Map<K, V> decoratedMap) {
    this.decoratedMap = decoratedMap;
  }

  public Map<K, V> getDecoratedMap() {
    return decoratedMap;
  }

  public void setDecoratedMap(Map<K, V> decoratedMap) {
    this.decoratedMap = decoratedMap;
  }
}
