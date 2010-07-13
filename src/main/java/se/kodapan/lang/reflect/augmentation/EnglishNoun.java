/*
 * Copyright 2007 James A. Mason
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

package se.kodapan.lang.reflect.augmentation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An experimental class that provides static methods
 * for testing and computing various properties of strings
 * that are presumed to be English nouns.
 *
 * @author James A. Mason <http://www.yorku.ca/jmason/>
 */
public class EnglishNoun {

  public static String upperCaseFirstLetter(String in) {
    char[] buf = in.toCharArray();
    upperCaseFirstLetter(buf);
    return new String(buf);
  }

  public static String lowerCaseFirstLetter(String in) {
    char[] buf = in.toCharArray();
    lowerCaseFirstLetter(buf);
    return new String(buf);
  }

  public static void lowerCaseFirstLetter(char[] in) {
    in[0] = String.valueOf(in[0]).toLowerCase().charAt(0);
  }

  public static void lowerCaseFirstLetter(StringBuffer in) {
    in.setCharAt(0, String.valueOf(in.charAt(0)).toLowerCase().charAt(0));
  }

  public static void upperCaseFirstLetter(char[] in) {
    in[0] = String.valueOf(in[0]).toUpperCase().charAt(0);
  }

  public static void upperCaseFirstLetter(StringBuffer in) {
    in.setCharAt(0, String.valueOf(in.charAt(0)).toUpperCase().charAt(0));
  }

  private static boolean isUpperCase(char in) {
    for (char c = 'A'; c < 'Z'; c++)
      if (in == c)
        return true;

    return false;
  }

  public static String pluralOfLastJavaName(String name) {
    return pluralOfLastJavaName(name, false);
  }

  public static String pluralOfLastJavaName(String name, boolean returnInitalUpperCase) {
    String[] names = javaNameNotationTokenizer(name);
    names[names.length - 1] = pluralOf(names[names.length - 1]);
    StringBuffer ret = new StringBuffer();
    for (int i = 0; i < names.length; i++)
      ret.append(names[i]);

    if (returnInitalUpperCase)
      upperCaseFirstLetter(ret);

    return ret.toString();
  }

  public static String singularOfLastJavaName(String name) {
    return singularOfLastJavaName(name, false);
  }

  public static String singularOfLastJavaName(String name, boolean returnInitalUpperCase) {
    String[] names = javaNameNotationTokenizer(name);

    boolean makeUpperCase = false;
    if (names.length > 1)
      makeUpperCase = true;
    names[names.length - 1] = singularOf(names[names.length - 1], makeUpperCase);

    StringBuffer ret = new StringBuffer();
    for (int i = 0; i < names.length; i++)
      ret.append(names[i]);

    if (returnInitalUpperCase)
      upperCaseFirstLetter(ret);

    return ret.toString();
  }

  public static String[] javaNameNotationTokenizer(String input) {
    return javaNameNotationTokenizer(input.toCharArray());
  }

  public static String[] javaNameNotationTokenizer(char[] input) {
    if (input.length == 0)
      throw new NullPointerException("Input is empty");

    ArrayList words = new ArrayList();
    StringBuffer tmp = new StringBuffer();
    for (int i = 0; i < input.length; i++) {
      if (tmp.length() > 0 && isUpperCase(input[i])) {
        words.add(tmp.toString());
        tmp = new StringBuffer();
      }
      tmp.append(input[i]);
    }
    if (tmp.length() > 0)
      words.add(tmp.toString());

    String[] ret = new String[words.size()];
    for (int i = 0; i < ret.length; i++)
      ret[i] = (String) words.get(i);
    return ret;
  }

  /**
   * Tests whether the given (presumed) English noun is plural. A word like "sheep" that can be either singular or plural yields true.
   */
  public static boolean isPlural(String word) {
    word = word.toLowerCase();
    if (irregularPlurals.containsKey(word))
      return true;
    if (word.length() <= 1)
      return false;
    // If it is not an irregular plural, it must end in -s,
    // but it must not be an irregular singular (like "bus")
    // nor end in -ss (like "boss").
    if (word.charAt(word.length() - 1) != 's')
      return false;
    if (irregularSingulars.containsKey(word))
      return false;
    if (word.length() >= 2 && word.charAt(word.length() - 2) == 's')
      return false;
    return true;
  }

  /**
   * Tests whether the given (presumed) English noun is singular. A word like "sheep" that can be either singular or plural yields true.
   */
  public static boolean isSingular(String word) {
    word = word.toLowerCase();
    if (irregularSingulars.containsKey(word))
      return true;
    // If it is not an irregular singular, it must not be an
    // irregular plural (like "children"), and it must not end
    // in -s unless it ends in -ss (like "boss")).
    if (irregularPlurals.containsKey(word))
      return false;
    if (word.length() <= 0)
      return false;
    if (word.charAt(word.length() - 1) != 's')
      return true;
    if (word.length() >= 2 && word.charAt(word.length() - 2) == 's')
      return true; // word ends in -ss
    return false; // word is not irregular, and ends in -s but not -ss
  }

