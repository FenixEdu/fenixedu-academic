package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.SeperateExecutionCourse;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/executionCourseManagement", module = "academicAdministration", formBeanClass = ExecutionCourseManagementForm.class)
@Forwards({ @Forward(name = "index", path = "/academicAdministration/executionCourseManagement/index.jsp"),
	@Forward(name = "create", path = "/academicAdministration/executionCourseManagement/create.jsp"),
	@Forward(name = "view", path = "/academicAdministration/executionCourseManagement/view.jsp"),
	@Forward(name = "viewSummary", path = "/academicAdministration/executionCourseManagement/viewSummary.jsp")

})
public class ExecutionCourseManagementDispatchAction extends FenixDispatchAction {

    public ActionForward index(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	ExecutionCourseSearchBean searchBean = new ExecutionCourseSearchBean();
	request.setAttribute("searchBean", searchBean);

	request.setAttribute("result", new ArrayList<ExecutionCourse>());

	return mapping.findForward("index");
    }

    public ActionForward chooseExecutionSemesterPostback(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	ExecutionCourseSearchBean searchBean = readSearchBean();

	searchBean.setCurricularCourse(null);
	searchBean.setDegreeCurricularPlan(null);

	request.setAttribute("searchBean", searchBean);
	request.setAttribute("result", resultsForSearchBean(searchBean));

	RenderUtils.invalidateViewState();
	return mapping.findForward("index");
    }

    public ActionForward chooseDegreeCurricularPlanPostback(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	ExecutionCourseSearchBean searchBean = readSearchBean();
	searchBean.setCurricularCourse(null);

	request.setAttribute("searchBean", searchBean);
	request.setAttribute("result", resultsForSearchBean(searchBean));
	request.setAttribute("unattachedCurricularCourses", unattachedCurricularCourses(searchBean));

	RenderUtils.invalidateViewState();
	return mapping.findForward("index");

    }

    public ActionForward chooseCurricularCoursePostback(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	ExecutionCourseSearchBean searchBean = readSearchBean();

	request.setAttribute("searchBean", searchBean);
	request.setAttribute("result", resultsForSearchBean(searchBean));
	request.setAttribute("unattachedCurricularCourses", unattachedCurricularCourses(searchBean));

	RenderUtils.invalidateViewState();
	return mapping.findForward("index");
    }

    public ActionForward prepareCreate(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	ExecutionSemester semester = getDomainObject(request, "semesterId");

	ExecutionCourseManagementBean bean = new ExecutionCourseManagementBean(semester);

	return prepareCreate(mapping, form, request, response, bean);
    }

    private ActionForward prepareCreate(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response, final ExecutionCourseManagementBean bean) {

	if (bean.getDegreeCurricularPlan() != null) {
	    Set<CurricularCourse> activeCurricularCourses = bean.getDegreeCurricularPlan().getActiveCurricularCourses(
		    bean.getSemester());
	    request.setAttribute("activeCurricularCourses", activeCurricularCourses);
	}

	request.setAttribute("bean", bean);

	return mapping.findForward("create");

    }

    public ActionForward chooseDegreeCurricularPlanPostbackForEdit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	ExecutionCourseManagementBean bean = readManagementBean();
	ExecutionCourse executionCourse = getDomainObject(request, "executionCourseId");
	ExecutionSemester semester = executionCourse.getExecutionPeriod();

	if (bean.getDegreeCurricularPlan() != null) {
	    request.setAttribute("curricularCoursesToAdd", bean.getDegreeCurricularPlan()
		    .getCurricularCoursesWithoutExecutionCourseFor(semester));
	} else {
	    request.setAttribute("curricularCoursesToAdd", new ArrayList<CurricularCourse>());
	}

	request.setAttribute("tabNum", 1);

	RenderUtils.invalidateViewState();

