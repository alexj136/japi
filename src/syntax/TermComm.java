package syntax;

import java.util.ArrayList;
/**
 * TermComm represents at a higher level those terms that are involved in
 * message passing. This includes Send and Receive.
 */
public abstract class TermComm<T> extends TermOneSub<T> {

    protected T chnl;
    protected ArrayList<T> msg;

    /**
     * Construct a new communicating term
     * @param chnl the channel to communicate over
     * @param msg the names to send or to bind received names to
     * @param subterm the subterm of this
     */
    public TermComm(T chnl, ArrayList<T> msg, Term<T> subterm) {
        super(subterm);
        this.chnl = chnl;
        this.msg = msg;
    }

    /**
     * Access the channel name on which a message can be received or sent.
     * @return the channel name on which a message can be received or sent
     */
    public T chnl() { return this.chnl; }

    /**
     * Access elements in the array of message content/binding names.
     * @param i the index of the value to be retreived
     * @return the i^th message/binding name
     */
    public T msg(int i) { return this.msg.get(i); }

    /**
     * Determine the arity (the number of names to bind or send) of this
     * TermComm.
     * @return the arity (the number of names to bind or send) of this TermComm
     */
    public int arity() { return this.msg.size(); }
    
    /**
     * Must be defined in the concrete classes.
     * @return a string representation of this TermComm
     */
    public abstract String toString();

    /**
     * Rename the names in a Term as is necessary after the exchange of a
     * message - this is not alpha-conversion.
     * @param from some names of this value must be renamed
     * @param to names being renamed are renamed to this value
     */
    public abstract void rename(T from, T to);

    /**
     * Rename every single occurence of the first given name with the second
     * given name.
     * @param from all names of this value must be renamed
     * @param to names being renamed are renamed to this value
     */
    public abstract void alphaConvert(T from, T to);

    /**
     * Deep-copy this TermComm.
     * @return a deep-copy of this TermComm
     */
    public abstract TermComm<T> copy();
}
