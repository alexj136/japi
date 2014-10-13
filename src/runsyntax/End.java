package runsyntax;

import java.util.HashMap;

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

    /**
     * Alpha-convert this End Term i.e. do nothing.
     * @param from an arbitrary integer
     * @param to an arbitrary integer
     */
    public void alphaConvert(int from, int to) {}

    /**
     * Deep-copy this End.
     * @return a deep-copy of this End
     */
    public End copy() { return new End(); }

    /**
     * Obtain the String "0".
     * @return "0"
     */
    public String toString() { return "0"; }

    /**
     * Obtain the String "0".
     * @param nameMap an arbitrary HashMap<Integer, String>.
     * @return "0"
     */
    public String toNiceString(HashMap<Integer, String> nameMap) { return "0"; }
}
