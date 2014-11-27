package interpreter;

import syntax.Term;
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
     * Reduce a LambdaTerm until it is in weak-head normal form. Mutates the
     * given term - do not keep any pointers to it after calling.
     * @param term the term to reduce
     * @param nameGenerator Function to obtain fresh names
     * @param nextAvailableName Accessor to the nextAvailableName field in the
     * Interpreter
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

        // Delve into the term to find reductions if there isn't one at the top
        if(term instanceof Application
                && (!(((Application) term).func() instanceof Abstraction))) {

            Application app = (Application) term;
            app.setFunc(LambdaReducer.reduce(app.func(), nameGenerator,
                    nextAvailableName));
            app.setArg(LambdaReducer.reduce(app.arg(), nameGenerator,
                    nextAvailableName));
        }

        // If there is a redex, reduce it
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

            // Prevent name clashes occuring.
            PiReducer.preventClashes(app.arg(), abs.body(), nameGenerator,
                    nextAvailableName);

            // Do the substitution.
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
