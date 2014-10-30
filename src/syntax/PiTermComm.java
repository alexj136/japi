package syntax;

import java.util.ArrayList;
/**
 * PiTermComm represents at a higher level those terms that are involved in
 * message passing. This includes Send and Receive.
 */
public abstract class PiTermComm<T> extends PiTermOneSub<T> {

    protected T chnl;
    protected ArrayList<T> msg;

    /**
     * Construct a new communicating term
     * @param chnl the channel to communicate over
     * @param msg the names to send or to bind received names to
     * @param subterm the subterm of this
     */
    public PiTermComm(T chnl, ArrayList<T> msg, PiTerm<T> subterm) {
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
     * PiTermComm.
     * @return the arity (the number of names to bind or send) of this
     * PiTermComm
     */
    public int arity() { return this.msg.size(); }

    /**
     * Alpha-convert this communicating term. Send and Receive have the same
     * behaviour under alpha conversion.
     * @param from names with this value are renamed
     * @param to names being renamed are renamed to this name
     */
    public void alphaConvert(T from, T to) {
        if(this.chnl.equals(from)) { this.chnl = to; }
        for(int i = 0; i < this.arity(); i++) {
            if(this.msg(i).equals(from)) {
                this.msg.remove(i);
                this.msg.add(i, to);
            }
        }
        this.subterm.alphaConvert(from, to);
    }
}
