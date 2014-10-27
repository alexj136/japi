package syntax;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Receive class represents a process waiting for a message. Once it
 * receives its message, it binds the message to a variable and 'disappears',
 * leaving behind its subterm in its place.
 */
public final class Receive<T> extends TermComm<T> {

    /**
     * Construct a new recieving process
     * @param chnl the channel to listen to for a message
     * @param msg the names to bind the received message components to
     * @param subterm the process to 'become' when the message is received
     * @return a new Receive object
     */
    public Receive(T chnl, ArrayList<T> msg, Term<T> subterm) {
        super(chnl, msg, subterm);
    }

    /**
     * Obtain a string representation of this Receive.
     * @return a string representing this Receive
     */
    @Override
    public String toString() {
        return this.chnl + " " + Term.stringifyList("(", ")", ",", this.msg) +
            " . " + this.subterm;
    }

    /**
     * Obtain a string representation of this Receive, using a different name
     * type.
     * @return a string representing the Receive, printing names of a different
     * type, the values of which are mapped to by the contained names.
     */
    public <U> String toStringWithNameMap(HashMap<T, U> nameMap) {
        ArrayList<U> msgNames = new ArrayList<U>();
        for(T name : this.msg) { msgNames.add(nameMap.get(name)); }
        return nameMap.get(this.chnl) + " " +
                Term.stringifyList("(", ")", ",", msgNames) + " . " +
                this.subterm.toStringWithNameMap(nameMap);
    }

    /**
     * Rename this Receive Term as appropriate when a message is received by a
     * 'higher-up' Receive Term.
     * @param from names with this value are renamed if they are free and not
     * binders
     * @param to if a name is renamed, it is renamed to this name
     */
    public void rename(T from, T to) {
        if(this.chnl.equals(from)) { this.chnl = to; }
         // Do not rename the message content, in accordance with the semantics
         // of the pi calculus. Only rename in the subprocess if the message did
         // not contain 'from'.
        if(!this.msg.contains(from)) {
            this.subterm.rename(from, to);
        }
    }

    /**
     * Copy this Receive.
     * @return a copy of this Receive.
     */
    public Receive<T> copy() {
        return new Receive<T>(this.chnl, (ArrayList<T>) this.msg.clone(),
                this.subterm.copy());
    }
}
