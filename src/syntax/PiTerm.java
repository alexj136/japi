package syntax;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A PiTerm is a pi-calculus expression. This is a generic class since different
 * data types can be (and are) used to represent names in the pi-calculus. For
 * example, the parser parses a PiTerm<String>, whereas the interpreter operates
 * over PiTerm<Integer>s. Thus a translation from String to Integer names must
 * occur before interpretation. The only constraint on the type of name that can
 * be used is that it must have a reasonable definition of equality (i.e. it
 * override java.lang.Object.equals(Object o) in a sensible way. Unfortunately,
 * java's type system does not allow one to actually enforce such a constraint.
 */
public abstract class PiTerm<T> extends Term<T> {

    /**
     * Copy a PiTerm. Contained name objects need not be deeply copied.
     * @return a copy of this PiTerm.
     */
    public abstract PiTerm<T> copy();

    /**
     * Generate a string of the given number of tabs, for use when pretty-
     * printing terms. The generated tabs are actually 4 space characters.
     * @param numTabs the desired number of tabs
     * @return a string of (4 * numTabs) space characters
     */
    public static String indent(int numTabs) {
        return new String(new char[4 * numTabs]).replace('\0', ' ');
    }


    /**
     * Generate strings from lists of different kinds, that are nicely delimited
     * and have nice opening/closing parentheses.
     * @param open a string used as the open-parenthesis
     * @param close a string used as the close-parenthesis
     * @param delimiter a string used to delimit elements of elems
     * @param elems the elements
     * @return a nice string representation of the given list, using the given
     * open and close parentheses and delimiter. Example:
     *     stringifyList("(", ")", ",", names) where names is a list of
     *     integers, yields:
     *     "(1, 2, 3, 4)"
     */
    public static String stringifyList(String open, String close,
            String delimiter, ArrayList elems) {

        if(elems.isEmpty()) { return open + close; }
        else {
            String out = open + elems.get(0).toString();
            for(int i = 1; i < elems.size(); i++) {
                out += delimiter + elems.get(i).toString();
            }
            return out + close;

        }
    }

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
    public static <T> boolean talksTo(PiTerm<T> t1, PiTerm<T> t2) {
        return PiTerm.talksTo(t1, t2, new HashSet<T>(),
                new HashSet<T>());
    }
    private static <T> boolean talksTo(PiTerm<T> t1, PiTerm<T> t2,
            HashSet<T> t1Restricted, HashSet<T> t2Restricted) {

        // If we have a send and a receive, return true if they are on the same
        // channel and neither of them have that channel restricted
        if(t1 instanceof Send && t2 instanceof Receive) {
            Send s1 = (Send) t1;
            Receive r2 = (Receive) t2;
            return s1.chnl().equals(r2.chnl()) && (s1.arity() == r2.arity()) &&
                    (!(t1Restricted.contains(s1.chnl()))) &&
                    (!(t2Restricted.contains(s1.chnl())));
        }
        else if(t1 instanceof Receive && t2 instanceof Send) {
            return PiTerm.talksTo(t2, t1, t2Restricted, t1Restricted);
        }

        // If one of the terms is a replicate, return true if the body of the
        // replicated term would talk to the other term, false otherwise
        else if(t1 instanceof Replicate) {
            return PiTerm.talksTo(((Replicate) t1).subterm(), t2, t1Restricted,
                    t2Restricted);
        }
        else if(t2 instanceof Replicate) {
            return PiTerm.talksTo(t2, t1, t2Restricted, t1Restricted);
        }

        // If one of the terms is a parallel composition, return true if either
        // of its subprocesses would talk to the other term
        else if(t1 instanceof PiTermManySub) {
            PiTermManySub ptms;
            if(t1 instanceof Parallel) { ptms = (Parallel) t1; }
            else if(t1 instanceof NDSum) { ptms = (NDSum) t1; }
            else { throw new IllegalArgumentException("Unrecognised " +
                    "PiTermManySub PiTerm in PiTerm.talksTo()"); }

            boolean foundMatch = false;
            int i = 0;
            while(i < ptms.arity() && !foundMatch) {
                if(PiTerm.talksTo(ptms.subterm(i), t2, t1Restricted,
                        t2Restricted)) {

                    foundMatch = true;
                }
                i++;
            }
            return foundMatch;
        }
        else if(t2 instanceof PiTermManySub) {
            return PiTerm.talksTo(t2, t1, t2Restricted, t1Restricted);
        }

        // If one of the terms is a restriction, add the restricted name to the
        // set of restricted names, and ask if the subterm would talk to the
        // other term
        else if(t1 instanceof Restrict) {
            Restrict r1 = (Restrict) t1;
            HashSet t1RestrictedNew = (HashSet) t1Restricted.clone();
            t1RestrictedNew.add(r1.boundName());
            return PiTerm.talksTo(r1.subterm(), t2, t1RestrictedNew,
                    t2Restricted);
        }
        else if(t2 instanceof Restrict) {
            return PiTerm.talksTo(t2, t1, t2Restricted, t1Restricted);
        }

        // No other possibilities, so return false if no other conditions catch
        else { return false; }
    }
}
