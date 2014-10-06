package parsersyntax;

/**
 * The Parallel class represents parallel composition - it contains two
 * concurrently executing processes.
 */
public class Parallel extends Term {

    private Term subprocess1;
    private Term subprocess2;

    /**
     * Construct a new Parallel object using the two concurrent processes.
     * @param subprocess1 the first of the two concurrent processes
     * @param subprocess2 the second of the two concurrent processes
     * @return a new Parallel object
     */
    public Parallel(Term subprocess1, Term subprocess2) {
        this.subprocess1 = subprocess1;
        this.subprocess2 = subprocess2;
    }

    /**
     * Access the first subprocess in this parallel composition.
     * @return the first subprocess in this parallel composition
     */
    public Term getSubprocess1() { return this.subprocess1; }

    /**
     * Access the second subprocess in this parallel composition.
     * @return the second subprocess in this parallel composition
     */
    public Term getSubprocess2() { return this.subprocess2; }

    /**
     * Obtain a pretty-printout of this Parallel.
     * @param indentLevel the number of tabs that should appear before text
     * @return a pretty string representing this Parallel
     */
    @Override
    public String prettyPrint(int indentLevel) {
        return Term.indent(indentLevel) + "concurrently\n" +
                this.subprocess1.prettyPrint(indentLevel + 1) +
                Term.indent(indentLevel) + "and\n" +
                this.subprocess2.prettyPrint(indentLevel + 1);
    }
}
