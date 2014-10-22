package parsersyntax;

/**
 * The Receive class represents a process waiting for a message. Once it
 * receives its message, it binds the message to a variable and 'disappears',
 * leaving behind its subterm in its place.
 */
public class Receive extends TermComm {

    /**
     * Construct a new recieving process
     * @param chnl the channel to listen to for a message
     * @param binds the names to bind the received message components to
     * @param subterm the process to 'become' when the message is received
     * @return a new Receive object
     */
    public Receive(String chnl, String[] binds, Term subterm) {
        super(chnl, msg, subterm);
    }

    /**
     * Obtain a string representation of this Receive.
     * @return a string representing this Receive
     */
    @Override
    public String toString() {
        return Term.stringifyList("(", ")", ",", this.msg);
    }
}
