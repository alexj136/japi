package syntax;

/**
 * A name represents a channel name in the Pi calculus. The user supplies names
 * represented by strings, however we prefer to use a more efficient
 * representation (integers) during execution. The Name interface abstracts over
 * the implementation detail of name representaion.
 */
public interface Name<R> {

    /**
     * Access the underlying name, represented by an object of type R.
     */
    public R get();
    
    /**
     * The only real requirement of a Name is that it be comparable to others,
     * i.e. that we can determine whether or not it is equal to another Name.
     * @param other The Name to compare this Name with
     * @return true if the names match, false otherwise
     */
    public boolean matches(Name other) throws NameRepresentationException;

    public void rename(Name from, Name to) throws NameRepresentationException;
    public String prettyPrint(int indentationLevel);
}
