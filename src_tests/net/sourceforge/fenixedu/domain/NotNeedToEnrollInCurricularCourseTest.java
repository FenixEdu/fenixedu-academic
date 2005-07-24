package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.degree.enrollment.INotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;

public class NotNeedToEnrollInCurricularCourseTest extends DomainTestBase {

	private INotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourseToDelete = null;
	private ICurricularCourse curricularCourse = null;
	private IStudentCurricularPlan studentCurricularPlan = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		setUpDelete();
	}

	private void setUpDelete() {
		notNeedToEnrollInCurricularCourseToDelete = new NotNeedToEnrollInCurricularCourse();

		curricularCourse = new CurricularCourse();

		curricularCourse.addNotNeedToEnrollInCurricularCourses(notNeedToEnrollInCurricularCourseToDelete);
		studentCurricularPlan = new StudentCurricularPlan();

		studentCurricularPlan.addNotNeedToEnrollCurricularCourses(notNeedToEnrollInCurricularCourseToDelete);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDelete() {
		notNeedToEnrollInCurricularCourseToDelete.delete();
		
		assertFalse(notNeedToEnrollInCurricularCourseToDelete.hasCurricularCourse());
		assertFalse(notNeedToEnrollInCurricularCourseToDelete.hasStudentCurricularPlan());
		
		assertFalse(curricularCourse.hasNotNeedToEnrollInCurricularCourses(notNeedToEnrollInCurricularCourseToDelete));
		assertFalse(studentCurricularPlan.hasNotNeedToEnrollCurricularCourses(notNeedToEnrollInCurricularCourseToDelete));
	}
}
