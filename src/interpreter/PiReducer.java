package interpreter;

import syntax.*;
import java.util.HashSet;
import java.util.HashMap;
import java.util.function.UnaryOperator;
import java.util.function.Supplier;

/**
 * Contains static functions for reducing PiTerms.
 */
public final class PiReducer {

    /**
     * Replace occurrences of the name 'replacing' with the LambdaTerm 'with'
     * within the PiTerm 'in'.
     * @param replacing replace names of this value
     * @param with replaced names are replaced with this LambdaTerm
     * @param in replace inside this PiTerm
     */
    public static void msgPass(Integer replacing, LambdaTerm with, PiTerm in,
            UnaryOperator<Integer> nameGenerator,
            Supplier<Integer> nextAvailableName) {

        PiReducer.preventClashes(with, in, nameGenerator, nextAvailableName);
        PiReducer.msgPassNoClashAssumed(replacing, with, in);
    }

    private static void msgPassNoClashAssumed(Integer replacing,
            LambdaTerm with, PiTerm in) {

        if(in instanceof Send) {
            Send send = (Send) in;

            if(replacing.equals(send.chnl()) && !(with instanceof Variable)) {
                throw new IllegalArgumentException("Tried to replace a " +
                        "channel name with an expression");
            }
            if(replacing.equals(send.chnl()) && with instanceof Variable) {
                Variable var = (Variable) with;
                send.setChnl(var.name());
            }
            for(int i = 0; i < send.arity(); i++) {
                LambdaTerm exp = send.exp(i);
                send.setExp(i, LambdaReducer.substitute(replacing, with, exp));
            }
            PiReducer.msgPassNoClashAssumed(replacing, with, send.subterm());
        }

        else if(in instanceof Receive) {
            Receive rece = (Receive) in;

            if(replacing.equals(rece.chnl()) && !(with instanceof Variable)) {
                throw new IllegalArgumentException("Tried to replace a " +
                        "channel name with an expression");
            }
            else if(replacing.equals(rece.chnl()) && with instanceof Variable) {
                rece.setChnl(((Variable) with).name());
            }
            // Do not rename the message content, in accordance with the
            // semantics of the pi calculus. Only rename in the subprocess if
            // the message did not contain 'from'.
            if(!rece.binds(replacing)) {
                PiReducer.msgPassNoClashAssumed(replacing, with,
                        rece.subterm());
            }
        }

        else if(in instanceof Replicate) {
            PiReducer.msgPassNoClashAssumed(replacing, with,
                    ((Replicate) in).subterm());
        }

        else if(in instanceof Tau) {
            PiReducer.msgPassNoClashAssumed(replacing, with,
                    ((Tau) in).subterm());
        }

        else if(in instanceof Restrict) {
            Restrict rest = (Restrict) in;

            if(!rest.boundName().equals(replacing)) {
                PiReducer.msgPassNoClashAssumed(replacing, with,
                        rest.subterm());
            }
        }

        else if(in instanceof PiTermManySub) {
            PiTermManySub ptms = (PiTermManySub) in;

            for(int i = 0; i < ptms.arity(); i++) {
                PiReducer.msgPassNoClashAssumed(replacing, with,
                        ptms.subterm(i));
            }
        }

        else {
            throw new IllegalArgumentException("Unrecognised PiTerm type in " +
                    "PiReducer.msgPass");
        }
    }

    /**
     * Rename binders and their variables within a Term 'subWithin' where they
     * may erroneously capture free variables within a Term 'toSubstitute'.
     * @param toSubstitute
     * @param subWithin
     * @param nameGenerator Function to obtain fresh names
     * @param nextAvailableName Accessor to the nextAvailableName field in the
     */
    public static void preventClashes(Term toSubstitute, Term subWithin,
            UnaryOperator<Integer> nameGenerator,
            Supplier<Integer> nextAvailableName) {

        HashSet<Integer> atRisk = PiReducer.toRename(
                toSubstitute.freeVars(), subWithin.binders());
        HashMap<Integer, Integer> oldToNew =
            new HashMap<Integer, Integer>();
        for(Integer name : atRisk) {
            oldToNew.put(name, nameGenerator.apply(name));
        }
        int firstIntermediate = nextAvailableName.get();
        int curIntermediate = firstIntermediate;
        for(Integer name : atRisk) {
            subWithin.renameNonFree(name, curIntermediate);
            curIntermediate++;
        }
        curIntermediate = firstIntermediate;
        for(Integer name : atRisk) {
            subWithin.renameNonFree(curIntermediate, oldToNew.get(name));
            curIntermediate++;
        }
    }

    /**
     * Given that a Term T1 whose body binds 'binders', will have a Term T2,
     * with free variables 'freeVars', substituted into it, compute the bound
     * names in T1 that have to be alpha converted to avoid erroneous capture of
     * free variables in T2.
     * @param freeVars the free variables of T2
     * @param binders the names bound in T1
     * @return the binder names that should be renamed in T1 for T2 to be safely
     * substituted in
     */
    public static HashSet<Integer> toRename(HashSet<Integer> freeVars,
            HashSet<Integer> binders) {

        HashSet<Integer> atRisk = new HashSet<Integer>();

        for(Integer freeVarI : freeVars) {
            for(Integer binderI : binders) {
                if(freeVarI.equals(binderI)) {
                    atRisk.add(freeVarI);
                }
            }
        }

        return atRisk;
    }
}
