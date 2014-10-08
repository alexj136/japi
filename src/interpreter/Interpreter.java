package interpreter;

import runsyntax.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Interpreter class contains methods used for interpreting pi calculus
 * ASTs.
 */
public class Interpreter {

    private Term term;
    private HashMap<String, Integer> nameMap;
    private int nextAvailableName;

    /**
     * Construct an Interpreter to operate on the given Term.
     * @param term the term to be interpreted
     * @return a new Interpreter
     */
    public Interpreter(Term term) {
        if(!(term instanceof Parallel))
        this.term = term;
    }

    /**
     * Perform reduction on the stored Term.
     */
    public void reduceTerm() {
        // Reduce the stored term
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
        else if(this.term instanceof End) {
            this.term = reduceEnd((End) this.term);
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
     * Perform reduction on a given End Term.
     * @param done the End Term to reduce
     * @return a term that is a reduction of the given term
     */
    public static Term reduceEnd(End end) {
        // Nothing to do
        return end;
    }
}
