package syntax;

import java.util.HashMap;

public final class Tau extends PiTermOneSub {

    /**
     * Construct a new Tau action process.
     * @param subterm the subterm of the Tau action
     */
    public Tau(PiTerm subterm) {
        super(subterm);
    }

    /**
     * Obtain a string representation of this Tau action.
     * @return a string representing this Tau action
     */
    @Override
    public String toString() { return "~ . " + this.subterm.toString(); }

    /**
     * Obtain a string representation of this Tau action, using String names.
     * @return a string representing the Tau action
     */
    public String toStringWithNameMap(HashMap<Integer, String> nameMap) {
        return "~ . " + this.subterm.toStringWithNameMap(nameMap);
    }

    /**
     * Copy this Tau action.
     * @return a copy of this Tau action
     */
    public Tau copy() { return new Tau(this.subterm.copy()); }
}
