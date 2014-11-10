package syntax;

public final class Variable<T> extends LambdaTerm<T> {

    private T name;

    public Variable(T name) {
        this.name = name;
    }
}
