package syntax;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Send object sends a message (a name) on a particular channel when there is
 * a corresponding Receive term to receive it.
 */
public final class Send<T> extends PiTermComm<T> {

    /**
     * Construct a new Send object.
     * @param chnl the channel name over which to send the message
     * @param msg the message (a list of channel names) being sent
     * @param subterm the term to 'become' once the message is sent
     */
    public Send(T chnl, ArrayList<T> msg, PiTerm<T> subterm) {
        super(chnl, msg, subterm);
    }

    /**
     * Obtain a string representation of this Send.
     * @return a string representing this Send
     */
    @Override
    public String toString() {
        return this.chnl + " " + PiTerm.stringifyList("<", ">", ",", this.msg) +
                " . " + this.subterm;
    }

    /**
     * Obtain a string representation of this Send, using a different name type.
     * @return a string representing the Send, printing names of a different
     * type, the values of which are mapped to by the contained names.
     */
    public <U> String toStringWithNameMap(HashMap<T, U> nameMap) {
        ArrayList<U> msgNames = new ArrayList<U>();
        for(T name : this.msg) { msgNames.add(nameMap.get(name)); }
        return nameMap.get(this.chnl) + " " +
                PiTerm.stringifyList("<", ">", ",", msgNames) + " . " +
                this.subterm.toStringWithNameMap(nameMap);
    }

    /**
     * Rename the names within this Send as though a message had been received
     * by a containing process
     * @param from names with this value are renamed
     * @param to names being renamed are renamed to this name
     */
    public void rename(T from, T to) {
        if(this.chnl.equals(from)) { this.chnl = to; }
        for(int i = 0; i < this.arity(); i++) {
            if(this.msg(i).equals(from)) {
                this.msg.remove(i);
                this.msg.add(i, to);
            }
        }
        this.subterm.rename(from, to);
    }

    /**
     * Copy this Send.
     * @return a copy of this Send.
     */
    public Send<T> copy() {
        return new Send<T>(this.chnl, (ArrayList<T>) this.msg.clone(),
                this.subterm.copy());
    }
}
