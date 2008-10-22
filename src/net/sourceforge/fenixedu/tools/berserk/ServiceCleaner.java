package net.sourceforge.fenixedu.tools.berserk;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceCleaner extends PatternReplacer {

    private final List<String> cleanList;

    private final StringBuilder removals = new StringBuilder();

    public ServiceCleaner(List<String> cleanList) {
	super("<service>[^<name>]*<name>([^</name>]*)</name>[^</service>]*</service>", Pattern.DOTALL);
	this.cleanList = cleanList;
    }

    @Override
    public SortedSet<Replacement> matchAndReplace(String contents) throws FileUntouchableException {
	Matcher matcher = pattern.matcher(contents);
	SortedSet<Replacement> replacements = new TreeSet<Replacement>();
	while (matcher.find()) {
	    String name = matcher.group(1);
	    if (cleanList.contains(name)) {
		replacements.add(new Replacement(matcher.start(), matcher.end(), "", this.getClass()));
		removals.append(matcher.group() + "\n");
	    }
	}
	return replacements;
    }
}
