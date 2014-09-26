package syntax;

/**
 * A Send object sends a message (a name) on a particular channel when there is
 * a corresponding Receive process to receive it.
 */
public class Send extends SyntaxElement {

    private Name sendOn;
    private Name toSend;
    private SyntaxElement subprocess;

    /**
     * Construct a new Send object.
     * @param sendOn the channel name over which to send the message
     * @param toSend the message (a channel name) being sent
     * @param subprocess the process to 'become' once the message is sent
     * @return a new Send object
     */
    public Send(Name sendOn, Name toSend, SyntaxElement subprocess) {
        this.sendOn = sendOn;
        this.toSend = toSend;
        this.subprocess = subprocess;
    }

    @Override
    public void rename(Name from, Name to) {
        this.sendOn.rename(from, to);
        this.toSend.rename(from, to);
        this.subprocess.rename(from, to);
    }
}
