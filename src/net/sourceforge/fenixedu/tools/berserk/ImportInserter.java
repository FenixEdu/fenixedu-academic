package net.sourceforge.fenixedu.tools.berserk;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;

import pt.utl.ist.berserk.logic.serviceManager.IServiceDefinition;

public class ImportInserter extends PatternReplacer {

    private final String classname;

    public ImportInserter(IServiceDefinition service) {
	super("package [^;]*;");
	this.classname = service.getImplementationClass().getCanonicalName();
    }

    public ImportInserter(String classname) {
	super("package [^;]*;");
	this.classname = classname;
    }

    @Override
    public SortedSet<Replacement> matchAndReplace(String contents) {
	Matcher matcher = pattern.matcher(contents);
	SortedSet<Replacement> replacements = new TreeSet<Replacement>();
	if (matcher.find()) {
	    replacements.add(new Replacement(matcher.end(), matcher.end(), "\n\nimport " + classname + ";", this.getClass()));
	}
	return replacements;
    }
}
