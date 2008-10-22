package net.sourceforge.fenixedu.tools.berserk;

public class Replacement implements Comparable<Replacement> {
    public int startIndex;

    public int endIndex;

    public String replacement;

    private final Class<? extends PatternReplacer> who;

    public Replacement(int startIndex, int endIndex, String replacement, Class<? extends PatternReplacer> who) {
	this.startIndex = startIndex;
	this.endIndex = endIndex;
	this.replacement = replacement;
	this.who = who;
    }

    @Override
    public int compareTo(Replacement other) {
	if (startIndex < other.startIndex && endIndex < other.startIndex)
	    return -1;
	if (startIndex > other.startIndex && startIndex > other.endIndex)
	    return 1;
	if (startIndex == endIndex && startIndex == other.startIndex && endIndex == other.endIndex)
	    return -1;
	throw new Error();
    }
}