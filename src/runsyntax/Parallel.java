package runsyntax;

import java.util.ArrayList;
/**
 * Binary parallel composition.
 */
public class Parallel extends Term {

    private Term subprocess1;
    private Term subprocess2;

    /**
     * Construct a new Parallel Term.
     * @param subprocess1 the first subprocess
     * @param subprocess2 the second subprocess
     * @return a new Term representing Parallel composition of the two given
     * terms
     */
    public Parallel(Term subprocess1, Term subprocess2) {
        this.subprocess1 = subprocess1;
        this.subprocess2 = subprocess2;
    }

    /**
     * Access the first subprocess in this parallel composition.
     * @return the first subprocess in this parallel composition
     */
    public Term getSubprocess1() { return this.subprocess1; }

    /**
     * Access the second subprocess in this parallel composition.
     * @return the second subprocess in this parallel composition
     */
    public Term getSubprocess2() { return this.subprocess2; }

    /**
     * Rename names in an Parallel node as is required when a message is passed.
     * In this case, we simply propagate the call to both subprocesses.
     * @param from all names of this value are renamed
     * @param to renamed names are renamed to this name
     */
    public void rename(int from, int to) {
        this.subprocess1.rename(from, to);
        this.subprocess2.rename(from, to);
    }

    /**
     * Deep-copy this Parallel.
     * @return a deep-copy of this Parallel
     */
    public Parallel copy() {
        return new Parallel(this.subprocess1.copy(), this.subprocess2.copy());
    }

    public String toString() {
        ArrayList<String> subTermStrings = this.allParallelStrings();
        String all = subTermStrings.remove(0);
        for(String s : subTermStrings) { all += "|" + s; }
        return "[" + all + "]";
    }
    public ArrayList<String> allParallelStrings() {
        ArrayList<String> strings = new ArrayList<String>();
        if(this.subprocess1 instanceof Parallel) {
            strings.addAll(((Parallel) this.subprocess1).allParallelStrings());
        }
        else { strings.add(this.subprocess1.toString()); }
        if(this.subprocess2 instanceof Parallel) {
            strings.addAll(((Parallel) this.subprocess2).allParallelStrings());
        }
        else { strings.add(this.subprocess2.toString()); }
        return strings;
    }
}