  /**
   * Returns the plural of a given (presumed) English word. The given word may be singular or (already) plural. The word will be returned in the caption as passed to the method, i.e. AWorD will be AWorDs
   */
  public static String pluralOf(String word) {

    // handle acronyms such as CVS
    if (word.length() <= 1 || !word.toUpperCase().equals(word)) {
      if (isPlural(word))
        return word;
    }

    // this is nasty code.
    String ret = word;

    word = word.toLowerCase();

    Object singularLookup = irregularSingulars.get(word);
    //String plural = null;
    if (singularLookup != null) {
      if (singularLookup instanceof ArrayList)
        return (String) (((ArrayList) singularLookup).get(0));
      else
        return (String) singularLookup;
    }
    int length = word.length();
    if (length <= 1)
      return ret + "s";
    char lastLetter = word.charAt(length - 1);
    char secondLast = word.charAt(length - 2);
    if ("sxzo".indexOf(lastLetter) >= 0 || (lastLetter == 'h' && (secondLast == 's' || secondLast == 'c')))
      return ret + "es";
    if (lastLetter == 'y') {
      if ("aeiou".indexOf(secondLast) >= 0)
        return ret + "s";
      else
        return ret.substring(0, length - 1) + "ies";
    }
    return ret + "s";
  } // end pluralOf

  /**
   * Returns the singular of a given (presumed) English word. The given word may be plural or (already) singular.
   */
  public static String singularOf(String word) {
    String ret = word;

    word = word.toLowerCase();
    if (isSingular(word))
      return ret;
    Object pluralLookup = irregularPlurals.get(word);
    //String singular = null;
    if (pluralLookup != null) {
      if (pluralLookup instanceof ArrayList)
        return (String) (((ArrayList) pluralLookup).get(0));
      else
        return (String) pluralLookup;
    }
    int length = word.length();
    if (length <= 1)
      return ret;
    char lastLetter = word.charAt(length - 1);
    if (lastLetter != 's')
      return ret; // no final -s
    char secondLast = word.charAt(length - 2);
    if (secondLast == '\'')
      return ret.substring(0, length - 2);
    // remove -'s
    if (word.equalsIgnoreCase("gas"))
      return ret;
    if (secondLast != 'e' || length <= 3)
      return ret.substring(0, length - 1); // remove final -s
    // Word ends in -es and has length >= 4:
    char thirdLast = word.charAt(length - 3);
    if (thirdLast == 'i') // -ies => -y
      return ret.substring(0, length - 3) + "y";
    if (thirdLast == 'x') // -xes => -x
      return ret.substring(0, length - 2);
    if (length <= 4) // e.g. uses => use
      return ret.substring(0, length - 1);
    char fourthLast = word.charAt(length - 4);
    if (thirdLast == 'h' && (fourthLast == 'c' || fourthLast == 's'))
      // -ches or -shes => -ch or -sh
      return ret.substring(0, length - 2);
    if (thirdLast == 's' && fourthLast == 's') // -sses => -ss
      return ret.substring(0, length - 2);
    return ret.substring(0, length - 1); // keep the final e.
  } // end singularOf

  private static HashMap irregularSingulars = new HashMap(100);

  private static HashMap irregularPlurals = new HashMap(100);

