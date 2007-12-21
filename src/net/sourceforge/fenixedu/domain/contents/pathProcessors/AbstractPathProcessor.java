package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.contents.Content;

public abstract class AbstractPathProcessor {

    public static Map<String, AbstractPathProcessor> strategies = new HashMap<String, AbstractPathProcessor>();
    private final static String DEFAULT = "defaultType"; 
    
    static {
	strategies.put("net.sourceforge.fenixedu.domain.homepage.Homepage", new HomePagePathProcessor());
	strategies.put("net.sourceforge.fenixedu.domain.ResearchUnitSite", new ResearchUnitPathProcessor());
	strategies.put("net.sourceforge.fenixedu.domain.ScientificCouncilSite", new ScientificCouncilPathProcessor());
	strategies.put("net.sourceforge.fenixedu.domain.PedagogicalCouncilSite", new PedagogicalCouncilPathProcessor());
	strategies.put("net.sourceforge.fenixedu.domain.DepartmentSite", new DepartmentSitePathProcessor());
	strategies.put("net.sourceforge.fenixedu.domain.DegreeSite", new DegreePathProcessor());
	strategies.put(DEFAULT,new DefaultPathProcessor());
    }
    
//    public String getSubPathForSearch(final String path) {
//	final int indexOfSlash = path.indexOf('/');
//	return indexOfSlash >= 0 ? path.substring(0, indexOfSlash) : path;	
//    }
    
    public String getTrailingPath(final String path) {
	final int indexOfSlash = path.indexOf('/');
	return indexOfSlash >= 0 ? path.substring(indexOfSlash+1, path.length()) : path;
    }
    
    public abstract Content processPath(String path);
    
    public static AbstractPathProcessor findStrategyFor(String type) {
	AbstractPathProcessor processor = strategies.get(type);
	return processor != null ? processor : strategies.get(DEFAULT);
    }
    
    public Content getInitialContent() {
	return null;
    }
}
