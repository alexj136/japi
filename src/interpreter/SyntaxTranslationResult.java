package interpreter;

import syntax.PiTerm;
import java.util.HashMap;

/**
 * A pair containing a PiTerm and a HashMap storing a map between the user's
 * String names and integer names used by the interpreter.
 */
public class SyntaxTranslationResult {

    private PiTerm<Integer> term;
    private HashMap<String, Integer> nameMap;
    private int nextAvailableName;

    /**
     * Construct a new SyntaxTranslationResult.
     * @param term the PiTerm to store
     * @param nameMap a mapping between the user's String names and the
     * interpreter's integer names
     */
    public SyntaxTranslationResult(PiTerm<Integer> term,
            HashMap<String, Integer> nameMap,
            int nextAvailableName) {

        this.term = term;
        this.nameMap = nameMap;
        this.nextAvailableName = nextAvailableName;
    }

    /**
     * Access the stored PiTerm.
     * @return the stored PiTerm
     */
    public PiTerm<Integer> getTerm() { return this.term; }

    /**
     * Access the stored name mapping.
     * @return the stored name mapping
     */
    public HashMap<String, Integer> getNameMap() { return this.nameMap; }

    /**
     * Access the next available name.
     * @return the next available name
     */
    public int getNextAvailableName() { return this.nextAvailableName; }

    /**
     * Set the stored PiTerm.
     * @param term the new PiTerm to store
     */
    public void setTerm(PiTerm<Integer> term) { this.term = term; }

    /**
     * Set the stored name mapping.
     * @param nameMap the new nameMap to store
     */
    public void setNameMap(HashMap<String, Integer> nameMap) {
        this.nameMap = nameMap;
    }

    /**
     * Set the next available name.
     * @param nextAvailableName the new next available name
     */
    public void setNextAvailableName(int nextAvailableName) {
        this.nextAvailableName = nextAvailableName;
    }

    /**
     * Increase the stored next available name by 1.
     */
    public void incrementNextAvailableName() { this.nextAvailableName++; }
}
