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
    public Receive(Integer chnl, ArrayList<Integer> boundNames,
            PiTerm subterm) {

        super(chnl, subterm);
        if(new HashSet<Integer>(boundNames).size() < boundNames.size()) {
            throw new IllegalArgumentException("Duplicate binders in Receive");
        }
        this.boundNames = boundNames;
    }

    /**
     * Access the i^th bound name.
     * @param i the index of the bound name to be retrieved
     * @return the i^th bound name
     */
    public Integer name(Integer i) { return this.boundNames.get(i); }

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
    public boolean binds(Integer name) {
        return this.boundNames.contains(name);
    }

    /**
     * Set the value of a particular name in this Receive.
     * @param index the binder to reset
     * @param name the new value
     */
    public void setName(int index, Integer name) {
        this.boundNames.remove(index);
        this.boundNames.add(index, name);
    }

    /**
     * Enumerate the binders in this Receive.
     * @return a HashSet of the binders in this Receive
     */
    @Override
    public HashSet<Integer> binders() {
        HashSet<Integer> subBinders = this.subterm.binders();
        for(Integer name : this.boundNames) { subBinders.add(name); }
        return subBinders;
    }

    /**
     * Enumerate the free variable names in this Receive.
     * @return a HashSet of the free variable names in this Receive
     */
    public HashSet<Integer> freeVars() {
        HashSet<Integer> freeVars = this.subterm.freeVars();
        for(Integer name : this.boundNames) {
            if(freeVars.contains(name)) { freeVars.remove(name); }
        }
        freeVars.add(this.chnl);
        return freeVars;
    }

    /**
     * Rename the free occurrences of 'from' to 'to' in this Receive.
     * @param from all free occurrences of this name are changed
     * @param to names being changed are replaced with this value
     */
    public void renameFree(Integer from, Integer to) {
        if(this.chnl.equals(from)) { this.chnl = to; }
        if(!this.binds(from)) { this.subterm.renameFree(from, to); }
    }

    /**
     * Rename the binding or bound occurrences of 'from' to 'to' in this
     * Receive.
     * @param from all binding or bound occurrences of this name are changed
     * @param to names being changed are replaced with this value
     */
    public void renameNonFree(Integer from, Integer to) {
        boolean found = false;
        int i = 0;
        while(i < this.arity() && !found) {
            if(this.name(i).equals(from)) {
                this.subterm.blindRename(from, to);
                this.setName(i, to);
                found = true;
            }
            i++;
        }
        if(!found) { this.subterm.renameNonFree(from, to); }
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
                new ArrayList<Integer>(this.boundNames),
                this.subterm.copy());
    }

    /**
     * Carelessly rename within this Receive.
     * @param from all names with this value are renamed
     * @param to all names being renamed are renamed to this name
     */
    public void blindRename(Integer from, Integer to) {
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
