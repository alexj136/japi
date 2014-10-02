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

    @Override
    public void rename(Name from, Name to) throws NameRepresentationException {
        this.sendOn.rename(from, to);
        this.toSend.rename(from, to);
        this.subprocess.rename(from, to);
    }

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
