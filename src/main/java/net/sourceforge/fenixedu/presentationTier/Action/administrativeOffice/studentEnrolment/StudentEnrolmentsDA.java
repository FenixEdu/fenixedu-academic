package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentEnrolments", module = "academicAdministration")
@Forwards({
        @Forward(name = "prepareChooseExecutionPeriod", path = "/academicAdminOffice/chooseStudentEnrolmentExecutionPeriod.jsp"),
        @Forward(name = "visualizeRegistration", path = "/student.do?method=visualizeRegistration") })
public class StudentEnrolmentsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        final Integer scpID = Integer.valueOf(getIntegerFromRequest(request, "scpID"));
        final StudentCurricularPlan plan = rootDomainObject.readStudentCurricularPlanByOID(scpID);
        StudentEnrolmentBean studentEnrolmentBean = new StudentEnrolmentBean();
        if (plan != null) {
            studentEnrolmentBean.setStudentCurricularPlan(plan);
            studentEnrolmentBean.setExecutionPeriod(ExecutionSemester.readActualExecutionSemester());
            return showExecutionPeriodEnrolments(studentEnrolmentBean, mapping, actionForm, request, response);
        } else {
            throw new FenixActionException();
        }
    }

    public ActionForward prepareFromExtraEnrolment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        StudentEnrolmentBean studentEnrolmentBean = (StudentEnrolmentBean) request.getAttribute("studentEnrolmentBean");
        return showExecutionPeriodEnrolments(studentEnrolmentBean, mapping, actionForm, request, response);
    }

    public ActionForward prepareFromStudentEnrollmentWithRules(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final StudentEnrolmentBean studentEnrolmentBean = new StudentEnrolmentBean();
        studentEnrolmentBean.setExecutionPeriod((ExecutionSemester) request.getAttribute("executionPeriod"));
        studentEnrolmentBean.setStudentCurricularPlan((StudentCurricularPlan) request.getAttribute("studentCurricularPlan"));
        return showExecutionPeriodEnrolments(studentEnrolmentBean, mapping, form, request, response);
    }

    private ActionForward showExecutionPeriodEnrolments(StudentEnrolmentBean studentEnrolmentBean, ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("studentEnrolmentBean", studentEnrolmentBean);

        if (studentEnrolmentBean.getExecutionPeriod() != null) {
            request.setAttribute("studentEnrolments", studentEnrolmentBean.getStudentCurricularPlan()
                    .getEnrolmentsByExecutionPeriod(studentEnrolmentBean.getExecutionPeriod()));
            request.setAttribute("studentImprovementEnrolments", studentEnrolmentBean.getStudentCurricularPlan()
                    .getEnroledImprovements(studentEnrolmentBean.getExecutionPeriod()));
            request.setAttribute("studentSpecialSeasonEnrolments", studentEnrolmentBean.getStudentCurricularPlan()
                    .getSpecialSeasonEnrolments(studentEnrolmentBean.getExecutionYear()));
        }

        return mapping.findForward("prepareChooseExecutionPeriod");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        StudentEnrolmentBean enrolmentBean = getRenderedObject();
        RenderUtils.invalidateViewState();

        return showExecutionPeriodEnrolments(enrolmentBean, mapping, actionForm, request, response);
    }

    public ActionForward backViewRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final StudentEnrolmentBean studentEnrolmentBean = getRenderedObject();
        request.setAttribute("registrationId", studentEnrolmentBean.getStudentCurricularPlan().getRegistration().getExternalId());
        return mapping.findForward("visualizeRegistration");
    }

}
