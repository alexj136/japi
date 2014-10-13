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
     * Access the channel name on which a message can be received.
     * @return the channel name on which a message can be received
     */
    public int getReceiveOn() { return this.receiveOn; }

    /**
     * Access the channel name to which the received name is to be bound.
     * @return the channel name to which the received name is to be bound
     */
    public int getBindTo() { return this.bindTo; }

    /**
     * Access the process to be executed once a message has be received.
     * @return the process to be executed once a message has be received
     */
    public Term getSubprocess() { return this.subprocess; }

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

    /**
     * Alpha-convert names in a Receive node as is required when performing
     * scope-extrusion.
     * @param from all names of this value are alpha-converted
     * @param to alpha-converted names are alpha-converted to this name
     */
    public void alphaConvert(int from, int to) {
        if(this.receiveOn == from) { this.receiveOn = to; }
        if(this.bindTo == from) { this.bindTo = to; }
        this.subprocess.alphaConvert(from, to);
    }

    /**
     * Deep-copy this Receive.
     * @return a deep-copy of this Receive
     */
    public Receive copy() {
        return new Receive(this.receiveOn, this.bindTo, this.subprocess.copy());
    }

    /**
     * Simple toString that just uses the integer names.
     * @return a not-very-nice string representation of this object
     */
    public String toString() {
        return "c" + this.receiveOn + "(c" + this.bindTo + ")." +
                this.subprocess;
    }
}
