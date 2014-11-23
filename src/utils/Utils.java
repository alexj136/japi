package utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.HashSet;

/**
 * General purpose functions ('static methods').
 */
public final class Utils {

    // Provides random numbers for Utils.arbitraryElement()
    private static Random rand = new Random();

    /**
     * Retreive an arbitrary element from an ArrayList.
     * @param matches the ArrayList to use
     * @return an arbitrary element of the given list
     */
    public static <E> E arbitraryElement(ArrayList<E> list) {
        return list.get(Utils.rand.nextInt(list.size()));
    }

    /**
     * Generate a string of the given number of tabs, for use when pretty-
     * printing terms. The generated tabs are actually 4 space characters.
     * @param numTabs the desired number of tabs
     * @return a string of (4 * numTabs) space characters
     */
    public static String indent(int numTabs) {
        return new String(new char[4 * numTabs]).replace('\0', ' ');
    }

    /**
     * Generate strings from lists of different kinds, that are nicely delimited
     * and have nice opening/closing parentheses.
     * @param open a string used as the open-parenthesis
     * @param close a string used as the close-parenthesis
     * @param delimiter a string used to delimit elements of elems
     * @param elems the elements
     * @return a nice string representation of the given list, using the given
     * open and close parentheses and delimiter. Example:
     *     stringifyList("(", ")", ",", names) where names is a list of
     *     integers, yields:
     *     "(1, 2, 3, 4)"
     */
    public static <T> String stringifyList(String open, String close,
            String delimiter, ArrayList<T> elems) {

        if(elems.isEmpty()) { return open + close; }
        else {
            String out = open + elems.get(0).toString();
            for(int i = 1; i < elems.size(); i++) {
                out += delimiter + elems.get(i).toString();
            }
            return out + close;

        }
    }

    /**
     * Generate a HashMap that is the reverse mapping of a given HashMap, i.e.
     * make a map whose keys are the values, and values are the keys of the
     * given map. Throw an exception if there are value duplicates in the given
     * map.
     * @param map the map to flip
     * @return the flipped map
     */
    public static <T, U> HashMap<U, T> flipMap(HashMap<T, U> map) {
        HashMap<U, T> flipped = new HashMap<U, T>();
        for(T key : map.keySet()) {
            if(flipped.containsKey(map.get(key))) {
                throw new IllegalArgumentException("Given map contained " +
                        "duplicate values");
            }
            else {
                flipped.put(map.get(key), key);
            }
        }
        return flipped;
    }

    /**
     * Obtain a HashSet of the keys in a HashMap. The generated set is not
     * backed by the map (like ones obtained with HashMap.keySet()).
     * @param map the map to obtain keys from
     * @return a set of the keys in the map
     */
    public static <T, U> HashSet<T> keys(HashMap<T, U> map) {
        HashSet<T> set = new HashSet<T>();
        for(T key : map.keySet()) { set.add(key); }
        return set;
    }
}
