package middleware.studentMigration.equivalences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import middleware.studentMigration.enrollments.CreateAndUpdateAllStudentsPastEnrolments;
import Dominio.DegreeCurricularPlan;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.EnrolmentInExtraCurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 * @author David Santos
 * 28/Out/2003
 */

public class MakeEquivalencesForAllStudentsPastEnrolments
{
	private static HashMap enrollmentsCreated = new HashMap();
	private static HashMap enrollmentEvaluationsCreated = new HashMap();
	
	private static int totalEnrollmentsCreated = 0;
	private static int totalEnrollmentEvaluationsCreated = 0;

	public static void main(String args[])
	{
		IStudent student = null;

		try {
			ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();
//			IPersistentStudent persistentStudent = fenixPersistentSuport.getIPersistentStudent();
//
//			fenixPersistentSuport.iniciarTransaccao();
//	
//			Integer numberOfStudents = persistentStudent.countAll();
//			
//			fenixPersistentSuport.confirmarTransaccao();
//	
//			System.out.println("[INFO] Total number of student curriculums to update [" + numberOfStudents + "].");
//
//			int numberOfElementsInSpan = 100;
//			int numberOfSpans = numberOfStudents.intValue() / numberOfElementsInSpan;
//			numberOfSpans =  numberOfStudents.intValue() % numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
//			
//			for (int span = 0; span < numberOfSpans; span++) {

				fenixPersistentSuport.iniciarTransaccao();

				System.gc();

				System.out.println("[INFO] Reading Students...");
//				List result = persistentStudent.readAllBySpan(new Integer(span), new Integer(numberOfElementsInSpan));
				List result = MakeEquivalencesForAllStudentsPastEnrolments.getStudents(new Integer("48"), fenixPersistentSuport);

				fenixPersistentSuport.confirmarTransaccao();
		
				System.out.println("[INFO] Updating [" + result.size() + "] student curriculums...");
				
//				MakeEquivalencesForAllStudentsPastEnrolments.printIndexes(span, numberOfElementsInSpan);
				
				Iterator iterator = result.iterator();
				while (iterator.hasNext()) {
					student = (IStudent) iterator.next();

					if (student.getDegreeType().equals(TipoCurso.LICENCIATURA_OBJ))
					{
						fenixPersistentSuport.iniciarTransaccao();
						
						MakeEquivalencesForAllStudentsPastEnrolments.makeEquivalences(student, fenixPersistentSuport);
						
						fenixPersistentSuport.confirmarTransaccao();
					}

					MakeEquivalencesForAllStudentsPastEnrolments.enrollmentsCreated.clear();
					MakeEquivalencesForAllStudentsPastEnrolments.enrollmentEvaluationsCreated.clear();
				}
//			}
		} catch (Throwable e) {
			System.out.println("[ERROR 301] Exception giving equivalences for student [" + student.getNumber() + "] enrolments!");
			e.printStackTrace(System.out);
		}

		System.out.println("[INFO] DONE!");
		System.out.println("[INFO] Total Enrolments created: [" + MakeEquivalencesForAllStudentsPastEnrolments.totalEnrollmentsCreated + "].");
		System.out.println("[INFO] Total EnrolmentEvaluations created: [" + MakeEquivalencesForAllStudentsPastEnrolments.totalEnrollmentEvaluationsCreated + "].");
	}

