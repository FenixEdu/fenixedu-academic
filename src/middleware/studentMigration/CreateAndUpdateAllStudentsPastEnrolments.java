package middleware.studentMigration;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWAluno;
import middleware.middlewareDomain.MWBranch;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWEnrolment;
import middleware.middlewareDomain.MWUniversity;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMWUniversity;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.IUniversity;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPersistentUniversity;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 * @author David Santos
 * 28/Out/2003
 */

public class CreateAndUpdateAllStudentsPastEnrolments
{
	private static HashMap studentCurricularPlansCreated = new HashMap();
	private static HashMap enrollmentsCreated = new HashMap();
	private static HashMap enrollmentEvaluationsCreated = new HashMap();

	public static void main(String args[])
	{
		MWAluno mwStudent = null;

		try {
			ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
			IPersistentMWAluno persistentMWAluno = mws.getIPersistentMWAluno();
			IPersistentMWEnrolment persistentMWEnrolment = mws.getIPersistentMWEnrolment();
	
			fenixPersistentSuport.iniciarTransaccao();
	
			Integer numberOfStudents = persistentMWAluno.countAll();
	
			fenixPersistentSuport.confirmarTransaccao();
	
			int numberOfElementsInSpan = 100;
			int numberOfSpans = numberOfStudents.intValue() / numberOfElementsInSpan;
			numberOfSpans =  numberOfStudents.intValue() % numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
			
			for (int span = 0; span < numberOfSpans; span++) {
				fenixPersistentSuport.iniciarTransaccao();
				fenixPersistentSuport.clearCache();	
	
				System.out.println("[INFO] Reading MWStudents...");
				List result = persistentMWAluno.readAllBySpan(new Integer(span), new Integer(numberOfElementsInSpan));
	
				fenixPersistentSuport.confirmarTransaccao();
		
				System.out.println("[INFO] Updating [" + result.size() + "] student curriculums...");
		
				Iterator iterator = result.iterator();
				while (iterator.hasNext()) {
					mwStudent = (MWAluno) iterator.next();
	
					fenixPersistentSuport.iniciarTransaccao();
	
					mwStudent.setEnrolments(persistentMWEnrolment.readByStudentNumber(mwStudent.getNumber()));
	
					CreateAndUpdateAllStudentsPastEnrolments.createAndUpdateAllStudentPastEnrolments(mwStudent, fenixPersistentSuport/*, currentExecutionPeriod*/);
	
					fenixPersistentSuport.confirmarTransaccao();
				}
			}
		} catch (Throwable e) {
			System.out.println("[ERROR 12] Exception migrating student [" + mwStudent.getNumber() + "] enrolments!");
			System.out.println("[ERROR 12] Number: [" + mwStudent.getNumber() + "]");
			System.out.println("[ERROR 12] Degree: [" + mwStudent.getDegreecode() + "]");
			System.out.println("[ERROR 12] Branch: [" + mwStudent.getBranchcode() + "]");
			e.printStackTrace(System.out);
		}

		ReportEnrolment.report(new PrintWriter(System.out, true));

		System.out.println("[INFO] DONE!");
		System.out.println("[INFO] Total StudentCurricularPlans created: [" + CreateAndUpdateAllStudentsPastEnrolments.studentCurricularPlansCreated.size() + "].");
		System.out.println("[INFO] Total Enrolments created: [" + CreateAndUpdateAllStudentsPastEnrolments.enrollmentsCreated.size() + "].");
		System.out.println("[INFO] Total EnrolmentEvaluations created: [" + CreateAndUpdateAllStudentsPastEnrolments.enrollmentEvaluationsCreated.size() + "].");
	}

