package syntax;

import java.util.HashSet;

/**
 * LambdaTerms are terms in the lambda calculus. This class contains static
 * methods for evaluating them.
 */
public abstract class LambdaTerm<T> extends Term<T> {

    /**
     * Copy a LambdaTerm. Contained name objects need not be deeply copied.
     * @return a copy of this LambdaTerm.
     */
    public abstract LambdaTerm<T> copy();

    /**
     * Enumerate the free variables in this LambdaTerm.
     * @return a HashSet of all the free variable names in this LambdaTerm
     */
    public abstract HashSet<T> freeVars();

    /**
     * Enumerate the variable names that have their binders within the given
     * LambdaTerm.
     * @return a HashSet of all the variable names that have their binders
     * within the given LambdaTerm
     */
    public abstract HashSet<T> binders();

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
     * @return the reduced term
     */
    public static <T> LambdaTerm<T> reduce(LambdaTerm<T> term) {
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
            Application<T> app = (Application) term;
            Abstraction<T> abs = (Abstraction) app.func();
            term = LambdaTerm.substitute(abs.body(), abs.name(), app.arg());
        }
        return term;
    }

    public static <T> LambdaTerm<T> substitute(LambdaTerm<T> subWithin,
            T subOut, LambdaTerm<T> subIn) {

        if(subWithin instanceof Abstraction) {
            Abstraction abs = (Abstraction) subWithin;
            if(abs.name().equals(subOut)) {
                return subWithin;
            }
            else {
                abs.setBody(LambdaTerm.substitute(abs.body(), subOut, subIn));
                return abs;
            }
        }
        else if(subWithin instanceof Application) {
            Application app = (Application) subWithin;
            app.setFunc(LambdaTerm.substitute(app.func(), subOut, subIn));
            app.setArg(LambdaTerm.substitute(app.arg(), subOut, subIn));
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
                    "in LambdaTerm.substitute()");
        }
    }
}
