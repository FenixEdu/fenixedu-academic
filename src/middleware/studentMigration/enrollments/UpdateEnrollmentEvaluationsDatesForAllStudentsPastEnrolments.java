package middleware.studentMigration.enrollments;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWEnrolment;
import middleware.middlewareDomain.MWStudent;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;

import org.apache.commons.lang.StringUtils;

import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationType;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 * @author David Santos
 * 28/Out/2003
 */

public class UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments
{
	private static int NUMBER_OF_ELEMENTS_IN_SPAN = 50;
	protected static int alteredDates = 0;

	public static void main(String args[])
	{
		MWStudent mwStudent = null;

		try {
			ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
			IPersistentMWAluno persistentMWAluno = mws.getIPersistentMWAluno();
			IPersistentMWEnrolment persistentMWEnrolment = mws.getIPersistentMWEnrolment();

			fenixPersistentSuport.iniciarTransaccao();
			
			Integer numberOfStudents = persistentMWAluno.countAll();
			
			fenixPersistentSuport.confirmarTransaccao();
			
			System.out.println("[INFO] Total number of student curriculums to update [" + numberOfStudents + "].");

			int numberOfElementsInSpan = UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.NUMBER_OF_ELEMENTS_IN_SPAN;
			int numberOfSpans = numberOfStudents.intValue() / numberOfElementsInSpan;
			numberOfSpans =  numberOfStudents.intValue() % numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
			
			for (int span = 0; span < numberOfSpans; span++) {
				fenixPersistentSuport.iniciarTransaccao();

				System.gc();
	
				System.out.println("[INFO] Reading MWStudents...");
				List result = persistentMWAluno.readAllBySpan(new Integer(span), new Integer(numberOfElementsInSpan));
	
				fenixPersistentSuport.confirmarTransaccao();
		
				System.out.println("[INFO] Updating [" + result.size() + "] student curriculums...");
		
//				MakeEquivalencesForAllStudentsPastEnrolments.printIndexes(span, numberOfElementsInSpan);
				
				Iterator iterator = result.iterator();
				while (iterator.hasNext()) {
					mwStudent = (MWStudent) iterator.next();

					fenixPersistentSuport.iniciarTransaccao();
					mwStudent.setEnrolments(persistentMWEnrolment.readByStudentNumber(mwStudent.getNumber()));
					UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.updateEnrollmentEvaluationsDatesForThisStudent(mwStudent, fenixPersistentSuport);
					fenixPersistentSuport.confirmarTransaccao();
				}
			}
		} catch (Throwable e) {
			System.out.println("[ERROR 501] Exception migrating student [" + mwStudent.getNumber() + "] enrolments!");
			System.out.println("[ERROR 501] Number: [" + mwStudent.getNumber() + "]");
			System.out.println("[ERROR 501] Degree: [" + mwStudent.getDegreecode() + "]");
			System.out.println("[ERROR 501] Branch: [" + mwStudent.getBranchcode() + "]");
			e.printStackTrace(System.out);
		}
		System.out.println("[INFO] DONE!");
		System.out.println("[INFO] Total EnrolmentEvaluations updated: [" + UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.alteredDates + "].");
	}

