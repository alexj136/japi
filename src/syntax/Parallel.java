package syntax;

/**
 * The Parallel class represents parallel composition - it contains two
 * concurrently executing processes.
 */
public class Parallel extends SyntaxElement {

    private SyntaxElement subprocess1;
    private SyntaxElement subprocess2;

    /**
     * Construct a new Parallel object using the two concurrent processes.
     * @param subprocess1 the first of the two concurrent processes
     * @param subprocess2 the second of the two concurrent processes
     * @return a new Parallel object
     */
    public Parallel(SyntaxElement subprocess1, SyntaxElement subprocess2) {
        this.subprocess1 = subprocess1;
        this.subprocess2 = subprocess2;
    }

    @Override
    public void rename(Name from, Name to) throws NameRepresentationException {
        this.subprocess1.rename(from, to);
        this.subprocess2.rename(from, to);
    }

    @Override
    public String prettyPrint(int indentationLevel) {
        return SyntaxElement.generateIndent(indentationLevel) +
                "concurrently\n" +
                SyntaxElement.generateIndent(indentationLevel) +
                this.subprocess1.prettyPrint(indentationLevel + 1) +
                SyntaxElement.generateIndent(indentationLevel) +
                "and\n" +
                SyntaxElement.generateIndent(indentationLevel) +
                this.subprocess2.prettyPrint(indentationLevel + 1);
    }
}
