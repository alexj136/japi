package interpreter;

import syntax.LambdaTerm;
import syntax.Abstraction;
import syntax.Application;
import syntax.Variable;
import java.util.HashSet;
import java.util.HashMap;
import java.util.function.UnaryOperator;
import java.util.function.Supplier;

/**
 * Contains static functions for reducing LambdaTerms.
 */
public final class LambdaReducer {

    /**
     * Given that a LambdaTerm T1 whose body binds 'binders', will have a
     * LambdaTerm T2, with free variables 'freeVars', substituted into it,
     * compute the bound names in T1 that have to be alpha converted to avoid
     * erroneous capture of free variables in T2.
     * @param freeVars the free variables of T2
     * @param binders the names bound in T1
     * @return the binder names that should be renamed in T1 for T2 to be safely
     * substituted in
     */
    public static <T> HashSet<T> toRename(HashSet<T> freeVars,
            HashSet<T> binders) {

        HashSet<T> atRisk = new HashSet<T>();

        for(T freeVarI : freeVars) {
            for(T binderI : binders) {
                if(freeVarI.equals(binderI)) {
                    atRisk.add(freeVarI);
                }
            }
        }

        return atRisk;
    }

    /**
     * Reduce a LambdaTerm until it is in weak-head normal form. Mutates the
     * given term - do not keep any pointers to it after calling.
     * @param term the term to reduce
     * @param nameGenerator
     * @return the reduced term
     */
    public static LambdaTerm<Integer> reduce(LambdaTerm<Integer> term,
            UnaryOperator<Integer> nameGenerator,
            Supplier<Integer> nextAvailableName) {

        if(!(term instanceof Abstraction
                || term instanceof Application
                || term instanceof Variable)) {

            throw new IllegalArgumentException("Unrecognised LambdaTerm type " +
                    "in LambdaTerm.reduce()");
        }
        while(term instanceof Application
                && ((Application) term).func() instanceof Abstraction) {

            /*
             *           app
             *          /   \
             *         /     \
             *       abs     arg    ->    body{arg/name}
             *      /   \
             *     /     \
             *   name    body
             */
            Application<Integer> app = (Application) term;
            Abstraction<Integer> abs = (Abstraction) app.func();

            /*
             * Prevent any chance of a name clash occuring.
             */
            HashSet<Integer> atRisk = LambdaReducer.toRename(
                    app.arg().freeVars(), abs.body().binders());
            HashMap<Integer, Integer> oldToNew =
                new HashMap<Integer, Integer>();
            for(Integer name : atRisk) {
                oldToNew.put(name, nameGenerator.apply(name));
            }
            int firstIntermediate = nextAvailableName.get();
            int curIntermediate = firstIntermediate;
            for(Integer name : atRisk) {
                abs.body().renameNonFree(name, curIntermediate);
                curIntermediate++;
            }
            curIntermediate = firstIntermediate;
            for(Integer name : atRisk) {
                abs.body().renameNonFree(curIntermediate, oldToNew.get(name));
                curIntermediate++;
            }

            /*
             * Do the substitution.
             */
            term = LambdaReducer.substitute(abs.body(), abs.name(), app.arg());
        }
        return term;
    }

    /**
     * Substitute all occurences of the variable subOut with the term subIn,
     * within the term subWithin.
     * @param subWithin the expression to substitute inside
     * @param subOut the variable to substitue out
     * @param subIn the term to substitute in
     * @return a pointer to the generated term
     */
    public static <T> LambdaTerm<T> substitute(LambdaTerm<T> subWithin,
            T subOut, LambdaTerm<T> subIn) {

        if(subWithin instanceof Abstraction) {
            Abstraction abs = (Abstraction) subWithin;
            if(abs.name().equals(subOut)) {
                return subWithin;
            }
            else {
                abs.setBody(LambdaReducer.substitute(abs.body(), subOut, subIn));
                return abs;
            }
        }
        else if(subWithin instanceof Application) {
            Application app = (Application) subWithin;
            app.setFunc(LambdaReducer.substitute(app.func(), subOut, subIn));
            app.setArg(LambdaReducer.substitute(app.arg(), subOut, subIn));
            return app;
        }
        else if(subWithin instanceof Variable) {
            Variable var = (Variable) subWithin;
            if(var.name().equals(subOut)) {
                return subIn.copy();
            }
            else {
                return var;
            }
        }
        else {
            throw new IllegalArgumentException("Unrecognised LambdaTerm type " +
                    "in LambdaReducer.substitute()");
        }
    }
}