	/**
	 * @param mwStudent
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void updateEnrollmentEvaluationsDatesForThisStudent(MWStudent mwStudent, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentStudent persistentStudent = fenixPersistentSuport.getIPersistentStudent();

		IStudent student = persistentStudent.readByNumero(mwStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);
		if (student == null) {
			System.out.println("[ERROR 502] Can't find Student in Fenix DB with number: [" + mwStudent.getNumber() + "]!");
			return;
		}

		IStudentCurricularPlan studentCurricularPlan = UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.getStudentCurricularPlan(student, fenixPersistentSuport);
		if (studentCurricularPlan == null) {
			System.out.println("[ERROR 503] No PAST StudentCurricularPlans were found for Student with number: [" + mwStudent.getNumber() + "] from Degree with code: [" + mwStudent.getDegreecode() + "]!");
			return;
		}
		
		UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.updateEnrollmentEvaluationsDates(mwStudent, studentCurricularPlan, fenixPersistentSuport);
	}

	/**
	 * @param degreeCode
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(Integer degreeCode, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWDegreeTranslation persistentMWDegreeTranslation = mws.getIPersistentMWDegreeTranslation();
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = fenixPersistentSuport.getIPersistentDegreeCurricularPlan();

		MWDegreeTranslation mwDegreeTranslation = persistentMWDegreeTranslation.readByDegreeCode(degreeCode);

		if (mwDegreeTranslation != null) {
			String degreeCurricularPlanName = "PAST-" + mwDegreeTranslation.getDegree().getSigla();
			ICurso degree = mwDegreeTranslation.getDegree();
			IDegreeCurricularPlan degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree(degreeCurricularPlanName, degree);
			return degreeCurricularPlan;
		} else {
			return null;
		}
	}

	/**
	 * @param degreeCurricularPlan
	 * @param student
	 * @param mwStudent
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static IStudentCurricularPlan getStudentCurricularPlan(IStudent student, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = fenixPersistentSuport.getIStudentCurricularPlanPersistente();

		List result = persistentStudentCurricularPlan.readAllByStudentAntState(student, StudentCurricularPlanState.PAST_OBJ);

		if ((result == null) || (result.isEmpty())) {
			return null;
		} else {
			return (IStudentCurricularPlan) result.get(0);
		}
	}

	/**
	 * @param degreeCode
	 * @param branchCode
	 * @param degreeCurricularPlan
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
//	private static IBranch getBranch(Integer degreeCode, Integer branchCode, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
//	{
//		IBranch branch = null;
//		
//		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
//		IPersistentMWBranch persistentMWBranch = mws.getIPersistentMWBranch();
//		IPersistentBranch persistentBranch = fenixPersistentSuport.getIPersistentBranch();
//
//		MWBranch mwBranch = persistentMWBranch.readByDegreeCodeAndBranchCode(degreeCode, branchCode);
//
//		if (mwBranch != null) {
//			String realBranchCode = null;
//
//			if (mwBranch.getDescription().startsWith("CURSO DE ")) {
//				realBranchCode = new String("");
//			} else {
//				realBranchCode = new String(mwBranch.getDegreecode().toString() + mwBranch.getBranchcode().toString() + mwBranch.getOrientationcode().toString());
//			}
//
//			branch = persistentBranch.readByDegreeCurricularPlanAndCode(degreeCurricularPlan, realBranchCode);
//
//		} else {
//			branch = CreateAndUpdateAllPastCurriculums.solveBranchesProblemsForDegrees1And4And6And51And53And54And64(degreeCode, branchCode, degreeCurricularPlan, persistentBranch);
//		}
//
//		return branch;
//	}

	/**
	 * @param mwStudent
	 * @param studentCurricularPlan
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void updateEnrollmentEvaluationsDates(MWStudent mwStudent, IStudentCurricularPlan studentCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentEnrolment enrolmentDAO = fenixPersistentSuport.getIPersistentEnrolment();
		IPersistentEnrolmentEvaluation enrolmentEvaluationDAO = fenixPersistentSuport.getIPersistentEnrolmentEvaluation();

		List mwEnrolments = mwStudent.getEnrolments();
		Iterator iterator = mwEnrolments.iterator();
		while (iterator.hasNext()) {
			MWEnrolment mwEnrolment = (MWEnrolment) iterator.next();
			
			IDegreeCurricularPlan degreeCurricularPlan = UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.getDegreeCurricularPlan(mwEnrolment.getDegreecode(), fenixPersistentSuport);
			if (degreeCurricularPlan == null) {
				System.out.println("[ERROR 504] No record of Degree with code: [" + mwEnrolment.getDegreecode() + "]!");
				return;
			}

			ICurricularCourse curricularCourse = UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.getCurricularCourse(mwEnrolment, degreeCurricularPlan, fenixPersistentSuport);
			if (curricularCourse == null) {
				System.out.println("[ERROR 505] Couldn't find Fenix CurricularCourse with code [" + mwEnrolment.getCoursecode() + " for Degree [" + degreeCurricularPlan.getDegree().getNome() + "]!");
				continue;
			}

//			ICurricularCourseScope curricularCourseScope = UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.getCurricularCourseScope(mwEnrolment, curricularCourse, /*studentCurricularPlan, */degreeCurricularPlan, fenixPersistentSuport);
//			if (curricularCourseScope == null) {
//				System.out.print("[ERROR 506] Couldn't find Fenix CurricularCourseScope for data: ");
//				System.out.print("Student Number - [" + mwEnrolment.getNumber() + "], ");
//				System.out.print("Course Code - [" + mwEnrolment.getCoursecode() + "], ");
//				System.out.print("Degree Code - [" + mwEnrolment.getDegreecode() + "], ");
//				System.out.print("Branch Code - [" + mwEnrolment.getBranchcode() + "], ");
//				System.out.print("Curricular Year - [" + mwEnrolment.getCurricularcourseyear() + "], ");
//				System.out.print("Curricular Semester - [" + mwEnrolment.getCurricularcoursesemester() + "], ");
//				System.out.println("Enrolment Year: [" + mwEnrolment.getEnrolmentyear() + "]!");
//				continue;
//			}


			IExecutionPeriod executionPeriod = UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.getExecutionPeriodForThisMWEnrolment(mwEnrolment, fenixPersistentSuport);

			EnrolmentEvaluationType enrolmentEvaluationType = UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.getEvaluationType(mwEnrolment);

