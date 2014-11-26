package utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;

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
        if(list.isEmpty()) {
            throw new IllegalArgumentException("Utils.arbitraryElement(): " +
                    "cannot retrieve an element from an empty list");
        }
        return list.get(Utils.rand.nextInt(list.size()));
    }

    /**
     * Slice a list from the start up to the given index (exclusive). Equivalent
     * to Utils.slice(list, 0, index).
     * @param list the list to slice
     * @param index the index after the index of the last element included in
     * the slice
     * @return the desired slice
     */
    public static <T> ArrayList<T> sliceStart(ArrayList<T> list, int index) {
        return Utils.slice(list, 0, index);
    }

    /**
     * Slice a list from the given index (inclusive) to the end of the list.
     * Equivalent to Utils.slice(list, index, list.size()).
     * @param list the list to slice
     * @param index the index of the first element to include in the slice
     * @return the desired slice
     */
    public static <T> ArrayList<T> sliceEnd(ArrayList<T> list, int index) {
        return Utils.slice(list, index, list.size());
    }

    /**
     * Obtain a slice of the given list from beginIndex (inclusive), to endIndex
     * (exclusive). Returns a new list and the original list is not modified.
     * @param list the list to slice
     * @param beginIndex the index of the first element in the slice
     * @param endIndex the index after the index of the last element in the
     * slice
     * @return the desired slice
     */
    public static <T> ArrayList<T> slice(ArrayList<T> list, int beginIndex,
            int endIndex) {

        if(beginIndex < 0 || endIndex < 0 || beginIndex >= list.size() ||
                endIndex > list.size() || (beginIndex >= endIndex)) {

            throw new IllegalArgumentException("Invalid indices");
        }
        ArrayList<T> newList = new ArrayList<T>();
        for(int i = beginIndex; i < endIndex; i++) {
            newList.add(list.get(i));
        }
        return newList;
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

    /**
     * Determine if any elements of the given List return true for the
     * given predicate.
     * @param list the list to test
     * @param test the test operation
     * @return true if the test passes for one or more elements of the list,
     * false otherwise
     */
    public static <T> boolean any(Predicate<T> pred, ArrayList<T> list) {
        ArrayList<Boolean> bools = Utils.map((T t) -> pred.test(t), list);
        for(Boolean b : bools) { if(b) { return true; } }
        return false;
    }

    /**
     * Apply a function to all elements of a list, to generate a new list.
     * @param list the list to apply the function to
     * @param function the function to apply
     * @return a list containing elements that are the result of applying the
     * function to the elements of the list, in the corresponding order.
     */
    public static <T, U> ArrayList<U> map(Function<T, U> function,
            ArrayList<T> list) {

        ArrayList<U> newList = new ArrayList<U>();
        for(T elem : list) { newList.add(function.apply(elem)); }
        return newList;
    }

    /**
     * Create a new ArrayList from the given ArrayList, with all elements from
     * the given list in the given order, omitting elements that do not pass the
     * given predicate. Does not modify the given list.
     * @param pred the predicate to test with
     * @param list the list to filter
     * @return the filtered list
     */
    public static <T> ArrayList<T> filter(Predicate<T> pred,
            ArrayList<T> list) {

        ArrayList<T> newList = new ArrayList<T>();
        for(T elem : list) {
            if(pred.test(elem)) {
                newList.add(elem);
            }
        }
        return newList;
    }
}
