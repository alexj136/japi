package syntax;

import java.util.HashSet;
import java.util.HashMap;

/**
 * An overarchin Term class - represents any kind of term, lambda or pi.
 */
public abstract class Term {
    
    /**
     * Enumerate the variable names that have their binders within the given
     * LambdaTerm.
     * @return a HashSet of all the variable names that have their binders
     * within the given LambdaTerm
     */
    public abstract HashSet<Integer> binders();

    /**
     * Enumerate the free variables in this LambdaTerm.
     * @return a HashSet of all the free variable names in this LambdaTerm
     */
    public abstract HashSet<Integer> freeVars();

    /**
     * Rename all free occurences of the name 'from' to the name 'to'.
     * @param from names of this value are renamed
     * @param to renamed names are renamed to this value
     */
    public abstract void renameFree(Integer from, Integer to);

    /**
     * Rename all occurences of the name 'from' to the name 'to', in and after
     * the first abstraction that binds this name.
     * @param from names of this value are renamed
     * @param to renamed names are renamed to this value
     */
    public abstract void renameNonFree(Integer from, Integer to);

    /**
     * Rename every single occurence of the first given name with the second
     * given name.
     * @param from all names of this value must be renamed
     * @param to names being renamed are renamed to this value
     */
    public abstract void blindRename(Integer from, Integer to);

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
    public abstract String toStringWithNameMap(
            HashMap<Integer, String> nameMap);

    /**
     * Copy a Term. Contained name objects need not be deeply copied.
     * @return a copy of this Term.
     */
    public abstract Term copy();
}
