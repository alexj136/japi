package syntax;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Parallel class represents parallel composition - it contains two
 * concurrently executing processes.
 */
public final class Parallel<T> extends PiTermManySub<T> {

    /**
     * Construct a new Parallel object using a list of concurrent processes.
     * @param subterms a list of the subterms of this Parallel
     */
    public Parallel(ArrayList<PiTerm<T>> subterms) { super(subterms); }

    /**
     * Obtain a string representation of this Parallel, using a different name
     * type.
     * @return a string representing the Parallel, printing names of a different
     * type, the values of which are mapped to by the contained names.
     */
    public <U> String toStringWithNameMap(HashMap<T, U> nameMap) {
        ArrayList<String> strSubs = new ArrayList<String>();
        for(PiTerm<T> subterm : this.subterms) {
            strSubs.add(subterm.toStringWithNameMap(nameMap));
        }
        return PiTerm.stringifyList("[", "]", " |", strSubs);
    }

    /**
     * Obtain a string representation of this Parallel.
     * @return a string representation of this Parallel
     */
    @Override
    public String toString() {
        return PiTerm.stringifyList("[ ", " ]", " | ", this.subterms);
    }

    /**
     * Deep copy this Parallel.
     * @return a deep copy of this Parallel
     */
    public Parallel<T> copy() {
        return new Parallel(this.copySubs());
    }
}