//			IEnrolment enrolment = enrolmentDAO.readByStudentCurricularPlanAndCurricularCourseScopeAndExecutionPeriod(studentCurricularPlan, curricularCourseScope, executionPeriod);
			IEnrolment enrolment = enrolmentDAO.readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(studentCurricularPlan, curricularCourse, executionPeriod);
			if (enrolment == null) {
				System.out.print("[ERROR 507] Couldn't find Fenix Enrolment for data: ");
				System.out.print("Student Number - [" + mwEnrolment.getNumber() + "], ");
				System.out.print("Course Code - [" + mwEnrolment.getCoursecode() + "], ");
				System.out.print("Degree Code - [" + mwEnrolment.getDegreecode() + "], ");
				System.out.print("Branch Code - [" + mwEnrolment.getBranchcode() + "], ");
				System.out.print("Curricular Year - [" + mwEnrolment.getCurricularcourseyear() + "], ");
				System.out.print("Curricular Semester - [" + mwEnrolment.getCurricularcoursesemester() + "], ");
				System.out.println("Enrolment Year: [" + mwEnrolment.getEnrolmentyear() + "]!");
				continue;
			}

			String grade = UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.getAcurateGrade(mwEnrolment);
			IEnrolmentEvaluation enrolmentEvaluation = enrolmentEvaluationDAO.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(enrolment, enrolmentEvaluationType, grade);

			if (enrolmentEvaluation == null) {
				System.out.print("[ERROR 508] Couldn't find Fenix EnrolmentEvaluation for data: ");
				System.out.print("Student Number - [" + mwEnrolment.getNumber() + "], ");
				System.out.print("Course Code - [" + mwEnrolment.getCoursecode() + "], ");
				System.out.print("Degree Code - [" + mwEnrolment.getDegreecode() + "], ");
				System.out.print("Branch Code - [" + mwEnrolment.getBranchcode() + "], ");
				System.out.print("Curricular Year - [" + mwEnrolment.getCurricularcourseyear() + "], ");
				System.out.print("Curricular Semester - [" + mwEnrolment.getCurricularcoursesemester() + "], ");
				System.out.print("Enrolment Year: [" + mwEnrolment.getEnrolmentyear() + "], ");
				System.out.println("Grade: [" + grade + "]!");
				continue;
			}

			Date whenAltered = null;

			if (mwEnrolment.getExamdate() == null) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(mwEnrolment.getEnrolmentyear().intValue(), 9, 1);
				whenAltered = new Date(calendar.getTimeInMillis());
			} else {
				whenAltered = enrolmentEvaluation.getWhen();
			}
			
			long dateInLongFormat = whenAltered.getTime();
			dateInLongFormat = dateInLongFormat + (mwEnrolment.getIdinternal().longValue() * 1000);
			Date newDate = new Date(dateInLongFormat);
			enrolmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);
			enrolmentEvaluation.setWhen(newDate);

			UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.alteredDates++;
