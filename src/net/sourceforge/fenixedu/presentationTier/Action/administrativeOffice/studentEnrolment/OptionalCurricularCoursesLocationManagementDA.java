package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.presentationTier.Action.commons.student.enrollment.AbstractOptionalCurricularCoursesLocationManagementDA;

public class OptionalCurricularCoursesLocationManagementDA extends AbstractOptionalCurricularCoursesLocationManagementDA {

    @Override
    public ActionForward backToStudentEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("backToStudentEnrolments");

    }
}
