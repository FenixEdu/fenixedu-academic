package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentEnrolmentsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
	Integer scpID = Integer.valueOf(request.getParameter("scpID"));
	StudentCurricularPlan studentCurricularPlan = rootDomainObject
		.readStudentCurricularPlanByOID(scpID);
	StudentEnrolmentBean studentEnrolmentBean = new StudentEnrolmentBean();
	if (studentCurricularPlan != null) {
	    studentEnrolmentBean.setStudentCurricularPlan(studentCurricularPlan);
	    studentEnrolmentBean.setExecutionPeriod(ExecutionPeriod.readActualExecutionPeriod());
	    return showExecutionPeriodEnrolments(studentEnrolmentBean, mapping, actionForm, request,
		    response);
	} else {
	    throw new FenixActionException();
	}
    }

    public ActionForward prepareFromExtraEnrolment(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	StudentEnrolmentBean studentEnrolmentBean = (StudentEnrolmentBean) request
		.getAttribute("studentEnrolmentBean");
	return showExecutionPeriodEnrolments(studentEnrolmentBean, mapping, actionForm, request,
		response);
    }

    public ActionForward prepareFromStudentEnrollmentWithRules(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	final StudentEnrolmentBean studentEnrolmentBean = new StudentEnrolmentBean();
	studentEnrolmentBean.setExecutionPeriod((ExecutionPeriod) request
		.getAttribute("executionPeriod"));
	studentEnrolmentBean.setStudentCurricularPlan((StudentCurricularPlan) request
		.getAttribute("studentCurricularPlan"));

	return showExecutionPeriodEnrolments(studentEnrolmentBean, mapping, form, request, response);
    }

    private ActionForward showExecutionPeriodEnrolments(StudentEnrolmentBean studentEnrolmentBean,
	    ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("studentEnrolmentBean", studentEnrolmentBean);

	if (studentEnrolmentBean.getExecutionPeriod() != null) {
	    request.setAttribute("studentEnrolments", studentEnrolmentBean.getStudentCurricularPlan()
		    .getEnrolmentsByExecutionPeriod(studentEnrolmentBean.getExecutionPeriod()));
	}

	return mapping.findForward("prepareChooseExecutionPeriod");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	StudentEnrolmentBean enrolmentBean = (StudentEnrolmentBean) getRenderedObject();
	RenderUtils.invalidateViewState();

	return showExecutionPeriodEnrolments(enrolmentBean, mapping, actionForm, request, response);
    }

}
