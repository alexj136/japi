package runsyntax

/**
 * Represents n-ary parallel composition, and contains methods to handle
 * integration of newly exposed terms into that n-ary parallel composition.
 */
public class MultiParallels extends Term {

    private ArrayList<Send> senders;
    private ArrayList<Receive> receivers;
    private ArrayList<Replicate> replicators;

    public MultiParallels(Term term) {
        this.senders = new ArrayList<Send>();
        this.receivers = new ArrayList<Receive>();
        this.replicators = new ArrayList<Replicate>();
        this.assimilate(term);
    }

    // Collect as many parallel terms as possible into one MultiParallels
    // Term
    public void assimilate(Term term) {
        if(!(term instanceof Parallel)) {
            throw new IllegalArgumentException("Attempted to construct a " +
                    "MultiParallels with a non-Parallel Term");
        }

        Parallel para = (Parallel) term;
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
            this.assimilate(term);
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

    public void rename(int from, int to) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