  /* this code sucks */
  static {
    irregularSingulars.put("ache", "aches");
    irregularSingulars.put("alumna", "alumnae");
    irregularSingulars.put("alumnus", "alumni");
    irregularSingulars.put("axis", "axes");
    irregularSingulars.put("bison", "bison");
    ArrayList busses = new ArrayList(2);
    busses.add("buses");
    busses.add("busses");
    irregularSingulars.put("bus", busses);
    irregularSingulars.put("calf", "calves");
    irregularSingulars.put("caribou", "caribou");
    irregularSingulars.put("child", "children");
    irregularSingulars.put("datum", "data");
    irregularSingulars.put("deer", "deer");
    ArrayList dice = new ArrayList(2);
    dice.add("dies");
    dice.add("dice");
    irregularSingulars.put("die", dice);
    irregularSingulars.put("elf", "elves");
    irregularSingulars.put("elk", "elk");
    irregularSingulars.put("fish", "fish");
    irregularSingulars.put("foot", "feet");
    irregularSingulars.put("gentleman", "gentlemen");
    irregularSingulars.put("gentlewoman", "gentlewomen");
    irregularSingulars.put("go", "goes");
    irregularSingulars.put("goose", "geese");
    irregularSingulars.put("grouse", "grouse");
    irregularSingulars.put("half", "halves");
    ArrayList hoof = new ArrayList(2);
    hoof.add("hooves");
    hoof.add("hoofs");
    irregularSingulars.put("hoof", hoof);
    irregularSingulars.put("knife", "knives");
    irregularSingulars.put("leaf", "leaves");
    irregularSingulars.put("life", "lives");
    irregularSingulars.put("louse", "lice");
    irregularSingulars.put("man", "men");
    irregularSingulars.put("money", "monies");
    irregularSingulars.put("moose", "moose");
    irregularSingulars.put("mouse", "mice");
    ArrayList octopus = new ArrayList(3);
    octopus.add("octopodes");
    octopus.add("octopi");
    octopus.add("octopuses");
    irregularSingulars.put("octopus", octopus);
    irregularSingulars.put("ox", "oxen");
    irregularSingulars.put("plus", "pluses");
    irregularSingulars.put("quail", "quail");
    irregularSingulars.put("reindeer", "reindeer");
    ArrayList scarf = new ArrayList(2);
    scarf.add("scarves");
    scarf.add("scarfs");
    irregularSingulars.put("scarf", scarf);
    irregularSingulars.put("self", "selves");
    irregularSingulars.put("sheaf", "sheaves");
    irregularSingulars.put("sheep", "sheep");
    irregularSingulars.put("shelf", "shelves");
    irregularSingulars.put("squid", "squid");
    irregularSingulars.put("thief", "thieves");
    irregularSingulars.put("tooth", "teeth");
    irregularSingulars.put("wharf", "wharves");
    irregularSingulars.put("wife", "wives");
    irregularSingulars.put("wolf", "wolves");
    irregularSingulars.put("woman", "women");

    irregularPlurals.put("aches", "ache");
    irregularPlurals.put("alumnae", "alumna");
    irregularPlurals.put("alumni", "alumnus");
    ArrayList axes = new ArrayList(2);
    axes.add("axe");
    axes.add("axis");
    irregularPlurals.put("axes", axes);
    irregularPlurals.put("bison", "bison");
    irregularPlurals.put("buses", "bus");
    irregularPlurals.put("busses", "bus");
    irregularPlurals.put("brethren", "brother");
    irregularPlurals.put("caches", "cache");
    irregularPlurals.put("calves", "calf");
    irregularPlurals.put("cargoes", "cargo");
    irregularPlurals.put("caribou", "caribou");
    irregularPlurals.put("children", "child");
    irregularPlurals.put("data", "datum");
    irregularPlurals.put("deer", "deer");
    irregularPlurals.put("dice", "die");
    irregularPlurals.put("dies", "die");
    irregularPlurals.put("dominoes", "domino");
    irregularPlurals.put("echoes", "echo");
    irregularPlurals.put("elves", "elf");
    irregularPlurals.put("elk", "elk");
    irregularPlurals.put("embargoes", "embargo");
    irregularPlurals.put("fish", "fish");
    irregularPlurals.put("feet", "foot");
    irregularPlurals.put("gentlemen", "gentleman");
    irregularPlurals.put("gentlewomen", "gentlewoman");
    irregularPlurals.put("geese", "goose");
    irregularPlurals.put("goes", "go");
    irregularPlurals.put("grottoes", "grotto");
    irregularPlurals.put("grouse", "grouse");
    irregularPlurals.put("halves", "half");
    irregularPlurals.put("hooves", "hoof");
    irregularPlurals.put("knives", "knife");
    irregularPlurals.put("leaves", "leaf");
    irregularPlurals.put("lives", "life");
    irregularPlurals.put("lice", "louse");
    irregularPlurals.put("men", "man");
    irregularPlurals.put("monies", "money");
    irregularPlurals.put("moose", "moose");
    irregularPlurals.put("mottoes", "motto");
    irregularPlurals.put("mice", "mouse");
    irregularPlurals.put("octopi", "octopus");
    irregularPlurals.put("octopodes", "octopus");
    irregularPlurals.put("octopuses", "octopus");
    irregularPlurals.put("oxen", "ox");
    irregularPlurals.put("pies", "pie");
    irregularPlurals.put("pluses", "plus");
    irregularPlurals.put("posses", "posse");
    irregularPlurals.put("potatoes", "potato");
    irregularPlurals.put("quail", "quail");
    irregularPlurals.put("reindeer", "reindeer");
    irregularPlurals.put("scarves", "scarf");
    irregularPlurals.put("sheaves", "sheaf");
    irregularPlurals.put("sheep", "sheep");
    irregularPlurals.put("shelves", "shelf");
    irregularPlurals.put("squid", "squid");
    irregularPlurals.put("teeth", "tooth");
    irregularPlurals.put("thieves", "thief");
    irregularPlurals.put("ties", "tie");
    irregularPlurals.put("tomatoes", "tomato");
    irregularPlurals.put("wharves", "wharf");
    irregularPlurals.put("wives", "wife");
    irregularPlurals.put("wolves", "wolf");
    irregularPlurals.put("women", "woman");
  } // end static initialization block

  public static final String pluralOf(String word, boolean returnCapitalInital) {
    StringBuffer ret = new StringBuffer(pluralOf(word));
    if (returnCapitalInital)
      upperCaseFirstLetter(ret);

    return ret.toString();
  }

  public static final String singularOf(String word, boolean returnCapitalInital) {
    StringBuffer ret = new StringBuffer(singularOf(word));
    if (returnCapitalInital)
      upperCaseFirstLetter(ret);

    return ret.toString();
  }

} // end class EnglishNoun