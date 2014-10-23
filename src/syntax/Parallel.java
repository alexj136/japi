package syntax;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Parallel class represents parallel composition - it contains two
 * concurrently executing processes.
 */
public final class Parallel<T> extends Term<T> implements Iterable<Term<T>> {

    private ArrayList<Term<T>> subterms;

    /**
     * Construct a new Parallel object using the two concurrent processes.
     * @param subterms a list of the subterms of this Parallel
     */
    public Parallel(ArrayList<Term<T>> subterms) {
        this.subterms = subterms;
    }

    /**
     * Access a subprocess in this parallel composition.
     * @param i the index of the subterm to retreive
     * @return the i^th subterm in this parallel composition
     */
    public Term<T> subterm(int i) { return this.subterms.get(i); }

    /**
     * Determine the number of terms in this parallel composition.
     * @return  the number of terms in this parallel composition
     */
    public int arity() { return this.subterms.size(); }

    /**
     * Obtain an iterator over the elements in this parallel composition.
     * @return an iterator over the elements in this parallel composition.
     */
    public Iterator<Term<T>> iterator() { return this.subterms.iterator(); }

    /**
     * Obtain a string representation of this Parallel.
     * @return a string representation of this Parallel
     */
    @Override
    public String toString() {
        return Term.stringifyList("[", "]", " |", this.subterms);
    }
}
