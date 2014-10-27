package syntax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

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
     * Obtain a string representation of this Parallel, using a different name
     * type.
     * @return a string representing the Parallel, printing names of a different
     * type, the values of which are mapped to by the contained names.
     */
    public <U> String toStringWithNameMap(HashMap<T, U> nameMap) {
        ArrayList<String> strSubs = new ArrayList<String>();
        for(Term<T> subterm : this.subterms) {
            strSubs.add(subterm.toStringWithNameMap(nameMap));
        }
        return Term.stringifyList("[", "]", " |", strSubs);
    }

    /**
     * Rename the names within this Parallel as though a message had been
     * received by a containing process
     * @param from names with this value are renamed
     * @param to names being renamed are renamed to this name
     */
    public void rename(T from, T to) {
        for(Term<T> subterm : subterms) { subterm.rename(from, to); }
    }

    /**
     * Alpha-convert this Parallel Term. Has the same behaviour in terms of the
     * names within this node as rename(), hence the existance of the
     * changeNames private method.
     * @param from names with this value are renamed
     * @param to names being renamed are renamed to this name
     */
    public void alphaConvert(T from, T to) {
        for(Term<T> subterm : this.subterms) { subterm.alphaConvert(from, to); }
    }

    /**
     * Obtain a string representation of this Parallel.
     * @return a string representation of this Parallel
     */
    @Override
    public String toString() {
        return Term.stringifyList("[", "]", " |", this.subterms);
    }

    /**
     * Deep copy this Parallel.
     * @return a deep copy of this Parallel
     */
    public Parallel<T> copy() {
        ArrayList<Term<T>> copySubs = new ArrayList<Term<T>>();
        for(Term<T> subterm : this.subterms) { copySubs.add(subterm.copy()); }
        return new Parallel<T>(copySubs);
    }
}