	/**
	 * @param mwStudent
	 * @param fenixPersistentSuport
	 * @param currentExecutionPeriod
	 * @throws Exception
	 */
	private static void createAndUpdateAllStudentPastEnrolments(MWAluno mwStudent, ISuportePersistente fenixPersistentSuport/*, IExecutionPeriod currentExecutionPeriod*/) throws Throwable
	{
		IPersistentStudent persistentStudent = fenixPersistentSuport.getIPersistentStudent();

		// Read Fenix Student.
		IStudent student = persistentStudent.readByNumero(mwStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

		if (student == null) {
			// This can only happen if the Students/Persons migration was not runed before this one!
			System.out.println("[ERROR 01] Can't find Student in Fenix DB with number: [" + mwStudent.getNumber() + "]!");
			return;
		}

		IDegreeCurricularPlan degreeCurricularPlan = CreateAndUpdateAllStudentsPastEnrolments.getDegreeCurricularPlan(mwStudent, fenixPersistentSuport);
		
		if (degreeCurricularPlan == null) {
			// This can only happen if the CreateAndUpdateAllPastCurriculums migration was not runed before this one!
			System.out.println("[ERROR 02] No record of Degree with code: [" + mwStudent.getDegreecode() + "]!");
			return;
		}

		IStudentCurricularPlan studentCurricularPlan = CreateAndUpdateAllStudentsPastEnrolments.getStudentCurricularPlan(degreeCurricularPlan, student, mwStudent, fenixPersistentSuport);
		
		if (studentCurricularPlan == null) {
			System.out.println("[ERROR 04] No record of StudentCurricularPlan for Student with number: [" + mwStudent.getNumber() + "] in Degree with code: [" + mwStudent.getDegreecode() + "]!");
			return;
		}
		
		CreateAndUpdateAllStudentsPastEnrolments.writeAndUpdateEnrolments(mwStudent, degreeCurricularPlan, studentCurricularPlan, fenixPersistentSuport/*, currentExecutionPeriod*/);
	}

	/**
	 * @param mwStudent
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Exception
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(MWAluno mwStudent, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWDegreeTranslation persistentMWDegreeTranslation = mws.getIPersistentMWDegreeTranslation();
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = fenixPersistentSuport.getIPersistentDegreeCurricularPlan();

		Integer studentDegreeCode = ((MWEnrolment) mwStudent.getEnrolments().get(0)).getDegreecode();

		MWDegreeTranslation mwDegreeTranslation = persistentMWDegreeTranslation.readByDegreeCode(studentDegreeCode);

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
	 * @throws Exception
	 */
	private static IStudentCurricularPlan getStudentCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan, IStudent student, MWAluno mwStudent, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = fenixPersistentSuport.getIStudentCurricularPlanPersistente();

		List result = persistentStudentCurricularPlan.readAllByDegreeCurricularPlanAndState(degreeCurricularPlan, StudentCurricularPlanState.PAST_OBJ);
		if ((result == null) || (result.isEmpty())) {

			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) CreateAndUpdateAllStudentsPastEnrolments.studentCurricularPlansCreated.get(student.getNumber().toString());
			
			if (studentCurricularPlan == null) {
				studentCurricularPlan = new StudentCurricularPlan();

				persistentStudentCurricularPlan.simpleLockWrite(studentCurricularPlan);

				studentCurricularPlan.setDegreeCurricularPlan(degreeCurricularPlan);
				studentCurricularPlan.setCurrentState(StudentCurricularPlanState.PAST_OBJ);
				studentCurricularPlan.setStartDate(new Date());
				studentCurricularPlan.setStudent(student);
			
				IBranch branch = CreateAndUpdateAllStudentsPastEnrolments.getBranch(mwStudent.getDegreecode(), mwStudent.getBranchcode(), degreeCurricularPlan, fenixPersistentSuport);
				if (branch == null) {
					// This should NEVER happen!
					System.out.println("[ERROR 03.1] No record of Branch with code: [" + mwStudent.getBranchcode() + "] for Degree with code: [" + mwStudent.getDegreecode() + "]!");
					return null;
				}
			
				studentCurricularPlan.setBranch(branch);

				studentCurricularPlan.setClassification(null);
				studentCurricularPlan.setCompletedCourses(null);
				studentCurricularPlan.setEmployee(null);
				studentCurricularPlan.setEnrolledCourses(null);
				studentCurricularPlan.setEnrolments(null);
				studentCurricularPlan.setGivenCredits(null);
				studentCurricularPlan.setObservations(null);
				studentCurricularPlan.setSpecialization(null);
				studentCurricularPlan.setWhen(null);
				
				CreateAndUpdateAllStudentsPastEnrolments.studentCurricularPlansCreated.put(student.getNumber().toString(), studentCurricularPlan);
			}
			
			return studentCurricularPlan;
		} else if (!result.isEmpty()) {
			return (IStudentCurricularPlan) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param degreeCode
	 * @param branchCode
	 * @param degreeCurricularPlan
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Exception
	 */
	public static IBranch getBranch(Integer degreeCode, Integer branchCode, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IBranch branch = null;
		
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentMWBranch = mws.getIPersistentMWBranch();
		IPersistentBranch persistentBranch = fenixPersistentSuport.getIPersistentBranch();

		MWBranch mwBranch = persistentMWBranch.readByDegreeCodeAndBranchCode(degreeCode, branchCode);
		
		if (mwBranch != null) {
			branch = persistentBranch.readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, mwBranch.getDescription());

			if (branch == null) {
				branch = fenixPersistentSuport.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, "");
			}
		}

