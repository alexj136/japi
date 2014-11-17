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

    /**
     * Construct a new recieving process
     * @param chnl the channel to listen to for a message
     * @param msg the names to bind the received message components to
     * @param subterm the process to 'become' when the message is received
     * @return a new Receive object
     */
    public Receive(T chnl, ArrayList<LambdaTerm<T>> msg, PiTerm<T> subterm) {
        super(chnl, msg, subterm);
        for(LambdaTerm<T> msgi : msg) {
            if(!(msgi instanceof Variable)) {
                throw new IllegalStateException("Non-Variable LambdaTerm in " +
                        "Receive");
            }
        }
    }

    /**
     * Obtain a string representation of this Receive.
     * @return a string representing this Receive
     */
    @Override
    public String toString() {
        return this.chnl + " " + Utils.stringifyList("(", ")", ",", this.msg) +
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
        for(LambdaTerm<T> name : this.msg) {
            nameStrs.add(name.toStringWithNameMap(nameMap));
        }
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
            Variable var = (Variable) with;
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
        ArrayList<LambdaTerm<T>> copyExps = new ArrayList<LambdaTerm<T>>();
        for(LambdaTerm<T> exp : this.msg) {
            copyExps.add(exp.copy());
        }
        return new Receive<T>(this.chnl, copyExps, this.subterm.copy());
    }

    /**
     * Determine if this Receive binds the given name.
     * @param name the name to test
     * @return true if name is bound by this Receive, false otherwise
     */
    public boolean binds(T name) {
        boolean found;
        int msgIdx = 0;
        while((!found) && msgIdx < this.arity()) {
            if(this.msg(msgIdx) instanceof Variable) {
                Variable var = (Variable) this.msg(msgIdx);
                if(var.name().equals(name)) {
                    found = true;
                }
            }
            else {
                throw new IllegalStateException("Non-Variable LambdaTerm in " +
                        "Receive");
            }
            msgIdx++;
        }
        return found;
    }
}
