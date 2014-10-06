package parsersyntax;

/**
 * The End class represents a completed process. It contains no information and
 * does nothing, except occasionally disappear.
 */
public class End extends Term {

    /**
     * Obtain a pretty-printout of this Done.
     * @param indentLevel the number of tabs that should appear before the
     * text
     * @return a string representing this Done
     */
    @Override
    public String prettyPrint(int indentLevel) {
        return Term.indent(indentLevel) + "end\n";
    }
}
