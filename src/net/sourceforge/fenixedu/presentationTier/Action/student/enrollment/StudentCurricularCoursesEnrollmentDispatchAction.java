package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.CurricularCoursesEnrollmentDispatchAction;

import org.apache.struts.action.DynaActionForm;

public class StudentCurricularCoursesEnrollmentDispatchAction extends CurricularCoursesEnrollmentDispatchAction {

	@Override
	protected Student getStudent(IUserView userView, DynaActionForm form) {	
		return userView.getPerson().getStudentByUsername();
	}
}