package syntax;

/**
 * The Receive class represents a process waiting for a message. Once it
 * receives its message, it binds the message to a variable and 'disappears',
 * leaving behind its subprocess in its place.
 */
public class Receive extends SyntaxElement {

    private Name receiveOn;
    private Name bindTo;
    private SyntaxElement subprocess;

    /**
     * Construct a new recieving process
     * @param receiveOn the channel to listen to for a message
     * @param bindTo the channel to bind the received message to
     * @param subprocess the process to 'become' when the message is received
     * @return a new Receive object
     */
    public Receive(Name receiveOn, Name bindTo, SyntaxElement subprocess) {
        this.receiveOn = receiveOn;
        this.bindTo = bindTo;
        this.subprocess = subprocess;
    }
}
