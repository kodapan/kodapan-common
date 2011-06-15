package se.kodapan.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kalle
 * @since 2010-nov-19 12:59:40
 */
public class ScoreMap<T> extends DecoratedMap<T, Double> {

  private static final long serialVersionUID = 1l;

  public ScoreMap() {
    this(new HashMap<T, Double>());
  }

  public ScoreMap(Map<T, Double> decoratedMap) {
    super(decoratedMap);
  }

  public Double increase(T t, double value) {
    Double score = get(t);
    if (score == null) {
      score = 0d;
    }
    score += value;    
    put(t, score);
    return score;
  }

  public Double decrease(T t, double value) {
    Double score = get(t);
    if (score == null) {
      score = 0d;
    }
    score -= value;
    put(t, score);
    return score;
  }

  public Entry<T, Double>[] getHits() {
    Entry<T, Double>[] hits = (Entry<T, Double>[])entrySet().toArray(new Entry[size()]);
    Arrays.sort(hits, new Comparator<Entry<T, Double>>(){
      @Override
      public int compare(Entry<T, Double> o1, Entry<T, Double> o2) {
        return o2.getValue().compareTo(o1.getValue());
      }
    });
    return hits;
  }

}
