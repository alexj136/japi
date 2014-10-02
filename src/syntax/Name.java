package syntax;

/**
 * A name represents a channel name in the Pi calculus. The user supplies names
 * represented by strings, however we prefer to use a more efficient
 * representation (integers) during execution. The Name interface abstracts over
 * the implementation detail of name representaion.
 */
public abstract class Name<R> extends SyntaxElement {

    /**
     * Access the underlying name, represented by an object of type R.
     */
    public abstract R get();
    
    /**
     * The only real requirement of a Name is that it be comparable to others,
     * i.e. that we can determine whether or not it is equal to another Name.
     * @param other The Name to compare this Name with
     * @return true if the names match, false otherwise
     */
    public abstract boolean matches(Name other)
    throws NameRepresentationException;

    /**
     * Name inherits the rename method from SyntaxElement, but concrete
     * subclasses of Name must implement this for themselves.
     */
    public abstract void rename(Name from, Name to)
    throws NameRepresentationException;

    /**
     * As with rename, Name also inherits prettyPrint from SyntaxElement, but it
     * must be implemented by concrete classes.
     */
    public abstract String prettyPrint(int indentationLevel);
}
