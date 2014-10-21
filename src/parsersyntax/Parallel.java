package parsersyntax;

import java.util.ArrayList;

/**
 * The Parallel class represents parallel composition - it contains two
 * concurrently executing processes.
 */
public class Parallel extends Term {

    private ArrayList<Term> subterms;

    /**
     * Construct a new Parallel object using the two concurrent processes.
     * @param subterms a list of the subterms of this Parallel
     */
    public Parallel(ArrayList<Term> subterms) {
        this.subterms = subterms;
    }

    /**
     * Access a subprocess in this parallel composition.
     * @param i the index of the subterm to retreive
     * @return the i^th subterm in this parallel composition
     */
    public Term subterm(int i) { return this.subterms.get(i); }

    /**
     * Obtain a string representation of this Parallel.
     * @return a string representation of this Parallel
     */
    @Override
    public String toString() {
        return Term.stringifyList("[", "]", " |", this.msg);
    }
}
