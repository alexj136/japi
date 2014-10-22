package syntax;

import java.util.ArrayList;

/**
 * The Receive class represents a process waiting for a message. Once it
 * receives its message, it binds the message to a variable and 'disappears',
 * leaving behind its subterm in its place.
 */
public class Receive<T> extends TermComm<T> {

    /**
     * Construct a new recieving process
     * @param chnl the channel to listen to for a message
     * @param binds the names to bind the received message components to
     * @param subterm the process to 'become' when the message is received
     * @return a new Receive object
     */
    public Receive(T chnl, ArrayList<T> binds, Term<T> subterm) {
        super(chnl, binds, subterm);
    }

    /**
     * Obtain a string representation of this Receive.
     * @return a string representing this Receive
     */
    @Override
    public String toString() {
        return this.chnl + " " + Term.stringifyList("(", ")", ",", this.msg) +
            " . " + this.subterm;
    }
}
