package interpreter;

import syntax.*;
import interpreter.LambdaReducer;
import utils.Pair;
import utils.Utils;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

/**
 * Represents n-ary parallel composition, and contains methods to handle
 * integration of newly exposed terms into that n-ary parallel composition.
 */
public class Interpreter {

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

    // This pair should not belong to a PiTerm list. Replicated terms should
    // have the Replicate wrapper, Parallels can be present, etc.
    private Optional<Pair<PiTerm<Integer>, PiTerm<Integer>>> actingTerms;

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

        this.actingTerms = Optional.empty();

        this.integrateNewlyExposedTerm(term);
    }

    /**
     * Construct a new Interpreter from the given SyntaxTranslationResult.
     * @param SyntaxTranslationResult the result of translating a source program
     * into an interpretable program.
     */
    public static Interpreter fromTranslation(
            SyntaxTranslationResult<PiTerm<Integer>> result) {

        return new Interpreter(result.getTerm(), result.getNameMap(),
                result.getNextAvailableName());
    }

    /**
     * Try to do a reduction.
     * @return true if a reduction was performed, false otherwise
     */
    public boolean doReduction() {

        // If we're in the middle of an indivisible action, we must default to
        // continuing this, to prevent illegal interference
        if(this.actingTerms.isPresent()) {
            this.continueInProgressReduction();
            return true;
        }

        // Always send messages if possible. If not, do another kind of
        // reduction in arbitrary order
        boolean doneReduction =
                tryRandomReductionBetweenLists(this.senders, this.receivers);
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
            ArrayList[] pair = Utils.arbitraryElement(todo);
            todo.remove(pair);
            doneReduction = tryRandomReductionBetweenLists(pair[0], pair[1]);
        }
        return doneReduction;
    }

    /*
     * Attempt to find a reduction possibility accross members of the two given
     * lists. If one is found, perform it and return true. Otherwise, return
     * false.
     */
    private boolean tryRandomReductionBetweenLists(
            ArrayList<? extends PiTerm> list1,
            ArrayList<? extends PiTerm> list2) {

        // Enumerate all matches between the given lists
        ArrayList<Pair<PiTerm<Integer>, PiTerm<Integer>>> matches =
                Interpreter.findMatches(list1, list2);

        // If there are no matches, we were unsuccessful, so return false
        if(matches.isEmpty()) { return false; }

        // Handle the chosen reduction and forward the returned status
        return this.handleChosenReduction(list1, list2,
                Utils.arbitraryElement(matches));
    }

    /*
     * Given two lists and a match between them, perform a reduction on that
     * matching pair. It is not checked that the match contains terms from the
     * given lists.
     */
    private boolean handleChosenReduction(ArrayList<? extends PiTerm> list1,
            ArrayList<? extends PiTerm> list2,
            Pair<PiTerm<Integer>, PiTerm<Integer>> reduction) {

        if (pairMatch(this.senders, this.receivers, list1, list2)) {
            this.doCommunicate((Send) reduction.fst, (Receive) reduction.snd);
        }
        else if(pairMatch(this.senders, this.replReceivers, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.snd.copy());
        }
        else if(pairMatch(this.receivers, this.replSenders, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.snd.copy());
        }
        else if(pairMatch(this.senders, this.restricts, list1, list2)) {
            this.integrateNewlyExposedTerm(
                    this.doScopeExtrusion((Restrict) reduction.snd));
        }
        else if(pairMatch(this.receivers, this.restricts, list1, list2)) {
            this.integrateNewlyExposedTerm(
                    this.doScopeExtrusion((Restrict) reduction.snd));
        }
        else if(pairMatch(this.restricts, this.replSenders, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.snd.copy());
        }
        else if(pairMatch(this.restricts, this.replReceivers, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.snd.copy());
        }
        else if(pairMatch(this.senders, this.replRestricts, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.snd.copy());
        }
        else if(pairMatch(this.receivers, this.replRestricts, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.snd.copy());
        }
        else if(pairMatch(this.restricts, this.restricts, list1, list2)) {
            this.integrateNewlyExposedTerm(
                    this.doScopeExtrusion((Restrict) reduction.snd));
        }
        else if(pairMatch(this.restricts, this.replRestricts, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.snd.copy());
        }
        else if(pairMatch(this.sums, this.sums, list1, list2)) {
            this.doSumSelection((NDSum) reduction.fst, reduction.snd);
        }
        else if(pairMatch(this.sums, this.senders, list1, list2)) {
            this.doSumSelection((NDSum) reduction.fst, reduction.snd);
        }
        else if(pairMatch(this.sums, this.receivers, list1, list2)) {
            this.doSumSelection((NDSum) reduction.fst, reduction.snd);
        }
        else if(pairMatch(this.sums, this.restricts, list1, list2)) {
            this.integrateNewlyExposedTerm(
                    this.doScopeExtrusion((Restrict) reduction.snd));
        }
        else if(pairMatch(this.sums, this.replSenders, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.snd.copy());
        }
        else if(pairMatch(this.sums, this.replReceivers, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.snd.copy());
        }
        else if(pairMatch(this.sums, this.replRestricts, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.snd.copy());
        }
        else if(pairMatch(this.sums, this.replSums, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.snd.copy());
        }
        else if(pairMatch(this.senders, this.replSums, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.snd.copy());
        }
        else if(pairMatch(this.receivers, this.replSums, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.snd.copy());
        }
        else if(pairMatch(this.restricts, this.replSums, list1, list2)) {
            this.integrateNewlyExposedTerm(reduction.snd.copy());
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
        for(int i = 0, firstIntermediateName = this.nextAvailableName;
                i < rece.arity(); i++, firstIntermediateName++) {

            PiReducer.msgPass(rece.name(i),
                    new Variable<Integer>(firstIntermediateName), receiverSub);
        }
        for(int i = 0, firstIntermediateName = this.nextAvailableName;
                i < rece.arity(); i++, firstIntermediateName++) {

            PiReducer.msgPass(firstIntermediateName, send.exp(i), receiverSub);
        }

        this.integrateNewlyExposedTerm(receiverSub);
    }

    /*
     * Perform scope extrusion to the given member of the restricts ArrayList.
     * Return a pointer to the newly exposed term, but do not reintegrate it.
     */
    private PiTerm<Integer> doScopeExtrusion(Restrict<Integer> rest) {

        if(!this.restricts.contains(rest)) {
            throw new IllegalArgumentException("Restrict rest parameter " +
                    "must be a member of the restricts ArrayList");
        }

        this.restricts.remove(rest);

        // Alpha convert
        int newName = this.leaseNewName(rest.boundName());
        rest.blindRename(rest.boundName(), newName);

        // Extrude the scope
        this.boundNames.add(rest.boundName());

        // Return the newly exposed term
        return rest.subterm();
    }

    /**
     * Given an existing name non-printable (Integer) name, generate a new
     * non-printable name for use as an alpha converted version of the given
     * name, such that the printable version of the new name is based on the
     * printable version of the given name. nameMap, usedNames and
     * nextAvailableName are updated appropriately.
     * @param existingName a name already in use
     * @return a name to use for alpha-converted versions of existingName
     */
    Integer leaseNewName(Integer existingName) {
        String printableVersion;
        try {
            printableVersion = this.getPrintableVersion(existingName);
        }
        catch(IllegalArgumentException iae) {
            throw new IllegalArgumentException("Tried to lease a new integer " +
                    "name from one that does not already exist in the " +
                    "program.");
        }
        String newPrintable = this.nextStringName(printableVersion);
        Integer newNonPrintable = this.nextAvailableName;
        this.nameMap.put(newNonPrintable, newPrintable);
        this.usedNames.add(newPrintable);
        this.nextAvailableName++;
        return newNonPrintable;
    }

    /**
     * Access the next available name that can be used externally for
     * intermediate substitutions.
     * @return the next available name
     */
    Integer getNextAvailableName() {
        return this.nextAvailableName;
    }

    /**
     * Given a printable name present in the program, obtain a new string name
     * to be used with alpha-converted versions of that name.
     * @param baseName the original name
     * @return the name that can be alpha-converted to
     */
    private String nextStringName(String baseName) {
        while(this.usedNames.contains(baseName)) {
            baseName += "'";
        }
        return baseName;
    }

    /**
     * Lookup the nameMap to determine what String should be printed to
     * represent a particular Integer name.
     * @param name the Integer name
     * @return the String version of the given name, for printing
     */
    private String getPrintableVersion(Integer name) {
        if(!this.nameMap.containsKey(name)) {
            throw new IllegalArgumentException("Tried to lookup the " +
                    "printable name for an Integer name that does not have a " +
                    "corresponding printable name - perhaps a String version " +
                    "was not leased.");
        }
        return this.nameMap.get(name);
    }

    /*
     * Reduce an NDSum PiTerm found in the sums ArrayList. This is done by
     * identifing all the possibilities of the sum that communicate with the
     * other given PiTerm, removing the whole sum from its list, and
     * reintegrating one of the identified possiblities (randomly chosen).
     */
    private void doSumSelection(NDSum<Integer> sum,
            PiTerm<Integer> other) {

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

        // Remove the sum and choose the possibility
        this.sums.remove(sum);
        PiTerm<Integer> chosen = Utils.arbitraryElement(commSubs);

        if(other instanceof Send) {
            this.senders.remove(other);
        }
        else if(other instanceof Receive) {
            this.receivers.remove(other);
        }
        else if(other instanceof NDSum) {
            this.sums.remove(other);
        }
        else {
            throw new IllegalArgumentException("Sum selection can only be " +
                    "invoked with a match between a sum and a member of the " +
                    "sums, senders or receivers lists.");
        }

        this.actingTerms = Optional.of(Pair.make(chosen, other));
    }

    /*
     * Force two terms to talk. Throws an exception if they don't.
     */
    private void continueInProgressReduction() {

        if(!this.actingTerms.isPresent()) {
            throw new IllegalStateException("continueInProgressReduction was " +
                    "called when no reduction was in progress");
        }

        PiTerm<Integer> t1 = this.actingTerms.get().fst;
        PiTerm<Integer> t2 = this.actingTerms.get().snd;

        if(t1 instanceof Send) {
            if(t2 instanceof Receive) {
                this.integrateNewlyExposedTerm(t1);
                this.integrateNewlyExposedTerm(t2);
                this.doCommunicate((Send) t1, (Receive) t2);
                this.actingTerms = Optional.empty();
            }
            else {
                this.actingTerms = Optional.of(Pair.make(t2, t1));
                this.continueInProgressReduction();
            }
        }
        else if(t1 instanceof Receive) {
            if(t2 instanceof Send) {
                this.integrateNewlyExposedTerm(t1);
                this.integrateNewlyExposedTerm(t2);
                this.doCommunicate((Send) t2, (Receive) t1);
                this.actingTerms = Optional.empty();
            }
            else {
                this.actingTerms = Optional.of(Pair.make(t2, t1));
                this.continueInProgressReduction();
            }
        }
        else if(t1 instanceof PiTermManySub) {
            PiTermManySub<Integer> ptms = (PiTermManySub) t1;
            ArrayList<PiTerm<Integer>> commSubs =
                    new ArrayList<PiTerm<Integer>>();

            // Enumerate the subterms which will communicate
            for(int i = 0; i < ptms.arity(); i++) {
                if(PiTerm.talksTo(ptms.subterm(i), t2)) {
                    commSubs.add(ptms.subterm(i));
                }
            }

            // Pick one to use
            PiTerm<Integer> chosen = Utils.arbitraryElement(commSubs);

            // Either discard the others (for a sum) or integrate them (for a
            // parallel composition)
            if(t1 instanceof NDSum) {
                // Do nothing - unselected options will be discarded
            }
            else if(t1 instanceof Parallel) {
                // Integrate unselected terms
                for(int i = 0; i < ptms.arity(); i++) {
                    if(ptms.subterm(i) != chosen) {
                        this.integrateNewlyExposedTerm(ptms.subterm(i));
                    }
                }
            }
            else {
                throw new IllegalStateException("Unrecognised PiTermManySub " +
                        "type in actingterms");
            }
            this.actingTerms = Optional.of(Pair.make(chosen, t2));
        }
        else if(t1 instanceof Replicate) {
            PiTerm<Integer> t1SubCopy = ((Replicate) t1).subterm().copy();
            this.integrateNewlyExposedTerm(t1);
            this.actingTerms = Optional.of(Pair.make(t1SubCopy, t2));
        }
        else if(t1 instanceof Restrict) {
            PiTerm<Integer> t1Sub = this.doScopeExtrusion((Restrict) t1);
            this.actingTerms = Optional.of(Pair.make(t1Sub, t2));
        }
        else {
            throw new IllegalStateException("Unrecognised PiTerm type in " +
                    "actingTerms");
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
        if(this.actingTerms.isPresent()) {
            termStrings.add(this.actingTerms.get().fst
                    .toStringWithNameMap(this.nameMap));
            termStrings.add(this.actingTerms.get().snd
                    .toStringWithNameMap(this.nameMap));
        }
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
     * Enumerate all matches possible matches between two lists of PiTerms.
     * @param list1 the first list of terms
     * @param list2 the second list of terms
     * @return an ArrayList of pairs of PiTerms, one for each possible matching
     * between the given lists
     */
    public static ArrayList<Pair<PiTerm<Integer>, PiTerm<Integer>>> findMatches(
            ArrayList<? extends PiTerm> list1,
            ArrayList<? extends PiTerm> list2) {

        ArrayList<Pair<PiTerm<Integer>, PiTerm<Integer>>> matches =
                new ArrayList<Pair<PiTerm<Integer>, PiTerm<Integer>>>();

        for(PiTerm t1 : list1) {
            for(PiTerm t2 : list2) {
                // If they talk to each other, and ARE NOT THE SAME TERM, we
                // consider this a match. Otherwise, sums can cause themselves
                // to reduce to one of their children.
                if(PiTerm.talksTo(t1, t2) && (t1 != t2)) {
                    matches.add(Pair.make(t1, t2));
                }
            }
        }
        return matches;
    }
}
