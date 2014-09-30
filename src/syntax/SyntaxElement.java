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
}
