package ServidorAplicacao.Servico.equivalence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoEnrolment;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.Service;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCreditsInAnySecundaryArea;
import ServidorPersistente.IPersistentCreditsInSpecificScientificArea;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author David Santos 9/Jul/2003
 */

public abstract class EnrollmentEquivalenceServiceUtils extends Service
{
	/**
	 * @param enrollment
	 * @return true/false
	 */
	protected boolean isAnAprovedEnrollment(IEnrollment enrollment)
	{
		return enrollment.getEnrolmentState().equals(EnrolmentState.APROVED);
	}

	/**
	 * @param enrollment
	 * @return true/false
	 */
	protected boolean isAnEnroledEnrollment(IEnrollment enrollment)
	{
		return (enrollment.getEnrolmentState().equals(EnrolmentState.ENROLED) || enrollment.getEnrolmentState().equals(
			EnrolmentState.TEMPORARILY_ENROLED));
	}

	/**
	 * @param enrollment
	 * @param degreeCurricularPlan
	 * @param studentCurricularPlan
	 * @return true/false
	 */
	protected boolean isAnEnrollmentWithNoEquivalences(IEnrollment enrollment, IDegreeCurricularPlan degreeCurricularPlan,
		IStudentCurricularPlan studentCurricularPlan)
	{
		List result1 = null;
		List result2 = null;
		List result3 = null;
		List result4 = null;

		try
		{
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

			IPersistentEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrolmentForEnrolmentEquivalenceDAO = persistentSupport
				.getIPersistentEquivalentEnrolmentForEnrolmentEquivalence();
			
			IPersistentCurricularCourse persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();

			IPersistentCreditsInAnySecundaryArea creditsInAnySecundaryAreaDAO =
				persistentSupport.getIPersistentCreditsInAnySecundaryArea();
			
			IPersistentCreditsInSpecificScientificArea creditsInSpecificScientificAreaDAO =
				persistentSupport.getIPersistentCreditsInSpecificScientificArea();

			result1 = equivalentEnrolmentForEnrolmentEquivalenceDAO.readByEquivalentEnrolment(enrollment);

			result2 = persistentCurricularCourse.readbyCourseNameAndDegreeCurricularPlan(enrollment.getCurricularCourse().getName(), degreeCurricularPlan);

			result3 = creditsInSpecificScientificAreaDAO.readAllByStudentCurricularPlan(studentCurricularPlan);
			
			result4 = creditsInAnySecundaryAreaDAO.readAllByStudentCurricularPlan(studentCurricularPlan);

		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}

//		if ( (result1 == null) && (result2 == null) )
//		{
//			return true;
//		} else if ( (result1 != null) && (result2 != null) )
//		{
//			return (result1.isEmpty() && result2.isEmpty());
//		} else if (result1 != null)
//		{
//			return result1.isEmpty();
//		} else
//		{
//			return result2.isEmpty();
//		}

//		return (result1.isEmpty() && result2.isEmpty());

		return (result1.isEmpty() && result2.isEmpty() && result3.isEmpty() && result4.isEmpty());
	}

	/**
	 * @param studentNumber
	 * @param degreeType
	 * @return IStudentCurricularPlan
	 * @throws ExcepcaoPersistencia
	 */
	protected IStudentCurricularPlan getActiveStudentCurricularPlan(Integer studentNumber, TipoCurso degreeType)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();

		IStudentCurricularPlanPersistente studentCurricularPlanDAO = persistenceDAO.getIStudentCurricularPlanPersistente();
		
		IStudentCurricularPlan activeStudentCurricularPlan = studentCurricularPlanDAO.readActiveStudentCurricularPlan(
			studentNumber, degreeType);
		
		return activeStudentCurricularPlan;
	}

	/**
	 * @param curricularCourses
	 * @return List
	 */
	protected List cloneCurricularCoursesToInfoCurricularCourses(List curricularCourses)
	{
		List infoCurricularCourses = new ArrayList();
		for (int i = 0; i < curricularCourses.size(); i++)
		{
			ICurricularCourse curricularCourse = (ICurricularCourse) curricularCourses.get(i);
			InfoCurricularCourse infoCurricularCourse = Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
			infoCurricularCourses.add(infoCurricularCourse);
		}
		return infoCurricularCourses;
	}

	/**
	 * @param enrolments
	 * @return List
	 */
	protected List cloneEnrolmentsToInfoEnrolments(List enrollments)
	{
		List infoEnrolments = new ArrayList();
		for (int i = 0; i < enrollments.size(); i++)
		{
			IEnrollment enrollment = (IEnrollment) enrollments.get(i);
			InfoEnrolment infoEnrolment = Cloner.copyIEnrolment2InfoEnrolment(enrollment);
			infoEnrolments.add(infoEnrolment);
		}
		return infoEnrolments;
	}

	/**
	 * @param infoEnrollment
	 * @return String
	 */
	protected String getEnrollmentGrade(IEnrollment enrollment)
	{
		// This sorts the list ascendingly so we need to reverse it to get the first object.
		Collections.sort(enrollment.getEvaluations());
		Collections.reverse(enrollment.getEvaluations());
		return ((IEnrolmentEvaluation) enrollment.getEvaluations().get(0)).getGrade();
	}
}