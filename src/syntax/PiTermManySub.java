package syntax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;

public abstract class PiTermManySub extends PiTerm implements Iterable<PiTerm> {

    protected ArrayList<PiTerm> subterms;

    /**
     * Construct a new PiTermManySub.
     * @param subterms The subterms of this PiTerm
     */
    public PiTermManySub(ArrayList<PiTerm> subterms) {
        this.subterms = subterms;
    }

    /**
     * Access a stored subterm.
     * @param index the index of the subterm to retrieve
     * @return the stored subterm
     */
    public PiTerm subterm(int index) { return this.subterms.get(index); }

    /**
     * Determine the number of terms in this parallel composition.
     * @return  the number of terms in this parallel composition
     */
    public int arity() { return this.subterms.size(); }

    /**
     * Obtain an iterator over the elements in this parallel composition.
     * @return an iterator over the elements in this parallel composition.
     */
    public Iterator<PiTerm> iterator() { return this.subterms.iterator(); }

    /**
     * Enumerate the binders in this PiTermManySub.
     * @return a HashSet of the binders in this PiTermManySub
     */
    public HashSet<Integer> binders() {
        HashSet<Integer> binders = new HashSet<Integer>();
        for(PiTerm subterm : subterms) {
            for(Integer binder : subterm.binders()) { binders.add(binder); }
        }
        return binders;
    }

    /**
     * Obtain an ArrayList with copies of the contained subterms.
     * @return an ArrayList with copies of the contained subterms
     */
    protected ArrayList<PiTerm> copySubs() {
        ArrayList<PiTerm> copies = new ArrayList<PiTerm>();
        for(PiTerm term : this.subterms) { copies.add(term); }
        return copies;
    }

    /**
     * Carelessly rename every occurence of 'from' with 'to'.
     * @param from names with this value are renamed
     * @param to names being renamed are renamed to this name
     */
    public void blindRename(int from, int to) {
        for(PiTerm subterm : this.subterms) {
            subterm.blindRename(from, to);
        }
    }
}