	/**
	 * @param student
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void makeEquivalences(IStudent student, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IStudentCurricularPlan pastStudentCurricularPlan =
			MakeEquivalencesForAllStudentsPastEnrolments.getStudentCurricularPlan(
				student,
				StudentCurricularPlanState.PAST_OBJ,
				fenixPersistentSuport);
		if (pastStudentCurricularPlan == null)
		{
			System.out.println(
				"[ERROR 302] Could not obtain past StudentCurricularPlan for Student with number: [" + student.getNumber() + "]!");
			return;
		}

		IStudentCurricularPlan currentStudentCurricularPlan =
			MakeEquivalencesForAllStudentsPastEnrolments.getStudentCurricularPlan(
				student,
				StudentCurricularPlanState.ACTIVE_OBJ,
				fenixPersistentSuport);
		if (currentStudentCurricularPlan == null)
		{
			System.out.println(
				"[ERROR 303] Could not obtain current StudentCurricularPlan for Student with number: ["
					+ student.getNumber()
					+ "]!");
			return;
		}

		MakeEquivalencesForAllStudentsPastEnrolments.writeAndUpdateEnrolments(
			student,
			pastStudentCurricularPlan,
			currentStudentCurricularPlan,
			fenixPersistentSuport);
	}

	/**
	 * @param student
	 * @param studentCurricularPlanState
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static IStudentCurricularPlan getStudentCurricularPlan(
		IStudent student,
		StudentCurricularPlanState studentCurricularPlanState,
		ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
			fenixPersistentSuport.getIStudentCurricularPlanPersistente();

		List result = persistentStudentCurricularPlan.readAllByStudentAntState(student, studentCurricularPlanState);
		if ((result != null) && (!result.isEmpty()))
		{
			return (IStudentCurricularPlan) result.get(0);
		} else
		{
			return null;
		}
	}

	/**
	 * @param student
	 * @param pastStudentCurricularPlan
	 * @param currentStudentCurricularPlan
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void writeAndUpdateEnrolments(
		IStudent student,
		IStudentCurricularPlan pastStudentCurricularPlan,
		IStudentCurricularPlan currentStudentCurricularPlan,
		ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		List realPastEnrolments = pastStudentCurricularPlan.getEnrolments();
		List pastEnrolments = MakeEquivalencesForILEECStudents.keepOnlyImprovments(realPastEnrolments, fenixPersistentSuport);
		Iterator iterator = pastEnrolments.iterator();
		while (iterator.hasNext()) {

			IEnrolment enrolment = (IEnrolment) iterator.next();

//			if (enrolment.getEnrolmentState().equals(EnrolmentState.APROVED))
//			{
				ICurricularCourse curricularCourse = enrolment.getCurricularCourse();
				IDegreeCurricularPlan currentDegreeCurricularPlan = currentStudentCurricularPlan.getDegreeCurricularPlan();

				ICurricularCourse curricularCourseFromCurrentDegreeCurricularPlan =
					MakeEquivalencesForAllStudentsPastEnrolments.getCurricularCourseFromCurrentDegreeCurricularPlan(
						curricularCourse,
						currentDegreeCurricularPlan,
						fenixPersistentSuport);
				
				if (curricularCourseFromCurrentDegreeCurricularPlan != null)
				{
					MakeEquivalencesForAllStudentsPastEnrolments.writeEnrollment(
						enrolment,
						curricularCourseFromCurrentDegreeCurricularPlan,
						currentStudentCurricularPlan,
						fenixPersistentSuport,
						false);
				} else
				{
					MakeEquivalencesForAllStudentsPastEnrolments.writeEnrollment(
						enrolment,
						curricularCourse,
						currentStudentCurricularPlan,
						fenixPersistentSuport,
						true);
				}
//			}
		}
	}

	/**
	 * @param curricularCourse
	 * @param currentDegreeCurricularPlan
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	protected static ICurricularCourse getCurricularCourseFromCurrentDegreeCurricularPlan(ICurricularCourse curricularCourse, IDegreeCurricularPlan currentDegreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentCurricularCourse persistentCurricularCourse = fenixPersistentSuport.getIPersistentCurricularCourse();

		List result = persistentCurricularCourse.readbyCourseCodeAndDegreeCurricularPlan(curricularCourse.getCode(), currentDegreeCurricularPlan);

		if ((result != null) && (!result.isEmpty()))
		{
			if (result.size() == 1)
			{
				return (ICurricularCourse) result.get(0);
			} else
			{
				System.out.println("[ERROR 305] Several Fenix CurricularCourses with code [" + curricularCourse.getCode() + "] were found for Degree [" + currentDegreeCurricularPlan.getDegree().getNome() + "]!");
				return null;
			}
		} else
		{
			result = persistentCurricularCourse.readbyCourseNameAndDegreeCurricularPlan(curricularCourse.getName(), currentDegreeCurricularPlan);

			if ((result != null) && (!result.isEmpty()))
			{
				if (result.size() == 1)
				{
					return (ICurricularCourse) result.get(0);
				} else
				{
					System.out.println("[ERROR 306] Several Fenix CurricularCourses with name [" + curricularCourse.getName() + "] were found for Degree [" + currentDegreeCurricularPlan.getDegree().getNome() + "]!");
					return null;
				}
			} else
			{
				return null;
			}
		}
	}

	/**
	 * @param enrolment
	 * @param curricularCourse
	 * @param currentStudentCurricularPlan
	 * @param fenixPersistentSuport
	 * @param extraCurricular
	 * @return
	 * @throws Throwable
	 */
	private static IEnrolment writeEnrollment(
		IEnrolment enrolment,
		ICurricularCourse curricularCourse,
		IStudentCurricularPlan currentStudentCurricularPlan,
		ISuportePersistente fenixPersistentSuport,
		boolean extraCurricular)
		throws Throwable
	{
		IPersistentEnrolment persistentEnrolment = fenixPersistentSuport.getIPersistentEnrolment();

		IExecutionPeriod executionPeriod = enrolment.getExecutionPeriod();

		IEnrolment enrolmentToWrite =
			persistentEnrolment.readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
				currentStudentCurricularPlan,
				curricularCourse,
				executionPeriod);

