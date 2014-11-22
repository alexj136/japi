package syntax;

import utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The Receive class represents a process waiting for a message. Once it
 * receives its message, it binds the message to a variable and 'disappears',
 * leaving behind its subterm in its place.
 */
public final class Receive extends PiTermComm {

    private ArrayList<Integer> boundNames;

    /**
     * Construct a new recieving process.
     * @param chnl the channel to listen to for a message
     * @param msg the names to bind the received message components to
     * @param subterm the process to 'become' when the message is received
     * @return a new Receive object
     */
    public Receive(int chnl, ArrayList<Integer> boundNames, PiTerm subterm) {
        super(chnl, subterm);
        this.boundNames = boundNames;
    }

    /**
     * Access the i^th bound name.
     * @param i the index of the bound name to be retrieved
     * @return the i^th bound name
     */
    public int name(int i) { return this.boundNames.get(i); }

    /**
     * Determine the arity (the number of bound names) of this Receive.
     * @return the arity (the number of bound names) of this Receive
     */
    public int arity() { return this.boundNames.size(); }

    /**
     * Determine if this Receive binds the given name.
     * @param name the name to test
     * @return true if name is bound by this Receive, false otherwise
     */
    public boolean binds(int name) {
        return this.boundNames.contains(name);
    }

    /**
     * Enumerate the binders in this Receive.
     * @return a HashSet of the binders in this Receive
     */
    @Override
    public HashSet<Integer> binders() {
        HashSet<Integer> subBinders = super.binders();
        for(Integer name : this.boundNames) { subBinders.add(name); }
        return subBinders;
    }

    /**
     * Obtain a string representation of this Receive.
     * @return a string representing this Receive
     */
    @Override
    public String toString() {
        return this.chnl + " " +
                Utils.stringifyList("(", ")", ",", this.boundNames) +
                " . " + this.subterm;
    }

    /**
     * Obtain a string representation of this Receive, using a different name
     * type.
     * @return a string representing the Receive, printing names of a different
     * type, the values of which are mapped to by the contained names.
     */
    public String toStringWithNameMap(HashMap<Integer, String> nameMap) {
        ArrayList<String> nameStrs = new ArrayList<String>();
        for(Integer name : this.boundNames) {
            nameStrs.add(nameMap.get(name).toString());
        }
        return nameMap.get(this.chnl) + " " +
                Utils.stringifyList("( ", " )", ", ", nameStrs) + " . " +
                this.subterm.toStringWithNameMap(nameMap);
    }

    /**
     * Copy this Receive.
     * @return a copy of this Receive.
     */
    public Receive copy() {
        return new Receive(this.chnl,
                (ArrayList<Integer>) this.boundNames.clone(),
                this.subterm.copy());
    }

    /**
     * Carelessly rename within this Receive.
     * @param from all names with this value are renamed
     * @param to all names being renamed are renamed to this name
     */
    public void blindRename(int from, int to) {
        if(this.chnl.equals(from)) { this.chnl = to; }
        for(int i = 0; i < this.arity(); i++) {
            if(this.name(i).equals(from)) {
                this.boundNames.remove(i);
                this.boundNames.add(i, to);
            }
        }
        this.subterm.blindRename(from, to);
    }
}
