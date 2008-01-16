package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;

public class ExecutionCourseProcessor extends PathProcessor {

    public ExecutionCourseProcessor(String forwardURI) {
	super(forwardURI);
    }

    public ExecutionCourseProcessor add(YearProcessor processor) {
	addChild(processor);
	return this;
    }

    public ExecutionCourseProcessor add(SemesterProcessor processor) {
	addChild(processor);
	return this;
    }

    public ExecutionCourseProcessor add(ContentProcessor processor) {
	addChild(processor);
	return this;
    }

    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
	return new Context(parentContext, getForwardURI());
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
	String current = provider.current();

	Context ownContext = (Context) context;
	ownContext.setAcronym(current);

	boolean accepts = ownContext.getCurricularCourse() != null;

	if (accepts) {
	    setFunctionalityContext(context, ownContext.getExecutionCourse());
	}
	return accepts;
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider) throws IOException, ServletException {
	if (provider.hasNext()) {
	    return false;
	} else {
	    ExecutionCourseContext ownContext = (ExecutionCourseContext) context;
	    ExecutionCourse executionCourse = ownContext.getExecutionCourse();

	    if (executionCourse == null) {
		return false;
	    } else {
		return doForward(context, new Object[] { "firstPage", executionCourse.getIdInternal() });
	    }
	}
    }

    private void setFunctionalityContext(ProcessingContext context, ExecutionCourse executionCourse) {
	ExecutionCourseSite site = executionCourse.getSite();
	MetaDomainObjectPortal portal = (MetaDomainObjectPortal) MetaDomainObject.getMeta(site.getClass()).getAssociatedPortal();

	List<Content> contents = new ArrayList<Content>();
	contents.add(portal);
	contents.add(site);

	HttpServletRequest request = context.getRequest();
	FilterFunctionalityContext filterContext = new FilterFunctionalityContext(request, contents);
	filterContext.setHasBeenForwarded();
	request.removeAttribute(FunctionalityContext.CONTEXT_KEY);
	request.setAttribute(FunctionalityContext.CONTEXT_KEY, filterContext);
    }

    public static class Context extends AbstractExecutionCourseContext {

	private String acronym;
	private CurricularCourse curricularCourse;

	public Context(ProcessingContext parent, String contextURI) {
	    super(parent, contextURI);
	}

	public String getAcronym() {
	    return this.acronym;
	}

	public void setAcronym(String acronym) {
	    this.acronym = acronym;
	    this.curricularCourse = null;
	}

	private DegreeCurricularPlan getDegreeCurricularPlan() {
	    return ((DegreeCurricularPlanContext) getParent()).getDegreeCurricularPlan();
	}

	public CurricularCourse getCurricularCourse() {
	    if (this.curricularCourse != null) {
		return this.curricularCourse;
	    }

	    for (CurricularCourse curricularCourse : getDegreeCurricularPlan().getCurricularCourses()) {
		if (curricularCourse.getAcronym() != null && curricularCourse.getAcronym().equalsIgnoreCase(getAcronym())) {
		    return this.curricularCourse = curricularCourse;
		}
	    }

	    return null;
	}

	public List<ExecutionCourse> getExecutionCourses() {
	    return getCurricularCourse().getAssociatedExecutionCourses();
	}

	@Override
	public ExecutionCourse getExecutionCourse() {
	    List<ExecutionCourse> executionCourses = getCurricularCourse().getMostRecentExecutionCourses();

	    if (executionCourses.isEmpty()) {
		return null;
	    } else {
		return executionCourses.get(executionCourses.size() - 1);
	    }
	}

    }

    public static String getExecutionCourseAbsolutePath(ExecutionCourse executionCourse) {
	String format = "/%s/%s/%s/%s/%s/%s-semestre";

	Iterator<CurricularCourse> iterator = executionCourse.getAssociatedCurricularCourses().iterator();
	if (!iterator.hasNext()) {
	    return null;
	}

	CurricularCourse course = iterator.next();

	Degree degree = course.getDegree();
	DegreeCurricularPlan curricularPlan = course.getDegreeCurricularPlan();
	ExecutionPeriod period = executionCourse.getExecutionPeriod();

	return String.format(format, degree.getSigla().toLowerCase(), ExecutionCoursesProcessor.PREFIX, curricularPlan
		.getInitialDateYearMonthDay().getYear(), course.getAcronym().toLowerCase(), period.getExecutionYear().getYear()
		.replace('/', '-'), period.getSemester());
    }

    public static String getExecutionCoursePath(ExecutionCourse executionCourse) {
	StringBuilder result = new StringBuilder();

	ExecutionPeriod currentPeriod = ExecutionPeriod.readActualExecutionPeriod();
	ExecutionPeriod period = executionCourse.getExecutionPeriod();

	CurricularCourse course = executionCourse.getAssociatedCurricularCourses().get(0);
	Degree degree = course.getDegree();

	DegreeCurricularPlan mostRecent = degree.getMostRecentDegreeCurricularPlan();
	DegreeCurricularPlan curricularPlan = course.getDegreeCurricularPlan();

	String acronym = course.getAcronym();
	String sigla = degree.getSigla();

	result.append(acronym + "/" + ExecutionCoursesProcessor.PREFIX + "/");

	if (!mostRecent.equals(curricularPlan)) {
	    result.append(curricularPlan.getInitialDateYearMonthDay().getYear() + "/" + sigla);
	} else {
	    result.append(sigla);
	}

	if (!period.equals(currentPeriod)) {
	    if (period.getExecutionYear().equals(currentPeriod.getExecutionYear())) {
		if (period.getSemester().equals(currentPeriod.getSemester())) {
		    result.append("/" + period.getSemester() + "-semestre");
		}
	    } else {
		result.append("/" + period.getExecutionYear().getYear().replace('/', '-'));

		if (period.getSemester().equals(currentPeriod.getSemester())) {
		    result.append("/" + period.getSemester() + "-semestre");
		}
	    }
	}

	return result.toString();
    }
}
