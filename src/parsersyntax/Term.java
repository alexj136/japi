package parsersyntax;

/**
 * Term is a subclass of SyntaxElement that represents actual expressions -
 * subclasses of Term must represent productions of the pi calculus grammar.
 */
public abstract class Term {

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

    public static String stringifyList(String open, String close,
            String delimiter, ArrayList elems) {

        if(elems.isEmpty()) { return open + close; }
        else {
            String out = open + elems.get(0).toString();
            for(int i = 1; i < elems.size(); i++) {
                out += delimiter + " " + elems.get(i).toString();
            }
            return out + " " + close;

        }
    }
}
