package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/periods", module = "resourceAllocationManager")
@Forwards(
	tileProperties = @Tile(extend = "definition.sop.periods"),
	value = { @Forward(name = "firstPage", path = "/resourceAllocationManager/periods/firstPage.jsp",
			contextRelative = true, useTile = true, redirect = false) }
)
public class PeriodsDA extends FenixDispatchAction {

    public static class ContextBean implements Serializable, HasExecutionSemester {

	private ExecutionSemester executionSemester;
	private ExecutionDegree executionDegree;

	public ContextBean() {
	    setExecutionSemester(ExecutionSemester.readActualExecutionSemester());
	}

	public ContextBean(final ExecutionSemester executionSemester) {
	    setExecutionSemester(executionSemester);
	}

	public ExecutionSemester getExecutionSemester() {
	    return executionSemester;
	}

	@Override
	public ExecutionSemester getExecutionPeriod() {
	    return getExecutionSemester();
	}

	public void setExecutionSemester(ExecutionSemester executionSemester) {
	    this.executionSemester = executionSemester;
	    this.executionDegree = null;
	}

	public ExecutionDegree getExecutionDegree() {
	    return executionDegree;
	}

	public void setExecutionDegree(ExecutionDegree executionDegree) {
	    this.executionDegree = executionDegree;
	}

	public EnrolmentPeriodInClasses getEnrolmentPeriodInClasses() {
	    if (executionSemester != null && executionDegree != null) {
		for (final EnrolmentPeriod enrolmentPeriod : executionSemester.getEnrolmentPeriodSet()) {
		    if (enrolmentPeriod instanceof EnrolmentPeriodInClasses
			    && executionDegree.getDegreeCurricularPlan() == enrolmentPeriod.getDegreeCurricularPlan()) {
			return (EnrolmentPeriodInClasses) enrolmentPeriod;
		    }
		}
	    }
	    return null;
	}

	public OccupationPeriod getLessonPeriod() {
	    if (executionSemester != null && executionDegree != null) {
		if (executionSemester.getSemester().intValue() == 1) {
		    return executionDegree.getPeriodLessonsFirstSemester();
		} else if (executionSemester.getSemester().intValue() == 2) {
		    return executionDegree.getPeriodLessonsSecondSemester();
		}
	    }
	    return null;
	}

	public OccupationPeriod getExamPeriod() {
	    if (executionSemester != null && executionDegree != null) {
		if (executionSemester.getSemester().intValue() == 1) {
		    return executionDegree.getPeriodExamsFirstSemester();
		} else if (executionSemester.getSemester().intValue() == 2) {
		    return executionDegree.getPeriodExamsSecondSemester();
		}
	    }
	    return null;
	}

	public OccupationPeriod getSpecialSeasonExamPeriod() {
	    if (executionSemester != null && executionDegree != null) {
		return executionDegree.getPeriodExamsSpecialSeason();
	    }
	    return null;
	}

    }

    public ActionForward firstPage(final ActionMapping mapping, final HttpServletRequest request, final ContextBean contextBean) {
	RenderUtils.invalidateViewState();
	request.setAttribute("contextBean", contextBean);
	return mapping.findForward("firstPage");
    }

    public ActionForward firstPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) {
	ContextBean contextBean = getRenderedObject();
	if (contextBean == null) {
	    contextBean = new ContextBean();
	}
	return firstPage(mapping, request, contextBean);
    }

}
