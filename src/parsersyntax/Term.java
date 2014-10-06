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
     * Pretty-print this term starting at the given level of indentation. (Don't
     * actually print, just return the String)
     * @param indentLevel how many tabs before the content
     * @return a string representing the Term
     */
    public abstract String prettyPrint(int indentLevel);
}
