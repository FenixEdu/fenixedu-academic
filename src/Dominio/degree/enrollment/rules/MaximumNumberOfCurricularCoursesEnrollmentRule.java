package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IEnrollment;
import Dominio.IStudentCurricularPlan;

/**
 * @author David Santos in Jun 23, 2004
 */

public class MaximumNumberOfCurricularCoursesEnrollmentRule implements IEnrollmentRule
{
	private IStudentCurricularPlan studentCurricularPlan;

	public MaximumNumberOfCurricularCoursesEnrollmentRule(IStudentCurricularPlan studentCurricularPlan)
	{
		this.studentCurricularPlan = studentCurricularPlan;
	}

	public List apply(List curricularCoursesToBeEnrolledIn)
	{
		List curricularCoursesToRemove = new ArrayList();

		int maxEnrolledCurricularCourses = this.studentCurricularPlan.getMaximumNumberOfCoursesToEnroll().intValue();
		int numberOfEnrolledCurricularCourses = 0;
		int availableCurricularCoursesToEnroll = 0;

		int size = this.studentCurricularPlan.getStudentEnrolledEnrollments().size();

		for(int i = 0; i < size; i++)
        {
            IEnrollment enrollment = (IEnrollment) this.studentCurricularPlan.getStudentEnrolledEnrollments().get(i);
            ICurricularCourse curricularCourse = enrollment.getCurricularCourse();
            numberOfEnrolledCurricularCourses += curricularCourse.getEnrollmentWeigth().intValue();
        }

		if (numberOfEnrolledCurricularCourses >= maxEnrolledCurricularCourses)
		{
			return new ArrayList();
		}

		availableCurricularCoursesToEnroll = maxEnrolledCurricularCourses - numberOfEnrolledCurricularCourses;

		size = curricularCoursesToBeEnrolledIn.size();
		for(int i = 0; i < size; i++)
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) curricularCoursesToBeEnrolledIn.get(i);
            int ew = curricularCourse.getEnrollmentWeigth().intValue();
            if (ew > availableCurricularCoursesToEnroll)
            {
            	curricularCoursesToRemove.add(curricularCourse);
            }
        }

		curricularCoursesToBeEnrolledIn.removeAll(curricularCoursesToRemove);

		return curricularCoursesToBeEnrolledIn;
	}
}