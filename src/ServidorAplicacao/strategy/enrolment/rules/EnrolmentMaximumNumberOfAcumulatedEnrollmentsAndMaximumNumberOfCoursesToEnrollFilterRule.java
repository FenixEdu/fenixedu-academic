package ServidorAplicacao.strategy.enrolment.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;

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

		Iterator iterator = getStudentEnrollmentsWithEnrolledState(studentEnrolmentContext).iterator();
		while (iterator.hasNext())
		{
			IEnrolment enrolment = (IEnrolment) iterator.next();
			ICurricularCourse curricularCourse = enrolment.getCurricularCourse();
			if (studentEnrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourse).intValue() > 0)
			{
				totalNAC += getMaxIncrementNac(curricularCourse).intValue();
			} else
			{
				totalNAC += getMinIncrementNac(curricularCourse).intValue();
			}

			if (studentEnrolmentContext.getStudentCurrentSemesterEnrollments().contains(enrolment))
			{
				NC += getWeigth(curricularCourse).intValue();
			}
		}

		if (totalNAC >= maxTotalNAC || NC >= maxCourses)
		{
			studentEnrolmentContext.setFinalCurricularCoursesWhereStudentCanBeEnrolled(new ArrayList());
		}

		return studentEnrolmentContext;
	}

	/**
	 * @param studentEnrolmentContext
	 * @return
	 */
	private List getStudentEnrollmentsWithEnrolledState(StudentEnrolmentContext studentEnrolmentContext)
	{
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentEnrolment enrolmentDAO = persistentSuport.getIPersistentEnrolment();
			return enrolmentDAO.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
				studentEnrolmentContext.getStudentCurricularPlan(),
				EnrolmentState.ENROLED);
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
		}
		return new ArrayList();
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