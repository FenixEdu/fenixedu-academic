package middleware.studentMigration.enrollments;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWBranch;
import middleware.middlewareDomain.MWCurricularCourseScope;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWEnrolment;
import middleware.middlewareDomain.MWStudent;
import middleware.middlewareDomain.MWUniversity;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMWUniversity;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.CurricularSemester;
import Dominio.CurricularYear;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEmployee;
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
import ServidorPersistente.IPersistentEmployee;
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
	private static HashMap curricularCoursesCreated = new HashMap();
	private static HashMap curricularCourseScopesCreated = new HashMap();

	public static void main(String args[])
	{
		MWStudent mwStudent = null;
		UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.alteredDates = 0;

		try {
			ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
			IPersistentMWAluno persistentMWAluno = mws.getIPersistentMWAluno();
			IPersistentMWEnrolment persistentMWEnrolment = mws.getIPersistentMWEnrolment();

			fenixPersistentSuport.iniciarTransaccao();
			
			Integer numberOfStudents = persistentMWAluno.countAll();
			
			fenixPersistentSuport.confirmarTransaccao();
			
			System.out.println("[INFO] Total number of student curriculums to update [" + numberOfStudents + "].");

			int numberOfElementsInSpan = 100;
			int numberOfSpans = numberOfStudents.intValue() / numberOfElementsInSpan;
			numberOfSpans =  numberOfStudents.intValue() % numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
			
			for (int span = 0; span < numberOfSpans; span++) {
				fenixPersistentSuport.iniciarTransaccao();

				System.gc();
	
				System.out.println("[INFO] Reading MWStudents...");
				List result = persistentMWAluno.readAllBySpan(new Integer(span), new Integer(numberOfElementsInSpan));
	
				fenixPersistentSuport.confirmarTransaccao();
		
				System.out.println("[INFO] Updating [" + result.size() + "] student curriculums...");
		
				Iterator iterator = result.iterator();
				while (iterator.hasNext())
				{
					mwStudent = (MWStudent) iterator.next();
	
					fenixPersistentSuport.iniciarTransaccao();
	
					mwStudent.setEnrolments(persistentMWEnrolment.readByStudentNumber(mwStudent.getNumber()));

					CreateAndUpdateAllStudentsPastEnrolments.createAndUpdateAllStudentPastEnrolments(mwStudent, fenixPersistentSuport);
	
					try
					{
						fenixPersistentSuport.confirmarTransaccao();
					} catch (NullPointerException e) {
						// NOTE [DAVID]: Isto é apenas para prevenir uma situação de excepção
						// ao confirmar a transacção que eu não consegui decifrar ainda.
						ReportAllPastEnrollmentMigration.addClassCastExceptions(mwStudent);
					}

					ReportAllPastEnrollmentMigration.addStudentCurricularPlansMigrated(CreateAndUpdateAllStudentsPastEnrolments.studentCurricularPlansCreated.size());
					ReportAllPastEnrollmentMigration.addEnrolmentsMigrated(CreateAndUpdateAllStudentsPastEnrolments.enrollmentsCreated.size());
					ReportAllPastEnrollmentMigration.addEnrollmentEvaluationsMigrated(CreateAndUpdateAllStudentsPastEnrolments.enrollmentEvaluationsCreated.size());
					ReportAllPastEnrollmentMigration.addCurricularCoursesMigrated(CreateAndUpdateAllStudentsPastEnrolments.curricularCoursesCreated.size());
					ReportAllPastEnrollmentMigration.addCurricularCourseScopesMigrated(CreateAndUpdateAllStudentsPastEnrolments.curricularCourseScopesCreated.size());

					CreateAndUpdateAllStudentsPastEnrolments.studentCurricularPlansCreated.clear();
					CreateAndUpdateAllStudentsPastEnrolments.enrollmentsCreated.clear();
					CreateAndUpdateAllStudentsPastEnrolments.enrollmentEvaluationsCreated.clear();
					CreateAndUpdateAllStudentsPastEnrolments.curricularCoursesCreated.clear();
					CreateAndUpdateAllStudentsPastEnrolments.curricularCourseScopesCreated.clear();
				}
			}
		} catch (Throwable e) {
			System.out.println("[ERROR 201] Exception migrating student [" + mwStudent.getNumber() + "] enrolments!");
			System.out.println("[ERROR 201] Number: [" + mwStudent.getNumber() + "]");
			System.out.println("[ERROR 201] Degree: [" + mwStudent.getDegreecode() + "]");
			System.out.println("[ERROR 201] Branch: [" + mwStudent.getBranchcode() + "]");
			e.printStackTrace(System.out);
		}

		System.out.println("[INFO] DONE!");
		System.out.println("[INFO] Total EnrolmentEvaluations updated: [" + UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.alteredDates + "].");

		ReportAllPastEnrollmentMigration.report(new PrintWriter(System.out, true));
	}

	/**
	 * @param mwStudent
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void createAndUpdateAllStudentPastEnrolments(MWStudent mwStudent, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentStudent persistentStudent = fenixPersistentSuport.getIPersistentStudent();

		// Read Fenix Student.
		IStudent student = persistentStudent.readByNumero(mwStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

		if (student == null) {
			// This can only happen if the Students/Persons migration was not runed before this one!
			System.out.println("[ERROR 202] Can't find Student in Fenix DB with number: [" + mwStudent.getNumber() + "]!");
			return;
		}

		IDegreeCurricularPlan degreeCurricularPlan = CreateAndUpdateAllStudentsPastEnrolments.getDegreeCurricularPlan(mwStudent.getDegreecode(), fenixPersistentSuport);
		
		if (degreeCurricularPlan == null) {
			// This can only happen if the CreateAndUpdateAllPastCurriculums migration was not runed before this one!
			System.out.println("[ERROR 203] No record of Degree with code: [" + mwStudent.getDegreecode() + "]!");
			return;
		}

		IStudentCurricularPlan studentCurricularPlan = CreateAndUpdateAllStudentsPastEnrolments.getStudentCurricularPlan(degreeCurricularPlan, student, mwStudent, fenixPersistentSuport);
		
		if (studentCurricularPlan == null) {
			System.out.println("[ERROR 204] Could not obtain StudentCurricularPlan for Student with number: [" + mwStudent.getNumber() + "] in Degree with code: [" + mwStudent.getDegreecode() + "]!");
			return;
		}
		
		CreateAndUpdateAllStudentsPastEnrolments.writeAndUpdateEnrolments(mwStudent, studentCurricularPlan, fenixPersistentSuport);
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
	private static IStudentCurricularPlan getStudentCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan, IStudent student, MWStudent mwStudent, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = fenixPersistentSuport.getIStudentCurricularPlanPersistente();

		List result = persistentStudentCurricularPlan.readAllByStudentAntState(student, StudentCurricularPlanState.PAST_OBJ);
		if ((result == null) || (result.isEmpty())) {

			IStudentCurricularPlan studentCurricularPlanToObtainKey = new StudentCurricularPlan();
			studentCurricularPlanToObtainKey.setStudent(student);
			studentCurricularPlanToObtainKey.setDegreeCurricularPlan(degreeCurricularPlan);
			studentCurricularPlanToObtainKey.setCurrentState(StudentCurricularPlanState.PAST_OBJ);
			String key = CreateAndUpdateAllStudentsPastEnrolments.getStudentCurricularPlanKey(studentCurricularPlanToObtainKey);

			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) CreateAndUpdateAllStudentsPastEnrolments.studentCurricularPlansCreated.get(key);
			
			if (studentCurricularPlan == null) {

				IBranch branch = CreateAndUpdateAllStudentsPastEnrolments.getBranch(mwStudent.getDegreecode(), mwStudent.getBranchcode(), degreeCurricularPlan, fenixPersistentSuport);
				if (branch == null) {
					System.out.println("[ERROR 205] No record of Branch with code: [" + mwStudent.getBranchcode() + "] for Degree with code: [" + mwStudent.getDegreecode() + "]!");
					return null;
				}

				Date startDate = null;
				if ((mwStudent.getEnrolments() == null) || (mwStudent.getEnrolments().isEmpty()) )
				{
				    startDate = new Date();
				} else
				{
				    ComparatorChain comparatorChain = new ComparatorChain();
				    comparatorChain.addComparator(new BeanComparator("enrolmentyear"));
				    Collections.sort(mwStudent.getEnrolments(), comparatorChain);
				    MWEnrolment mwEnrolment = (MWEnrolment) mwStudent.getEnrolments().get(0);
				    startDate = CreateAndUpdateAllStudentsPastEnrolments.getExecutionPeriodForThisMWEnrolment(mwEnrolment, fenixPersistentSuport).getBeginDate();
				}    

				studentCurricularPlan = new StudentCurricularPlan();

				persistentStudentCurricularPlan.simpleLockWrite(studentCurricularPlan);

				studentCurricularPlan.setDegreeCurricularPlan(degreeCurricularPlan);
				studentCurricularPlan.setCurrentState(StudentCurricularPlanState.PAST_OBJ);
				studentCurricularPlan.setStartDate(startDate);
				studentCurricularPlan.setStudent(student);
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
				
				CreateAndUpdateAllStudentsPastEnrolments.studentCurricularPlansCreated.put(key, studentCurricularPlan);
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
	 * @throws Throwable
	 */
	private static IBranch getBranch(Integer degreeCode, Integer branchCode, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IBranch branch = null;
		
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentMWBranch = mws.getIPersistentMWBranch();
		IPersistentBranch persistentBranch = fenixPersistentSuport.getIPersistentBranch();

		MWBranch mwBranch = persistentMWBranch.readByDegreeCodeAndBranchCode(degreeCode, branchCode);

		if (mwBranch != null) {
			String realBranchCode = null;

			if (mwBranch.getDescription().startsWith("CURSO DE ")) {
				realBranchCode = new String("");
			} else {
				realBranchCode = new String(mwBranch.getDegreecode().toString() + mwBranch.getBranchcode().toString() + mwBranch.getOrientationcode().toString());
			}

			branch = persistentBranch.readByDegreeCurricularPlanAndCode(degreeCurricularPlan, realBranchCode);

		} else {
			branch = CreateAndUpdateAllPastCurriculums.solveBranchesProblemsForDegrees1And4And6And51And53And54And64(degreeCode, branchCode, degreeCurricularPlan, persistentBranch);
		}

		return branch;
	}

	/**
	 * @param mwStudent
	 * @param studentCurricularPlan
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void writeAndUpdateEnrolments(MWStudent mwStudent, IStudentCurricularPlan studentCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		List mwEnrolments = mwStudent.getEnrolments();
		Iterator iterator = mwEnrolments.iterator();
		while (iterator.hasNext()) {
			final MWEnrolment mwEnrolment = (MWEnrolment) iterator.next();
			
			IDegreeCurricularPlan degreeCurricularPlan = CreateAndUpdateAllStudentsPastEnrolments.getDegreeCurricularPlan(mwEnrolment.getDegreecode(), fenixPersistentSuport);
			if (degreeCurricularPlan == null) {
				// This can only happen if the CreateAndUpdateAllPastCurriculums migration was not runed before this one!
				System.out.println("[ERROR 206] No record of Degree with code: [" + mwEnrolment.getDegreecode() + "]!");
				return;
			}

			ICurricularCourse curricularCourse = CreateAndUpdateAllStudentsPastEnrolments.getCurricularCourse(mwEnrolment, degreeCurricularPlan, fenixPersistentSuport);
			
			if (curricularCourse == null) {
				System.out.println("[ERROR 207] Couldn't create CurricularCourse with code [" + mwEnrolment.getCoursecode() + "]!");
				ReportAllPastEnrollmentMigration.addUnknownCurricularCourse(mwEnrolment);
				continue;
			}

			ICurricularCourseScope curricularCourseScope = CreateAndUpdateAllStudentsPastEnrolments.getCurricularCourseScope(mwEnrolment, curricularCourse, /*studentCurricularPlan, */degreeCurricularPlan, fenixPersistentSuport);

			if (curricularCourseScope == null) {
				System.out.print("[ERROR 208] Couldn't create CurricularCourseScope with data: ");
				System.out.print("Course Code - [" + mwEnrolment.getCoursecode() + "], ");
				System.out.print("Degree Code - [" + mwEnrolment.getDegreecode() + "], ");
				System.out.print("Branch Code - [" + mwEnrolment.getBranchcode() + "], ");
				System.out.print("Curricular Year - [" + mwEnrolment.getCurricularcourseyear() + "], ");
				System.out.print("Curricular Semester - [" + mwEnrolment.getCurricularcoursesemester() + "], ");
				System.out.println("Enrolment Year: [" + mwEnrolment.getEnrolmentyear() + "]!");
				continue;
			}

			IEnrolment enrolment = CreateAndUpdateAllStudentsPastEnrolments.createAndUpdateEnrolment(mwEnrolment, studentCurricularPlan, curricularCourseScope, fenixPersistentSuport);

			if (enrolment == null) {
				System.out.println("[ERROR 209] Couldn't create nor update enrolment!");
				continue;
			}
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

		ICurricularCourse curricularCourseToObtainKey = new CurricularCourse();
		curricularCourseToObtainKey.setDegreeCurricularPlan(degreeCurricularPlan);
		curricularCourseToObtainKey.setCode(mwEnrolment.getCoursecode());
		curricularCourseToObtainKey.setName(CreateAndUpdateAllPastCurriculums.getCurricularCourseName(mwEnrolment.getCoursecode()));
		String key = CreateAndUpdateAllStudentsPastEnrolments.getCurricularCourseKey(curricularCourseToObtainKey);
		
		ICurricularCourse curricularCourse = (ICurricularCourse) CreateAndUpdateAllStudentsPastEnrolments.curricularCoursesCreated.get(key);
		if (curricularCourse == null) {
			int x = CreateAndUpdateAllPastCurriculums.curricularCoursesCreated;
			curricularCourse = CreateAndUpdateAllPastCurriculums.getCurricularCourse(mwEnrolment.getCoursecode(), degreeCurricularPlan, fenixPersistentSuport);
			if (CreateAndUpdateAllPastCurriculums.curricularCoursesCreated > x)
			{
				CreateAndUpdateAllStudentsPastEnrolments.curricularCoursesCreated.put(key, curricularCourse);
			}
		}

		if ((curricularCourse != null) && (curricularCourse.getUniversity() == null) ) {
			IUniversity university = CreateAndUpdateAllStudentsPastEnrolments.getUniversity(mwEnrolment.getUniversitycode(), fenixPersistentSuport);
			if (university == null) {
				System.out.println("[ERROR 210] No record of University with code: [" + mwEnrolment.getUniversitycode() + "]!");
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
	 * @throws Throwable
	 */
	private static ICurricularCourseScope getCurricularCourseScope(MWEnrolment mwEnrolment, ICurricularCourse curricularCourse, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IBranch branch = CreateAndUpdateAllStudentsPastEnrolments.getBranch(mwEnrolment.getDegreecode(), mwEnrolment.getBranchcode(), degreeCurricularPlan, fenixPersistentSuport);
		if (branch == null) {
			branch = CreateAndUpdateAllStudentsPastEnrolments.getBranch(mwEnrolment.getDegreecode(), new Integer(0), degreeCurricularPlan, fenixPersistentSuport);
			if (branch == null) {
				System.out.println("[ERROR 211] No record of Branch with code: [" + mwEnrolment.getBranchcode() + "] for Degree with code: [" + mwEnrolment.getDegreecode() + "]!");
				return null;
			}
		}

		ICurricularYear curricularYear = new CurricularYear();
		curricularYear.setYear(mwEnrolment.getCurricularcourseyear());
		ICurricularSemester curricularSemester = new CurricularSemester();
		curricularSemester.setCurricularYear(curricularYear);
		curricularSemester.setSemester(mwEnrolment.getCurricularcoursesemester());
		ICurricularCourseScope curricularCourseScopeToObtainKey = new CurricularCourseScope();
		curricularCourseScopeToObtainKey.setCurricularSemester(curricularSemester);
		curricularCourseScopeToObtainKey.setCurricularCourse(curricularCourse);
		curricularCourseScopeToObtainKey.setBranch(branch);
		curricularCourseScopeToObtainKey.setBeginDate(Calendar.getInstance());
		String key = CreateAndUpdateAllStudentsPastEnrolments.getCurricularCourseScopeKey(curricularCourseScopeToObtainKey);

		ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) CreateAndUpdateAllStudentsPastEnrolments.curricularCourseScopesCreated.get(key);
		if (curricularCourseScope == null) {
			MWCurricularCourseScope mwCurricularCourseScope = new MWCurricularCourseScope();
			mwCurricularCourseScope.setCurricularsemester(mwEnrolment.getCurricularcoursesemester());
			mwCurricularCourseScope.setCurricularyear(mwEnrolment.getCurricularcourseyear());
			mwCurricularCourseScope.setExecutionyear(mwEnrolment.getEnrolmentyear());
			int x = CreateAndUpdateAllPastCurriculums.curricularCourseScopesCreated;
			curricularCourseScope = CreateAndUpdateAllPastCurriculums.getCurricularCourseScope(mwCurricularCourseScope, curricularCourse, branch, fenixPersistentSuport);
			if (CreateAndUpdateAllPastCurriculums.curricularCourseScopesCreated > x)
			{
				CreateAndUpdateAllStudentsPastEnrolments.curricularCourseScopesCreated.put(key, curricularCourseScope);
			}	
		}
		return curricularCourseScope;
	}

	/**
	 * @param universityCode
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
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
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static IEnrolment createAndUpdateEnrolment(MWEnrolment mwEnrolment, IStudentCurricularPlan studentCurricularPlan, ICurricularCourseScope curricularCourseScope, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentEnrolment persistentEnrolment = fenixPersistentSuport.getIPersistentEnrolment();
		IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = fenixPersistentSuport.getIPersistentEnrolmentEvaluation();

		IExecutionPeriod executionPeriod = CreateAndUpdateAllStudentsPastEnrolments.getExecutionPeriodForThisMWEnrolment(mwEnrolment, fenixPersistentSuport);

		EnrolmentEvaluationType enrolmentEvaluationType = CreateAndUpdateAllStudentsPastEnrolments.getEvaluationType(mwEnrolment);

		IEnrolment enrolment = persistentEnrolment.readByStudentCurricularPlanAndCurricularCourseScopeAndExecutionPeriod(studentCurricularPlan, curricularCourseScope, executionPeriod);

		if (enrolment == null) {

			IEnrolment enrolmentToObtainKey = new Enrolment();
			enrolmentToObtainKey.setStudentCurricularPlan(studentCurricularPlan);
			enrolmentToObtainKey.setCurricularCourseScope(curricularCourseScope);
			enrolmentToObtainKey.setExecutionPeriod(executionPeriod);
			String key = CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentKey(enrolmentToObtainKey);

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
		Date whenAltered = null;
		if (mwEnrolment.getExamdate() == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(mwEnrolment.getEnrolmentyear().intValue(), 9, 1);
			whenAltered = new Date(calendar.getTimeInMillis());
		} else {
			whenAltered = mwEnrolment.getExamdate();
		}
		long dateInLongFormat = whenAltered.getTime();
		dateInLongFormat = dateInLongFormat + (mwEnrolment.getIdinternal().longValue() * 1000);
		Date newDate = new Date(dateInLongFormat);

		String grade = CreateAndUpdateAllStudentsPastEnrolments.getAcurateGrade(mwEnrolment);

//		IEnrolmentEvaluation enrolmentEvaluation = persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(enrolment, enrolmentEvaluationType, grade, whenAltered);
		IEnrolmentEvaluation enrolmentEvaluation = persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(enrolment, enrolmentEvaluationType, grade);
		
		IEnrolmentEvaluation enrolmentEvaluationToObtainKey = new EnrolmentEvaluation();
		enrolmentEvaluationToObtainKey.setEnrolment(enrolment);
		enrolmentEvaluationToObtainKey.setGrade(grade);
		enrolmentEvaluationToObtainKey.setEnrolmentEvaluationType(enrolmentEvaluationType);
		enrolmentEvaluationToObtainKey.setWhen(whenAltered);
		String key = CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentEvaluationKey(enrolmentEvaluationToObtainKey);

		if (enrolmentEvaluation == null) {
			
			enrolmentEvaluation = (IEnrolmentEvaluation) CreateAndUpdateAllStudentsPastEnrolments.enrollmentEvaluationsCreated.get(key);

			if (enrolmentEvaluation == null) {
				enrolmentEvaluation = new EnrolmentEvaluation();

				fenixPersistentSuport.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluation);

				enrolmentEvaluation.setEnrolment(enrolment);
				enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
				enrolmentEvaluation.setEnrolmentEvaluationType(enrolmentEvaluationType);
				enrolmentEvaluation.setExamDate(mwEnrolment.getExamdate());
				enrolmentEvaluation.setGrade(grade);
				enrolmentEvaluation.setObservation(mwEnrolment.getRemarks());
				enrolmentEvaluation.setPersonResponsibleForGrade(CreateAndUpdateAllStudentsPastEnrolments.getPersonResponsibleForGrade(mwEnrolment, fenixPersistentSuport));

				enrolmentEvaluation.setGradeAvailableDate(mwEnrolment.getExamdate());
				enrolmentEvaluation.setWhen(newDate);
				enrolmentEvaluation.setEmployee(CreateAndUpdateAllStudentsPastEnrolments.getEmployee(mwEnrolment, fenixPersistentSuport));

				enrolmentEvaluation.setCheckSum(null);

				CreateAndUpdateAllStudentsPastEnrolments.enrollmentEvaluationsCreated.put(key, enrolmentEvaluation);
			} else {
//				System.out.println("[WARNING 203] No EnrolmentEvaluation was created!");
//				ReportAllPastEnrollmentMigration.addNotCreatedEnrolmentEvaluation(key, mwEnrolment);
			}
		} else {
//			System.out.println("[WARNING 204] No EnrolmentEvaluation was created!");
//			ReportAllPastEnrollmentMigration.addNotCreatedEnrolmentEvaluation(key, mwEnrolment);
			fenixPersistentSuport.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluation);
			enrolmentEvaluation.setWhen(newDate);
			UpdateEnrollmentEvaluationsDatesForAllStudentsPastEnrolments.alteredDates++;
		}

		CreateAndUpdateAllStudentsPastEnrolments.updateEnrollmentStateAndEvaluationType(enrolment, enrolmentEvaluation);
		
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
				System.out.println("[ERROR 212] No record of EnrolmentEvaluationType with code: [" + mwEnrolment.getSeason() + "]!");
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

		if (grade.equals("0")) {
			return EnrolmentState.NOT_EVALUATED;
		}

		if (grade.equals("NA")) {
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
			System.out.println("[ERROR 213] Grade from MWEnrolment is not a number: [" + mwEnrolment.getGrade() + "]!");
			return null;
		}

		if ((intGrade > 20) || (intGrade < 0)) {
			System.out.println("[ERROR 214] Grade from MWEnrolment is not valid: [" + mwEnrolment.getGrade() + "]!");
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
	 * @throws Throwable
	 */
	private static IPessoa getPersonResponsibleForGrade(MWEnrolment mwEnrolment, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentTeacher persistentTeacher = fenixPersistentSuport.getIPersistentTeacher();
		
		ITeacher teacher = persistentTeacher.readByNumber(mwEnrolment.getTeachernumber());
		
		if (teacher == null) {
			if (mwEnrolment.getTeachernumber().intValue() != 0) {
				System.out.println("[WARNING 205] No Teacher with number: [" + mwEnrolment.getTeachernumber() + "] was found in the Fenix DB!");
				ReportAllPastEnrollmentMigration.addUnknownTeachersAndEmployees(mwEnrolment);
			}
			return null;
		}
		
		return teacher.getPerson();
	}

	/**
	 * @param mwEnrolment
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static IEmployee getEmployee(MWEnrolment mwEnrolment, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentEmployee persistentEmployee = fenixPersistentSuport.getIPersistentEmployee();
		
		IEmployee employee = persistentEmployee.readByNumber(mwEnrolment.getTeachernumber());
		
		if (employee == null) {
			if (mwEnrolment.getTeachernumber().intValue() != 0) {
				ReportAllPastEnrollmentMigration.addUnknownTeachersAndEmployees(mwEnrolment);
				System.out.println("[WARNING 206] No Employee with number: [" + mwEnrolment.getTeachernumber() + "] was found in the Fenix DB!");
			}
			return null;
		}
		
		return employee;
	}

	/**
	 * @param person
	 * @return
	 */
	private static String getPersonKey(IPessoa person)
	{
		return person.getNumeroDocumentoIdentificacao() + person.getUsername() + person.getTipoDocumentoIdentificacao().toString();
	}

	/**
	 * @param student
	 * @return
	 */
	private static String getStudentKey(IStudent student)
	{
		return CreateAndUpdateAllStudentsPastEnrolments.getPersonKey(student.getPerson()) + student.getNumber().toString() + student.getDegreeType().toString();
	}

	/**
	 * @param degree
	 * @return
	 */
	private static String getDegreeKey(ICurso degree)
	{
		return degree.getSigla() + degree.getNome() + degree.getTipoCurso().toString();
	}

	/**
	 * @param degreeCurricularPlan
	 * @return
	 */
	private static String getDegreeCurricularPlanKey(IDegreeCurricularPlan degreeCurricularPlan)
	{
		return CreateAndUpdateAllStudentsPastEnrolments.getDegreeKey(degreeCurricularPlan.getDegree()) + degreeCurricularPlan.getName();
	}

	/**
	 * @param studentCurricularPlan
	 * @return
	 */
	private static String getStudentCurricularPlanKey(IStudentCurricularPlan studentCurricularPlan)
	{
		return CreateAndUpdateAllStudentsPastEnrolments.getStudentKey(studentCurricularPlan.getStudent()) + 
			CreateAndUpdateAllStudentsPastEnrolments.getDegreeCurricularPlanKey(studentCurricularPlan.getDegreeCurricularPlan()) +
			studentCurricularPlan.getCurrentState().toString();
	}

	/**
	 * @param curricularYear
	 * @return
	 */
	private static String getCurricularYearKey(ICurricularYear curricularYear)
	{
		return curricularYear.getYear().toString();
	}

	/**
	 * @param curricularSemester
	 * @return
	 */
	private static String getCurricularSemesterKey(ICurricularSemester curricularSemester)
	{
		return CreateAndUpdateAllStudentsPastEnrolments.getCurricularYearKey(curricularSemester.getCurricularYear()) + curricularSemester.getSemester().toString();
	}

	/**
	 * @param curricularCourse
	 * @return
	 */
	private static String getCurricularCourseKey(ICurricularCourse curricularCourse)
	{
		return CreateAndUpdateAllStudentsPastEnrolments.getDegreeCurricularPlanKey(curricularCourse.getDegreeCurricularPlan()) + 
			curricularCourse.getName() + curricularCourse.getCode();
	}

	/**
	 * @param branch
	 * @return
	 */
	private static String getBranchKey(IBranch branch)
	{
		return CreateAndUpdateAllStudentsPastEnrolments.getDegreeCurricularPlanKey(branch.getDegreeCurricularPlan()) + 
			branch.getCode();
	}

	/**
	 * @param curricularCourseScope
	 * @return
	 */
	private static String getCurricularCourseScopeKey(ICurricularCourseScope curricularCourseScope)
	{
		return CreateAndUpdateAllStudentsPastEnrolments.getCurricularSemesterKey(curricularCourseScope.getCurricularSemester()) +
			CreateAndUpdateAllStudentsPastEnrolments.getCurricularCourseKey(curricularCourseScope.getCurricularCourse()) +
			CreateAndUpdateAllStudentsPastEnrolments.getBranchKey(curricularCourseScope.getBranch()) +
			curricularCourseScope.getBeginDate().get(Calendar.YEAR) + "-" +
			curricularCourseScope.getBeginDate().get(Calendar.MONTH) + "-" +
			curricularCourseScope.getBeginDate().get(Calendar.DAY_OF_MONTH); 
	}

	/**
	 * @param executionYear
	 * @return
	 */
	private static String getExecutionYearKey(IExecutionYear executionYear)
	{
		return executionYear.getYear();
	}

	/**
	 * @param executionPeriod
	 * @return
	 */
	private static String getExecutionPeriodKey(IExecutionPeriod executionPeriod)
	{
		return CreateAndUpdateAllStudentsPastEnrolments.getExecutionYearKey(executionPeriod.getExecutionYear()) + executionPeriod.getName();
	}

	/**
	 * @param enrolment
	 * @return
	 */
	public static String getEnrollmentKey(IEnrolment enrolment)
	{
		return CreateAndUpdateAllStudentsPastEnrolments.getStudentCurricularPlanKey(enrolment.getStudentCurricularPlan()) +
			CreateAndUpdateAllStudentsPastEnrolments.getCurricularCourseScopeKey(enrolment.getCurricularCourseScope()) +
			CreateAndUpdateAllStudentsPastEnrolments.getExecutionPeriodKey(enrolment.getExecutionPeriod());
	}

	/**
	 * @param enrolmentEvaluation
	 * @return
	 */
	public static String getEnrollmentEvaluationKey(IEnrolmentEvaluation enrolmentEvaluation)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTimeInMillis(enrolmentEvaluation.getWhen().getTime());

		return CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentKey(enrolmentEvaluation.getEnrolment()) +
			enrolmentEvaluation.getGrade() +
			enrolmentEvaluation.getEnrolmentEvaluationType().toString() +
			calendar.get(Calendar.YEAR) + "-" +
			calendar.get(Calendar.MONTH) + "-" +
			calendar.get(Calendar.DAY_OF_MONTH); 
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
			System.out.println("[ERROR 215] Grade from MWEnrolment is not a number: [" + mwEnrolment.getGrade() + "]!");
			return "0";
		}

		if ((intGrade > 20) || (intGrade < 0)) {
			System.out.println("[ERROR 216] Grade from MWEnrolment is not valid: [" + mwEnrolment.getGrade() + "]!");
			return "0";
		} else
		{
			return (new Integer(intGrade)).toString();
		}
	}

	/**
	 * @param enrolment
	 * @param enrolmentEvaluation
	 */
	private static void updateEnrollmentStateAndEvaluationType(IEnrolment enrolment, IEnrolmentEvaluation enrolmentEvaluation)
	{
		MWEnrolment mwEnrolment = new MWEnrolment();
		mwEnrolment.setGrade(enrolmentEvaluation.getGrade());
		EnrolmentState enrolmentStateFromEnrolmentEvaluation = CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentStateByGrade(mwEnrolment);

		if (!enrolment.getEnrolmentState().equals(enrolmentStateFromEnrolmentEvaluation))
		{
			if (enrolment.getEnrolmentState().equals(EnrolmentState.NOT_APROVED) && enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.APROVED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			} else if (enrolment.getEnrolmentState().equals(EnrolmentState.NOT_APROVED) && enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.ANNULED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			} else if (enrolment.getEnrolmentState().equals(EnrolmentState.NOT_EVALUATED) && enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.NOT_APROVED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			} else if (enrolment.getEnrolmentState().equals(EnrolmentState.NOT_EVALUATED) && enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.APROVED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			} else if (enrolment.getEnrolmentState().equals(EnrolmentState.NOT_EVALUATED) && enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.ANNULED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			} else if (enrolment.getEnrolmentState().equals(EnrolmentState.APROVED) && enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.ANNULED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			}
		}
	}
}