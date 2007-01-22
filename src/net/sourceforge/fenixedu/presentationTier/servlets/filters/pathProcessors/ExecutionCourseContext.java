package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

public interface ExecutionCourseContext extends SiteContext {
    public CurricularCourse getCurricularCourse();
    public List<ExecutionCourse> getExecutionCourses();
    public ExecutionCourse getExecutionCourse();
}
