package syntax;

public abstract class LambdaTerm<T> extends Term<T> {

    public static LambdaTerm<T> reduce(LambdaTerm<T> term) {
        if(term instanceof Abstraction) {
            // Nothing to do
            return term;
        }
        else if(term instanceof Application) {
            Application<T> app = (Application) term;
            if(app.func() instanceof Abstraction) {
                Abstraction<T> abs = (Abstraction) app.func();
                return LambdaTerm.substitute(abs.body(), abs.name(), app.arg());
            }
            else {
                // Nothing to do
                return term;
            }
        }
        else if(term instanceof Var) {
            // Nothing to do
            return term;
        }
        else {
            throw new IllegalArgumentException("Unrecognised LambdaTerm type " +
                    "in LambdaTerm.reduce()");
        }
    }
}
