package syntax;

import utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Parallel class represents parallel composition - it contains two
 * concurrently executing processes.
 */
public final class Parallel extends PiTermManySub {

    /**
     * Construct a new Parallel object using a list of concurrent processes.
     * @param subterms a list of the subterms of this Parallel
     */
    public Parallel(ArrayList<PiTerm> subterms) { super(subterms); }

    /**
     * Obtain a string representation of this Parallel, using a different name
     * type.
     * @return a string representing the Parallel, printing names of a different
     * type, the values of which are mapped to by the contained names.
     */
    public String toStringWithNameMap(HashMap<Integer, String> nameMap) {
        if(this.arity() < 1) { return "0"; }
        else {
            ArrayList<String> strSubs = new ArrayList<String>();
            for(PiTerm subterm : this.subterms) {
                strSubs.add(subterm.toStringWithNameMap(nameMap));
            }
            return Utils.stringifyList("[ ", " ]", " | ", strSubs);
        }
    }

    /**
     * Obtain a string representation of this Parallel.
     * @return a string representation of this Parallel
     */
    @Override
    public String toString() {
        if(this.arity() < 1) { return "0"; }
        else { return Utils.stringifyList("[ ", " ]", " | ", this.subterms); }
    }

    /**
     * Deep copy this Parallel.
     * @return a deep copy of this Parallel
     */
    public Parallel copy() {
        return new Parallel(this.copySubs());
    }
}
