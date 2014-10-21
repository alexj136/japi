package interpreter;

import runsyntax.Term;
import java.util.ArrayList;

/**
 * Contains references to two terms that will talk to each other.
 */
public class Match {

    public final Term t1, t2;

    /**
     * Construct a new Match. If assertions are enabled, the condition that the
     * given parameters actually match is checked.
     * @param t1 a Term that will reduce with t2
     * @param t2 a Term that will reduce with t1
     */
    public Match(Term t1, Term t2) {
        assert Term.talksTo(t1, t2);
        this.t1 = t1;
        this.t2 = t2;
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