		if (enrolmentToWrite == null)
		{
			IEnrolment enrolmentToObtainKey = new Enrolment();
			enrolmentToObtainKey.setStudentCurricularPlan(currentStudentCurricularPlan);
			enrolmentToObtainKey.setCurricularCourse(curricularCourse);
			enrolmentToObtainKey.setExecutionPeriod(executionPeriod);
			String key = CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentKey(enrolmentToObtainKey);

			enrolmentToWrite = (IEnrolment) MakeEquivalencesForAllStudentsPastEnrolments.enrollmentsCreated.get(key);

			if (enrolmentToWrite == null)
			{
				if (extraCurricular)
				{
					enrolmentToWrite = new EnrolmentInExtraCurricularCourse();
				} else
				{
					enrolmentToWrite = new Enrolment();
				}

				fenixPersistentSuport.getIPersistentEnrolment().simpleLockWrite(enrolmentToWrite);

				enrolmentToWrite.setCurricularCourse(curricularCourse);
				enrolmentToWrite.setEnrolmentEvaluationType(enrolment.getEnrolmentEvaluationType());
				enrolmentToWrite.setEnrolmentState(enrolment.getEnrolmentState());
				enrolmentToWrite.setExecutionPeriod(executionPeriod);
				enrolmentToWrite.setStudentCurricularPlan(currentStudentCurricularPlan);
				enrolmentToWrite.setAckOptLock(new Integer(1));
				
				MakeEquivalencesForAllStudentsPastEnrolments.enrollmentsCreated.put(key, enrolmentToWrite);
				MakeEquivalencesForAllStudentsPastEnrolments.totalEnrollmentsCreated++;
			}
		}

		List enrolmentEvaluationsList = enrolment.getEvaluations();
		Iterator iterator = enrolmentEvaluationsList.iterator();
		while (iterator.hasNext())
		{
			IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();
			MakeEquivalencesForAllStudentsPastEnrolments.writeEnrollmentEvaluation(
				enrolmentEvaluation,
				enrolmentToWrite,
				fenixPersistentSuport);
		}

		return enrolmentToWrite;
	}

	/**
	 * @param enrolment
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
//	private static ICurricularCourseScope getCurricularCourseScope(IEnrolment enrolment, ICurricularCourse curricularCourse) throws Throwable
//	{
//		List scopes = curricularCourse.getScopes();
//
//		if (scopes != null)
//		{
//			if (scopes.size() == 1)
//			{
//				return (ICurricularCourseScope) scopes.get(0);
//			} else if (!scopes.isEmpty())
//			{
//				Iterator iterator = scopes.iterator();
//				while (iterator.hasNext())
//				{
//					ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
//					boolean isTheOne = MakeEquivalencesForAllStudentsPastEnrolments.compare(curricularCourseScope, enrolment.getCurricularCourseScope());
//					if (isTheOne)
//					{
//						return curricularCourseScope;
//					}
//				}
//				ComparatorChain comparatorChain = new ComparatorChain();
//				comparatorChain.addComparator(new BeanComparator("curricularSemester.idInternal"));
//				Collections.sort(scopes, comparatorChain);
//				return (ICurricularCourseScope) scopes.get(0);
//			} else
//			{
//				return null;
//			}
//		} else
//		{
//			return null;
//		}
//	}

	/**
	 * @param enrolmentEvaluation
	 * @param enrolment
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void writeEnrollmentEvaluation(IEnrolmentEvaluation enrolmentEvaluation, IEnrolment enrolment, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = fenixPersistentSuport.getIPersistentEnrolmentEvaluation();

		IEnrolmentEvaluation enrolmentEvaluationToWrite =
			persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(
				enrolment,
				enrolmentEvaluation.getEnrolmentEvaluationType(),
				enrolmentEvaluation.getGrade(),
				enrolmentEvaluation.getWhen());

		if (enrolmentEvaluationToWrite == null)
		{
			IEnrolmentEvaluation enrolmentEvaluationToObtainKey = new EnrolmentEvaluation();
			enrolmentEvaluationToObtainKey.setEnrolment(enrolment);
			enrolmentEvaluationToObtainKey.setGrade(enrolmentEvaluation.getGrade());
			enrolmentEvaluationToObtainKey.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			enrolmentEvaluationToObtainKey.setWhen(enrolmentEvaluation.getWhen());
			String key = CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentEvaluationKey(enrolmentEvaluationToObtainKey);

			enrolmentEvaluationToWrite = (IEnrolmentEvaluation) MakeEquivalencesForAllStudentsPastEnrolments.enrollmentEvaluationsCreated.get(key);

			if (enrolmentEvaluationToWrite == null)
			{
				enrolmentEvaluationToWrite = new EnrolmentEvaluation();

				persistentEnrolmentEvaluation.simpleLockWrite(enrolmentEvaluationToWrite);
		
				enrolmentEvaluationToWrite.setEnrolment(enrolment);
				enrolmentEvaluationToWrite.setEnrolmentEvaluationState(enrolmentEvaluation.getEnrolmentEvaluationState());
				enrolmentEvaluationToWrite.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
				enrolmentEvaluationToWrite.setExamDate(enrolmentEvaluation.getExamDate());
				enrolmentEvaluationToWrite.setGrade(enrolmentEvaluation.getGrade());
				enrolmentEvaluationToWrite.setObservation(enrolmentEvaluation.getObservation());
				enrolmentEvaluationToWrite.setPersonResponsibleForGrade(enrolmentEvaluation.getPersonResponsibleForGrade());
				enrolmentEvaluationToWrite.setGradeAvailableDate(enrolmentEvaluation.getGradeAvailableDate());
				enrolmentEvaluationToWrite.setWhen(enrolmentEvaluation.getWhen());
				enrolmentEvaluationToWrite.setEmployee(enrolmentEvaluation.getEmployee());
				enrolmentEvaluationToWrite.setAckOptLock(new Integer(1));

				enrolmentEvaluation.setCheckSum(null);

				MakeEquivalencesForAllStudentsPastEnrolments.enrollmentEvaluationsCreated.put(key, enrolmentEvaluationToWrite);
				MakeEquivalencesForAllStudentsPastEnrolments.totalEnrollmentEvaluationsCreated++;
			}
		}
	}

	/**
	 * @param curricularCourseScope
	 * @param curricularCourseScopeToCompare
	 * @param howToCompare
	 * @return
	 */
