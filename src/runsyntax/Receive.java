package runsyntax;

/**
 * Represents a process waiting to send a message.
 */
public class Receive extends Term {

    private int receiveOn;
    private int bindTo;
    private Term subprocess;

    /**
     * Construct a new Receive.
     * @param receiveOn the channel on which to receive a message
     * @param bindTo the channel name to which the received message will be
     * bound
     * @param subprocess the process to be executed after the message is
     * received
     */
    public Receive(int receiveOn, int bindTo, Term subprocess) {
        this.receiveOn = receiveOn;
        this.bindTo = bindTo;
        this.subprocess = subprocess;
    }

    /**
     * Rename this Receive Term as appropriate when a message is received by a
     * 'higher-up' Receive Term.
     * @param from names with this value are renamed if they are free and not
     * binders
     * @param to if a name is renamed, it is renamed to this name
     */
    public void rename(int from, int to) {
        if(this.receiveOn == from) { this.receiveOn = to; }
         // Do not rename bindTo, in accordance with the semantics of the pi
         // calculus. Only rename in the subprocess if bindTo was not equal to
         // from.
        if(this.bindTo != from) {
            this.subprocess.rename(from, to);
        }
    }
}
