package syntax;

import java.util.ArrayList;
/**
 * PiTermComm represents at a higher level those terms that are involved in
 * message passing. This includes Send and Receive.
 */
public abstract class PiTermComm<T> extends PiTermOneSub<T> {

    protected T chnl;
    protected ArrayList<LambdaTerm<T>> msg;

    /**
     * Construct a new communicating term
     * @param chnl the channel to communicate over
     * @param msg the expressions to send or names to bind received expressions
     * to
     * @param subterm the subterm of this
     */
    public PiTermComm(T chnl, ArrayList<LambdaTerm<T>> msg, PiTerm<T> subterm) {
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
    public LambdaTerm<T> msg(int i) { return this.msg.get(i); }

    /**
     * Determine the arity (the number of names to bind or send) of this
     * PiTermComm.
     * @return the arity (the number of names to bind or send) of this
     * PiTermComm
     */
    public int arity() { return this.msg.size(); }

    /**
     * Carelessly rename this communicating term. Send and Receive have the same
     * behaviour here.
     * @param from all names with this value are renamed
     * @param to all names being renamed are renamed to this name
     */
    public void blindRename(T from, T to) {
        if(this.chnl.equals(from)) { this.chnl = to; }
        for(int i = 0; i < this.arity(); i++) {
            this.msg(i).blindRename(from, to);
        }
        this.subterm.blindRename(from, to);
    }
}
