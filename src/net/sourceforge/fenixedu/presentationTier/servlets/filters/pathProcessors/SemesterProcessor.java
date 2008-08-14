package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;

public class SemesterProcessor extends PathProcessor {

    public SemesterProcessor(String forwardURI) {
	super(forwardURI);
    }

    public SemesterProcessor add(PathProcessor processor) {
	addChild(processor);
	return this;
    }

    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
	return new SemesterContext(parentContext, getForwardURI());
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
	String current = provider.current();

	SemesterContext ownContext = (SemesterContext) context;
	ownContext.setSemester(current);

	return ownContext.getSemesterNumber() != null;
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider) throws IOException, ServletException {
	if (provider.hasNext()) {
	    return false;
	} else {
	    SemesterContext ownContext = (SemesterContext) context;
	    ExecutionCourse executionCourse = ownContext.getExecutionCourse();

	    if (executionCourse == null) {
		return false;
	    } else {

		/*
		 * This is an ugly hack so this process still works until the
		 * semester ends where we can easily notify everyone that the
		 * link format has changed.
		 * 
		 * 04-02-2007 pcma
		 */
		HttpServletRequest request = context.getRequest();
		ExecutionCourseSite portalInstance = executionCourse.getSite();
		MetaDomainObjectPortal portal = (MetaDomainObjectPortal) MetaDomainObject.getMeta(portalInstance.getClass())
			.getAssociatedPortal();

		List<Content> contents = new ArrayList<Content>();
		contents.add(portal);
		contents.add(portalInstance);

		FilterFunctionalityContext filterContext = (FilterFunctionalityContext) request
			.getAttribute(FunctionalityContext.CONTEXT_KEY);
		if (filterContext != null) {
		    request.removeAttribute(FunctionalityContext.CONTEXT_KEY);
		}
		filterContext = new FilterFunctionalityContext(request, contents);
		filterContext.setHasBeenForwarded();
		request.setAttribute(FunctionalityContext.CONTEXT_KEY, filterContext);

		String url = portalInstance.getReversePath();
		context.getResponse().sendRedirect(context.getRequest().getContextPath() + url);
		return true;
	    }
	}
    }

    public static class SemesterContext extends AbstractExecutionCourseContext {

	private String semester;

	public SemesterContext(ProcessingContext parent, String contextURI) {
	    super(parent, contextURI);
	}

	public String getSemester() {
	    return this.semester;
	}

	public void setSemester(String semester) {
	    this.semester = semester;
	}

	public CurricularCourse getCurricularCourse() {
	    return ((ExecutionCourseContext) getParent()).getCurricularCourse();
	}

	public List<ExecutionCourse> getExecutionCourses() {
	    SortedSet<ExecutionCourse> result = new TreeSet<ExecutionCourse>(
		    ExecutionCourse.EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);

	    Integer semester = getSemesterNumber();
	    for (ExecutionCourse executionCourse : getParentExecutionCourses()) {
		if (executionCourse.getExecutionPeriod().getSemester().equals(semester)) {
		    result.add(executionCourse);
		}
	    }

	    return new ArrayList<ExecutionCourse>(result);
	}

	private List<ExecutionCourse> getParentExecutionCourses() {
	    return ((ExecutionCourseContext) getParent()).getExecutionCourses();
	}

	public Integer getSemesterNumber() {
	    if (getSemester() == null) {
		return null;
	    }

	    String semester = getSemester();
	    if (!semester.matches("\\p{Digit}-[sS][eE][mM][eE][sS][tT][rR][eE]")) {
		return null;
	    }

	    return new Integer(semester.substring(0, 1));
	}

    }

}
