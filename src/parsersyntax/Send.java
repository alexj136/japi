package parsersyntax;

/**
 * A Send object sends a message (a name) on a particular channel when there is
 * a corresponding Receive process to receive it.
 */
public class Send extends Term {

    private String sendOn;
    private String toSend;
    private Term subprocess;

    /**
     * Construct a new Send object.
     * @param sendOn the channel name over which to send the message
     * @param toSend the message (a channel name) being sent
     * @param subprocess the process to 'become' once the message is sent
     * @return a new Send object
     */
    public Send(String sendOn, String toSend, Term subprocess) {
        this.sendOn = sendOn;
        this.toSend = toSend;
        this.subprocess = subprocess;
    }

    /**
     * Access the channel over which the message is to be sent.
     * @return the channel over which the message is to be sent
     */
    public String getSendOn() { return this.sendOn; }

    /**
     * Access the channel name which is to be sent as a message.
     * @return the channel name which is to be sent as a message
     */
    public String getToSend() { return this.toSend; }

    /**
     * Access the process to be executed once the message has been sent.
     * @return the process to be executed once the message has been sent.
     */
    public Term getSubprocess() { return this.subprocess; }

    /**
     * Obtain a pretty-printout of this Send.
     * @param indentLevel the number of tabs that should appear before the
     * text
     * @return a string representing this Send
     */
    @Override
    public String prettyPrint(int indentLevel) {
        return Term.indent(indentLevel) + "send " + this.toSend + " over " +
                this.sendOn + " then\n" + subprocess.prettyPrint(indentLevel);
    }
}
