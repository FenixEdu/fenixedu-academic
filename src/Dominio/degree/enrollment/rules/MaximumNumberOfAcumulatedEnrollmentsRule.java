package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IEnrollment;
import Dominio.IStudentCurricularPlan;

/**
 * @author David Santos in Jun 22, 2004
 */

public class MaximumNumberOfAcumulatedEnrollmentsRule implements IEnrollmentRule
{
	private IStudentCurricularPlan studentCurricularPlan;

	public MaximumNumberOfAcumulatedEnrollmentsRule(IStudentCurricularPlan studentCurricularPlan)
	{
		this.studentCurricularPlan = studentCurricularPlan;
		this.studentCurricularPlan.calculateStudentAcumulatedEnrollments();
	}

	public List apply(List curricularCoursesToBeEnrolledIn)
	{
		List curricularCoursesToRemove = new ArrayList();

		int totalNAC = 0;

		int size = this.studentCurricularPlan.getStudentEnrolledEnrollments().size();

		for(int i = 0; i < size; i++)
        {
            IEnrollment enrollment = (IEnrollment) this.studentCurricularPlan.getStudentEnrolledEnrollments().get(i);
            ICurricularCourse curricularCourse = enrollment.getCurricularCourse();
            totalNAC += this.studentCurricularPlan.getCurricularCourseAcumulatedEnrolments(curricularCourse).intValue();
        }
		
		int maxNAC = this.studentCurricularPlan.getMaximumNumberOfAcumulatedEnrollments().intValue();
		
		if (totalNAC >= maxNAC)
		{
			return new ArrayList();
		}

		int availableNACToEnroll = maxNAC - totalNAC;

		size = curricularCoursesToBeEnrolledIn.size();
		for(int i = 0; i < size; i++)
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) curricularCoursesToBeEnrolledIn.get(i);
            int ac = this.studentCurricularPlan.getCurricularCourseAcumulatedEnrolments(curricularCourse).intValue();
            if (ac > availableNACToEnroll)
            {
            	curricularCoursesToRemove.add(curricularCourse);
            }
        }

		curricularCoursesToBeEnrolledIn.removeAll(curricularCoursesToRemove);

		return curricularCoursesToBeEnrolledIn;
	}
}