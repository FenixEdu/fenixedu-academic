package ServidorAplicacao.strategy.enrolment.rules;

import java.util.ArrayList;
import java.util.Iterator;

import Dominio.ICurricularCourse;
import Dominio.IEnrollment;
import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;

/**
 * @author David Santos in Jan 27, 2004
 * @see This rule should be used when the intention is to remove curricular courses that cannot be
 * enrolled due to overlaping the maximum number of acumulated enrolments or/and the maximum number of curricular courses
 * that a student can be enrolled in one semester.
 * NOTE that this rule sould be the last filter to be executed on the list
 * of possible curricular courses that a student can be enrolled in that semester.
 */

public class EnrolmentMaximumNumberOfAcumulatedEnrollmentsAndMaximumNumberOfCoursesToEnrollFilterRule implements IEnrolmentRule
{
	public StudentEnrolmentContext apply(StudentEnrolmentContext studentEnrolmentContext)
	{
		// NAC stands for Number of Aumulated enrollments in one Curricular course.
		// NC stands for Number of Curricular courses the student can be enrolled in.
		int maxCourses =
			studentEnrolmentContext.getStudentCurricularPlan().getStudent().getStudentKind().getMaxCoursesToEnrol().intValue();
		int maxTotalNAC =
			studentEnrolmentContext.getStudentCurricularPlan().getStudent().getStudentKind().getMaxNACToEnrol().intValue();

		int totalNAC = 0;
		int NC = 0;

		Iterator iterator = studentEnrolmentContext.getStudentCurrentSemesterEnrollments().iterator();
		while (iterator.hasNext())
		{
			IEnrollment enrolment = (IEnrollment) iterator.next();
			ICurricularCourse curricularCourse = enrolment.getCurricularCourse();
			if (studentEnrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourse).intValue() > 1)
			{
				totalNAC += getMaxIncrementNac(curricularCourse).intValue();
			} else
			{
				totalNAC += getMinIncrementNac(curricularCourse).intValue();
			}

			NC = NC + getWeigth(curricularCourse).intValue();
		}

		if (totalNAC >= maxTotalNAC || NC >= maxCourses)
		{
			studentEnrolmentContext.setFinalCurricularCoursesWhereStudentCanBeEnrolled(new ArrayList());
		}

		return studentEnrolmentContext;
	}

	/**
	 * @param curricularCourse
	 * @return
	 */
	private Integer getMinIncrementNac(ICurricularCourse curricularCourse)
	{
		return curricularCourse.getMinimumValueForAcumulatedEnrollments();
	}

	/**
	 * @param curricularCourse
	 * @return
	 */
	private Integer getMaxIncrementNac(ICurricularCourse curricularCourse)
	{
		return curricularCourse.getMaximumValueForAcumulatedEnrollments();
	}

	/**
	 * @param curricularCourse
	 * @return
	 */
	private Integer getWeigth(ICurricularCourse curricularCourse)
	{
		return curricularCourse.getEnrollmentWeigth();
	}

}