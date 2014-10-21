package parsersyntax;

/**
 * A Send object sends a message (a name) on a particular channel when there is
 * a corresponding Receive term to receive it.
 */
public class Send extends Term {

    private String chnl;
    private String[] msg;
    private Term subterm;

    /**
     * Construct a new Send object.
     * @param chnl the channel name over which to send the message
     * @param msg the message (a list of channel names) being sent
     * @param subterm the term to 'become' once the message is sent
     */
    public Send(String chnl, String[] msg, Term subterm) {
        this.chnl = chnl;
        this.msg = msg;
        this.subterm = subterm;
    }

    /**
     * Access the channel over which the message is to be sent.
     * @return the channel over which the message is to be sent
     */
    public String chnl() { return this.chnl; }

    /**
     * Access a particular channel name which is to be sent as part of the
     * message.
     * @param i the index of the message component to be retrieved
     * @return the channel name which is to be sent as the i^th part of the
     * message
     */
    public String msg(int i) { return this.msg[i]; }

    /**
     * Access the process to be executed once the message has been sent.
     * @return the process to be executed once the message has been sent.
     */
    public Term subterm() { return this.subterm; }

    /**
     * Determine the arity (the number of names in the message) of this Send.
     * @return the arity (the number of names in the message) of this Send
     */
    public int arity() { return this.msg.length; }

    /**
     * Obtain a string representation of this Send.
     * @return a string representing this Send
     */
    @Override
    public String toString() {
        return Term.stringifyList("<", ">", ",", this.msg);
    }
}
