package se.kodapan.collections;

/**
 * @author kalle
 * @since 2010-aug-25 12:01:20
 */
public class DecoratedSetMap<K, V> extends SetMapDecorator<K, V> {

  private static final long serialVersionUID = 1l;
  
  private SetMap<K, V> decoratedSetMap;

  public DecoratedSetMap() {
  }

  public DecoratedSetMap(SetMap<K, V> decoratedSetMap) {
    this.decoratedSetMap = decoratedSetMap;
  }

  public SetMap<K, V> getDecoratedSetMap() {
    return decoratedSetMap;
  }

  public void setDecoratedSetMap(SetMap<K, V> decoratedSetMap) {
    this.decoratedSetMap = decoratedSetMap;
  }
}
