package utils;

import java.util.Optional;

/**
 * Binary sum type.
 */
public class Either<T, U> {

    public final Optional<T> frst;
    public final Optional<U> scnd;

    private Either(Optional<T> frst, Optional<U> scnd) {
        this.frst = frst;
        this.scnd = scnd;
    }

    /**
     * Construct a new Either of the first type.
     * @param frst the value to store
     */
    public static <T, U> Either<T, U> frst(T frst) {
        return new Either<T, U>(Optional.of(frst), Optional.empty());
    }

    /**
     * Construct a new Either of the second type.
     * @param scnd the value to store
     */
    public static <T, U> Either<T, U> scnd(U scnd) {
        return new Either<T, U>(Optional.empty(), Optional.of(scnd));
    }
}
