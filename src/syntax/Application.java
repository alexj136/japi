package syntax;

public final class Application<T> extends LambdaTerm<T> {

    private LambdaTerm<T> func;
    private LambdaTerm<T> arg;

    public Application(LambdaTerm<T> func, LambdaTerm<T> arg) {
        this.func = func;
        this.arg = arg;
    }
}
