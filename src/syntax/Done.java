package syntax;

/**
 * The Done class represents a completed process. It contains no information and
 * does nothing, except occasionally disappear.
 */
public class Done extends Term {

    /**
     * Do nothing.
     * @param from it's irrelevant
     * @param to it's irrelevant
     */
    @Override
    public void rename(Name from, Name to) {
        return;
    }

    /**
     * Obtain a pretty-printout of this Done.
     * @param indentationLevel the number of tabs that should appear before the
     * text
     * @return a string representing this Done
     */
    @Override
    public String prettyPrint(int indentationLevel) {
        return SyntaxElement.generateIndent(indentationLevel) + "end\n";
    }
}
