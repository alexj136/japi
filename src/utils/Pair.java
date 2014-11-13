package utils;

/**
 * Standard two-tuple class using generics.
 */
public final class Pair<T, U> {
    
    private final T first;
    private final U second;

    /**
     * Construct a new Pair.
     * @param first the first element
     * @param second the second element
     */
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Access the first element.
     * @return the first element
     */
    public T first() { return this.first; }

    /**
     * Access the second element.
     * @return the second element
     */
    public U second() { return this.second; }
}
