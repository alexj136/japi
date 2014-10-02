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
