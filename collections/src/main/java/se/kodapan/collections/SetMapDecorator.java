package se.kodapan.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author kalle
 * @since 2010-aug-25 11:58:23
 */
public abstract class SetMapDecorator<K, V> extends SetMap<K, V> {
  
  private static final long serialVersionUID = 1l;

  
  protected abstract SetMap<K, V> getDecoratedSetMap();

  @Override
  protected Map<K, Set<V>> getDecoratedMap() {
    return getDecoratedSetMap().getDecoratedMap();
  }

  @Override
  public void putAll(Map<? extends K, ? extends Set<V>> map) {
    getDecoratedSetMap().putAll(map);
  }

  @Override
  public boolean add(K k, V v) {
    return getDecoratedSetMap().add(k, v);
  }

  @Override
  public boolean containsSetValue(V v) {
    return getDecoratedSetMap().containsSetValue(v);
  }

  @Override
  public boolean removeSetValue(V v) {
    return getDecoratedSetMap().removeSetValue(v);
  }

  @Override
  public int size() {
    return getDecoratedSetMap().size();
  }

  @Override
  public boolean isEmpty() {
    return getDecoratedSetMap().isEmpty();
  }

  @Override
  public boolean containsKey(Object o) {
    return getDecoratedSetMap().containsKey(o);
  }

  @Override
  public boolean containsValue(Object o) {
    return getDecoratedSetMap().containsValue(o);
  }

  @Override
  public Set<V> get(Object o) {
    return getDecoratedSetMap().get(o);
  }

  @Override
  public Set<V> put(K k, Set<V> vs) {
    return getDecoratedSetMap().put(k, vs);
  }

  @Override
  public Set<V> remove(Object o) {
    return getDecoratedSetMap().remove(o);
  }

  @Override
  public void clear() {
    getDecoratedSetMap().clear();
  }

  @Override
  public Set<K> keySet() {
    return getDecoratedSetMap().keySet();
  }

  @Override
  public Collection<Set<V>> values() {
    return getDecoratedSetMap().values();
  }

  @Override
  public Set<Entry<K, Set<V>>> entrySet() {
    return getDecoratedSetMap().entrySet();
  }

  @Override
  public boolean equals(Object o) {
    return getDecoratedSetMap().equals(o);
  }

  @Override
  public int hashCode() {
    return getDecoratedSetMap().hashCode();
  }

  @Override
  public String toString() {
    return getDecoratedSetMap().toString();
  }


}
