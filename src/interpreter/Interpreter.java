package interpreter;

import runsyntax.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * Represents n-ary parallel composition, and contains methods to handle
 * integration of newly exposed terms into that n-ary parallel composition.
 */
public class Interpreter {

    private static Random rand = new Random();

    private HashMap<Integer, String> nameMap;
    private HashSet<String> usedNames;
    private int nextAvailableName;

    private ArrayList<Send> senders;
    private ArrayList<Receive> receivers;
    private ArrayList<Restrict> restrictions;

    private ArrayList<Send> replSenders;
    private ArrayList<Receive> replReceivers;
    private ArrayList<Restrict> replRestrictions;

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
        this.restrictions = new ArrayList<Restrict>();

        this.replSenders = new ArrayList<Send>();
        this.replReceivers = new ArrayList<Receive>();
        this.replRestrictions = new ArrayList<Restrict>();

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
        throw new UnsupportedOperationException("Not yet implemented");
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
     * Replicate the given member of the replicators ArrayList
     */
    /*private void doReplication(Replicate repl) {

        if(!this.replicators.contains(repl)) {
            throw new IllegalArgumentException("Replicator repl parameter " +
                    "must be a member of the replicators ArrayList");
        }

        Replicate repl =
                (Replicate) matches.get(rand.nextInt(matches.size())).t2;
        this.integrateNewlyExposedTerm(repl.getToReplicate().copy());

        return true;
    }*/

    /*
     * Perform scope extrusion to the given member of the restrictions ArrayList
     */
    private void doScopeExtrusion(Restrict rest) {

        if(!this.restrictions.contains(rest)) {
            throw new IllegalArgumentException("Restrict rest parameter " +
                    "must be a member of the restrictions ArrayList");
        }

        this.restrictions.remove(rest);

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
                this.replRestrictions.add((Restrict) subterm);
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
        for(Send send : senders) {
            termStrings.add(send.toNiceString(this.nameMap));
        }
        for(Receive rece : receivers) {
            termStrings.add(rece.toNiceString(this.nameMap));
        }
        for(Restrict rest : restrictions) {
            termStrings.add(rest.toNiceString(this.nameMap));
        }
        for(Send send : replSenders) {
            termStrings.add("!" + send.toNiceString(this.nameMap));
        }
        for(Receive rece : replReceivers) {
            termStrings.add("!" + rece.toNiceString(this.nameMap));
        }
        for(Restrict rest : replRestrictions) {
            termStrings.add("!" + rest.toNiceString(this.nameMap));
        }
        String procs = termStrings.isEmpty() ? "" : termStrings.remove(0);
        while(!termStrings.isEmpty()) {
            procs += " | " + termStrings.remove(0);
        }
        String scope = "";
        for(Integer i : boundNames) {
            scope += "new " + this.nameMap.get(i) + " in ";
        }
        return scope + "[ " + procs + " ]";
    }

    /**
     * Enumerate all matches possible matches between two lists of Terms.
     * @param list1 the first list of terms
     * @param list2 the second list of terms
     * @return an ArrayList of Match objects, one for each possible matching
     * between the given lists
     */
    public static ArrayList<Match> findMatches(ArrayList<? extends Term> list1,
            ArrayList<? extends Term> list2) {

        ArrayList<Match> matches = new ArrayList<Match>();
        for(Term t1 : list1) {
            for(Term t2 : list2) {
                if(Term.talksTo(t1, t2)) {
                    matches.add(new Match(t1, t2));
                }
            }
        }
        return matches;
    }
}

/**
 * Contains references to two terms that will talk to each other.
 */
class Match {
    public final Term t1, t2;
    public Match(Term t1, Term t2) { this.t1 = t1; this.t2 = t2; }
}
