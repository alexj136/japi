package runsyntax;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A Term object represents any pi calculus term, with names as integers.
 */
public abstract class Term {

    /**
     * Rename the names in a Term as is necessary after the exchange of a
     * message - this is not alpha-conversion.
     * @param from some names of this value must be renamed
     * @param to names being renamed are renamed to this value
     */
    public abstract void rename(int from, int to);

    /**
     * Rename every single occurence of the first given name with the second
     * given name.
     * @param from all names of this value must be renamed
     * @param to names being renamed are renamed to this value
     */
    public abstract void alphaConvert(int from, int to);

    /**
     * Deep-copy a Term.
     * @return a deep-copy of this Term
     */
    public abstract Term copy();

    /**
     * Force subclasses to override toString. This is not supposed to be a
     * particularly nice pretty-print, just something simple to use for casual
     * tests.
     * @return a string representation of this Term
     */
    @Override
    public abstract String toString();

    /**
     * Prettier stringification method that converts variable names back to
     * those given by the user.
     * @param nameMap a HashMap used to obtain user variable names from
     * interpreter ones
     * @return a string representation of this Term
     */
    public abstract String toNiceString(HashMap<Integer, String> nameMap);

    /**
     * Determine if two terms will exchange a message. Two terms are defined as
     * able to do so when any top-level (i.e. not to the right of a '.') send or
     * receive nodes have matching channels. These may be within parallel
     * compositions, replications and restrictions, although of course if the
     * communication channel is restricted then no communication can occur. Note
     * that is implementation is only correct when the given terms are in the
     * same scope.
     * @param t1 the first term
     * @param t2 the second term
     * @return true if the terms will exchange a message, false otherwise
     */
    public static boolean talksTo(Term t1, Term t2) {
        return Term.talksTo(t1, t2, new HashSet<Integer>(),
                new HashSet<Integer>());
    }
    private static boolean talksTo(Term t1, Term t2,
            HashSet<Integer> t1Restricted, HashSet<Integer> t2Restricted) {

        // If we have a send and a receive, return true if they are on the same
        // channel and neither of them have that channel restricted
        if(t1 instanceof Send && t2 instanceof Receive) {
            Send s1 = (Send) t1;
            return s1.getSendOn() == ((Receive) t2).getReceiveOn() &&
                    (!(t1Restricted.contains(s1.getSendOn()))) &&
                    (!(t2Restricted.contains(s1.getSendOn())));
        }
        else if(t1 instanceof Receive && t2 instanceof Send) {
            Send s2 = (Send) t2;
            return s2.getSendOn() == ((Receive) t1).getReceiveOn() &&
                    (!(t1Restricted.contains(s2.getSendOn()))) &&
                    (!(t2Restricted.contains(s2.getSendOn())));
        }

        // If one of the terms is a replicate, return true if the body of the
        // replicated term would talk to the other term, false otherwise
        else if(t1 instanceof Replicate) {
            return Term.talksTo(((Replicate) t1).getToReplicate(), t2,
                    t1Restricted, t2Restricted);
        }
        else if(t2 instanceof Replicate) {
            return Term.talksTo(t1, ((Replicate) t2).getToReplicate(),
                    t1Restricted, t2Restricted);
        }

        // If one of the terms is a parallel composition, return true if either
        // of its subprocesses would talk to the other term
        else if(t1 instanceof Parallel) {
            return Term.talksTo(((Parallel) t1).getSubprocess1(), t2,
                    t1Restricted, t2Restricted) ||
                    Term.talksTo(((Parallel) t1).getSubprocess2(), t2,
                    t1Restricted, t2Restricted);
        }
        else if(t2 instanceof Parallel) {
            return Term.talksTo(t1, ((Parallel) t2).getSubprocess1(),
                    t1Restricted, t2Restricted) ||
                    Term.talksTo(t1, ((Parallel) t2).getSubprocess2(),
                    t1Restricted, t2Restricted);
        }

        // If one of the terms is a restriction, add the restricted name to the
        // set of restricted names, and ask if the subterm would talk to the
        // other term
        else if(t1 instanceof Restrict) {
            Restrict r1 = (Restrict) t1;
            HashSet<Integer> t1RestrictedNew =
                    (HashSet<Integer>) t1Restricted.clone();
            t1RestrictedNew.add(r1.getBoundName());
            return Term.talksTo(r1.getRestrictIn(), t2, t1RestrictedNew,
                    t2Restricted);
        }
        else if(t2 instanceof Restrict) {
            Restrict r2 = (Restrict) t2;
            HashSet<Integer> t2RestrictedNew =
                    (HashSet<Integer>) t2Restricted.clone();
            t2RestrictedNew.add(r2.getBoundName());
            return Term.talksTo(t1, r2.getRestrictIn(), t1Restricted,
                    t2RestrictedNew);
        }

        // No other possibilities, so return false if no other conditions catch
        else { return false; }
    }
}
