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
                new HashMap<String, Integer>(), 0);
    }

    private static SyntaxTranslationResult _translate(parsersyntax.Term term,
            HashMap<String, Integer> nameMap, int nextAvailableName) {

        // For Send and Receive, determine the channel names by looking up the
        // string channel names in the map. Set them appropriately, and
        // translate the subterm.
        if(term instanceof parsersyntax.Send) {
            parsersyntax.Send send = (parsersyntax.Send) term;

            int sendOn, toSend;

            if(nameMap.containsKey(send.getSendOn())) {
                sendOn = nameMap.get(send.getSendOn());
            }
            else {
                sendOn = nextAvailableName;
                nextAvailableName++;
                nameMap.put(send.getSendOn(), sendOn);
            }

            if(nameMap.containsKey(send.getToSend())) {
                toSend = nameMap.get(send.getToSend());
            }
            else {
                toSend = nextAvailableName;
                nextAvailableName++;
                nameMap.put(send.getToSend(), toSend);
            }

            SyntaxTranslationResult result = _translate(send.getSubprocess(),
                    nameMap, nextAvailableName);
            result.setTerm(new runsyntax.Send(sendOn, toSend,
                        result.getTerm()));
            return result;
        }

        else if(term instanceof parsersyntax.Receive) {
            parsersyntax.Receive rece = (parsersyntax.Receive) term;

            int receiveOn, bindTo;

            if(nameMap.containsKey(rece.getReceiveOn())) {
                receiveOn = nameMap.get(rece.getReceiveOn());
            }
            else {
                receiveOn = nextAvailableName;
                nextAvailableName++;
                nameMap.put(rece.getReceiveOn(), receiveOn);
            }

            if(nameMap.containsKey(rece.getBindTo())) {
                bindTo = nameMap.get(rece.getBindTo());
            }
            else {
                bindTo = nextAvailableName;
                nextAvailableName++;
                nameMap.put(rece.getBindTo(), bindTo);
            }

            SyntaxTranslationResult result = _translate(rece.getSubprocess(),
                    nameMap, nextAvailableName);
            result.setTerm(new runsyntax.Receive(receiveOn, bindTo,
                        result.getTerm()));
            return result;
        }

        // Translate the LHS of the composition, and use the generated nameMap
        // to translate the RHS
        else if(term instanceof parsersyntax.Parallel) {
            parsersyntax.Parallel para = (parsersyntax.Parallel) term;

            SyntaxTranslationResult result = _translate(para.getSubprocess1(),
                    nameMap, nextAvailableName);
            runsyntax.Term sub1 = result.getTerm();
            result = _translate(para.getSubprocess2(), result.getNameMap(),
                    result.getNextAvailableName());
            runsyntax.Term sub2 = result.getTerm();
            result.setTerm(new runsyntax.Parallel(sub1, sub2));
            return result;
        }

        // With Replicate, translate the subterm, and return the translated term
        // inside a Replicate node, with the nameMap from the translation of the
        // subterm.
        else if(term instanceof parsersyntax.Replicate) {
            parsersyntax.Replicate repl = (parsersyntax.Replicate) term;

            SyntaxTranslationResult result = _translate(repl.getToReplicate(),
                    nameMap, nextAvailableName);
            result.setTerm(new runsyntax.Replicate(result.getTerm()));
            return result;
        }

        // With Restrict, we determine what integer will represent the bound
        // name and update the map accordingly. Then we use the updated map to
        // rename the subterm.
        else if(term instanceof parsersyntax.Restrict) {
            parsersyntax.Restrict rest = (parsersyntax.Restrict) term;

            int boundName;

            if(nameMap.containsKey(rest.getBoundName())) {
                boundName = nameMap.get(rest.getBoundName());
            }
            else {
                boundName = nextAvailableName;
                nextAvailableName++;
                nameMap.put(rest.getBoundName(), boundName);
            }

            SyntaxTranslationResult result = _translate(rest.getRestrictIn(),
                    nameMap, nextAvailableName);
            result.setTerm(new runsyntax.Restrict(boundName, result.getTerm()));
            return result;
        }

        // This is the simplest case - return a new End node with an unchanged
        // nameMap.
        else if(term instanceof parsersyntax.End) {
            return new SyntaxTranslationResult(new runsyntax.End(), nameMap,
                    nextAvailableName);
        }

        // Throw an exception if we got a parsersyntax.Term of unrecognised
        // type.
        else {
            throw new IllegalArgumentException("Unrecognised " +
                    "parsersyntax.Term type");
        }
    }
}
