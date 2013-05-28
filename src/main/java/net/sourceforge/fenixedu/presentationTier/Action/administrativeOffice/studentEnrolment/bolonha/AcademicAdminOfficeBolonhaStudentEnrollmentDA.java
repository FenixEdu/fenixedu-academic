package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment.bolonha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.student.enrollment.bolonha.AbstractBolonhaStudentEnrollmentDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/bolonhaStudentEnrollment", module = "academicAdministration", formBean = "bolonhaStudentEnrollmentForm")
@Forwards({ @Forward(name = "showStudentEnrollmentMenu",
        path = "/studentEnrolments.do?method=prepareFromStudentEnrollmentWithRules") })
public class AcademicAdminOfficeBolonhaStudentEnrollmentDA extends AbstractBolonhaStudentEnrollmentDA {

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return prepareShowDegreeModulesToEnrol(mapping, form, request, response, getStudentCurricularPlan(request),
                getExecutionPeriod(request));
    }

    @Override
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {

        request.setAttribute("action", getAction());
        addDebtsWarningMessages(studentCurricularPlan.getRegistration().getStudent(), executionSemester, request);

        return super.prepareShowDegreeModulesToEnrol(mapping, form, request, response, studentCurricularPlan, executionSemester);
    }

    protected void addDebtsWarningMessages(final Student student, final ExecutionSemester executionSemester,
            final HttpServletRequest request) {
        if (student.isAnyGratuityOrAdministrativeOfficeFeeAndInsuranceInDebt()) {
            addActionMessage("warning", request, "label.student.events.in.debt.warning");
        }
    }

    protected StudentCurricularPlan getStudentCurricularPlan(final HttpServletRequest request) {
        return AbstractDomainObject.fromExternalId(getRequestParameterAsInteger(request, "scpID"));
    }

    protected ExecutionSemester getExecutionPeriod(final HttpServletRequest request) {
        return AbstractDomainObject.fromExternalId(getRequestParameterAsInteger(request, "executionPeriodID"));
    }

    private Boolean getWithRules(final ActionForm form) {
        return (Boolean) ((DynaActionForm) form).get("withRules");
    }

    public ActionForward backToStudentEnrollments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean = getBolonhaStudentEnrollmentBeanFromViewState();
        request.setAttribute("studentCurricularPlan", bolonhaStudentEnrollmentBean.getStudentCurricularPlan());
        request.setAttribute("executionPeriod", bolonhaStudentEnrollmentBean.getExecutionPeriod());

        return mapping.findForward("showStudentEnrollmentMenu");

    }

    @Override
    protected int[] getCurricularYearForCurricularCourses() {
        return null;
    }

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(final ActionForm form) {
        return getWithRules(form) ? CurricularRuleLevel.ENROLMENT_WITH_RULES : CurricularRuleLevel.ENROLMENT_NO_RULES;
    }

    @Override
    protected String getAction() {
        return "/bolonhaStudentEnrollment.do";
    }

    @Override
    public ActionForward prepareChooseCycleCourseGroupToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("withRules", request.getParameter("withRules"));
        return super.prepareChooseCycleCourseGroupToEnrol(mapping, form, request, response);
    }

    public ActionForward cancelChooseCycleCourseGroupToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return prepareShowDegreeModulesToEnrol(mapping, form, request, response, getStudentCurricularPlan(request),
                getExecutionPeriod(request));
    }

}
