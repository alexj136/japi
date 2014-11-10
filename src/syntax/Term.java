package syntax;

import java.util.HashMap;

public abstract class Term<T> {
    
    /**
     * Obtain a string representation of this Term.
     * @return a string representing the Term
     */
    public abstract String toString();

    /**
     * Obtain a string representation of this Term, but instead of using the
     * toString method of the contained names, use the toString method of
     * objects mapped to by the contained names in the given map.
     * @return a string representing the Term, printing names of a different
     * type, the values of which are mapped to by the contained names.
     */
    public abstract <U> String toStringWithNameMap(HashMap<T, U> nameMap);

    /**
     * Rename the names in a Term as is necessary after the exchange of a
     * message - this is not alpha-conversion.
     * @param from some names of this value must be renamed
     * @param to names being renamed are renamed to this value
     */
    public abstract void rename(T from, T to);

    /**
     * Rename every single occurence of the first given name with the second
     * given name.
     * @param from all names of this value must be renamed
     * @param to names being renamed are renamed to this value
     */
    public abstract void alphaConvert(T from, T to);

    /**
     * Copy a Term. Contained name objects need not be deeply copied.
     * @return a copy of this Term.
     */
    public abstract Term<T> copy();
}
