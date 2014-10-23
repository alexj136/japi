package interpreter;

import syntax.*;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Contains static methods used to translate Term<String>s to
 * Term<Integer>s.
 */
public abstract class SyntaxTranslator {

    /**
     * Translate a Term<String> into a Term<Integer>.
     * @param term the Term<String> to translate
     * @return a SyntaxTranslationResult object containing a Term<Integer>
     * representing the given Term<String>, and a HashMap that maps String names
     * in the Term<String> to integer names in the Term<Integer>.
     */
    public static SyntaxTranslationResult translate(Term<String> term) {
        return SyntaxTranslator._translate(term,
                new HashMap<String, Integer>(), 0);
    }

    private static SyntaxTranslationResult _translate(Term<String> term,
            HashMap<String, Integer> nameMap, int nextAvailableName) {

        // For Send and Receive, determine the channel names by looking up the
        // string channel names in the map. Set them appropriately, and
        // translate the subterm.
        if(term instanceof TermComm) {
            TermComm<String> comm = (TermComm<String>) term;

            int chnl;

            if(nameMap.containsKey(comm.chnl())) {
                chnl = nameMap.get(comm.chnl());
            }
            else {
                chnl = nextAvailableName;
                nameMap.put(comm.chnl(), nextAvailableName);
                nextAvailableName++;
            }

            ArrayList<Integer> msg = new ArrayList<Integer>();

            for(int i = 0; i < comm.arity(); i++) {

                if(nameMap.containsKey(comm.msg(i))) {
                    msg.add(nameMap.get(comm.msg(i)));
                }
                else {
                    msg.add(nextAvailableName);
                    nameMap.put(comm.msg(i), nextAvailableName);
                    nextAvailableName++;
                }

            }

            SyntaxTranslationResult result = _translate(comm.subterm(),
                    nameMap, nextAvailableName);

            if(comm instanceof Send) {
                result.setTerm(new Send<Integer>(chnl, msg, result.getTerm()));
            }
            else if(comm instanceof Receive) {
                result.setTerm(new Receive<Integer>(chnl, msg,
                        result.getTerm()));
            }
            else {
                throw new IllegalArgumentException(
                        "Unrecognised TermComm type");
            }
            return result;
        }

        // Translate the LHS of the composition, and use the generated nameMap
        // to translate the RHS
        else if(term instanceof Parallel) {
            Parallel<String> para = (Parallel<String>) term;

            SyntaxTranslationResult result = new SyntaxTranslationResult(null,
                    nameMap, nextAvailableName);

            ArrayList<Term<Integer>> subterms = new ArrayList<Term<Integer>>();

            for(Term<String> subterm : para) {
                result = _translate(subterm, result.getNameMap(),
                        result.getNextAvailableName());
                subterms.add(result.getTerm());
            }

            result.setTerm(new Parallel<Integer>(subterms));
            return result;
        }

        // With Replicate, translate the subterm, and return the translated term
        // inside a Replicate node, with the nameMap from the translation of the
        // subterm.
        else if(term instanceof Replicate) {
            Replicate repl = (Replicate<String>) term;

            SyntaxTranslationResult result = _translate(repl.subterm(), nameMap,
                    nextAvailableName);
            result.setTerm(new Replicate<Integer>(result.getTerm()));
            return result;
        }

        // With Restrict, we determine what integer will represent the bound
        // name and update the map accordingly. Then we use the updated map to
        // rename the subterm.
        else if(term instanceof Restrict) {
            Restrict<String> rest = (Restrict<String>) term;

            int boundName;

            if(nameMap.containsKey(rest.boundName())) {
                boundName = nameMap.get(rest.boundName());
            }
            else {
                boundName = nextAvailableName;
                nextAvailableName++;
                nameMap.put(rest.boundName(), boundName);
            }

            SyntaxTranslationResult result = _translate(rest.subterm(), nameMap,
                    nextAvailableName);
            result.setTerm(new Restrict<Integer>(boundName, result.getTerm()));
            return result;
        }

        // Throw an exception if we got a Term<String> of unrecognised
        // type.
        else {
            throw new IllegalArgumentException("Unrecognised Term type");
        }
    }
}
