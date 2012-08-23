package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.io.Serializable;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.PeriodsManagementBean;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.OccupationPeriodType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
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

/**
 * @author Luis Cruz
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */

@Mapping(path = "/periods", module = "resourceAllocationManager")
@Forwards(tileProperties = @Tile(extend = "definition.sop.periods"), value = {
	@Forward(name = "firstPage", path = "/resourceAllocationManager/periods/firstPage.jsp", contextRelative = true, useTile = true, redirect = false),
	@Forward(name = "managePeriods", path = "/resourceAllocationManager/periods/managePeriods.jsp") })
public class PeriodsDA extends FenixDispatchAction {

    public static class ContextBean implements Serializable, HasExecutionSemester {

	private static final long serialVersionUID = 1L;

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
	    return getPeriod(OccupationPeriodType.LESSONS, executionSemester);
	}

	public OccupationPeriod getExamPeriod() {
	    return getPeriod(OccupationPeriodType.EXAMS, executionSemester);
	}

	public OccupationPeriod getSpecialSeasonExamPeriod() {
	    return getPeriod(OccupationPeriodType.EXAMS_SPECIAL_SEASON, null);
	}

	private OccupationPeriod getPeriod(OccupationPeriodType type, ExecutionSemester semester) {
	    if (executionSemester != null && executionDegree != null) {
		Collection<OccupationPeriod> periods = executionDegree.getPeriods(type,
			semester == null ? null : semester.getSemester());
		if (periods.isEmpty())
		    return null;
		else
		    return periods.iterator().next();
	    }
	    return null;
	}

    }

    public ActionForward firstPage(final ActionMapping mapping, final HttpServletRequest request, final ContextBean contextBean) {
	RenderUtils.invalidateViewState();
	request.setAttribute("contextBean", contextBean);
	return mapping.findForward("firstPage");
    }

    public ActionForward firstPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	ContextBean contextBean = getRenderedObject();
	if (contextBean == null) {
	    contextBean = new ContextBean();
	}
	return firstPage(mapping, request, contextBean);
    }

    /*
     * New period management Actions
     */

    public ActionForward managePeriods(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	PeriodsManagementBean bean = getRenderedObject();

	if (bean == null) {
	    bean = new PeriodsManagementBean();
	} else {
	    try {
		updateBean(request, bean);
	    } catch (DomainException e) {
		addActionMessage("error", request, e.getKey());
	    }
	}

	request.setAttribute("managementBean", bean);

	return mapping.findForward("managePeriods");
    }

    public ActionForward addNewPeriod(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	PeriodsManagementBean bean = getRenderedObject();

	bean.addNewBean();

	request.setAttribute("managementBean", bean);

	return mapping.findForward("managePeriods");
    }

    public ActionForward duplicatePeriod(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	PeriodsManagementBean bean = getRenderedObject();

	try {
	    bean.duplicatePeriod(request.getParameter("toDuplicateId"));
	    addActionMessage("success", request, "label.occupation.period.duplicate.success");
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getKey());
	}

	request.setAttribute("managementBean", bean);

	return mapping.findForward("managePeriods");
    }

    /*
     * Utility method, used to perform the various functions
     */

    private void updateBean(HttpServletRequest request, PeriodsManagementBean bean) {

	if (request.getParameter("editPeriod") != null) {
	    bean.getBeanById(request.getParameter("periodId")).updateDates(request.getParameter("intervals"));
	} else if (request.getParameter("removePeriod") != null) {
	    bean.removePeriod(request.getParameter("removePeriod"));
	} else if (request.getParameter("editPeriodCourses") != null) {
	    bean.getBeanById(request.getParameter("periodId")).updateCourses(request.getParameter("courses"));
	} else if (request.getParameter("createPeriod") != null) {
	    bean.getBeanById(request.getParameter("periodId")).create(request.getParameter("intervals"),
		    request.getParameter("courses"));
	}
    }

}
