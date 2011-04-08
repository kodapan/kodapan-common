package se.kodapan.collections;

/**
 * @author kalle
 * @since 2010-aug-25 12:01:20
 */
public class DecoratedMapSet<K, V> extends MapSetDecorator<K, V> {

  private static final long serialVersionUID = 1l;
  
  private MapSet<K, V> decoratedMapSet;

  public DecoratedMapSet() {
  }

  public DecoratedMapSet(MapSet<K, V> decoratedMapSet) {
    this.decoratedMapSet = decoratedMapSet;
  }

  public MapSet<K, V> getDecoratedMapSet() {
    return decoratedMapSet;
  }

  public void setDecoratedMapSet(MapSet<K, V> decoratedMapSet) {
    this.decoratedMapSet = decoratedMapSet;
  }
}
