package interpreter;

import syntax.*;
import java.util.Collections;
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
    public boolean doReduction() {

        // Always send messages if possible. If not, do another kind of
        // reduction in arbitrary order

        ArrayList<Match> matches =
                Match.findMatches(this.senders, this.receivers);
        if(!matches.isEmpty()) {
            Match reduction = Interpreter.arbitraryElement(matches);
            this.reduce((Send) reduction.t1, (Receive) reduction.t2);
            return true;
        }

        ArrayList[][] pairings = {
            { this.senders          , this.replReceivers    },
            { this.receivers        , this.replSenders      },
            { this.senders          , this.restricts        },
            { this.receivers        , this.restricts        },
            { this.restricts        , this.replSenders      },
            { this.restricts        , this.replReceivers    },
            { this.senders          , this.replRestricts    },
            { this.receivers        , this.replRestricts    },
            { this.restricts        , this.restricts        },
            { this.restricts        , this.replRestricts    }
        };

        ArrayList<ArrayList[]> todo = new ArrayList<ArrayList[]>();
        for(ArrayList[] pair : pairings) { todo.add(pair); }

        boolean doneReduction = false;
        while(!(todo.isEmpty() || doneReduction)) {
            ArrayList[] pair = Interpreter.arbitraryElement(todo);
            todo.remove(pair);

            matches = Match.findMatches(pair[0], pair[1]);
            if(!matches.isEmpty()) {

                Match reduction = Interpreter.arbitraryElement(matches);

                if(reduction.t2 instanceof Send) {
                    this.reduce((Send) reduction.t2);
                }
                else if(reduction.t2 instanceof Receive) {
                    this.reduce((Receive) reduction.t2);
                }
                else if(reduction.t2 instanceof Restrict) {
                    this.reduce((Restrict) reduction.t2);
                }
                else {
                    throw new IllegalStateException("Term to reduce was " +
                            "not Send, Receive or Restrict");
                }

                doneReduction = true;
            }
        }

        return doneReduction;
    }

    /*
     * Reduce the given Send and Receive that are members of senders and
     * receivers respectively, by exchanging a message.
     */
    private void reduce(Send send, Receive rece) {

        assert Term.talksTo(send, rece);

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
    private void reduce(Send send) {

        if(!this.replSenders.contains(send)) {
            throw new IllegalArgumentException("Send send parameter " +
                    "must be a member of the replSenders ArrayList");
        }

        this.integrateNewlyExposedTerm(send);
    }

    /*
     * Replicate the given member of the replReceivers ArrayList
     */
    private void reduce(Receive rece) {

        if(!this.replReceivers.contains(rece)) {
            throw new IllegalArgumentException("Receive rece parameter " +
                    "must be a member of the replReceivers ArrayList");
        }

        this.integrateNewlyExposedTerm(rece);
    }

    /*
     * Replicate the given member of the replRestricts ArrayList
     */
    private void reduce(Restrict rest) {

        boolean isInRestricts = this.restricts.contains(rest);
        boolean isInReplRestricts = this.replRestricts.contains(rest);

        if(isInRestricts == isInReplRestricts) {
            throw new IllegalArgumentException("Restrict rest parameter " +
                    "must be a member of the replRestricts ArrayList or the " +
                    "restricts ArrayList, but not both");
        }

        if(isInRestricts) {
            this.doScopeExtrusion(rest);
            return;
        }

        if(isInReplRestricts) {
            this.integrateNewlyExposedTerm(rest);
            return;
        }

        throw new IllegalStateException("Logic is dead!!!");
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
            for(Term subterm : term) {
                this.integrateNewlyExposedTerm(subterm);
            }
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

    /**
     * Retreive an arbitrary element from an ArrayList.
     * @param matches the ArrayList to use
     * @return an arbitrary element of the given list
     */
    public static <E> E arbitraryElement(ArrayList<E> list) {
        return list.get(Interpreter.rand.nextInt(list.size()));
    }
}
