package syntax;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The NDSum class represents n-ary nondeterministic sums. An NDSum PiTerm can
 * behave as though it were any one of its contained subterms.
 */
public final class NDSum<T> extends PiTermManySub<T> {

    /**
     * Construct a new NDSum object using a list of processes.
     * @param subterms the list of processes
     */
    public NDSum(ArrayList<PiTerm<T>> subterms) {
        super(subterms);
        if(subterms.size() < 2) {
            throw new IllegalArgumentException("Tried to create an NDSum " +
                    "with less than two subterms.");
        }
    }

    /**
     * Obtain a string representation of this NDSum.
     * @return a string representation of this NDSum
     */
    @Override
    public String toString() {
        return PiTerm.stringifyList("", "", " + ", this.subterms);
    }

    /**
     * Obtain a string representation of this NDSum, using a different name
     * type.
     * @return a string representing the NDSum, printing names of a different
     * type, the values of which are mapped to by the contained names.
     */
    public <U> String toStringWithNameMap(HashMap<T, U> nameMap) {
        ArrayList<String> strSubs = new ArrayList<String>();
        for(PiTerm<T> subterm : this.subterms) {
            strSubs.add(subterm.toStringWithNameMap(nameMap));
        }
        return PiTerm.stringifyList("", "", " + ", strSubs);
    }

    /**
     * Copy this NDSum.
     * @return a copy of this NDSum
     */
    public NDSum<T> copy() {
        return new NDSum(this.copySubs());
    }
}
