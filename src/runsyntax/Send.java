package runsyntax;

/**
 * A Send object sends a message (a name i.e. an int) on a particular channel
 * when there is a corresponding Receive process to receive it.
 */
public class Send extends Term {

    private int sendOn;
    private int toSend;
    private Term subprocess;

    /**
     * Construct a new Send object.
     * @param sendOn the channel name over which to send the message
     * @param toSend the message (a channel name i.e. an int) being sent
     * @param subprocess the process to 'become' once the message is sent
     * @return a new Send object
     */
    public Send(int sendOn, int toSend, Term subprocess) {
        this.sendOn = sendOn;
        this.toSend = toSend;
        this.subprocess = subprocess;
    }

    /**
     * Access the channel over which the message is to be sent.
     * @return the channel over which the message is to be sent
     */
    public int getSendOn() { return this.sendOn; }

    /**
     * Access the channel name which is to be sent as a message.
     * @return the channel name which is to be sent as a message
     */
    public int getToSend() { return this.toSend; }

    /**
     * Access the process to be executed once the message has been sent.
     * @return the process to be executed once the message has been sent.
     */
    public Term getSubprocess() { return this.subprocess; }

    /**
     * Rename the names within this Send as though a message had been received
     * by a containing process
     * @param from names with this value are renamed
     * @param to names being renamed are renamed to this name
     */
    public void rename(int from, int to) {
        if(this.sendOn == from) { this.sendOn = to; }
        if(this.toSend == from) { this.toSend = to; }
        this.subprocess.rename(from, to);
    }

    public String toString() {
        return "-c" + this.sendOn + "<c" + this.toSend + ">." + this.subprocess;
    }
}
