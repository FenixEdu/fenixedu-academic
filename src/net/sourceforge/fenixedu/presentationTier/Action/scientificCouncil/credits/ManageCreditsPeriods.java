package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.teacherCredits.TeacherCreditsPeriodBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageCreditsPeriods extends FenixDispatchAction {

    public ActionForward showPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	TeacherCreditsPeriodBean bean = (TeacherCreditsPeriodBean) getRenderedObject("teacherCreditsBeanID");
	if (bean == null) {
	    ExecutionSemester executionSemester = getExecutionPeriodToEditPeriod(request);
	    if (executionSemester == null) {
		bean = new TeacherCreditsPeriodBean(ExecutionSemester.readActualExecutionSemester());
	    } else {
		bean = new TeacherCreditsPeriodBean(executionSemester);
	    }
	} else {
	    bean.refreshDates();
	}

	request.setAttribute("teacherCreditsBean", bean);
	return mapping.findForward("show-credits-periods");
    }

    public ActionForward prepareEditTeacherCreditsPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ExecutionSemester executionSemester = getExecutionPeriodToEditPeriod(request);
	request.setAttribute("teacherCreditsBean", new TeacherCreditsPeriodBean(executionSemester, true));
	return mapping.findForward("edit-teacher-credits-periods");
    }

    public ActionForward prepareEditDepartmentAdmOfficeCreditsPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ExecutionSemester executionSemester = getExecutionPeriodToEditPeriod(request);
	request.setAttribute("teacherCreditsBean", new TeacherCreditsPeriodBean(executionSemester, false));
	return mapping.findForward("edit-teacher-credits-periods");
    }

    public ActionForward editPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	TeacherCreditsPeriodBean bean = (TeacherCreditsPeriodBean) getRenderedObject("teacherCreditsBeanID");

	try {
	    executeService("CreateTeacherCreditsFillingPeriod", new Object[] { bean });

	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("teacherCreditsBean", bean);
	    return mapping.findForward("edit-teacher-credits-periods");
	}

	request.setAttribute("teacherCreditsBean", bean);
	RenderUtils.invalidateViewState("teacherCreditsBeanID");
	return mapping.findForward("show-credits-periods");
    }

    private ExecutionSemester getExecutionPeriodToEditPeriod(HttpServletRequest request) {
	String parameter = request.getParameter("executionPeriodId");
	Integer executionPeriodId = parameter != null ? Integer.valueOf(parameter) : null;
	return rootDomainObject.readExecutionSemesterByOID(executionPeriodId);
    }
}
