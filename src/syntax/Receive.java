package syntax;

/**
 * The Receive class represents a process waiting for a message. Once it
 * receives its message, it binds the message to a variable and 'disappears',
 * leaving behind its subprocess in its place.
 */
public class Receive extends Term {

    private Name receiveOn;
    private Name bindTo;
    private Term subprocess;

    /**
     * Construct a new recieving process
     * @param receiveOn the channel to listen to for a message
     * @param bindTo the channel to bind the received message to
     * @param subprocess the process to 'become' when the message is received
     * @return a new Receive object
     */
    public Receive(Name receiveOn, Name bindTo, Term subprocess) {
        this.receiveOn = receiveOn;
        this.bindTo = bindTo;
        this.subprocess = subprocess;
    }

    /**
     * Access the channel name on which a message can be received.
     * @return the channel name on which a message can be received
     */
    public Name getReceiveOn() { return this.receiveOn; }

    /**
     * Access the channel name to which the received name is to be bound.
     * @return the channel name to which the received name is to be bound
     */
    public Name getBindTo() { return this.bindTo; }

    /**
     * Access the process to be executed once a message has be received.
     * @return the process to be executed once a message has be received
     */
    public Term getSubprocess() { return this.subprocess; }

    /**
     * In a Receive Term, only the channel to listen to is renamed if it matches
     * a received name. The binding channel is not renamed, as the semantics of
     * the pi calculus are such.
     * @param from The channel name on which a name was received
     * @param to The name that was sent - the receiving channel should be
     * renamed to this if it matches 'from'
     */
    @Override
    public void rename(Name from, Name to) throws NameRepresentationException {
        this.receiveOn.rename(from, to);
        // Do not rename bindTo, in accordance with the semantics of the pi
        // calculus. Only rename in the subprocess if bindTo was not equal to
        // from.
        if(!(this.bindTo.matches(from))) {
            this.subprocess.rename(from, to);
        }
    }

    /**
     * Obtain a pretty-printout of this Receive.
     * @param indentationLevel the number of tabs that should appear before the
     * text
     * @return a string representing this Receive
     */
    @Override
    public String prettyPrint(int indentationLevel) {
        return SyntaxElement.generateIndent(indentationLevel) +
                "receive " +
                bindTo.prettyPrint(indentationLevel) +
                " over " +
                receiveOn.prettyPrint(indentationLevel) +
                " then\n" +
                subprocess.prettyPrint(indentationLevel);
    }
}
