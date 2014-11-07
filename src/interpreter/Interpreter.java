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

    private ArrayList<Send<Integer>> senders;
    private ArrayList<Receive<Integer>> receivers;
    private ArrayList<Restrict<Integer>> restricts;
    private ArrayList<NDSum<Integer>> sums;

    private ArrayList<Send<Integer>> replSenders;
    private ArrayList<Receive<Integer>> replReceivers;
    private ArrayList<Restrict<Integer>> replRestricts;
    private ArrayList<NDSum<Integer>> replSums;

    private HashSet<Integer> boundNames;

    /**
     * Construct a new Interpreter.
     * @param term the PiTerm to interpret
     * @param nameMap a mapping from the user's String names to integer names
     * used for interpretation
     * @param nextAvailableName the value to use next time a fresh name is
     * required
     */
    public Interpreter(PiTerm<Integer> term, HashMap<String, Integer> nameMap,
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

        this.senders = new ArrayList<Send<Integer>>();
        this.receivers = new ArrayList<Receive<Integer>>();
        this.restricts = new ArrayList<Restrict<Integer>>();
        this.sums = new ArrayList<NDSum<Integer>>();

        this.replSenders = new ArrayList<Send<Integer>>();
        this.replReceivers = new ArrayList<Receive<Integer>>();
        this.replRestricts = new ArrayList<Restrict<Integer>>();
        this.replSums = new ArrayList<NDSum<Integer>>();

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
        boolean doneReduction = tryReduction(this.senders, this.receivers);
        if(doneReduction) { return true; }

        // The lists of terms that can interact. The order of the pair does not
        // matter.
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
            { this.restricts        , this.replRestricts    },
            { this.sums             , this.sums             },
            { this.sums             , this.senders          },
            { this.sums             , this.receivers        },
            { this.sums             , this.restricts        },
            { this.sums             , this.replSenders      },
            { this.sums             , this.replReceivers    },
            { this.sums             , this.replRestricts    },
            { this.sums             , this.replSums         },
            { this.senders          , this.replSums         },
            { this.receivers        , this.replSums         },
            { this.restricts        , this.replSums         }
        };

        // Convert the pairings array into an ArrayList for easier processing
        ArrayList<ArrayList[]> todo = new ArrayList<ArrayList[]>();
        for(ArrayList[] pair : pairings) { todo.add(pair); }

        // Keep trying the possible reductions in random order until one works
        while(!(todo.isEmpty() || doneReduction)) {
            ArrayList[] pair = Interpreter.arbitraryElement(todo);
            todo.remove(pair);
            doneReduction = tryReduction(pair[0], pair[1]);
        }
        return doneReduction;
    }

    /*
     * Attempt to find a reduction possibility accross members of the two given
     * lists. If one is found, perform it and return true. Otherwise, return
     * false.
     */
    private boolean tryReduction(ArrayList<? extends PiTerm> list1,
            ArrayList<? extends PiTerm> list2) {

        ArrayList<Match> matches =
                Match.findMatches(list1, list2);
        if(matches.isEmpty()) { return false; }
        Match reduction = Interpreter.arbitraryElement(matches);

        if (pairMatch(this.senders, this.receivers, list1, list2)) {
            this.doCommunicate((Send) reduction.t1, (Receive) reduction.t2);
        }
        else if(pairMatch(this.senders, this.replReceivers, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.t2.copy());
        }
        else if(pairMatch(this.receivers, this.replSenders, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.t2.copy());
        }
        else if(pairMatch(this.senders, this.restricts, list1, list2)) {
            this.doScopeExtrusion((Restrict) reduction.t2);
        }
        else if(pairMatch(this.receivers, this.restricts, list1, list2)) {
            this.doScopeExtrusion((Restrict) reduction.t2);
        }
        else if(pairMatch(this.restricts, this.replSenders, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.t2.copy());
        }
        else if(pairMatch(this.restricts, this.replReceivers, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.t2.copy());
        }
        else if(pairMatch(this.senders, this.replRestricts, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.t2.copy());
        }
        else if(pairMatch(this.receivers, this.replRestricts, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.t2.copy());
        }
        else if(pairMatch(this.restricts, this.restricts, list1, list2)) {
            this.doScopeExtrusion((Restrict) reduction.t2);
        }
        else if(pairMatch(this.restricts, this.replRestricts, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.t2.copy());
        }
        else if(pairMatch(this.sums, this.sums, list1, list2)) {
            this.doSumSelection((NDSum) reduction.t1, reduction.t2);
        }
        else if(pairMatch(this.sums, this.senders, list1, list2)) {
            this.doSumSelection((NDSum) reduction.t1, reduction.t2);
        }
        else if(pairMatch(this.sums, this.receivers, list1, list2)) {
            this.doSumSelection((NDSum) reduction.t1, reduction.t2);
        }
        else if(pairMatch(this.sums, this.restricts, list1, list2)) {
            this.doScopeExtrusion((Restrict) reduction.t2);
        }
        else if(pairMatch(this.sums, this.replSenders, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.t2.copy());
        }
        else if(pairMatch(this.sums, this.replReceivers, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.t2.copy());
        }
        else if(pairMatch(this.sums, this.replRestricts, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.t2.copy());
        }
        else if(pairMatch(this.sums, this.replSums, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.t2.copy());
        }
        else if(pairMatch(this.senders, this.replSums, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.t2.copy());
        }
        else if(pairMatch(this.receivers, this.replSums, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.t2.copy());
        }
        else if(pairMatch(this.restricts, this.replSums, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.t2.copy());
        }
        else {
            throw new IllegalArgumentException("Given lists not allowed to " +
                    "communicate");
        }

        return true;
    }
    // Determine if two lists are the same lists as the two other lists.
    private static boolean pairMatch(
            ArrayList<? extends PiTerm> e1,
            ArrayList<? extends PiTerm> e2,
            ArrayList<? extends PiTerm> t1,
            ArrayList<? extends PiTerm> t2) {

        return (e1 == t1 && e2 == t2) || (e1 == t2 && e2 == t1);
    }

    /*
     * Reduce the given Send and Receive that are members of senders and
     * receivers respectively, by exchanging a message.
     */
    private void doCommunicate(Send<Integer> send, Receive<Integer> rece) {

        if(!send.chnl().equals(rece.chnl())) {
            throw new IllegalArgumentException("Tried to pass a message " +
                    "between terms on different channels");
        }

        if(!(send.arity() == rece.arity())) {
            throw new IllegalArgumentException("Tried to pass a message " +
                    "between terms of unequal arity");
        }

        if(!(this.senders.contains(send) && this.receivers.contains(rece))) {
            throw new IllegalArgumentException("Send send and Receive " +
                    "rece parameters must be members of the senders and " +
                    "receivers ArrayLists, respectively");
        }

        this.senders.remove(send);
        this.receivers.remove(rece);
        this.integrateNewlyExposedTerm(send.subterm());
        PiTerm<Integer> receiverSub = rece.subterm();

        // To avoid clashes, first rename all sent names to a fresh name, and
        // then rename those fresh names with the sent ones.
        int firstIntermediateName = this.nextAvailableName;
        for(int i = 0; i < rece.arity(); i++) {
            receiverSub.rename(rece.msg(i), firstIntermediateName);
            firstIntermediateName++;
        }
        firstIntermediateName = this.nextAvailableName;
        for(int i = 0; i < rece.arity(); i++) {
            receiverSub.rename(firstIntermediateName, send.msg(i));
            firstIntermediateName++;
        }

        this.integrateNewlyExposedTerm(receiverSub);
    }

    /*
     * Perform scope extrusion to the given member of the restricts ArrayList
     */
    private void doScopeExtrusion(Restrict<Integer> rest) {

        if(!this.restricts.contains(rest)) {
            throw new IllegalArgumentException("Restrict rest parameter " +
                    "must be a member of the restricts ArrayList");
        }

        this.restricts.remove(rest);

        // Update nameMap and usedNames
        String baseName = this.nameMap.get(rest.boundName());
        String printableName = this.nextStringName(baseName);
        this.usedNames.add(printableName);
        this.nameMap.put(this.nextAvailableName, printableName);

        // Alpha convert and reintegrate
        rest.alphaConvert(rest.boundName(), this.nextAvailableName);
        this.integrateNewlyExposedTerm(rest.subterm());
        this.boundNames.add(rest.boundName());

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

    /*
     * Reduce an NDSum PiTerm found in the sums ArrayList. This is done by
     * identifing all the possibilities of the sum that communicate with the
     * other given PiTerm, removing the whole sum from its list, and
     * reintegrating one of the identified possiblities (randomly chosen).
     */
    private void doSumSelection(NDSum<Integer> sum, PiTerm<Integer> other) {

        if(!this.sums.contains(sum)) {
            throw new IllegalArgumentException("NDSum sum parameter must be " +
                    "a member of the sums ArrayList");
        }

        // Obtain a list of all the subterms of sum that talk to other.
        ArrayList<PiTerm<Integer>> commSubs = new ArrayList<PiTerm<Integer>>();
        for(int i = 0; i < sum.arity(); i++) {
            if(PiTerm.talksTo(sum.subterm(i), other)) {
                commSubs.add(sum.subterm(i));
            }
        }

        // Just to be safe...
        if(commSubs.isEmpty()) {
            throw new IllegalStateException("Tried to do sum selection with " +
                    "terms that cannot communicate");
        }

        // Remove sum from the sums ArrayList and reintegrate one of its
        // children
        this.sums.remove(sum);
        PiTerm<Integer> chosen = Interpreter.arbitraryElement(commSubs);
        this.integrateNewlyExposedTerm(chosen);

        // Since sum selection is not supposed to be an atomic action in terms
        // of the pi calculus semantics, we must reduce the chosen sum
        // possibilty and the reacting term until they communicate.
        this.forceCommunication(chosen, other);
    }

    /*
     * Force two terms to talk. Throws an exception if they don't.
     */
    private void forceCommunication(PiTerm<Integer> t1, PiTerm<Integer> t2) {
        if(t1 instanceof Send) {
            if(t2 instanceof Receive) {
                this.doCommunicate((Send) t1, (Receive) t2);
            }
            else {
                this.forceCommunication(t2, t1);
            }
        }
        if(t1 instanceof Receive) {
            if(t2 instanceof Send) {
                this.doCommunicate((Send) t2, (Receive) t1);
            }
            else {
                this.forceCommunication(t2, t1);
            }
        }
        if(t1 instanceof PiTermManySub) {
            PiTermManySub<Integer> ptms = (PiTermManySub) t1;
            ArrayList<PiTerm<Integer>> commSubs =
                    new ArrayList<PiTerm<Integer>>();
            for(int i = 0; i < ptms.arity(); i++) {
                if(PiTerm.talksTo(ptms.subterm(i), t2)) {
                    commSubs.add(ptms.subterm(i));
                }
            }
            PiTerm<Integer> chosen = Interpreter.arbitraryElement(commSubs);
            if(t1 instanceof NDSum) {
                if(this.sums.contains(t1)) { this.sums.remove(t1); }
            }
            this.integrateNewlyExposedTerm(chosen);
            this.forceCommunication(chosen, t2);
        }
        if(t1 instanceof Replicate) {
            PiTerm<Integer> t1SubCopy = ((Replicate) t1).subterm().copy();
            this.integrateNewlyExposedTerm(t1);
            this.integrateNewlyExposedTerm(t1SubCopy);
            this.forceCommunication(t1SubCopy, t2);
        }
        if(t1 instanceof Restrict) {
            PiTerm<Integer> t1Sub = ((Restrict) t1).subterm();
            this.doScopeExtrusion((Restrict) t1);
            this.forceCommunication(t1, t2);
        }
    }

    // Add a newly exposed term to the appropriate arraylist
    private void integrateNewlyExposedTerm(PiTerm<Integer> term) {
        if(term instanceof Send) {
            this.senders.add((Send) term);
        }
        else if(term instanceof Receive) {
            this.receivers.add((Receive) term);
        }
        else if(term instanceof Replicate) {

            PiTerm<Integer> subterm = ((Replicate) term).subterm();

            if(subterm instanceof Send) {
                if(!(this.replSenders.contains(subterm))) {
                    this.replSenders.add((Send) subterm);
                }
            }
            else if(subterm instanceof Receive) {
                if(!(this.replReceivers.contains(subterm))) {
                    this.replReceivers.add((Receive) subterm);
                }
            }
            else if(subterm instanceof Restrict) {
                if(!(this.replRestricts.contains(subterm))) {
                    this.replRestricts.add((Restrict) subterm);
                }
            }
            else if(subterm instanceof Parallel) {
                Parallel para = (Parallel) subterm;
                for(int i = 0; i < para.arity(); i++) {
                    this.integrateNewlyExposedTerm(
                            new Replicate(para.subterm(i)));
                }
            }
            else if(subterm instanceof Replicate) {
                this.integrateNewlyExposedTerm(subterm);
            }
            else if(subterm instanceof NDSum) {
                if(!(this.replSums.contains(subterm))) {
                    this.replSums.add((NDSum) subterm);
                }
            }
            else {
                throw new IllegalArgumentException("Non-standard PiTerm " + 
                        "found in program");
            }

        }
        else if(term instanceof Parallel) {
            Parallel para = (Parallel) term;
            for(int i = 0; i < para.arity(); i++) {
                this.integrateNewlyExposedTerm(para.subterm(i));
            }
        }
        else if(term instanceof Restrict) {
            this.restricts.add((Restrict) term);
        }
        else if(term instanceof NDSum) {
            this.sums.add((NDSum) term);
        }
        else {
            throw new IllegalArgumentException("Non-standard PiTerm found " +
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
            termStrings.add(send.toStringWithNameMap(this.nameMap));
        }
        for(Receive rece : this.receivers) {
            termStrings.add(rece.toStringWithNameMap(this.nameMap));
        }
        for(Restrict rest : this.restricts) {
            termStrings.add(rest.toStringWithNameMap(this.nameMap));
        }
        for(NDSum sum : this.sums) {
            termStrings.add(sum.toStringWithNameMap(this.nameMap));
        }
        for(Send send : this.replSenders) {
            termStrings.add("! " + send.toStringWithNameMap(this.nameMap));
        }
        for(Receive rece : this.replReceivers) {
            termStrings.add("! " + rece.toStringWithNameMap(this.nameMap));
        }
        for(Restrict rest : this.replRestricts) {
            termStrings.add("! " + rest.toStringWithNameMap(this.nameMap));
        }
        for(NDSum sum : this.replSums) {
            termStrings.add("! " + sum.toStringWithNameMap(this.nameMap));
        }
        String procs = termStrings.isEmpty() ? "" : termStrings.remove(0);
        while(!termStrings.isEmpty()) {
            procs += " | " + termStrings.remove(0);
        }
        String scope = "";
        for(Integer i : this.boundNames) {
            scope += "new " + this.nameMap.get(i) + " in ";
        }
        return scope + (procs.equals("") ? "0" : "[ " + procs + " ]");
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
