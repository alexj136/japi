package interpreter;

import syntax.*;

import java.util.ArrayList;

/**
 * The Interpreter class contains methods used for interpreting pi calculus
 * ASTs.
 */
public class Interpreter {

    private class MultiParallels extends Term {

        private ArrayList<Send> senders;
        private ArrayList<Receive> receivers;
        private ArrayList<Replicate> replicators;

        private MultiParallels(Term term) {
            this.senders = new ArrayList<Send>();
            this.receivers = new ArrayList<Receive>();
            this.replicators = new ArrayList<Replicate>();
            this.assimilate(term);
        }

        // Collect as many parallel terms as possible into one MultiParallels
        // Term
        private void assimilate(Term term) {
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
            else if(term instanceof Done) {
                // Do nothing
            }
            else {
                throw new IllegalArgumentException("Non-standard Term found " +
                        "in handleTerm()");
            }
        }

        // Take all collected parallel terms and build them back into a 'pure'
        // term with only binary parallel composition
        private Term collapse() {
            throw new UnsupportedOperationException("Not implemented");
        }

        public void rename(Name from, Name to) {
            throw new UnsupportedOperationException("Not implemented");
        }
        public String prettyPrint(int indentationLevel) {
            throw new UnsupportedOperationException("Not implemented");
        }
    }

    private Term term;

    /**
     * Construct an Interpreter to operate on the given Term.
     * @param term the term to be interpreted
     * @return a new Interpreter
     */
    public Interpreter(Term term) { this.term = term; }

    /**
     * Perform reduction on the stored Term.
     */
    public void reduceTerm() throws NameRepresentationException {
        // Propagate the call to the appropriate handler for each type of Term
        if(this.term instanceof Send) {
            this.term = reduceSend((Send) this.term);
        }
        else if(this.term instanceof Receive) {
            this.term = reduceReceive((Receive) this.term);
        }
        else if(this.term instanceof Parallel) {
            this.term = reduceParallel((Parallel) this.term);
        }
        else if(this.term instanceof Restrict) {
            this.term = reduceRestrict((Restrict) this.term);
        }
        else if(this.term instanceof Replicate) {
            this.term = reduceReplicate((Replicate) this.term);
        }
        else if(this.term instanceof Done) {
            this.term = reduceDone((Done) this.term);
        }

        // This block should be unreachable.
        else {
            throw new IllegalArgumentException("Found a Term that isn't " +
                    "recognised by Interpreter.reduceTerm()");
        }
    }

    /**
     * Perform reduction on a given Send Term.
     * @param send the Send Term to reduce
     * @return a term that is a reduction of the given term
     */
    public static Term reduceSend(Send send) {
        // Send Terms cannot be reduced, and the pi calculus semantics do not
        // allow us to reduce the subexpression within the Send. Therefore, do
        // nothing, and return the given Send as is.
        return send;
    }

    /**
     * Perform reduction on a given Receive Term.
     * @param rece the Receive Term to reduce
     * @return a term that is a reduction of the given term
     */
    public static Term reduceReceive(Receive rece) {
        // Receive Terms cannot be reduced, and the pi calculus semantics do not
        // allow us to reduce the subexpression within the Receive. Therefore,
        // do nothing, and return the given Receive as is.
        return rece;
    }

    /**
     * Perform reduction on a given Parallel Term.
     * @param para the Parallel Term to reduce
     * @return a term that is a reduction of the given term
     */
    public static Term reduceParallel(Parallel para) {
        throw new UnsupportedOperationException("Reduction of Parallel Terms " +
                "not yet implemented");
    }

    /**
     * Perform reduction on a given Restrict Term.
     * @param rest the Restrict Term to reduce
     * @return a term that is a reduction of the given term
     */
    public static Term reduceRestrict(Restrict rest) {
        throw new UnsupportedOperationException("Reduction of Restrict Terms " +
                "not yet implemented");
    }

    /**
     * Perform reduction on a given Replicate Term.
     * @param repl the Replicate Term to reduce
     * @return a term that is a reduction of the given term
     */
    public static Term reduceReplicate(Replicate repl) {
        throw new UnsupportedOperationException("Reduction of Replicate " +
                "Terms not yet implemented");
    }

    /**
     * Perform reduction on a given Done Term.
     * @param done the Done Term to reduce
     * @return a term that is a reduction of the given term
     */
    public static Term reduceDone(Done done) {
        // Nothing to do
        return done;
    }
}
