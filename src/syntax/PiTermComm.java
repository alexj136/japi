package syntax;

import java.util.ArrayList;
/**
 * PiTermComm represents at a higher level those terms that are involved in
 * message passing. This includes Send and Receive.
 */
public abstract class PiTermComm<T> extends PiTermOneSub<T> {

    protected T chnl;

    /**
     * Construct a new communicating term
     * @param chnl the channel to communicate over
     * @param subterm the subterm of this
     */
    public PiTermComm(T chnl, PiTerm<T> subterm) {
        super(subterm);
        this.chnl = chnl;
    }

    /**
     * Access the channel name on which a message can be received or sent.
     * @return the channel name on which a message can be received or sent
     */
    public T chnl() { return this.chnl; }

    /**
     * Set the channel name.
     * @param chnl the new channel name
     */
    public void setChnl(T chnl) { this.chnl = chnl; }
}
