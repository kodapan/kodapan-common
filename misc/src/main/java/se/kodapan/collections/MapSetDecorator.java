package se.kodapan.collections;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author kalle
 * @since 2010-aug-25 11:58:23
 */
public abstract class MapSetDecorator<K, V> extends MapSet<K, V> {
  
  private static final long serialVersionUID = 1l;

  
  protected abstract MapSet<K, V> getDecoratedMapSet();

  @Override
  protected Map<K, Set<V>> getDecoratedMap() {
    return getDecoratedMapSet().getDecoratedMap();    
  }

  @Override
  public void writeExternal(ObjectOutput objectOutput) throws IOException {
    getDecoratedMapSet().writeExternal(objectOutput);    
  }

  @Override
  public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
    getDecoratedMapSet().readExternal(objectInput);    
  }

  @Override
  public void putAll(Map<? extends K, ? extends Set<V>> map) {
    getDecoratedMapSet().putAll(map);    
  }

  @Override
  public boolean add(K k, V v) {
    return getDecoratedMapSet().add(k, v);    
  }

  @Override
  public boolean containsSetValue(V v) {
    return getDecoratedMapSet().containsSetValue(v);    
  }

  @Override
  public boolean removeSetValue(V v) {
    return getDecoratedMapSet().removeSetValue(v);    
  }

  @Override
  public int size() {
    return getDecoratedMapSet().size();    
  }

  @Override
  public boolean isEmpty() {
    return getDecoratedMapSet().isEmpty();    
  }

  @Override
  public boolean containsKey(Object o) {
    return getDecoratedMapSet().containsKey(o);    
  }

  @Override
  public boolean containsValue(Object o) {
    return getDecoratedMapSet().containsValue(o);    
  }

  @Override
  public Set<V> get(Object o) {
    return getDecoratedMapSet().get(o);    
  }

  @Override
  public Set<V> put(K k, Set<V> vs) {
    return getDecoratedMapSet().put(k, vs);    
  }

  @Override
  public Set<V> remove(Object o) {
    return getDecoratedMapSet().remove(o);    
  }

  @Override
  public void clear() {
    getDecoratedMapSet().clear();    
  }

  @Override
  public Set<K> keySet() {
    return getDecoratedMapSet().keySet();    
  }

  @Override
  public Collection<Set<V>> values() {
    return getDecoratedMapSet().values();    
  }

  @Override
  public Set<Entry<K, Set<V>>> entrySet() {
    return getDecoratedMapSet().entrySet();    
  }

  @Override
  public boolean equals(Object o) {
    return getDecoratedMapSet().equals(o);    
  }

  @Override
  public int hashCode() {
    return getDecoratedMapSet().hashCode();    
  }

  @Override
  public String toString() {
    return getDecoratedMapSet().toString();    
  }


}
