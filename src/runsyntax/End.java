package runsyntax;

/**
 * A concluded process.
 */
public class End extends Term {

    /**
     * Rename names in an End node as is required when a message is passed.
     * Nothing is done in this case.
     * @param from all names of this value are renamed
     * @param to renamed names are renamed to this name
     */
    public void rename(int from, int to) {}
}
