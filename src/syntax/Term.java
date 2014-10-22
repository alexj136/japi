package syntax;

import java.util.ArrayList;

/**
 * A Term is a pi-calculus expression. This is a generic class since different
 * data types can be (and are) used to represent names in the pi-calculus. For
 * example, the parser parses a Term<String>, whereas the interpreter operates
 * over Term<Integer>s. Thus a translation from String to Integer names must
 * occur before interpretation. The only constraint on the type of name that can
 * be used is that it must have a reasonable definition of equality (i.e. it
 * override java.lang.Object.equals(Object o) in a sensible way. Unfortunately,
 * java's type system does not allow one to actually enforce such a constraint.
 */
public abstract class Term<T> {

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
     * Obtain a string representation of this Term.
     * @return a string representing the Term
     */
    public abstract String toString();

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
            String out = open + " " + elems.get(0).toString();
            for(int i = 1; i < elems.size(); i++) {
                out += delimiter + " " + elems.get(i).toString();
            }
            return out + " " + close;

        }
    }
}
