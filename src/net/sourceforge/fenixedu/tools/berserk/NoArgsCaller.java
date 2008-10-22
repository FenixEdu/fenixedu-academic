/**
 * 
 */
package net.sourceforge.fenixedu.tools.berserk;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.utl.ist.berserk.logic.serviceManager.IServiceDefinition;

public class NoArgsCaller extends PatternReplacer {

    public NoArgsCaller(Map<String, IServiceDefinition> services) {
	super(SERVICE_MANAGER_EXPRESSION + "\\(\\s*\"([\\w|\\.]*)\"(,\\s*null)?\\)", Pattern.DOTALL, services);
    }

    @Override
    public SortedSet<Replacement> matchAndReplace(String contents) {
	Matcher matcher = pattern.matcher(contents);
	SortedSet<Replacement> replacements = new TreeSet<Replacement>();
	while (matcher.find()) {
	    String name = matcher.group(3);
	    if (isToReplace(name)) {
		replacements.addAll(new ImportInserter(services.get(name)).matchAndReplace(contents));
		replacements.add(new Replacement(matcher.start(), matcher.end(), services.get(name).getImplementationClass()
			.getSimpleName()
			+ ".run()", this.getClass()));
	    }
	}
	return replacements;
    }
}