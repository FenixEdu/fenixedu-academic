package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.commons.student.enrollment.AbstractOptionalCurricularCoursesLocationManagementDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

@Mapping(path = "/optionalCurricularCoursesLocation", module = "academicAdminOffice", formBeanClass = OptionalCurricularCoursesLocationManagementDA.OptionalCurricularCoursesLocationForm.class)
@Forwards( {
	@Forward(name = "showEnrolments", path = "/academicAdminOffice/curriculum/enrolments/location/showEnrolments.jsp"),
	@Forward(name = "chooseNewDestination", path = "/academicAdminOffice/curriculum/enrolments/location/chooseNewDestination.jsp"),
	@Forward(name = "backToStudentEnrolments", path = "/studentEnrolments.do?method=prepare")

})
public class OptionalCurricularCoursesLocationManagementDA extends AbstractOptionalCurricularCoursesLocationManagementDA {

    @Override
    public ActionForward backToStudentEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("backToStudentEnrolments");
    }
}
