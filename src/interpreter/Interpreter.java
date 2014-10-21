package interpreter;

import runsyntax.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents n-ary parallel composition, and contains methods to handle
 * integration of newly exposed terms into that n-ary parallel composition.
 */
public class Interpreter {

    private HashMap<Integer, String> nameMap;
    private HashSet<String> usedNames;
    private int nextAvailableName;

    private ArrayList<Send> senders;
    private ArrayList<Receive> receivers;
    private ArrayList<Restrict> restricts;

    private ArrayList<Send> replSenders;
    private ArrayList<Receive> replReceivers;
    private ArrayList<Restrict> replRestricts;

    private HashSet<Integer> boundNames;

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

        // Use the SyntaxTranslationResult's String to Integer map, useful for
        // translation, to an Integer to String map, useful for printing terms
        // during or after interpretation.
        this.nameMap = new HashMap<Integer, String>();
        this.usedNames = new HashSet<String>();
        for(String name : nameMap.keySet()) {
            this.nameMap.put(nameMap.get(name), name);
            this.usedNames.add(name);
        }

        this.nextAvailableName = nextAvailableName;

        this.senders = new ArrayList<Send>();
        this.receivers = new ArrayList<Receive>();
        this.restricts = new ArrayList<Restrict>();

        this.replSenders = new ArrayList<Send>();
        this.replReceivers = new ArrayList<Receive>();
        this.replRestricts = new ArrayList<Restrict>();

        this.boundNames = new HashSet<Integer>();

