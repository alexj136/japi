package syntax;

import utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
    public Send(Integer chnl, ArrayList<LambdaTerm> exps, PiTerm subterm) {
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
     * Setter for expressions in this Send.
     * @param index the index of the expression to set
     * @param exp the new value for this.exp(i)
     */
    public void setExp(int index, LambdaTerm exp) {
        this.exps.remove(index);
        this.exps.add(index, exp);
    }

    /**
     * Enumerate the binders in this Send.
     * @return a HashSet of the binders in this Send
     */
    public HashSet<Integer> binders() {
        return this.subterm.binders();
    }

    /**
     * Enumerate the free variable names in this Send.
     * @return a HashSet of the free variable names in this Send
     */
    public HashSet<Integer> freeVars() {
        HashSet<Integer> freeVars = this.subterm.freeVars();
        freeVars.add(this.chnl);
        for(LambdaTerm exp : this.exps) {
            for(Integer freeVar : exp.freeVars()) { freeVars.add(freeVar); }
        }
        return freeVars;
    }

    /**
     * Rename all free occurrences of 'from' to 'to' in this Send.
     * @param from free occurrences of this name are renamed
     * @param to renamed names are renamed to this value
     */
    public void renameFree(Integer from, Integer to) {
        if(this.chnl.equals(from)) { this.chnl = to; }
        for(LambdaTerm exp : this.exps) { exp.renameFree(from, to); }
        this.subterm.renameFree(from, to);
    }

    /**
     * Rename all binding and bound occurrences of 'from' to 'to' in this Send.
     * @param from binding and bound occurrences of this name are renamed
     * @param to renamed names are renamed to this value
     */
    public void renameNonFree(Integer from, Integer to) {
        for(LambdaTerm exp : this.exps) { exp.renameNonFree(from, to); }
        this.subterm.renameNonFree(from, to);
    }

    /**
     * Carelessly rename within this Send.
     * @param from all names with this value are renamed
     * @param to all names being renamed are renamed to this name
     */
    public void blindRename(Integer from, Integer to) {
        if(this.chnl.equals(from)) { this.chnl = to; }
        for(int i = 0; i < this.arity(); i++) {
            this.exp(i).blindRename(from, to);
        }
        this.subterm.blindRename(from, to);
    }

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
}
