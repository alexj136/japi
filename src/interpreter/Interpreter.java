package interpreter;

import runsyntax.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents n-ary parallel composition, and contains methods to handle
 * integration of newly exposed terms into that n-ary parallel composition.
 */
public class Interpreter {

    private HashMap<String, Integer> nameMap;
    private int nextAvailableName;

    private ArrayList<Send> senders;
    private ArrayList<Receive> receivers;
    private ArrayList<Replicate> replicators;
    private ArrayList<Restrict> restrictions;
    private ArrayList<Integer> boundNames;

    /**
     * Construct a new Interpreter.
     * @param term the Term to interpret
     * @param nameMap a mapping from the user's String names to integer names
     * used for interpretation
     * @param nextAvailableName the value to use next time a fresh name is
     * required
     */
    public Interpreter(Term term, HashMap<String, Integer> nameMap,
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

    /**
     * Perform a reduction step.
     */
    public void doReduction() {

        // Find a match in senders and receivers
        int sendIdx = 0, receiveIdx = 0;
        boolean reductionFound = false;
        while(sendIdx < this.senders.size() && !reductionFound) {
            while(receiveIdx < this.receivers.size() && !reductionFound) {
                if(this.senders.get(sendIdx).getSendOn() ==
                        this.receivers.get(receiveIdx).getReceiveOn()) {

                    reductionFound = true;
                }
                else { receiveIdx++; }
            }
            if(!reductionFound) { sendIdx++; }
        }

        if(reductionFound) {
            // Do the reduction
            Send sender = senders.remove(sendIdx);
            Receive receiver = receivers.remove(receiveIdx);

            this.integrateNewlyExposedTerm(sender.getSubprocess());

            Term receiverSub = receiver.getSubprocess();
            receiverSub.rename(receiver.getBindTo(), sender.getToSend());

            this.integrateNewlyExposedTerm(receiverSub);
        }
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
                    "in program");
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
        String str = termStrings.isEmpty() ? "" : termStrings.remove(0);
        while(!termStrings.isEmpty()) { str += "|" + termStrings.remove(0); }
        return "[" + str + "]";
    }
}
