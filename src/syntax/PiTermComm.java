package syntax;

import java.util.ArrayList;
/**
 * PiTermComm represents at a higher level those terms that are involved in
 * message passing. This includes Send and Receive.
 */
public abstract class PiTermComm extends PiTermOneSub {

    protected Integer chnl;

    /**
     * Construct a new communicating term
     * @param chnl the channel to communicate over
     * @param subterm the subterm of this
     */
    public PiTermComm(Integer chnl, PiTerm subterm) {
        super(subterm);
        this.chnl = chnl;
    }

    /**
     * Access the channel name on which a message can be received or sent.
     * @return the channel name on which a message can be received or sent
     */
    public Integer chnl() { return this.chnl; }

    /**
     * Set the channel name.
     * @param chnl the new channel name
     */
    public void setChnl(Integer chnl) { this.chnl = chnl; }
}
