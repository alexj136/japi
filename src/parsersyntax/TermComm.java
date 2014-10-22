package parsersyntax;

/**
 * TermComm represents at a higher level those terms that are involved in
 * message passing. This includes Send and Receive.
 */
public abstract class TermComm extends TermOneSub {

    private String chnl;
    private String[] msg;

    /**
     * Construct a new communicating term
     * @param chnl the channel to communicate over
     * @param msg the names to send or to bind received names to
     * @param subterm the subterm of this
     */
    public Receive(String chnl, String[] msg, Term subterm) {
        super(subterm);
        this.chnl = chnl;
        this.msg = msg;
    }

    /**
     * Access the channel name on which a message can be received or sent.
     * @return the channel name on which a message can be received or sent
     */
    public String chnl() { return this.chnl; }

    /**
     * Access elements in the array of message content/binding names.
     * @param i the index of the value to be retreived
     * @return the i^th message/binding name
     */
    public String msg(int i) { return this.msg[i]; }

    /**
     * Determine the arity (the number of names to bind or send) of this
     * TermComm.
     * @return the arity (the number of names to bind or send) of this TermComm
     */
    public int arity() { return this.msg.length; }
    
    /**
     * Must be defined in the concrete classes.
     * @return a string representation of this TermComm
     */
    public abstract String toString();
}
