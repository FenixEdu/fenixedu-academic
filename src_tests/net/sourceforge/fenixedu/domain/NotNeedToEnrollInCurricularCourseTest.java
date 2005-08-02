package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.degree.enrollment.INotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;

public class NotNeedToEnrollInCurricularCourseTest extends DomainTestBase {

	private INotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourseToDelete = null;

	private void setUpDelete() {
		notNeedToEnrollInCurricularCourseToDelete = new NotNeedToEnrollInCurricularCourse();

		ICurricularCourse curricularCourse = new CurricularCourse();

		curricularCourse.addNotNeedToEnrollInCurricularCourses(notNeedToEnrollInCurricularCourseToDelete);
		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();

		studentCurricularPlan.addNotNeedToEnrollCurricularCourses(notNeedToEnrollInCurricularCourseToDelete);
	}
	
	public void testDelete() {
		
		setUpDelete();
		
		notNeedToEnrollInCurricularCourseToDelete.delete();
		
		assertFalse("Failed to dereference CurricularCourse", notNeedToEnrollInCurricularCourseToDelete.hasCurricularCourse());
		assertFalse("Failed to dereference StudentCurricularPlan", notNeedToEnrollInCurricularCourseToDelete.hasStudentCurricularPlan());
	}
}
