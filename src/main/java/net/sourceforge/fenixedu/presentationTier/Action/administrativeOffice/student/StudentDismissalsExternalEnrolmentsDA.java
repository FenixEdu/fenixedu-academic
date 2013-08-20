package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment.StudentExternalEnrolmentsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentDismissalsExternalEnrolments", module = "academicAdministration",
        formBean = "studentExternalEnrolmentsForm")
@Forwards({ @Forward(name = "manageDismissals", path = "/academicAdminOffice/dismissal/managementDismissals.jsp"),
        @Forward(name = "visualizeRegistration", path = "/academicAdminOffice/student/registration/viewRegistrationDetails.jsp") })
public class StudentDismissalsExternalEnrolmentsDA extends StudentExternalEnrolmentsDA {

    @Override
    public ActionForward backToMainPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("studentCurricularPlan", getStudentCurricularPlan(request));
        return mapping.findForward("manageDismissals");
    }

    @Override
    public ActionForward cancelExternalEnrolment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("registration", getRegistration(request, actionForm));
        return mapping.findForward("visualizeRegistration");
    }

    @Override
    public String getContextInformation() {
        return "/studentDismissalsExternalEnrolments.do?";
    }

    @Override
    protected String getParameters(final HttpServletRequest request) {
        return "scpID=" + getStringFromRequest(request, "scpID");
    }

    private StudentCurricularPlan getStudentCurricularPlan(final HttpServletRequest request) {
        return getDomainObject(request, "scpID");
    }
}
