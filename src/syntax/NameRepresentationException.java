package syntax;

/**
 * A NameRepresentationException represents any exceptional condition relating
 * to the representation of names in the pi calculus. This will usually be
 * thrown when a name of integer representation is compared with a name with a
 * string representation.
 */
public class NameRepresentationException extends Exception {

    /**
     * Construct a new NameRepresentationException.
     * @param message A detail message
     * @return a new NameRepresentationException
     */
    public NameRepresentationException(String message) {
        super(message);
    }
}