		return branch;
	}

	/**
	 * @param mwStudent
	 * @param degreeCurricularPlan
	 * @param studentCurricularPlan
	 * @param fenixPersistentSuport
	 * @throws Exception
	 */
	private static void writeAndUpdateEnrolments(MWAluno mwStudent, IDegreeCurricularPlan degreeCurricularPlan, IStudentCurricularPlan studentCurricularPlan, ISuportePersistente fenixPersistentSuport/*, IExecutionPeriod currentExecutionPeriod*/) throws Throwable
	{
		List mwEnrolments = mwStudent.getEnrolments();
		Iterator iterator = mwEnrolments.iterator();
		while (iterator.hasNext()) {
			final MWEnrolment mwEnrolment = (MWEnrolment) iterator.next();
			
			ICurricularCourse curricularCourse = CreateAndUpdateAllStudentsPastEnrolments.getCurricularCourse(mwEnrolment, degreeCurricularPlan, fenixPersistentSuport);
			
			if (curricularCourse == null) {
				continue;
			}

			ICurricularCourseScope curricularCourseScope = CreateAndUpdateAllStudentsPastEnrolments.getCurricularCourseScope(mwEnrolment, curricularCourse, studentCurricularPlan, degreeCurricularPlan, fenixPersistentSuport);

			if (curricularCourseScope == null) {
				ReportEnrolment.addCurricularCourseScopeNotFound(mwEnrolment.getCoursecode(),
					mwEnrolment.getDegreecode().toString(),
					mwEnrolment.getNumber().toString(),
					mwEnrolment.getCurricularcourseyear().toString(),
					mwEnrolment.getCurricularcoursesemester().toString(),
					mwEnrolment.getBranchcode().toString());
				continue;
			}

			IEnrolment enrolment = CreateAndUpdateAllStudentsPastEnrolments.createAndUpdateEnrolment(mwEnrolment, studentCurricularPlan, curricularCourseScope, /*currentExecutionPeriod, */fenixPersistentSuport);

			if (enrolment == null) {
				// This should NEVER happen!
				System.out.println("[ERROR 11] Couldn't create nor update enrolment!");
				continue;
			}
		}
	}

	/**
	 * @param mwEnrolment
	 * @param degreeCurricularPlan
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Exception
	 */
	private static ICurricularCourse getCurricularCourse(MWEnrolment mwEnrolment, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentCurricularCourse persistentCurricularCourse = fenixPersistentSuport.getIPersistentCurricularCourse();

		ICurricularCourse curricularCourse = UpdateStudentEnrolments.getCurricularCourse(mwEnrolment, degreeCurricularPlan, fenixPersistentSuport);

		if (curricularCourse != null) {
			IUniversity university = CreateAndUpdateAllStudentsPastEnrolments.getUniversity(mwEnrolment.getUniversitycode(), fenixPersistentSuport);
			if (university == null) {
				// This should NEVER happen!
				System.out.println("[ERROR 06] No record of University with code: [" + mwEnrolment.getUniversitycode() + "]!");
			} else {
				persistentCurricularCourse.simpleLockWrite(curricularCourse);
				curricularCourse.setUniversity(university);
			}
		}

		return curricularCourse;
	}

	/**
	 * @param mwEnrolment
	 * @param curricularCourse
	 * @param studentCurricularPlan
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Exception
	 */
	private static ICurricularCourseScope getCurricularCourseScope(MWEnrolment mwEnrolment, ICurricularCourse curricularCourse, IStudentCurricularPlan studentCurricularPlan, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IBranch branch = CreateAndUpdateAllStudentsPastEnrolments.getBranch(mwEnrolment.getDegreecode(), mwEnrolment.getBranchcode(), degreeCurricularPlan, fenixPersistentSuport);
		if (branch == null) {
			// This should NEVER happen!
			System.out.println("[ERROR 03.2] No record of Branch with code: [" + mwEnrolment.getBranchcode() + "] for Degree with code: [" + mwEnrolment.getDegreecode() + "]!");
			return null;
		}

		return UpdateStudentEnrolments.getCurricularCourseScopeToEnrollIn(studentCurricularPlan, mwEnrolment, curricularCourse, branch, fenixPersistentSuport);
	}

	/**
	 * @param universityCode
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Exception
	 */
	private static IUniversity getUniversity(String universityCode, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWUniversity persistentMWUniversity = mws.getIPersistentMWUniversity();
		IPersistentUniversity persistentUniversity = fenixPersistentSuport.getIPersistentUniversity();
		
		MWUniversity mwUniversity = persistentMWUniversity.readByCode(universityCode);
		
		if (mwUniversity == null) {
			return null;
		}

		IUniversity university = persistentUniversity.readByNameAndCode(mwUniversity.getUniversityName(), mwUniversity.getUniversityCode());
		
		return university;
	}

	/**
	 * @param mwEnrolment
	 * @param studentCurricularPlan
	 * @param curricularCourseScope
	 * @param currentExecutionPeriod
	 * @param fenixPersistentSuport
	 * @return
	 */
	private static IEnrolment createAndUpdateEnrolment(MWEnrolment mwEnrolment, IStudentCurricularPlan studentCurricularPlan, ICurricularCourseScope curricularCourseScope/*, IExecutionPeriod currentExecutionPeriod*/, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		EnrolmentEvaluationType enrolmentEvaluationType = CreateAndUpdateAllStudentsPastEnrolments.getEvaluationType(mwEnrolment);

		IPersistentEnrolment persistentEnrolment = fenixPersistentSuport.getIPersistentEnrolment();
		IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = fenixPersistentSuport.getIPersistentEnrolmentEvaluation();
		IPersistentExecutionPeriod persistentExecutionPeriod = fenixPersistentSuport.getIPersistentExecutionPeriod();
		IPersistentExecutionYear persistentExecutionYear = fenixPersistentSuport.getIPersistentExecutionYear();

		String executionYearName = mwEnrolment.getEnrolmentyear().intValue() + "/" + (mwEnrolment.getEnrolmentyear().intValue() + 1);
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName(executionYearName);
		String executionPeriodName = mwEnrolment.getCurricularcoursesemester() + " Semestre";
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(executionPeriodName, executionYear);

		IEnrolment enrolment = persistentEnrolment.readByStudentCurricularPlanAndCurricularCourseScopeAndExecutionPeriod(studentCurricularPlan, curricularCourseScope, executionPeriod);

		if (enrolment == null) {

			String key = executionYearName + executionPeriodName + curricularCourseScope.getCurricularCourse().getCode() + studentCurricularPlan.getStudent().getNumber();
			enrolment = (IEnrolment) CreateAndUpdateAllStudentsPastEnrolments.enrollmentsCreated.get(key);

			if (enrolment == null) {
				// Create the Enrolment.
				enrolment = new Enrolment();

				fenixPersistentSuport.getIPersistentEnrolment().simpleLockWrite(enrolment);

				enrolment.setCurricularCourseScope(curricularCourseScope);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluationType);
				enrolment.setEnrolmentState(CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentStateByGrade(mwEnrolment));
				enrolment.setExecutionPeriod(executionPeriod);
				enrolment.setStudentCurricularPlan(studentCurricularPlan);
				CreateAndUpdateAllStudentsPastEnrolments.enrollmentsCreated.put(key, enrolment);
			}
		}

		// Create the EnrolmentEvaluation.
		IEnrolmentEvaluation enrolmentEvaluation = persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade(enrolment, enrolmentEvaluationType, mwEnrolment.getGrade());

		if (enrolmentEvaluation == null) {

			String key = executionYearName + executionPeriodName + curricularCourseScope.getCurricularCourse().getCode() + studentCurricularPlan.getStudent().getNumber() + mwEnrolment.getGrade() + enrolmentEvaluationType.getType().toString();
			enrolmentEvaluation = (IEnrolmentEvaluation) CreateAndUpdateAllStudentsPastEnrolments.enrollmentEvaluationsCreated.get(key);

			if (enrolmentEvaluation == null) {
				enrolmentEvaluation = new EnrolmentEvaluation();

				fenixPersistentSuport.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluation);

				enrolmentEvaluation.setEnrolment(enrolment);
				enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
				enrolmentEvaluation.setEnrolmentEvaluationType(enrolmentEvaluationType);
				enrolmentEvaluation.setExamDate(mwEnrolment.getExamdate());
				enrolmentEvaluation.setGrade(mwEnrolment.getGrade());
				enrolmentEvaluation.setObservation(mwEnrolment.getRemarks());
				enrolmentEvaluation.setPersonResponsibleForGrade(CreateAndUpdateAllStudentsPastEnrolments.getPersonResponsibleForGrade(mwEnrolment, fenixPersistentSuport));

				enrolmentEvaluation.setEmployee(null);
				enrolmentEvaluation.setGradeAvailableDate(null);
				enrolmentEvaluation.setWhen(null);
				enrolmentEvaluation.setCheckSum(null);
				CreateAndUpdateAllStudentsPastEnrolments.enrollmentEvaluationsCreated.put(key, enrolmentEvaluation);
			}
		}

		ReportEnrolment.addEnrolmentMigrated();

		return enrolment;
	}

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
				// This should NEVER happen!
				System.out.println("[ERROR 07] No record of EnrolmentEvaluationType with code: [" + mwEnrolment.getSeason() + "]!");
				break;
		}

		return enrolmentEvaluationType;
	}

	/**
	 * @param mwEnrolment
	 * @return
	 */
	private static EnrolmentState getEnrollmentStateByGrade(MWEnrolment mwEnrolment) {

		String grade = mwEnrolment.getGrade();

		if (mwEnrolment.getRemarks() != null) {
			if (mwEnrolment.getRemarks().equals("ANULADO")) {
				return EnrolmentState.ANNULED;
			}
		}

		if (grade == null) {
			return EnrolmentState.NOT_EVALUATED;
		}

		if (grade.equals("")) {
			return EnrolmentState.NOT_EVALUATED;
		}

		if (grade.equals("RE")) {
			return EnrolmentState.NOT_APROVED;
		}

		if (grade.equals("AP")) {
			return EnrolmentState.APROVED;
		}

		int intGrade;

		try {
			intGrade = new Integer(grade).intValue();
		} catch (NumberFormatException e) {
			System.out.println("[ERROR 08] Grade from MWEnrolment is not a number: [" + mwEnrolment.getGrade() + "]!");
			return null;
		}

		if ((intGrade > 20) || (intGrade < 0)) {
			System.out.println("[ERROR 09] Grade from MWEnrolment is not valid: [" + mwEnrolment.getGrade() + "]!");
			return null;
		} else if (intGrade < 10) {
			return EnrolmentState.NOT_APROVED;
		}

		return EnrolmentState.APROVED;
	}

	/**
	 * @param mwEnrolment
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Exception
	 */
	private static IPessoa getPersonResponsibleForGrade(MWEnrolment mwEnrolment, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentTeacher persistentTeacher = fenixPersistentSuport.getIPersistentTeacher();
		
		ITeacher teacher = persistentTeacher.readTeacherByNumber(mwEnrolment.getTeachernumber());
		
		if (teacher == null) {
			System.out.println("[ERROR 10] No Teacher with number: [" + mwEnrolment.getTeachernumber() + "] was found in the Fenix DB!");
			return null;
		}
		
		return teacher.getPerson();
	}
}