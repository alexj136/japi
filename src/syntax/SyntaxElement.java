package syntax;

/**
 * An abstact class for all types of syntax (i.e. all productions of pi calculus
 * grammar) to extend.
 */
public abstract class SyntaxElement {

    /**
     * Rename all occurences of one name to another in any contained
     * subexpressions.
     * @param from All occurences of this Name will be renamed
     * @param to any renamed occurences recieve this as their new name
     */
    public abstract void rename(Name from, Name to)
    throws NameRepresentationException;

    /**
     * Pretty-print this Syntax element starting at the given level of
     * indentation. (Don't actually print, just return the String)
     * @param indentationLevel how many tabs before the content
     * @return a string representing the SyntaxElement
     */
    public abstract String prettyPrint(int indentationLevel);

    /**
     * Generate a string of the given number of tabs, for use when pretty-
     * printing terms. The generated tabs are actually 4 space characters.
     * @param numTabs the desired number of tabs
     * @return a string of (4 * numTabs) space characters
     */
    public static String generateIndent(int numTabs) {
        return new String(new char[4 * numTabs]).replace('\0', ' ');
    }
}
