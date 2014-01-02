package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.contents.Content;

public abstract class AbstractPathProcessor {

    public static Map<String, AbstractPathProcessor> strategies = new HashMap<String, AbstractPathProcessor>();
    private final static String DEFAULT = "defaultType";

    protected synchronized static void initialize() {
        strategies.put("net.sourceforge.fenixedu.domain.homepage.Homepage", new HomePagePathProcessor());
        strategies.put("net.sourceforge.fenixedu.domain.ResearchUnitSite", new ResearchUnitPathProcessor());
        strategies.put("net.sourceforge.fenixedu.domain.ScientificCouncilSite", new ScientificCouncilPathProcessor());
        strategies.put("net.sourceforge.fenixedu.domain.PedagogicalCouncilSite", new PedagogicalCouncilPathProcessor());
        strategies.put("net.sourceforge.fenixedu.domain.DepartmentSite", new DepartmentSitePathProcessor());
        strategies.put("net.sourceforge.fenixedu.domain.DegreeSite", new DegreePathProcessor());
        strategies.put("net.sourceforge.fenixedu.domain.ExecutionCourseSite", new ExecutionCoursePathProcessor());
        strategies.put("net.sourceforge.fenixedu.domain.ScientificAreaSite", new ScientificAreaPathProcessor());
        strategies.put("net.sourceforge.fenixedu.domain.TutorSite", new TutorUnitProcessor());
        strategies.put("net.sourceforge.fenixedu.domain.StudentsSite", new StudentsSiteProcessor());
        strategies.put("net.sourceforge.fenixedu.domain.thesis.ThesisSite", new ThesisSiteProcessor());
        strategies.put("net.sourceforge.fenixedu.domain.AssemblySite", new AssemblySiteProcess());
        strategies.put("net.sourceforge.fenixedu.domain.ManagementCouncilSite", new ManagementCouncilSiteProcessor());
        strategies.put("net.sourceforge.fenixedu.domain.EdamSite", new EdamPathProcessor());
        strategies.put(DEFAULT, new DefaultPathProcessor());
    }

    // public String getSubPathForSearch(final String path) {
    // final int indexOfSlash = path.indexOf('/');
    // return indexOfSlash >= 0 ? path.substring(0, indexOfSlash) : path;
    // }

    public String getTrailingPath(final String path) {
        final int indexOfSlash = path.indexOf('/');
        return indexOfSlash >= 0 ? path.substring(indexOfSlash + 1, path.length()) : "";
    }

    public abstract Content processPath(String path);

    public static AbstractPathProcessor findStrategyFor(String type) {
        synchronized (DEFAULT) {
            if (strategies.isEmpty()) {
                initialize();
            }
        }

        AbstractPathProcessor processor = strategies.get(type);
        return processor != null ? processor : strategies.get(DEFAULT);
    }

    public Content getInitialContent() {
        return null;
    }

    public boolean keepPortalInContentsPath() {
        return true;
    }
}
