package utils;

/**
 * Standard two-tuple class using generics.
 */
public final class Pair<T, U> {
    
    public final T fst;
    public final U snd;

    /**
     * Construct a new Pair.
     * @param fst the first element
     * @param snd the second element
     */
    private Pair(T fst, U snd) {
        this.fst = fst;
        this.snd = snd;
    }

    /**
     * Static constructor for Pairs. Removes the need for the lengthy type name
     * in the constructor call.
     * @param fst the first element
     * @param snd the second element
     * @return a pair (fst, snd) that is compile-time type checked
     */
    public static <T, U> Pair<T, U> make(T fst, U snd) {
        return new Pair<T, U>(fst, snd);
    }

    /**
     * Construct a Pair with the snd element of this Pair and a new fst element
     * @param fst the new fst element
     * @return a new pair with the same snd element as this one, and a new fst
     * element
     */
    public Pair<T, U> withFst(T fst) {
        return new Pair<T, U>(fst, this.snd);
    }

    /**
     * Construct a Pair with the fst element of this Pair and a new snd element
     * @param snd the new snd element
     * @return a new pair with the same fst element as this one, and a new snd
     * element
     */
    public Pair<T, U> withSnd(U snd) {
        return new Pair<T, U>(this.fst, snd);
    }
}
