package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment.StudentExternalEnrolmentsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentDismissalsExternalEnrolmentsDA extends StudentExternalEnrolmentsDA {
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("studentId", getStudentCurricularPlan(request).getRegistration().getStudent().getIdInternal());
        return super.execute(mapping, actionForm, request, response);
    }
    
    @Override
    public ActionForward backToMainPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("studentCurricularPlan", getStudentCurricularPlan(request));
	return mapping.findForward("manageDismissals");
    }
    
    @Override
    public ActionForward cancelExternalEnrolment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("registrationId", getStudentCurricularPlan(request).getRegistration().getIdInternal().toString());
	return mapping.findForward("visualizeRegistration");
    }
    
    @Override
    public String getContextInformation() {
	return "/studentDismissalsExternalEnrolments.do?";
    }
    
    @Override
    protected String getParameters(final HttpServletRequest request) {
        return "scpID=" + getIntegerFromRequest(request, "scpID");
    }

    private StudentCurricularPlan getStudentCurricularPlan(final HttpServletRequest request) {
	final Integer scpID = getIntegerFromRequest(request, "scpID");
	return rootDomainObject.readStudentCurricularPlanByOID(scpID);
    }
}
