package syntax;

/**
 * A Send object sends a message (a name) on a particular channel when there is
 * a corresponding Receive process to receive it.
 */
public class Send extends Term {

    private Name sendOn;
    private Name toSend;
    private Term subprocess;

    /**
     * Construct a new Send object.
     * @param sendOn the channel name over which to send the message
     * @param toSend the message (a channel name) being sent
     * @param subprocess the process to 'become' once the message is sent
     * @return a new Send object
     */
    public Send(Name sendOn, Name toSend, Term subprocess) {
        this.sendOn = sendOn;
        this.toSend = toSend;
        this.subprocess = subprocess;
    }

    /**
     * Access the channel over which the message is to be sent.
     * @return the channel over which the message is to be sent
     */
    public Name getSendOn() { return this.sendOn; }

    /**
     * Access the channel name which is to be sent as a message.
     * @return the channel name which is to be sent as a message
     */
    public Name getToSend() { return this.toSend; }

    /**
     * Access the process to be executed once the message has been sent.
     * @return the process to be executed once the message has been sent.
     */
    public Term getSubprocess() { return this.subprocess; }

    /**
     * In a Send Term, both the message content and sending channel are to be
     * renamed if they match the name that was sent.
     * @param from The channel name on which a name was received
     * @param to The name that was sent - names should be renamed to this if
     * they match 'from'
     */
    @Override
    public void rename(Name from, Name to) throws NameRepresentationException {
        this.sendOn.rename(from, to);
        this.toSend.rename(from, to);
        this.subprocess.rename(from, to);
    }

    /**
     * Obtain a pretty-printout of this Send.
     * @param indentationLevel the number of tabs that should appear before the
     * text
     * @return a string representing this Send
     */
    @Override
    public String prettyPrint(int indentationLevel) {
        return SyntaxElement.generateIndent(indentationLevel) +
                "send " +
                toSend.prettyPrint(indentationLevel) +
                " over " +
                sendOn.prettyPrint(indentationLevel) +
                " then\n" +
                subprocess.prettyPrint(indentationLevel);
    }
}
