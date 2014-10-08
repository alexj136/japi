package interpreter;

import runsyntax.*;
import java.util.ArrayList;

/**
 * Represents n-ary parallel composition, and contains methods to handle
 * integration of newly exposed terms into that n-ary parallel composition.
 */
public class InterpreterState {

    private ArrayList<Send> senders;
    private ArrayList<Receive> receivers;
    private ArrayList<Replicate> replicators;
    private ArrayList<Restrict> inactiveRestrictions;
    private ArrayList<Integer> activeRestrictions;

    /**
     */
    public InterpreterState(Parallel para) {
        this.senders = new ArrayList<Send>();
        this.receivers = new ArrayList<Receive>();
        this.replicators = new ArrayList<Replicate>();
        this.inactiveRestrictions = new ArrayList<Restrict>();
        this.activeRestrictions = new ArrayList<Integer>();
        this.assimilate(para);
    }

    // Collect as many parallel terms as possible into one MultiParallels
    // Term
    private void assimilate(Parallel para) {

        this.handleTerm(para.getSubprocess1());
        this.handleTerm(para.getSubprocess2());

        throw new UnsupportedOperationException("Not implemented");
    }

    // Add a newly encountered term to the appropriate arraylist
    private void handleTerm(Term term) {
        if(term instanceof Send) {
            this.senders.add((Send) term);
        }
        else if(term instanceof Receive) {
            this.receivers.add((Receive) term);
        }
        else if(term instanceof Replicate) {
            this.replicators.add((Replicate) term);
        }
        else if(term instanceof Parallel) {
            this.assimilate((Parallel) term);
        }
        else if(term instanceof Restrict) {
            throw new UnsupportedOperationException("Not implemented");
        }
        else if(term instanceof End) {
            // Do nothing
        }
        else {
            throw new IllegalArgumentException("Non-standard Term found " +
                    "in handleTerm()");
        }
    }

    // Take all collected parallel terms and build them back into a 'pure'
    // term with only binary parallel composition
    public Term collapse() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
