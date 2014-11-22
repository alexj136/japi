package syntax;

import utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Send object sends a message (a name) on a particular channel when there is
 * a corresponding Receive term to receive it.
 */
public final class Send extends PiTermComm {

    private ArrayList<LambdaTerm> exps;

    /**
     * Construct a new Send object.
     * @param chnl the channel name over which to send the message
     * @param exps the message (a list of expressions) to evaluate an send
     * @param subterm the term to 'become' once the message is sent
     */
    public Send(int chnl, ArrayList<LambdaTerm> exps, PiTerm subterm) {
        super(chnl, subterm);
        this.exps = exps;
    }

    /**
     * Access elements in the array of message content/binding names.
     * @param i the index of the value to be retreived
     * @return the i^th message/binding name
     */
    public LambdaTerm exp(int i) { return this.exps.get(i); }

    /**
     * Determine the arity (the number of expressions to send) of this Send.
     * @return the arity (the number of expressions to send) of this Send
     */
    public int arity() { return this.exps.size(); }

    /**
     * Obtain a string representation of this Send.
     * @return a string representing this Send
     */
    @Override
    public String toString() {
        return this.chnl + " " + Utils.stringifyList("<", ">", ",", this.exps) +
                " . " + this.subterm;
    }

    /**
     * Obtain a string representation of this Send, using a different name type.
     * @return a string representing the Send, printing names of a different
     * type, the values of which are mapped to by the contained names.
     */
    public String toStringWithNameMap(HashMap<Integer, String> nameMap) {
        ArrayList<String> nameStrs = new ArrayList<String>();
        for(LambdaTerm name : this.exps) {
            nameStrs.add(name.toStringWithNameMap(nameMap));
        }
        return nameMap.get(this.chnl) + " " +
                Utils.stringifyList("< ", " >", ", ", nameStrs) + " . " +
                this.subterm.toStringWithNameMap(nameMap);
    }

    /**
     * Copy this Send.
     * @return a copy of this Send.
     */
    public Send copy() {
        ArrayList<LambdaTerm> copyExps = new ArrayList<LambdaTerm>();
        for(LambdaTerm exp : this.exps) {
            copyExps.add(exp.copy());
        }
        return new Send(this.chnl, copyExps, this.subterm.copy());
    }

    /**
     * Carelessly rename within this Send.
     * @param from all names with this value are renamed
     * @param to all names being renamed are renamed to this name
     */
    public void blindRename(int from, int to) {
        if(this.chnl.equals(from)) { this.chnl = to; }
        for(int i = 0; i < this.arity(); i++) {
            this.exp(i).blindRename(from, to);
        }
        this.subterm.blindRename(from, to);
    }
}
