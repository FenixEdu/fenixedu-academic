package net.sourceforge.fenixedu.tools.berserk;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.utl.ist.berserk.logic.serviceManager.IServiceDefinition;

public class OuterArgsCaller extends PatternReplacer {
    public OuterArgsCaller(Map<String, IServiceDefinition> services) {
	super("^\\s*(final )?Object\\[?\\]? (\\w*)\\[?\\]?\\s*=\\s*(new\\s*Object\\[\\]\\s*)?\\{([^\\}]*)\\};" + "(\\s*.*?\\s*)"
		+ SERVICE_MANAGER_EXPRESSION + "\\(\\s*\"([\\w|\\.]*)\",\\s*([^\\)]*)\\)", Pattern.DOTALL | Pattern.MULTILINE,
		services);
    }

    @Override
    public SortedSet<Replacement> matchAndReplace(String contents) {
	Matcher matcher = pattern.matcher(contents);
	SortedSet<Replacement> replacements = new TreeSet<Replacement>();
	while (matcher.find()) {
	    String name = matcher.group(8);
	    String argNameDeclaration = matcher.group(2).trim();
	    String args = matcher.group(4).trim();
	    String argNameUse = matcher.group(9);
	    String intermediate = matcher.group(5);
	    if (isToReplace(name) && argNameDeclaration.equals(argNameUse) && !intermediate.contains("executeService")) {
		replacements.addAll(new ImportInserter(services.get(name)).matchAndReplace(contents));
		replacements.add(new Replacement(matcher.start(), matcher.start(5), "", this.getClass()));
		replacements.add(new Replacement(matcher.end(5), matcher.end(), services.get(name).getImplementationClass()
			.getSimpleName()
			+ ".run(" + args + ")", this.getClass()));
	    }
	}
	return replacements;
    }
}