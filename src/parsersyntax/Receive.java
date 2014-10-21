package parsersyntax;

/**
 * The Receive class represents a process waiting for a message. Once it
 * receives its message, it binds the message to a variable and 'disappears',
 * leaving behind its subterm in its place.
 */
public class Receive extends Term {

    private String chnl;
    private String[] binds;
    private Term subterm;

    /**
     * Construct a new recieving process
     * @param chnl the channel to listen to for a message
     * @param binds the names to bind the received message components to
     * @param subterm the process to 'become' when the message is received
     * @return a new Receive object
     */
    public Receive(String chnl, String[] binds, Term subterm) {
        this.chnl = chnl;
        this.binds = binds;
        this.subterm = subterm;
    }

    /**
     * Access the channel name on which a message can be received.
     * @return the channel name on which a message can be received
     */
    public String chnl() { return this.chnl; }

    /**
     * Access the channel name to which the received name is to be bound.
     * @param i the index of the binder to be retrieved
     * @return the channel name to which the received name is to be bound
     */
    public String binds(int i) { return this.binds[i]; }

    /**
     * Access the process to be executed once a message has be received.
     * @return the process to be executed once a message has be received
     */
    public Term subterm() { return this.subterm; }

    /**
     * Determine the arity (the number of names to bind) of this Receive.
     * @return the arity (the number of names to bind) of this Receive
     */
    public int arity() { return this.binds.length; }

    /**
     * Obtain a string representation of this Receive.
     * @return a string representing this Receive
     */
    @Override
    public String toString() {
        return Term.stringifyList("(", ")", ",", this.msg);
    }
}
