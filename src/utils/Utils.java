package utils;

import java.util.ArrayList;

/**
 * General purpose functions ('static methods').
 */
public final class Utils {

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
    public static String stringifyList(String open, String close,
            String delimiter, ArrayList elems) {

        if(elems.isEmpty()) { return open + close; }
        else {
            String out = open + elems.get(0).toString();
            for(int i = 1; i < elems.size(); i++) {
                out += delimiter + elems.get(i).toString();
            }
            return out + close;

        }
    }
}
