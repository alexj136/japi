package interpreter;

import runsyntax.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents n-ary parallel composition, and contains methods to handle
 * integration of newly exposed terms into that n-ary parallel composition.
 */
public class InterpreterState {

    private HashMap<String, Integer> nameMap;
    private int nextAvailableName;

    private ArrayList<Send> senders;
    private ArrayList<Receive> receivers;
    private ArrayList<Replicate> replicators;
    private ArrayList<Restrict> restrictions;
    private ArrayList<Integer> boundNames;

    /**
     */
    public InterpreterState(Term term, HashMap<String, Integer> nameMap,
            int nextAvailableName) {

        this.nameMap = nameMap;
        this.nextAvailableName = nextAvailableName;

        this.senders = new ArrayList<Send>();
        this.receivers = new ArrayList<Receive>();
        this.replicators = new ArrayList<Replicate>();
        this.restrictions = new ArrayList<Restrict>();
        this.boundNames = new ArrayList<Integer>();

        this.integrateNewlyExposedTerm(term);
    }


    // Add a newly exposed term to the appropriate arraylist
    private void integrateNewlyExposedTerm(Term term) {
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
            this.integrateNewlyExposedTerm(((Parallel) term).getSubprocess1());
            this.integrateNewlyExposedTerm(((Parallel) term).getSubprocess2());
        }
        else if(term instanceof Restrict) {
            this.restrictions.add((Restrict) term);
        }
        else if(term instanceof End) {
            // Do nothing
        }
        else {
            throw new IllegalArgumentException("Non-standard Term found " +
                    "in handleTerm()");
        }
    }

    /**
     * Obtain a String representation of the 'term' in its current state.
     * @return a String representation of the 'term' in its current state
     */
    public String toString() {
        ArrayList<String> termStrings = new ArrayList<String>();
        for(Send send : senders) { termStrings.add(send.toString()); }
        for(Receive rece : receivers) { termStrings.add(rece.toString()); }
        for(Replicate repl : replicators) { termStrings.add(repl.toString()); }
        for(Restrict rest : restrictions) { termStrings.add(rest.toString()); }
        if(termStrings.isEmpty()) { return ""; }
        else {
            String str = termStrings.remove(0)
            while(!termStrings.isEmpty()) {
                str += "|" + termStrings.remove(0);
            }
        }
    }
}