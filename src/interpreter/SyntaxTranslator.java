package interpreter;

import syntax.*;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Contains static methods used to translate PiTerm<String>s to
 * PiTerm<Integer>s.
 */
public abstract class SyntaxTranslator {

    /**
     * Translate a PiTerm<String> into a PiTerm<Integer>.
     * @param term the PiTerm<String> to translate
     * @return a SyntaxTranslationResult object containing a PiTerm<Integer>
     * representing the given PiTerm<String>, and a HashMap that maps String
     * names in the PiTerm<String> to integer names in the PiTerm<Integer>.
     */
    public static SyntaxTranslationResult<PiTerm<Integer>> translate(
            PiTerm<String> term) {

        return SyntaxTranslator._translate(term,
                new HashMap<String, Integer>(), 0);
    }

    private static SyntaxTranslationResult<PiTerm<Integer>> _translate(
            PiTerm<String> term, HashMap<String, Integer> nameMap,
            int nextAvailableName) {

        // For Send and Receive, determine the channel names by looking up the
        // string channel names in the map. Set them appropriately, and
        // translate the subterm.
        if(term instanceof PiTermComm) {
            PiTermComm<String> comm = (PiTermComm<String>) term;

            int chnl;

            if(nameMap.containsKey(comm.chnl())) {
                chnl = nameMap.get(comm.chnl());
            }
            else {
                chnl = nextAvailableName;
                nameMap.put(comm.chnl(), nextAvailableName);
                nextAvailableName++;
            }

            SyntaxTranslationResult<PiTerm<Integer>> result =
                    _translate(comm.subterm(), nameMap, nextAvailableName);

            if(comm instanceof Send) {
                Send<String> send = (Send) comm;

                ArrayList<LambdaTerm<Integer>> exps =
                        new ArrayList<LambdaTerm<Integer>>();

                for(int i = 0; i < send.arity(); i++) {

                    SyntaxTranslationResult<LambdaTerm<Integer>> expResult =
                            _translate(send.exp(i), nameMap, nextAvailableName);

                    exps.add(expResult.getTerm());
                    nextAvailableName = expResult.getNextAvailableName();
                }

                result.setTerm(new Send<Integer>(chnl, exps, result.getTerm()));
            }
            else if(comm instanceof Receive) {
                Receive<String> rece = (Receive) comm;

                ArrayList<Integer> boundNames = new ArrayList<Integer>();

                for(int i = 0; i < rece.arity(); i++) {
                    int newName;
                    if(nameMap.containsKey(rece.name(i))) {
                        newName = nameMap.get(rece.name(i));
                    }
                    else {
                        nameMap.put(rece.name(i), nextAvailableName);
                        newName = nextAvailableName;
                        nextAvailableName++;
                    }

                    boundNames.add(newName);
                }

                result.setTerm(new Receive<Integer>(chnl, boundNames,
                        result.getTerm()));
            }
            else {
                throw new IllegalArgumentException(
                        "Unrecognised PiTermComm type");
            }
            result.setNextAvailableName(nextAvailableName);
            return result;
        }

        // Translate the LHS of the composition, and use the generated nameMap
        // to translate the RHS
        else if(term instanceof PiTermManySub) {
            PiTermManySub ptms;
            if(term instanceof Parallel) {
                ptms = (Parallel<String>) term;
            }
            else if(term instanceof NDSum) {
                ptms = (NDSum<String>) term;
            }
            else {
                throw new IllegalArgumentException(
                        "Unrecognised PiTermManySub type");
            }

            SyntaxTranslationResult<PiTerm<Integer>> result =
                    new SyntaxTranslationResult<PiTerm<Integer>>(null, nameMap,
                            nextAvailableName);

            ArrayList<PiTerm<Integer>> subterms =
                    new ArrayList<PiTerm<Integer>>();

            for(int i = 0; i < ptms.arity(); i++) {
                result = _translate(ptms.subterm(i), result.getNameMap(),
                        result.getNextAvailableName());
                subterms.add(result.getTerm());
            }

            if(term instanceof Parallel) {
                result.setTerm(new Parallel<Integer>(subterms));
            }
            else if(term instanceof NDSum) {
                result.setTerm(new NDSum<Integer>(subterms));
            }
            else {
                throw new IllegalArgumentException(
                        "Unrecognised PiTermManySub type");
            }
            return result;
        }

        // With Replicate, translate the subterm, and return the translated term
        // inside a Replicate node, with the nameMap from the translation of the
        // subterm.
        else if(term instanceof Replicate) {
            Replicate repl = (Replicate<String>) term;

            SyntaxTranslationResult<PiTerm<Integer>> result =
                    _translate(repl.subterm(), nameMap, nextAvailableName);

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

            SyntaxTranslationResult<PiTerm<Integer>> result =
                    _translate(rest.subterm(), nameMap, nextAvailableName);

            result.setTerm(new Restrict<Integer>(boundName, result.getTerm()));
            return result;
        }

        // Throw an exception if we got a PiTerm<String> of unrecognised
        // type.
        else {
            throw new IllegalArgumentException("Unrecognised PiTerm type");
        }
    }
    
    private static SyntaxTranslationResult<LambdaTerm<Integer>> _translate(
            LambdaTerm<String> term, HashMap<String, Integer> nameMap,
            int nextAvailableName) {

        LambdaTerm<Integer> newTerm;

        if(term instanceof Variable) {
            Variable<String> var = (Variable) term;
            if(nameMap.containsKey(var.name())) {
                newTerm = new Variable<Integer>(nameMap.get(var.name()));
            }
            else {
                nameMap.put(var.name(), nextAvailableName);
                newTerm = new Variable<Integer>(nextAvailableName);
                nextAvailableName++;
            }
        }

        else {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        return new SyntaxTranslationResult<LambdaTerm<Integer>>(newTerm,
                nameMap, nextAvailableName);
    }
}
