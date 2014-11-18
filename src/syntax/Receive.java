package syntax;

import utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Receive class represents a process waiting for a message. Once it
 * receives its message, it binds the message to a variable and 'disappears',
 * leaving behind its subterm in its place.
 */
public final class Receive<T> extends PiTermComm<T> {

    private ArrayList<T> boundNames;

    /**
     * Construct a new recieving process.
     * @param chnl the channel to listen to for a message
     * @param msg the names to bind the received message components to
     * @param subterm the process to 'become' when the message is received
     * @return a new Receive object
     */
    public Receive(T chnl, ArrayList<T> boundNames, PiTerm<T> subterm) {
        super(chnl, subterm);
        this.boundNames = boundNames;
    }

    /**
     * Access the i^th bound name.
     * @param i the index of the bound name to be retrieved
     * @return the i^th bound name
     */
    public T name(int i) { return this.boundNames.get(i); }

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
    public boolean binds(T name) {
        return this.boundNames.contains(name);
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
    public <U> String toStringWithNameMap(HashMap<T, U> nameMap) {
        ArrayList<String> nameStrs = new ArrayList<String>();
        for(T name : this.boundNames) { nameStrs.add(name.toString()); }
        return nameMap.get(this.chnl) + " " +
                Utils.stringifyList("( ", " )", ", ", nameStrs) + " . " +
                this.subterm.toStringWithNameMap(nameMap);
    }

    /**
     * Rename this Receive Term as appropriate when a message is received by a
     * 'higher-up' Receive Term.
     * @param from names with this value are renamed if they are free and not
     * binders
     * @param to if a name is renamed, it is renamed to this name
     */
    public void msgPass(T replacing, LambdaTerm<T> with) {
        if(replacing.equals(this.chnl) && !(with instanceof Variable)) {
            throw new IllegalArgumentException("Tried to replace a " +
                    "channel name with an expression");
        }
        else if(replacing.equals(this.chnl) && with instanceof Variable) {
            Variable<T> var = (Variable) with;
            this.chnl = var.name();
        }
         // Do not rename the message content, in accordance with the semantics
         // of the pi calculus. Only rename in the subprocess if the message did
         // not contain 'from'.
        if(!this.binds(replacing)) {
            this.subterm.msgPass(replacing, with);
        }
    }

    /**
     * Copy this Receive.
     * @return a copy of this Receive.
     */
    public Receive<T> copy() {
        return new Receive<T>(this.chnl, (ArrayList<T>) this.boundNames.clone(),
                this.subterm.copy());
    }

    /**
     * Carelessly rename within this Receive.
     * @param from all names with this value are renamed
     * @param to all names being renamed are renamed to this name
     */
    public void blindRename(T from, T to) {
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
