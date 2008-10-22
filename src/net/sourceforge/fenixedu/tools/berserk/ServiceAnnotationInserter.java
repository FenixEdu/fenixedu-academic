package net.sourceforge.fenixedu.tools.berserk;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceAnnotationInserter extends PatternReplacer {

    private final Pattern correct;

    public ServiceAnnotationInserter() {
	super(
		"^    (final )?(public|private|protected)\\s+([\\w|<|>|, ]+)\\s+(\\w+)(\\s*\\([^\\)]*\\)\\s*(throws\\s*[^\\{]*)?\\{)",
		Pattern.DOTALL | Pattern.MULTILINE);
	this.correct = Pattern.compile("extends\\s+FenixService\\s+", Pattern.DOTALL);
    }

    @Override
    public SortedSet<Replacement> matchAndReplace(String contents) throws FileUntouchableException {
	if (contents.contains("AutoCompleteSearchService") || contents.contains("UpdateObjects")
		|| contents.contains("Authenticate") || contents.contains("CreateGratuitySituationsForCurrentExecutionYear")) {
	    throw new FileUntouchableException();
	}
	Matcher correctMatcher = correct.matcher(contents);
	if (correctMatcher.find()) {
	    Matcher matcher = pattern.matcher(contents);
	    SortedSet<Replacement> replacements = new TreeSet<Replacement>();
	    while (matcher.find()) {
		String methodType = matcher.group(2);
		String returnType = matcher.group(3);
		String methodName = matcher.group(4);
		String rest = matcher.group(5);

		if (methodType.equals("public") && methodName.equals("run")) {
		    replacements.add(new Replacement(matcher.start(), matcher.end(), "    @Service\n    " + methodType
			    + " static " + returnType + " " + methodName + rest, this.getClass()));
		} else if (methodType.equals("private") || methodType.equals("protected") || methodType.equals("public")) {
		    replacements.add(new Replacement(matcher.start(), matcher.end(), "    " + methodType + " static "
			    + returnType + " " + methodName + rest, this.getClass()));
		}
	    }
	    return replacements;
	} else {
	    throw new FileUntouchableException();
	}
    }
}
