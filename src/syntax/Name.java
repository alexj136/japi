package syntax;

/**
 * A Name object represents a Pi-Calculus name.
 */
class Name {

    private final int nameID;

    /**
     * Construct a new Name object.
     * @param nameID the 'name' to store. A name is an integer for the sake of
     * efficiency. User-supplied names may be Strings, but they must be
     * converted into integers.
     * @return a Name object storing the suppied ID/name/whatever you want to
     * call it.
     */
    public Name(int nameID) {
        this.nameID = nameID;
    }

    /**
     * Access the nameID
     * @return the name value.
     */
    public int get() {
        return this.nameID;
    }

    /**
     * Compare this Name to another object.
     * @param obj the object to compare
     * @return true if the passed object is a Name with the same ID value, false
     * otherwise
     */
    @Override
    public boolean equals(Object obj) {
        // This is safe from ClassCastException since the right-hand-side is
        // only evaluated in the case that obj is a Name.
        return (obj instanceof Name) && ((Name) obj).get() == this.nameID;
    }
}