//	private static boolean compare(ICurricularCourseScope curricularCourseScope, ICurricularCourseScope curricularCourseScopeToCompare)
//	{
//		Integer year = curricularCourseScope.getCurricularSemester().getCurricularYear().getYear();
//		Integer yearToCompare = curricularCourseScopeToCompare.getCurricularSemester().getCurricularYear().getYear();
//		Integer semester = curricularCourseScope.getCurricularSemester().getSemester();
//		Integer semesterToCompare = curricularCourseScopeToCompare.getCurricularSemester().getSemester();
//		String branchCode = curricularCourseScope.getBranch().getCode();
//		String branchCodeToCompare = curricularCourseScopeToCompare.getBranch().getCode();
//		
//		if (year.equals(yearToCompare) && semester.equals(semesterToCompare) && branchCode.equals(branchCodeToCompare))
//		{
//			return true;
//		} else if (year.equals(yearToCompare) && semester.equals(semesterToCompare))
//		{
//			return true;
//		} else if (year.equals(yearToCompare))
//		{
//			return true;
//		} else if (semester.equals(semesterToCompare))
//		{
//			return true;
//		} else
//		{
//			return false;
//		}
//	}

	/**
	 * @param span
	 * @param numberOfElementsInSpan
	 */
	protected static void printIndexes(int span, int numberOfElementsInSpan)
	{
		int startIndex = 1;
		if (span != 0)
		{
			startIndex = span * numberOfElementsInSpan;
		}
		int endIndex = startIndex + numberOfElementsInSpan - 1;
		if (startIndex == 1)
		{
			endIndex = numberOfElementsInSpan - 1;
		}
		System.out.println("[INFO] Indexes from [" + startIndex + "] to [" + endIndex + "].");
	}

	/**
	 * @param degreeCurricularPlanID
	 * @param persistentSuport
	 * @return List of students
	 * @throws ExcepcaoPersistencia
	 */
	private static List getStudents(Integer degreeCurricularPlanID, ISuportePersistente persistentSuport) throws ExcepcaoPersistencia
	{
		IStudentCurricularPlanPersistente studentCurricularPlanDAO = persistentSuport.getIStudentCurricularPlanPersistente();
		IPersistentDegreeCurricularPlan degreeCurricularPlanDAO = persistentSuport.getIPersistentDegreeCurricularPlan();

		IDegreeCurricularPlan degreeCurricularPlan =
			(IDegreeCurricularPlan) degreeCurricularPlanDAO.readByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);

		List result1 =
			studentCurricularPlanDAO.readAllByDegreeCurricularPlanAndState(
				degreeCurricularPlan,
				StudentCurricularPlanState.ACTIVE_OBJ);
		
		List result2 = new ArrayList();
		Iterator iterator = result1.iterator();
		while (iterator.hasNext())
		{
			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();
			if (!result2.contains(studentCurricularPlan.getStudent()))
			{
				result2.add(studentCurricularPlan.getStudent());
			}
		}
		
		return result2;
	}
}