package interpreter;

import runsyntax.Term;
import java.util.HashMap;

/**
 * A pair containing a runsyntax.Term and a HashMap storing a map between the
 * user's String names and integer names used by the interpreter.
 */
public class SyntaxTranslationResult {

    private Term term;
    private HashMap<String, Integer> nameMap;

    /**
     * Construct a new SyntaxTranslationResult.
     * @param term the runsyntax.Term to store
     * @param nameMap a mapping between the user's String names and the
     * interpreter's integer names
     */
    public SyntaxTranslationResult(Term term,
            HashMap<String, Integer> nameMap) {

        this.term = term;
        this.nameMap = nameMap;
    }

    /**
     * Access the stored Term
     * @return the stored Term
     */
    public Term getTerm() { return this.term; }

    /**
     * Access the stored name mapping
     * @return the stored name mapping
     */
    public HashMap<String, Integer> getNameMap() { return this.nameMap; }
}
