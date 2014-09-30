package syntax;

/**
 * The Done class represents a completed process. It contains no information and
 * does nothing, except occasionally disappear.
 */
public class Done extends SyntaxElement {

    /**
     * Do nothing.
     * @param from it's irrelevant
     * @param to it's irrelevant
     */
    @Override
    public void rename(Name from, Name to) {
        return;
    }

    @Override
    public String prettyPrint(int indentationLevel) {
        return "end\n";
    }
}
