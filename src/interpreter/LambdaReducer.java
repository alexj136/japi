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

    /**
     * Reduce a LambdaTerm until it is in weak-head normal form. Mutates the
     * given term - do not keep any pointers to it after calling.
     * @param term the term to reduce
     * @param nameGenerator
     * @return the reduced term
     */
    public static LambdaTerm reduce(LambdaTerm term,
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
            Application app = (Application) term;
            Abstraction abs = (Abstraction) app.func();

            /*
             * Prevent name clashes occuring.
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
            term = LambdaReducer.substitute(abs.name(), app.arg(), abs.body());
        }
        return term;
    }

    /**
     * Substitute all occurences of the variable replacing with the term with,
     * within the term in.
     * @param in the expression to substitute inside
     * @param replacing the variable to substitue out
     * @param with the term to substitute in
     * @return a pointer to the generated term
     */
    public static LambdaTerm substitute(Integer replacing, LambdaTerm with,
            LambdaTerm in) {

        if(in instanceof Abstraction) {
            Abstraction abs = (Abstraction) in;
            if(abs.name().equals(replacing)) {
                return in;
            }
            else {
                abs.setBody(LambdaReducer.substitute(replacing, with,
                        abs.body()));
                return abs;
            }
        }
        else if(in instanceof Application) {
            Application app = (Application) in;
            app.setFunc(LambdaReducer.substitute(replacing, with, app.func()));
            app.setArg(LambdaReducer.substitute(replacing, with, app.arg()));
            return app;
        }
        else if(in instanceof Variable) {
            Variable var = (Variable) in;
            if(var.name().equals(replacing)) {
                return with.copy();
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
