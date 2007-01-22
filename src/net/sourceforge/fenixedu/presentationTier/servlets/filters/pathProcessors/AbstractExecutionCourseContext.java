package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;

public abstract class AbstractExecutionCourseContext extends ProcessingContext implements ExecutionCourseContext {

    private String contexURI;

    public AbstractExecutionCourseContext(ProcessingContext parent, String contextURI) {
        super(parent);
        
        this.contexURI = contextURI;
    }

    public ExecutionCourse getExecutionCourse() {
        List<ExecutionCourse> executionCourses = getExecutionCourses();
        
        if (executionCourses.isEmpty()) {
            return null;
        }
        else {
            return executionCourses.get(executionCourses.size() - 1);
        }
    }

    public Site getSite() {
        return getExecutionCourse().getSite();
    }
    
    public String getSiteBasePath() {
        return String.format(this.contexURI, "%s", getExecutionCourse().getIdInternal());
    }
    
}
