package middleware.studentMigration.enrollments;

import java.util.Iterator;
import java.util.List;

import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.StudentCurricularPlanState;

/**
 * @author David Santos
 * 28/Out/2003
 */

public class MakeEquivalencesForAllStudentsPastEnrolments
{
	private static int totalEnrollmentsCreated = 0;
	private static int totalEnrollmentEvaluationsCreated = 0;
    
	public static void main(String args[])
	{
		IStudent student = null;

		try {
			ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentStudent persistentStudent = fenixPersistentSuport.getIPersistentStudent();

			fenixPersistentSuport.iniciarTransaccao();
	
			Integer numberOfStudents = persistentStudent.countAll();
	
			fenixPersistentSuport.confirmarTransaccao();
	
			System.out.println("[INFO] Total number of student curriculums to update [" + numberOfStudents + "].");

			int numberOfElementsInSpan = 100;
			int numberOfSpans = numberOfStudents.intValue() / numberOfElementsInSpan;
			numberOfSpans =  numberOfStudents.intValue() % numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
			
			for (int span = 0; span < numberOfSpans; span++) {

				fenixPersistentSuport.iniciarTransaccao();

//				fenixPersistentSuport.clearCache();
				System.gc();

				System.out.println("[INFO] Reading Students...");
				List result = persistentStudent.readAllBySpan(new Integer(span), new Integer(numberOfElementsInSpan));
	
				fenixPersistentSuport.confirmarTransaccao();
		
				System.out.println("[INFO] Updating [" + result.size() + "] student curriculums...");
		
				Iterator iterator = result.iterator();
				while (iterator.hasNext()) {
					student = (IStudent) iterator.next();
	
					fenixPersistentSuport.iniciarTransaccao();
	
					MakeEquivalencesForAllStudentsPastEnrolments.makeEquivalences(student, fenixPersistentSuport);
	
					fenixPersistentSuport.confirmarTransaccao();
				}
			}
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
		IStudentCurricularPlan pastStudentCurricularPlan = MakeEquivalencesForAllStudentsPastEnrolments.getStudentCurricularPlan(student, StudentCurricularPlanState.PAST_OBJ, fenixPersistentSuport);
		if (pastStudentCurricularPlan == null) {
			System.out.println("[ERROR 302] Could not obtain past StudentCurricularPlan for Student with number: [" + student.getNumber() + "]!");
			return;
		}
		
		IStudentCurricularPlan currentStudentCurricularPlan = MakeEquivalencesForAllStudentsPastEnrolments.getStudentCurricularPlan(student, StudentCurricularPlanState.ACTIVE_OBJ, fenixPersistentSuport);
		if (pastStudentCurricularPlan == null) {
			System.out.println("[ERROR 303] Could not obtain current StudentCurricularPlan for Student with number: [" + student.getNumber() + "]!");
			return;
		}
		
		MakeEquivalencesForAllStudentsPastEnrolments.writeAndUpdateEnrolments(student, pastStudentCurricularPlan, currentStudentCurricularPlan, fenixPersistentSuport);
	}

	/**
	 * @param student
	 * @param studentCurricularPlanState
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static IStudentCurricularPlan getStudentCurricularPlan(IStudent student, StudentCurricularPlanState studentCurricularPlanState,ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = fenixPersistentSuport.getIStudentCurricularPlanPersistente();

		List result = persistentStudentCurricularPlan.readAllByStudentAntState(student, studentCurricularPlanState);
		if ((result != null) && (!result.isEmpty())) {
			return (IStudentCurricularPlan) result.get(0);
		} else {
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
	private static void writeAndUpdateEnrolments(IStudent student, IStudentCurricularPlan pastStudentCurricularPlan, IStudentCurricularPlan currentStudentCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		List pastEnrolments = pastStudentCurricularPlan.getEnrolments();
		Iterator iterator = pastEnrolments.iterator();
		while (iterator.hasNext()) {
			IEnrolment enrolment = (IEnrolment) iterator.next();

			ICurricularCourse curricularCourse = enrolment.getCurricularCourseScope().getCurricularCourse();
			IDegreeCurricularPlan currentDegreeCurricularPlan = currentStudentCurricularPlan.getDegreeCurricularPlan();

			ICurricularCourse curricularCourseFromCurrentDegreeCurricularPlan = MakeEquivalencesForAllStudentsPastEnrolments.getCurricularCourseFromCurrentDegreeCurricularPlan(curricularCourse, currentDegreeCurricularPlan, fenixPersistentSuport);
			
			if (curricularCourseFromCurrentDegreeCurricularPlan == null)
			{
				continue;
			}

			MakeEquivalencesForAllStudentsPastEnrolments.writeEnrollment(enrolment, curricularCourseFromCurrentDegreeCurricularPlan, currentStudentCurricularPlan, fenixPersistentSuport);
		}
	}

	/**
	 * @param curricularCourse
	 * @param currentDegreeCurricularPlan
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static ICurricularCourse getCurricularCourseFromCurrentDegreeCurricularPlan(ICurricularCourse curricularCourse, IDegreeCurricularPlan currentDegreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
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
				System.out.println("[ERROR 304] Several Fenix CurricularCourses with code [" + curricularCourse.getCode() + "] were found for Degree [" + currentDegreeCurricularPlan.getDegree().getNome() + "]!");
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
					System.out.println("[ERROR 305] Several Fenix CurricularCourses with name [" + curricularCourse.getName() + "] were found for Degree [" + currentDegreeCurricularPlan.getDegree().getNome() + "]!");
					return null;
				}
			} else
			{
				System.out.println("[ERROR 306] Cannot find Fenix CurricularCourse with code [" + curricularCourse.getCode() + "] and name [" + curricularCourse.getName() + "] for Degree [" + currentDegreeCurricularPlan.getDegree().getNome() + "]!");
				return null;
			}
		}
	}

	/**
	 * @param enrolment
	 * @param currentStudentCurricularPlan
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void writeEnrollment(IEnrolment enrolment, ICurricularCourse curricularCourse, IStudentCurricularPlan currentStudentCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		ICurricularCourseScope curricularCourseScope = MakeEquivalencesForAllStudentsPastEnrolments.getCurricularCourseScope(enrolment, curricularCourse);

		if (curricularCourseScope != null)
		{	
			IEnrolment enrolmentToWrite = new Enrolment();

			fenixPersistentSuport.getIPersistentEnrolment().simpleLockWrite(enrolmentToWrite);

			enrolmentToWrite.setCurricularCourseScope(curricularCourseScope);
			enrolmentToWrite.setEnrolmentEvaluationType(enrolment.getEnrolmentEvaluationType());
			enrolmentToWrite.setEnrolmentState(enrolment.getEnrolmentState());
			enrolmentToWrite.setExecutionPeriod(enrolment.getExecutionPeriod());
			enrolmentToWrite.setStudentCurricularPlan(currentStudentCurricularPlan);

			MakeEquivalencesForAllStudentsPastEnrolments.totalEnrollmentsCreated++;
			
			List enrolmentEvaluationsList = enrolment.getEvaluations();
			Iterator iterator = enrolmentEvaluationsList.iterator();
			while (iterator.hasNext())
			{
				IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();
				MakeEquivalencesForAllStudentsPastEnrolments.writeEnrollmentEvaluation(enrolmentEvaluation, enrolmentToWrite, fenixPersistentSuport);
			}
		} else
		{
			System.out.println("[ERROR 307] Cannot find Fenix CurricularCourseScope for CurricularCourse with code [" + curricularCourse.getCode() + "] and name [" + curricularCourse.getName() + "] in period [year: " + enrolment.getCurricularCourseScope().getCurricularSemester().getCurricularYear().getYear().toString() + " semester: " + enrolment.getCurricularCourseScope().getCurricularSemester().getSemester().toString() + "]!");
		}
	}

	/**
	 * @param enrolment
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static ICurricularCourseScope getCurricularCourseScope(IEnrolment enrolment, ICurricularCourse curricularCourse) throws Throwable
	{
		List scopes = curricularCourse.getScopes();

		if (scopes != null)
		{
			if (scopes.size() == 1)
			{
				return (ICurricularCourseScope) scopes.get(0);
			} else
			{
				Iterator iterator = scopes.iterator();
				while (iterator.hasNext())
				{
					ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
					boolean isTheOne = MakeEquivalencesForAllStudentsPastEnrolments.compare(curricularCourseScope, enrolment.getCurricularCourseScope());
					if (isTheOne)
					{
						return curricularCourseScope;
					}
				}
				return null;
			}
		} else
		{
			return null;
		}
	}

	/**
	 * @param enrolmentEvaluation
	 * @param enrolment
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void writeEnrollmentEvaluation(IEnrolmentEvaluation enrolmentEvaluation, IEnrolment enrolment, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IEnrolmentEvaluation enrolmentEvaluationToWrite = new EnrolmentEvaluation();

		fenixPersistentSuport.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluationToWrite);

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

		enrolmentEvaluation.setCheckSum(null);
		
		MakeEquivalencesForAllStudentsPastEnrolments.totalEnrollmentEvaluationsCreated++;
	}

	/**
	 * @param curricularCourseScope
	 * @param curricularCourseScopeToCompare
	 * @param howToCompare
	 * @return
	 */
	private static boolean compare(ICurricularCourseScope curricularCourseScope, ICurricularCourseScope curricularCourseScopeToCompare)
	{
		Integer year = curricularCourseScope.getCurricularSemester().getCurricularYear().getYear();
		Integer yearToCompare = curricularCourseScopeToCompare.getCurricularSemester().getCurricularYear().getYear();
		Integer semester = curricularCourseScope.getCurricularSemester().getSemester();
		Integer semesterToCompare = curricularCourseScopeToCompare.getCurricularSemester().getSemester();
		String branchCode = curricularCourseScope.getBranch().getCode();
		String branchCodeToCompare = curricularCourseScopeToCompare.getBranch().getCode();
		
		if (year.equals(yearToCompare) && semester.equals(semesterToCompare) && branchCode.equals(branchCodeToCompare))
		{
			return true;
		} else if (year.equals(yearToCompare) && semester.equals(semesterToCompare))
		{
			return true;
		} else if (year.equals(yearToCompare))
		{
			return true;
		} else if (semester.equals(semesterToCompare))
		{
			return true;
		} else
		{
			return false;
		}
	}

}