package se.kodapan.lang;

/**
 * @author kalle
 * @since 2010-jul-17 11:48:01
 */
public class Time {

  public static final long second = 1000;
  public static final long minute = second * 60;
  public static final long hour = minute * 60;
  public static final long day = hour * 24;
  public static final long week = day * 7;
  public static final long month = day * 30;
  public static final long year = (int) (day * 365.4f);


}
