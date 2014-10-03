package interpreter;

import syntax.*;

/**
 * The Interpreter class contains methods used for interpreting pi calculus
 * ASTs.
 */
public class Interpreter {

    /**
     * Perform reduction on a Term passed inside a TermContainer object. No
     * external pointers should be kept to the underlying term, as it will be
     * muted by this method or callouts from this method.
     * @param term the term to reduce
     */
    public static void reduceTerm(TermContainer tc)
    throws NameRepresentationException {
        Term term = tc.get();

        // Propagate the call to the appropriate handler for each type of Term
        if(term instanceof Send) {
            term = reduceSend((Send) term);
        }
        else if(term instanceof Receive) {
            term = reduceReceive((Receive) term);
        }
        else if(term instanceof Parallel) {
            term = reduceParallel((Parallel) term);
        }
        else if(term instanceof Restrict) {
            term = reduceRestrict((Restrict) term);
        }
        else if(term instanceof Replicate) {
            term = reduceReplicate((Replicate) term);
        }
        else if(term instanceof Done) {
            term = reduceDone((Done) term);
        }

        // This block should be unreachable.
        else {
            throw new IllegalArgumentException("Found a Term that isn't " +
                    "recognised by Interpreter.reduceTerm()");
        }

        tc.set(term);
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
    public static Term reduceParallel(Parallel para)
    throws NameRepresentationException {
        Term lhs = para.getSubprocess1();
        Term rhs = para.getSubprocess2();

        // The simple case of a sender and receiver on the same channel
        if(lhs instanceof Send && rhs instanceof Receive &&
                ((Send) lhs).getSendOn().matches(((Receive) rhs).getReceiveOn())) {

            Term newLhs = ((Send) lhs).getSubprocess();
            Term newRhs = ((Receive) rhs).getSubprocess();
            newRhs.rename(((Receive) rhs).getBindTo(), ((Send) lhs).getToSend());

            return new Parallel(newLhs, newRhs);
        }
        
        throw new UnsupportedOperationException("Reduction of Parallel Terms " +
                "not yet fully implemented");
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
