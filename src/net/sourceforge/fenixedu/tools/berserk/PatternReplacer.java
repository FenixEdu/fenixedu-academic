package net.sourceforge.fenixedu.tools.berserk;

import java.util.Map;
import java.util.SortedSet;
import java.util.regex.Pattern;

import pt.utl.ist.berserk.logic.serviceManager.IServiceDefinition;

public abstract class PatternReplacer {
    protected static final String SERVICE_MANAGER_EXPRESSION = "((ServiceUtils|ServiceManagerServiceFactory)\\s*\\.)?executeService\\s*";

    protected final Pattern pattern;

    protected Map<String, IServiceDefinition> services;

    public PatternReplacer(String patternString, int flags, Map<String, IServiceDefinition> services) {
	pattern = Pattern.compile(patternString, flags);
	this.services = services;
    }

    public PatternReplacer(String patternString, Map<String, IServiceDefinition> services) {
	pattern = Pattern.compile(patternString);
	this.services = services;
    }

    public PatternReplacer(String patternString, int flags) {
	pattern = Pattern.compile(patternString, flags);
    }

    public PatternReplacer(String patternString) {
	pattern = Pattern.compile(patternString);
    }

    public abstract SortedSet<Replacement> matchAndReplace(String contents) throws FileUntouchableException;

    protected boolean isToReplace(String name) {
	return services.containsKey(name);
    }
    //
    // protected String getImports(String name) {
    // if (services.containsKey(name))
    // return "import " +
    // services.get(name).getImplementationClass().getCanonicalName();
    // return null;
    // }
}