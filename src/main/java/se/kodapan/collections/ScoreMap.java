package se.kodapan.collections;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kalle
 * @since 2010-nov-19 12:59:40
 */
public class ScoreMap<T> extends DecoratedMap<T, Double> {

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


}