//			System.out.println("[INFO] Date updated from: [" + whenAltered.toString() + "] to [" + newDate.toString() + "].");
//			System.out.println("[INFO] Number of EnrolmentEvaluations dates yet updated: [" + UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.alteredDates + "]");
		}
	}

	/**
	 * @param mwEnrolment
	 * @param degreeCurricularPlan
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static ICurricularCourse getCurricularCourse(MWEnrolment mwEnrolment, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentCurricularCourse persistentCurricularCourse = fenixPersistentSuport.getIPersistentCurricularCourse();
		String courseCode = mwEnrolment.getCoursecode();

		List curricularCourses = persistentCurricularCourse.readbyCourseCodeAndDegreeCurricularPlan(StringUtils.trim(courseCode), degreeCurricularPlan);

		if ((curricularCourses == null) || (curricularCourses.isEmpty())) {
			return null;
		} else if (curricularCourses.size() > 1) {
			System.out.println("[ERROR 509] Several Fenix CurricularCourses with code [" + StringUtils.trim(courseCode) + "] were found for Degree [" + degreeCurricularPlan.getDegree().getNome() + "]!");
			return null;
		} else {
			return (ICurricularCourse) curricularCourses.get(0);
		}
	}

	/**
	 * @param mwEnrolment
	 * @param curricularCourse
	 * @param studentCurricularPlan
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
//	private static ICurricularCourseScope getCurricularCourseScope(MWEnrolment mwEnrolment, ICurricularCourse curricularCourse, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
//	{
//		IPersistentCurricularCourseScope persistentCurricularCourseScope = fenixPersistentSuport.getIPersistentCurricularCourseScope();
//		IPersistentCurricularSemester persistentCurricularSemester = fenixPersistentSuport.getIPersistentCurricularSemester();
//		IPersistentCurricularYear persistentCurricularYear = fenixPersistentSuport.getIPersistentCurricularYear();
//		
//		IBranch branch = UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.getBranch(mwEnrolment.getDegreecode(), mwEnrolment.getBranchcode(), degreeCurricularPlan, fenixPersistentSuport);
//		if (branch == null) {
//			branch = UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.getBranch(mwEnrolment.getDegreecode(), new Integer(0), degreeCurricularPlan, fenixPersistentSuport);
//			if (branch == null) {
//				System.out.println("[ERROR 510] No record of Branch with code: [" + mwEnrolment.getBranchcode() + "] for Degree with code: [" + mwEnrolment.getDegreecode() + "]!");
//				return null;
//			}
//		}
//
//		ICurricularYear curricularYear = persistentCurricularYear.readCurricularYearByYear(mwEnrolment.getCurricularcourseyear());
//		if (curricularYear == null) {
//			System.out.println("[ERROR 511] Can't find in Fenix DB CurricularYear with year [" + mwEnrolment.getCurricularcourseyear() + "]!");
//			return null;
//		}
//
//		ICurricularSemester curricularSemester = persistentCurricularSemester.readCurricularSemesterBySemesterAndCurricularYear(mwEnrolment.getCurricularcoursesemester(), curricularYear);
//		if (curricularSemester == null) {
//			System.out.println("[ERROR 512] Can't find in Fenix DB CurricularSemester with semester [" + mwEnrolment.getCurricularcoursesemester() + "] and year [" + curricularYear.getYear() + "]!");
//			return null;
//		}
//
//		ICurricularCourseScope curricularCourseScope = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(curricularCourse, curricularSemester, branch);
//
//		return curricularCourseScope;
//	}

	/**
	 * @param mwEnrolment
	 * @return
	 */
	private static EnrolmentEvaluationType getEvaluationType(MWEnrolment mwEnrolment) {

		EnrolmentEvaluationType enrolmentEvaluationType = null;

		int season = mwEnrolment.getSeason().intValue();

		switch (season) {
			case 0 :
				enrolmentEvaluationType = EnrolmentEvaluationType.NO_SEASON_OBJ;
				break;
			case 1 :
				enrolmentEvaluationType = EnrolmentEvaluationType.FIRST_SEASON_OBJ;
				break;
			case 2 :
				enrolmentEvaluationType = EnrolmentEvaluationType.SECOND_SEASON_OBJ;
				break;
			case 3 :
				enrolmentEvaluationType = EnrolmentEvaluationType.SPECIAL_SEASON_OBJ;
				break;
			case 4 :
				enrolmentEvaluationType = EnrolmentEvaluationType.IMPROVEMENT_OBJ;
				break;
			case 5 :
				enrolmentEvaluationType = EnrolmentEvaluationType.EXTERNAL_OBJ;
				break;
			default :
				System.out.println("[ERROR 513] No record of EnrolmentEvaluationType with code: [" + mwEnrolment.getSeason() + "]!");
				break;
		}

		return enrolmentEvaluationType;
	}

	/**
	 * @param mwEnrolment
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	protected static IExecutionPeriod getExecutionPeriodForThisMWEnrolment(MWEnrolment mwEnrolment, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentExecutionPeriod persistentExecutionPeriod = fenixPersistentSuport.getIPersistentExecutionPeriod();
		IPersistentExecutionYear persistentExecutionYear = fenixPersistentSuport.getIPersistentExecutionYear();
	
		String executionYearName = mwEnrolment.getEnrolmentyear().intValue() + "/" + (mwEnrolment.getEnrolmentyear().intValue() + 1);
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName(executionYearName);
		String executionPeriodName = mwEnrolment.getCurricularcoursesemester() + " Semestre";
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(executionPeriodName, executionYear);
		
		return executionPeriod;
	}

	/**
	 * @param mwEnrolment
	 * @return
	 */
	private static String getAcurateGrade(MWEnrolment mwEnrolment)
	{
		String grade = mwEnrolment.getGrade();

		if (grade == null) {
			return "NA";
		}

		if (grade.equals("")) {
			return "NA";
		}

		if (grade.equals("RE")) {
			return grade;
		}

		if (grade.equals("AP")) {
			return grade;
		}

		int intGrade;

		try {
			intGrade = new Integer(grade).intValue();
		} catch (NumberFormatException e) {
			System.out.println("[ERROR 514] Grade from MWEnrolment is not a number: [" + mwEnrolment.getGrade() + "]!");
			return "0";
		}

		if ((intGrade > 20) || (intGrade < 0)) {
			System.out.println("[ERROR 515] Grade from MWEnrolment is not valid: [" + mwEnrolment.getGrade() + "]!");
			return "0";
		} else
		{
			return (new Integer(intGrade)).toString();
		}
	}
}