	return viewExecutionCourse(mapping, form, request, response, bean, executionCourse);
    }

    public ActionForward addSelectedCurricularCourseOnEdit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	ExecutionCourse executionCourse = getDomainObject(request, "executionCourseId");
	CurricularCourse curricularCourse = getDomainObject(request, "curricularCourseId");
	DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();

	ExecutionCourseManagementBean bean = new ExecutionCourseManagementBean(degreeCurricularPlan);
	bean.setSemester(executionCourse.getExecutionPeriod());

	ExecutionSemester semester = executionCourse.getExecutionPeriod();
	request.setAttribute("curricularCoursesToAdd",
		degreeCurricularPlan.getCurricularCoursesWithoutExecutionCourseFor(semester));
	request.setAttribute("tabNum", 1);

	try {
	    executionCourse.associateCurricularCourse(curricularCourse);
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getKey(), e.getArgs());
	}

	return viewExecutionCourse(mapping, form, request, response, bean, executionCourse);
    }

    public ActionForward removeSelectedCurricularCourseOnEdit(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	DegreeCurricularPlan degreeCurricularPlan = getDomainObject(request, "degreeCurricularPlanId");
	ExecutionCourseManagementBean bean = new ExecutionCourseManagementBean(degreeCurricularPlan);
	ExecutionCourse executionCourse = getDomainObject(request, "executionCourseId");

	bean.setSemester(executionCourse.getExecutionPeriod());
	ExecutionSemester semester = executionCourse.getExecutionPeriod();

	if (bean.getDegreeCurricularPlan() != null) {
	    request.setAttribute("curricularCoursesToAdd",
		    degreeCurricularPlan.getCurricularCoursesWithoutExecutionCourseFor(semester));
	} else {
	    request.setAttribute("curricularCoursesToAdd", new ArrayList<CurricularCourse>());
	}

	request.setAttribute("tabNum", 1);

	CurricularCourse curricularCourse = getDomainObject(request, "curricularCourseId");

	try {
	    executionCourse.dissociateCurricularCourse(curricularCourse);
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getKey(), e.getArgs());
	}

	bean.getCurricularCourseList().remove(curricularCourse);

	RenderUtils.invalidateViewState();
	return viewExecutionCourse(mapping, form, request, response, bean, executionCourse);
    }

    public ActionForward createExecutionCourse(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	ExecutionCourseManagementBean bean = readManagementBean();

	try {
	    ExecutionCourseManagementBean executionCourseBean = new ExecutionCourseManagementBean(bean.getSemester());
	    ExecutionCourse executionCourse = ExecutionCourse.createExecutionCourse(bean);

	    return viewExecutionCourse(mapping, form, request, response, executionCourseBean, executionCourse);
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getKey(), e.getArgs());
	    return prepareCreate(mapping, form, request, response, bean);
	}
    }

    public ActionForward createExecutionCourseInvalid(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	ExecutionCourseManagementBean bean = readManagementBean();

	return prepareCreate(mapping, form, request, response, bean);
    }

    private ExecutionCourseSearchBean readSearchBean() {
	return getRenderedObject("searchBean");
    }

    private ExecutionCourseManagementBean readManagementBean() {
	return getRenderedObject("bean");
    }

    public ActionForward viewExecutionCourse(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	ExecutionCourse executionCourse = getDomainObject(request, "executionCourseId");
	ExecutionCourseManagementBean bean = new ExecutionCourseManagementBean(executionCourse.getExecutionPeriod());

	return viewExecutionCourse(mapping, form, request, response, bean, executionCourse);
    }

    private ActionForward viewExecutionCourse(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response, final ExecutionCourseManagementBean bean,
	    final ExecutionCourse executionCourse) {
	request.setAttribute("bean", bean);
	request.setAttribute("executionCourse", executionCourse);

	return mapping.findForward("view");
    }

    public ActionForward removeAttends(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	Attends attends = getDomainObject(request, "attendsId");
	ExecutionCourse executionCourse = getDomainObject(request, "executionCourseId");

	try {
	    attends.getRegistration().removeAttendFor(executionCourse);
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getKey(), e.getArgs());
	}

	request.setAttribute("tabNum", 2);

	ExecutionCourseManagementBean bean = new ExecutionCourseManagementBean();

	return viewExecutionCourse(mapping, form, request, response, bean, executionCourse);
    }

    public ActionForward chooseDegreeCurricularPlanPostbackForTransfer(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	ExecutionCourseManagementBean bean = readManagementBean();
	bean.setCurricularYear(null);
	bean.setExecutionCourse(null);

	request.setAttribute("tabNum", 3);
	RenderUtils.invalidateViewState();

	ExecutionCourse executionCourse = getDomainObject(request, "executionCourseId");

	return viewExecutionCourse(mapping, form, request, response, bean, executionCourse);
    }

    public ActionForward chooseCurricularYearPostbackForTransfer(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	ExecutionCourseManagementBean bean = readManagementBean();
	bean.setExecutionCourse(null);

	request.setAttribute("tabNum", 3);
	RenderUtils.invalidateViewState();
	ExecutionCourse executionCourse = getDomainObject(request, "executionCourseId");

	return viewExecutionCourse(mapping, form, request, response, bean, executionCourse);
    }

    public ActionForward chooseExecutionCoursePostbackForTransfer(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	ExecutionCourseManagementBean bean = readManagementBean();

	request.setAttribute("tabNum", 3);
	RenderUtils.invalidateViewState();
	ExecutionCourse executionCourse = getDomainObject(request, "executionCourseId");

	return viewExecutionCourse(mapping, form, request, response, bean, executionCourse);
    }

    public ActionForward transfer(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	ExecutionCourseManagementBean bean = readManagementBean();
	ExecutionCourse executionCourse = getDomainObject(request, "executionCourseId");
	List<CurricularCourse> curricularCoursesToTransfer = readCurricularCoursesToTransfer(request);
	List<Shift> shiftsToTransfer = readShiftsToTransfer(request);
	ExecutionCourse destinyExecutionCourse = bean.getExecutionCourse();

	SeperateExecutionCourse.run(executionCourse, destinyExecutionCourse, shiftsToTransfer, curricularCoursesToTransfer);

	request.setAttribute("tabNum", 3);

	return viewExecutionCourse(mapping, form, request, response, bean, executionCourse);
    }

    public ActionForward backTransfer(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	ExecutionCourseManagementBean bean = readManagementBean();
	ExecutionCourse executionCourse = getDomainObject(request, "executionCourseId");
	List<CurricularCourse> curricularCoursesToTransfer = readCurricularCoursesToTransfer(request);
	List<Shift> shiftsToTransfer = readShiftsToTransfer(request);
	ExecutionCourse destinyExecutionCourse = bean.getExecutionCourse();

	SeperateExecutionCourse.run(destinyExecutionCourse, executionCourse, shiftsToTransfer, curricularCoursesToTransfer);

	request.setAttribute("tabNum", 3);

	return viewExecutionCourse(mapping, form, request, response, bean, executionCourse);
    }

    public ActionForward separate(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	ExecutionCourseManagementBean bean = readManagementBean();
	ExecutionCourse executionCourse = getDomainObject(request, "executionCourseId");
	List<CurricularCourse> curricularCoursesToTransfer = readCurricularCoursesToTransfer(request);
	List<Shift> shiftsToTransfer = readShiftsToTransfer(request);

	ExecutionCourse newExecutionCourse = SeperateExecutionCourse.run(executionCourse, null, shiftsToTransfer,
		curricularCoursesToTransfer);

	request.setAttribute("newExecutionCourse", newExecutionCourse);
	request.setAttribute("tabNum", 4);

	return viewExecutionCourse(mapping, form, request, response, bean, executionCourse);
    }

    public ActionForward viewSummary(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	Summary summary = getDomainObject(request, "summaryId");

	request.setAttribute("summary", summary);

	return mapping.findForward("viewSummary");

    }

    public ActionForward backFromSummary(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	ExecutionCourse executionCourse = getDomainObject(request, "executionCourseId");
	ExecutionCourseManagementBean bean = new ExecutionCourseManagementBean(executionCourse.getExecutionPeriod());

	request.setAttribute("tabNum", 5);

	return viewExecutionCourse(mapping, form, request, response, bean, executionCourse);
    }

    public ActionForward createExecutionCourseForCurricularCourse(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	ExecutionSemester semester = getDomainObject(request, "semesterId");
	CurricularCourse curricularCourse = getDomainObject(request, "curricularCourseId");

	ExecutionCourseManagementBean bean = new ExecutionCourseManagementBean(semester, curricularCourse);

	return prepareCreate(mapping, form, request, response, bean);
    }

    private List<CurricularCourse> readCurricularCoursesToTransfer(final HttpServletRequest request) {
	List<CurricularCourse> result = new ArrayList<CurricularCourse>();

	String[] curricularCourseIds = request.getParameterValues("curricularCourseIdsToTransfer");

	if (curricularCourseIds == null) {
	    return result;
	}

	for (String externalId : curricularCourseIds) {
	    result.add((CurricularCourse) CurricularCourse.fromExternalId(externalId));
	}

	return result;
    }

    private List<Shift> readShiftsToTransfer(final HttpServletRequest request) {
	List<Shift> result = new ArrayList<Shift>();

	String[] shiftsIds = request.getParameterValues("shiftsIdsToTransfer");

	if (shiftsIds == null) {
	    return result;
	}

	for (String externalId : shiftsIds) {
	    result.add((Shift) Shift.fromExternalId(externalId));
	}

	return result;
    }

    private List<ExecutionCourse> resultsForSearchBean(final ExecutionCourseSearchBean searchBean) {

	List<ExecutionCourse> semesterResult = new ArrayList<ExecutionCourse>();

	if (searchBean.getSemester() == null) {
	    return semesterResult;
	}

	semesterResult = searchBean.getSemester().getAssociatedExecutionCourses();

	if (searchBean.getDegreeCurricularPlan() == null) {
	    return semesterResult;
	}

	List<ExecutionCourse> degreeCurricularPlanResult = new ArrayList<ExecutionCourse>();
	CollectionUtils.select(semesterResult, new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		ExecutionCourse executionCourse = (ExecutionCourse) arg0;
		return executionCourse.getAssociatedDegreeCurricularPlans().contains(searchBean.getDegreeCurricularPlan());
	    }
	}, degreeCurricularPlanResult);

	if (searchBean.getCurricularCourse() == null) {
	    return degreeCurricularPlanResult;
	}

	List<ExecutionCourse> curricularCourseResult = new ArrayList<ExecutionCourse>();

	CollectionUtils.select(degreeCurricularPlanResult, new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		ExecutionCourse executionCourse = (ExecutionCourse) arg0;
		return executionCourse.getAssociatedCurricularCourses().contains(searchBean.getCurricularCourse());

	    }

	}, curricularCourseResult);

	return curricularCourseResult;

    }

    private Set<CurricularCourse> unattachedCurricularCourses(final ExecutionCourseSearchBean searchBean) {
	DegreeCurricularPlan degreeCurricularPlan = searchBean.getDegreeCurricularPlan();

	if (degreeCurricularPlan == null) {
	    return new HashSet<CurricularCourse>();
	}

	return degreeCurricularPlan.getCurricularCoursesWithoutExecutionCourseFor(searchBean.getSemester());
    }

}
