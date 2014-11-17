package syntax;

import utils.Utils;
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
    public Send(T chnl, ArrayList<LambdaTerm<T>> msg, PiTerm<T> subterm) {
        super(chnl, msg, subterm);
    }

    /**
     * Obtain a string representation of this Send.
     * @return a string representing this Send
     */
    @Override
    public String toString() {
        return this.chnl + " " + Utils.stringifyList("<", ">", ",", this.msg) +
                " . " + this.subterm;
    }

    /**
     * Obtain a string representation of this Send, using a different name type.
     * @return a string representing the Send, printing names of a different
     * type, the values of which are mapped to by the contained names.
     */
    public <U> String toStringWithNameMap(HashMap<T, U> nameMap) {
        ArrayList<String> nameStrs = new ArrayList<String>();
        for(LambdaTerm<T> name : this.msg) {
            nameStrs.add(name.toStringWithNameMap(nameMap));
        }
        return nameMap.get(this.chnl) + " " +
                Utils.stringifyList("< ", " >", ", ", nameStrs) + " . " +
                this.subterm.toStringWithNameMap(nameMap);
    }

    /**
     * Substitute the names within this Send with expressions, as though a
     * message had been received by a containing process.
     * @param replacing names with this value are replaced
     * @param with names being replaced are replaced with this expression
     */
    public void msgPass(T replacing, LambdaTerm<T> with) {
        if(replacing.equals(this.chnl) && !(with instanceof Variable)) {
            throw new IllegalArgumentException("Tried to replace a " +
                    "channel name with an expression");
        }
        if(replacing.equals(this.chnl) && with instanceof Variable) {
            Variable var = (Variable) with;
            this.chnl = var.name();
        }
        for(int i = 0; i < this.arity(); i++) {
            this.msg(i).renameFree(replacing, (T) new Object());
        }
        this.subterm.msgPass(replacing, with);
    }

    /**
     * Copy this Send.
     * @return a copy of this Send.
     */
    public Send<T> copy() {
        ArrayList<LambdaTerm<T>> copyExps = new ArrayList<LambdaTerm<T>>();
        for(LambdaTerm<T> exp : this.msg) {
            copyExps.add(exp.copy());
        }
        return new Send<T>(this.chnl, copyExps, this.subterm.copy());
    }
}
