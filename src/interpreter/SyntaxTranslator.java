package interpreter;

import java.util.HashMap;

/**
 * Contains static methods used to translate parsersyntax.Terms to
 * runsyntax.Terms.
 */
public abstract class SyntaxTranslator {

    /**
     * Translate a parsersyntax.Term into a runsyntax.Term.
     * @param term the parsersyntax.Term to translate
     * @return a SyntaxTranslationResult object containing a runsyntax.Term
     * representing the given parsersyntax.Term, and a HashMap that maps
     * String names in the parsersyntax.Term to integer names in the
     * runsyntax.Term.
     */
    public static SyntaxTranslationResult translate(parsersyntax.Term term) {
        return SyntaxTranslator._translate(term,
                new HashMap<String, Integer>());
    }

    private static SyntaxTranslationResult _translate(parsersyntax.Term term,
            HashMap<String, Integer> nameMap) {

        if(term instanceof parsersyntax.Send) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        else if(term instanceof parsersyntax.Receive) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        else if(term instanceof parsersyntax.Parallel) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        // With Replicate, translate the subterm, and return the translated term
        // inside a Replicate node, with the nameMap from the translation of the
        // subterm.
        else if(term instanceof parsersyntax.Replicate) {
            parsersyntax.Replicate repl = (parsersyntax.Replicate) term;
            SyntaxTranslationResult tempResult =
                    _translate(repl.getToReplicate(), nameMap);
            runsyntax.Term newTerm =
                    new runsyntax.Replicate(tempResult.getTerm());
            HashMap<String, Integer> newNameMap = tempResult.getNameMap();
            return new SyntaxTranslationResult(newTerm, newNameMap);
        }

        else if(term instanceof parsersyntax.Restrict) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        // This is the simplest case - return a new End node with an unchanged
        // nameMap.
        else if(term instanceof parsersyntax.End) {
            return new SyntaxTranslationResult(new runsyntax.End(), nameMap);
        }

        // Throw an exception if we got a parsersyntax.Term of unrecognised
        // type.
        else {
            throw new IllegalArgumentException("Unrecognised " +
                    "parsersyntax.Term type");
        }
    }
}
