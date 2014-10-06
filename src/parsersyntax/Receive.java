package parsersyntax;

/**
 * The Receive class represents a process waiting for a message. Once it
 * receives its message, it binds the message to a variable and 'disappears',
 * leaving behind its subprocess in its place.
 */
public class Receive extends Term {

    private String receiveOn;
    private String bindTo;
    private Term subprocess;

    /**
     * Construct a new recieving process
     * @param receiveOn the channel to listen to for a message
     * @param bindTo the channel to bind the received message to
     * @param subprocess the process to 'become' when the message is received
     * @return a new Receive object
     */
    public Receive(String receiveOn, String bindTo, Term subprocess) {
        this.receiveOn = receiveOn;
        this.bindTo = bindTo;
        this.subprocess = subprocess;
    }

    /**
     * Access the channel name on which a message can be received.
     * @return the channel name on which a message can be received
     */
    public String getReceiveOn() { return this.receiveOn; }

    /**
     * Access the channel name to which the received name is to be bound.
     * @return the channel name to which the received name is to be bound
     */
    public String getBindTo() { return this.bindTo; }

    /**
     * Access the process to be executed once a message has be received.
     * @return the process to be executed once a message has be received
     */
    public Term getSubprocess() { return this.subprocess; }

    /**
     * Obtain a pretty-printout of this Receive.
     * @param indentLevel the number of tabs that should appear before the
     * text
     * @return a string representing this Receive
     */
    @Override
    public String prettyPrint(int indentLevel) {
        return Term.indent(indentLevel) + "receive " + this.bindTo + " over " +
                this.receiveOn + " then\n" +
                subprocess.prettyPrint(indentLevel);
    }
}
