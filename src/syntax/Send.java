package syntax;

import java.util.ArrayList;

/**
 * A Send object sends a message (a name) on a particular channel when there is
 * a corresponding Receive term to receive it.
 */
public class Send<T> extends TermComm<T> {

    /**
     * Construct a new Send object.
     * @param chnl the channel name over which to send the message
     * @param msg the message (a list of channel names) being sent
     * @param subterm the term to 'become' once the message is sent
     */
    public Send(T chnl, ArrayList<T> msg, Term<T> subterm) {
        super(chnl, msg, subterm);
    }

    /**
     * Obtain a string representation of this Send.
     * @return a string representing this Send
     */
    @Override
    public String toString() {
        return Term.stringifyList("<", ">", ",", this.msg);
    }
}