        this.integrateNewlyExposedTerm(term);
    }

    /**
     * Construct a new Interpreter from the given SyntaxTranslationResult.
     * @param SyntaxTranslationResult the result of translating a source program
     * into an interpretable program.
     */
    public static Interpreter fromTranslation(SyntaxTranslationResult result) {
        return new Interpreter(result.getTerm(), result.getNameMap(),
                result.getNextAvailableName());
    }

    /**
     * Try to do a reduction.
     * @return true if a reduction was performed, false otherwise
     */
    public boolean reduce() {

        // Look for simple matches between senders and receivers - perform one
        // if any are found.
        ArrayList<Match> matches =
            Match.findMatches(this.senders, this.receivers);
        if(!matches.isEmpty()) {
            Match match = Match.arbitraryMatch(matches);
            this.doSendReceiveReduction((Send) match.t1, (Receive) match.t2);
            return true;
        }

        // Look for matches between senders and replicating receivers. If any
        // are found, replicate one of the receivers.
        matches = Match.findMatches(this.senders, this.replReceivers);
        if(!matches.isEmpty()) {
            Match match = Match.arbitraryMatch(matches);
            this.doReceiveReplication((Receive) match.t2);
            return true;
        }
        
        // Look for matches between receivers and replicating senders. If any
        // are found, replicate one of the senders.
        matches = Match.findMatches(this.replSenders, this.receivers);
        if(!matches.isEmpty()) {
            Match match = Match.arbitraryMatch(matches);
            this.doSendReplication((Send) match.t1);
            return true;
        }

        // Look for a match between the senders and the restricts. If any are
        // found, perform scope extrusion on one of the restricts.
        matches = Match.findMatches(this.senders, this.restricts);
        if(!matches.isEmpty()) {
            Match match = Match.arbitraryMatch(matches);
            this.doScopeExtrusion((Restrict) match.t2);
            return true;
        }

        // Look for a match between the receivers and the restricts. If any are
        // found, perform scope extrusion on one of the restricts.
        matches = Match.findMatches(this.receivers, this.restricts);
        if(!matches.isEmpty()) {
            Match match = Match.arbitraryMatch(matches);
            this.doScopeExtrusion((Restrict) match.t2);
            return true;
        }

        // Look for matches between replicating senders and restricts. If any
        // are found, perform scope extrusion on one of the restricts.
        matches = Match.findMatches(this.replSenders, this.restricts);
        if(!matches.isEmpty()) {
            Match match = Match.arbitraryMatch(matches);
            this.doScopeExtrusion((Restrict) match.t2);
            return true;
        }
        
        // Look for matches between replicating receivers and restricts. If any
        // are found, perform scope extrusion on one of the restricts.
        matches = Match.findMatches(this.replReceivers, this.restricts);
        if(!matches.isEmpty()) {
            Match match = Match.arbitraryMatch(matches);
            this.doScopeExtrusion((Restrict) match.t2);
            return true;
        }

        // Look for a match between the senders and the replicating restricts.
        // If any are found, replicate one of the restricts.
        matches = Match.findMatches(this.senders, this.replRestricts);
        if(!matches.isEmpty()) {
            Match match = Match.arbitraryMatch(matches);
            this.doRestrictReplication((Restrict) match.t2);
            return true;
        }

        // Look for a match between the receivers and the replicating restricts.
        // If any are found, replicate one of the restricts.
        matches = Match.findMatches(this.receivers, this.replRestricts);
        if(!matches.isEmpty()) {
            Match match = Match.arbitraryMatch(matches);
            this.doRestrictReplication((Restrict) match.t2);
            return true;
        }
        
        return false;
    }

    /*
     * Reduce the given Send and Receive that are members of senders and
     * receivers respectively
     */
    private void doSendReceiveReduction(Send send, Receive rece) {

        if(!(this.senders.contains(send) && this.receivers.contains(rece))) {
            throw new IllegalArgumentException("Send send and Receive " +
                    "rece parameters must be members of the senders and " +
                    "receivers ArrayLists, respectively");
        }

        this.senders.remove(send);
        this.receivers.remove(rece);
        this.integrateNewlyExposedTerm(send.getSubprocess());
        Term receiverSub = rece.getSubprocess();
        receiverSub.rename(rece.getBindTo(), send.getToSend());
        this.integrateNewlyExposedTerm(receiverSub);
    }

    /*
     * Replicate the given member of the replSenders ArrayList
     */
    private void doSendReplication(Send send) {

        if(!this.replSenders.contains(send)) {
            throw new IllegalArgumentException("Send send parameter " +
                    "must be a member of the replSenders ArrayList");
        }

        this.integrateNewlyExposedTerm(send);
    }

    /*
     * Replicate the given member of the replReceivers ArrayList
     */
    private void doReceiveReplication(Receive rece) {

        if(!this.replReceivers.contains(rece)) {
            throw new IllegalArgumentException("Receive rece parameter " +
                    "must be a member of the replReceivers ArrayList");
        }

        this.integrateNewlyExposedTerm(rece);
    }

    /*
     * Replicate the given member of the replRestricts ArrayList
     */
    private void doRestrictReplication(Restrict rest) {

        if(!this.replRestricts.contains(rest)) {
            throw new IllegalArgumentException("Restrict rest parameter " +
                    "must be a member of the replRestricts ArrayList");
        }

        this.integrateNewlyExposedTerm(rest);
    }

    /*
     * Perform scope extrusion to the given member of the restricts ArrayList
     */
    private void doScopeExtrusion(Restrict rest) {

        if(!this.restricts.contains(rest)) {
            throw new IllegalArgumentException("Restrict rest parameter " +
                    "must be a member of the restricts ArrayList");
        }

        this.restricts.remove(rest);

        // Update nameMap and usedNames
        String baseName = this.nameMap.get(rest.getBoundName());
        String printableName = this.nextStringName(baseName);
        this.usedNames.add(printableName);
        this.nameMap.put(this.nextAvailableName, printableName);

        // Alpha convert and reintegrate
        rest.alphaConvert(rest.getBoundName(), this.nextAvailableName);
        this.integrateNewlyExposedTerm(rest.getRestrictIn());
        this.boundNames.add(rest.getBoundName());

        // Update nextAvailableName
        this.nextAvailableName++;
    }

    // Add ' to a name until it is one that does not appear in the usedNames set
    private String nextStringName(String baseName) {
        while(this.usedNames.contains(baseName)) {
            baseName += "'";
        }
        return baseName;
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

            Term subterm = ((Replicate) term).getToReplicate();

            if(subterm instanceof Send) {
                this.replSenders.add((Send) subterm);
            }
            else if(subterm instanceof Receive) {
                this.replReceivers.add((Receive) subterm);
            }
            else if(subterm instanceof Restrict) {
                this.replRestricts.add((Restrict) subterm);
            }
            else if(subterm instanceof Parallel) {
                this.integrateNewlyExposedTerm(
                        new Replicate(((Parallel) subterm).getSubprocess1()));
                this.integrateNewlyExposedTerm(
                        new Replicate(((Parallel) subterm).getSubprocess2()));
            }
            else if(subterm instanceof Replicate) {
                this.integrateNewlyExposedTerm(subterm);
            }
            else if(subterm instanceof End) {
                // Do nothing
            }
            else {
                throw new IllegalArgumentException("Non-standard Term found " +
                        "in program");
            }

        }
        else if(term instanceof Parallel) {
            this.integrateNewlyExposedTerm(((Parallel) term).getSubprocess1());
            this.integrateNewlyExposedTerm(((Parallel) term).getSubprocess2());
        }
        else if(term instanceof Restrict) {
            this.restricts.add((Restrict) term);
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
        for(Send send : this.senders) {
            termStrings.add(send.toNiceString(this.nameMap));
        }
        for(Receive rece : this.receivers) {
            termStrings.add(rece.toNiceString(this.nameMap));
        }
        for(Restrict rest : this.restricts) {
            termStrings.add(rest.toNiceString(this.nameMap));
        }
        for(Send send : this.replSenders) {
            termStrings.add("! " + send.toNiceString(this.nameMap));
        }
        for(Receive rece : this.replReceivers) {
            termStrings.add("! " + rece.toNiceString(this.nameMap));
        }
        for(Restrict rest : this.replRestricts) {
            termStrings.add("! " + rest.toNiceString(this.nameMap));
        }
        String procs = termStrings.isEmpty() ? "" : termStrings.remove(0);
        while(!termStrings.isEmpty()) {
            procs += " | " + termStrings.remove(0);
        }
        String scope = "";
        for(Integer i : this.boundNames) {
            scope += "new " + this.nameMap.get(i) + " in ";
        }
        return scope + "[ " + procs + " ]";
    }
}
