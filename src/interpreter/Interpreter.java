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
     * Try to do a reduction.
     * @return true if a reduction was performed, false otherwise
     */
    public boolean reduce() {
        return trySendReceiveReduction() || tryReducibleReplication();
    }

    /*
     * Look for a matching channel accross the senders and receivers. If one is
     * found, reduce it and return true, otherwise do nothing and return false.
     */
    private boolean trySendReceiveReduction() {

        int sendIdx = 0;
        int receiveIdx = 0;
        boolean reductionFound = false;

        // Scan the sender and receiver lists until a match is found or until
        // all combinations have been checked. TODO: Optimise this search using
        // sorted lists.
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

        // If a match was found, perform the reduction, renaming in the receiver
        // subterm.
        if(reductionFound) {
            Send sender = senders.remove(sendIdx);
            Receive receiver = receivers.remove(receiveIdx);
            this.integrateNewlyExposedTerm(sender.getSubprocess());
            Term receiverSub = receiver.getSubprocess();
            receiverSub.rename(receiver.getBindTo(), sender.getToSend());
            this.integrateNewlyExposedTerm(receiverSub);
        }

        return reductionFound;
    }

    /*
     * Attempt scope extrusion via alpha conversion. Fails (i.e. returns false
     * without doing anything) if the restrictions list is empty.
     */
    private boolean tryScopeExtrusion() {
        throw new UnsupportedOperationException("Not yet implemented");/*
        if(this.restrictions.isEmpty) { return false; }

        Restrict rest = this.restrictions.remove(0);
        rest.alphaConvert(rest.getBoundName(),
                this.nameMap, this.nextAvailableName);
        this.integrateNewlyExposedTerm(rest.getRestrictIn());
        this.boundNames.add(rest.getBoundName());

        return true;*/
    }

    /*
     * Look for a Replicate node, where the subterm is a Send with a match in
     * the recievers list, or a Receive with a match in the senders list. If
     * such a term is found, replicate it.
     */
    private boolean tryReducibleReplication() {

        int sendIdx = 0;
        int replicateIdx = 0;
        boolean reductionFound = false;

        // Look for a Send that matches a Receive subterm of a member of the
        // replicators list.
        while(sendIdx < this.senders.size() && !reductionFound) {
            while(replicateIdx < this.replicators.size() && !reductionFound) {

                int sendChannel = this.senders.get(sendIdx).getSendOn();

                boolean replicatorIsReceive =
                        this.replicators.get(replicateIdx).getToReplicate()
                        instanceof Receive;

                int receiveChannel = (!replicatorIsReceive) ? 0 :
                        ((Receive) this.replicators.get(replicateIdx)
                        .getToReplicate()).getReceiveOn();

                if(replicatorIsReceive && (sendChannel == receiveChannel)) {
                    reductionFound = true;
                }
                else { replicateIdx++; }
            }
            if(!reductionFound) { sendIdx++; }
        }

        // If a match was found, replicate the body of the matching replicator,
        // and return. If we did not return here, we would look for a match with
        // a receiver.
        if(reductionFound) {
            this.integrateNewlyExposedTerm(
                    this.replicators.get(replicateIdx).getToReplicate().copy());
            return reductionFound; // always true
        }

        int receiveIdx = 0;
        replicateIdx = 0;

        // Look for a Receive that matches a Send subterm of a member of the
        // replicators list.
        while(receiveIdx < this.receivers.size() && !reductionFound) {
            while(replicateIdx < this.replicators.size() && !reductionFound) {

                int receiveChannel =
                        this.receivers.get(receiveIdx).getReceiveOn();

                boolean replicatorIsSend =
                        this.replicators.get(replicateIdx).getToReplicate()
                        instanceof Send;

                int sendChannel = (!replicatorIsSend) ? 0 :
                        ((Send) this.replicators.get(replicateIdx)
                        .getToReplicate()).getSendOn();

                if(replicatorIsSend && (sendChannel == receiveChannel)) {
                    reductionFound = true;
                }
                else { replicateIdx++; }
            }
            if(!reductionFound) { receiveIdx++; }
        }

        // If a match was found, replicate the body of the matching replicator,
        // and return.
        if(reductionFound) {
            this.integrateNewlyExposedTerm(
                    this.replicators.get(replicateIdx).getToReplicate().copy());
            return reductionFound; // always true
        }

        return reductionFound; // always false
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
        String procs = termStrings.isEmpty() ? "" : termStrings.remove(0);
        while(!termStrings.isEmpty()) { procs += "|" + termStrings.remove(0); }
        return "[" + procs + "]";
    }

    /**
     * Obtain a nice string representation of the 'term' in its current state,
     * using the user's names rather than integer representations, where
     * possible.
     * @return a nice string representation of the contained 'term'
     */
    public String niceToString() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
