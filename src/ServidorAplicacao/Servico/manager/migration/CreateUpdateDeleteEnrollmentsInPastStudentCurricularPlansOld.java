package ServidorAplicacao.Servico.manager.migration;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWBranch;
import middleware.middlewareDomain.MWCurricularCourse;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWEnrolment;
import middleware.middlewareDomain.MWStudent;
import middleware.middlewareDomain.MWUniversity;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourse;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMWUniversity;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.studentMigration.enrollments.CreateAndUpdateAllPastCurriculums;
import middleware.studentMigration.enrollments.ReportAllPastEnrollmentMigration;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CurricularCourse;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IUniversity;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentUniversity;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 * @author David Santos in Feb 7, 2004
 */

public class CreateUpdateDeleteEnrollmentsInPastStudentCurricularPlansOld extends CreateUpdateDeleteEnrollments implements IService
{
	private boolean toWriteEnrollments;
	private HashMap studentCurricularPlansCreated;
	private HashMap enrollmentsCreated;
	private HashMap enrollmentEvaluationsCreated;
	private HashMap curricularCoursesCreated;
	private int numberOfElementsInSpan;
	private int maximumNumberOfElementsToConsider;
	
	public CreateUpdateDeleteEnrollmentsInPastStudentCurricularPlansOld()
	{
		try
		{
			super.persistentSuport = SuportePersistenteOJB.getInstance();
			super.persistentMiddlewareSuport = PersistentMiddlewareSupportOJB.getInstance();
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace(System.out);
		}

		this.toWriteEnrollments = true;
		this.studentCurricularPlansCreated = new HashMap();
		this.enrollmentsCreated = new HashMap();
		this.enrollmentEvaluationsCreated = new HashMap();
		this.curricularCoursesCreated = new HashMap();
		super.out = null;
		this.numberOfElementsInSpan = 50;
		this.maximumNumberOfElementsToConsider = 10000;
	}

	public void run(Boolean toWriteEnrollments, Boolean toLogToFile, String fileName)
	{
		this.toWriteEnrollments = toWriteEnrollments.booleanValue();
		MWStudent mwStudent = null;

		try
		{
			super.out = new PrintWriter(System.out, true);
			if (toLogToFile.booleanValue())
			{
				FileOutputStream file = new FileOutputStream(fileName);
				super.out = new PrintWriter(file);
			}

			IPersistentMWAluno mwAlunoDAO = super.persistentMiddlewareSuport.getIPersistentMWAluno();
			IPersistentMWEnrolment mwEnrollmentDAO = super.persistentMiddlewareSuport.getIPersistentMWEnrolment();

			Integer numberOfStudents = mwAlunoDAO.countAll();

			super.out.println("[INFO] Total number of student curriculums to update [" + numberOfStudents + "].");

			int numberOfSpans = numberOfStudents.intValue() / this.numberOfElementsInSpan;
			numberOfSpans = numberOfStudents.intValue() % this.numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
			int totalElementsConsidered = 0;

			for (int span = 0; span < numberOfSpans && totalElementsConsidered < this.maximumNumberOfElementsToConsider; span++)
			{
				super.out.println("[INFO] Reading MWStudents...");
//				List result = mwAlunoDAO.readAllBySpan(new Integer(span), new Integer(numberOfElementsInSpan));
//				super.out.println("[INFO] Updating [" + result.size() + "] student curriculums...");

//				Iterator iterator = result.iterator();

				Iterator iterator = mwAlunoDAO.readAllBySpanIterator(new Integer(span), new Integer(numberOfElementsInSpan));
				
				while (iterator.hasNext())
				{
//					mwStudent = (MWStudent) iterator.next();

					mwStudent = (MWStudent) mwAlunoDAO.lockIteratorNextObj(iterator);
					
					mwStudent.setEnrolments(mwEnrollmentDAO.readByStudentNumber(mwStudent.getNumber()));

					this.createAndUpdateAllStudentPastEnrolments(mwStudent);

					totalElementsConsidered++;

//					ReportAllPastEnrollmentMigration.addStudentCurricularPlansMigrated(this.studentCurricularPlansCreated.size());
//					ReportAllPastEnrollmentMigration.addEnrolmentsMigrated(this.enrollmentsCreated.size());
//					ReportAllPastEnrollmentMigration.addEnrollmentEvaluationsMigrated(this.enrollmentEvaluationsCreated.size());
//					ReportAllPastEnrollmentMigration.addCurricularCoursesMigrated(this.curricularCoursesCreated.size());
//
//					this.studentCurricularPlansCreated.clear();
//					this.enrollmentsCreated.clear();
//					this.enrollmentEvaluationsCreated.clear();
//					this.curricularCoursesCreated.clear();
				}
			}
		} catch (Throwable e)
		{
			super.out.println("[ERROR 201] Exception migrating student [" + mwStudent.getNumber() + "] enrolments!");
			super.out.println("[ERROR 201] Number: [" + mwStudent.getNumber() + "]");
			super.out.println("[ERROR 201] Degree: [" + mwStudent.getDegreecode() + "]");
			super.out.println("[ERROR 201] Branch: [" + mwStudent.getBranchcode() + "]");
			e.printStackTrace(super.out);
		}

		ReportAllPastEnrollmentMigration.addStudentCurricularPlansMigrated(this.studentCurricularPlansCreated.size());
		ReportAllPastEnrollmentMigration.addEnrolmentsMigrated(this.enrollmentsCreated.size());
		ReportAllPastEnrollmentMigration.addEnrollmentEvaluationsMigrated(this.enrollmentEvaluationsCreated.size());
		ReportAllPastEnrollmentMigration.addCurricularCoursesMigrated(this.curricularCoursesCreated.size());
		
		ReportAllPastEnrollmentMigration.report(super.out);
		super.out.close();
	}

	/**
	 * @param mwStudent
	 * @throws Throwable
	 */
	private void createAndUpdateAllStudentPastEnrolments(MWStudent mwStudent) throws Throwable
	{
		IPersistentStudent persistentStudent = super.persistentSuport.getIPersistentStudent();

		// Read Fenix Student.
		IStudent student = persistentStudent.readStudentByNumberAndDegreeType(mwStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

		if (student == null)
		{
			// This can only happen if the Students/Persons migration was not runed before this one!
			super.out.println("[ERROR 202] Can't find Student in Fenix DB with number: [" + mwStudent.getNumber() + "]!");
			return;
		}

		IDegreeCurricularPlan degreeCurricularPlan = this.getDegreeCurricularPlan(mwStudent.getDegreecode());

		if (degreeCurricularPlan == null)
		{
			// This can only happen if the CreateAndUpdateAllPastCurriculums migration was not runed before this one!
			super.out.println("[ERROR 203] No record of Degree with code: [" + mwStudent.getDegreecode() + "]!");
			return;
		}

		IStudentCurricularPlan studentCurricularPlan = this.getStudentCurricularPlan(degreeCurricularPlan, student, mwStudent);

		if (studentCurricularPlan == null)
		{
			super.out.println(
				"[ERROR 204] Could not obtain StudentCurricularPlan for Student with number: ["
					+ mwStudent.getNumber()
					+ "] in Degree with code: ["
					+ mwStudent.getDegreecode()
					+ "]!");
			return;
		}

		this.writeAndUpdateEnrolments(mwStudent, studentCurricularPlan);
	}

	/**
	 * @param degreeCode
	 * @return IDegreeCurricularPlan
	 * @throws Throwable
	 */
	private IDegreeCurricularPlan getDegreeCurricularPlan(Integer degreeCode) throws Throwable
	{
		IPersistentMWDegreeTranslation mwDegreeTranslationDAO = super.persistentMiddlewareSuport.getIPersistentMWDegreeTranslation();
		IPersistentDegreeCurricularPlan degreeCurricularPlanDAO = super.persistentSuport.getIPersistentDegreeCurricularPlan();

		MWDegreeTranslation mwDegreeTranslation = mwDegreeTranslationDAO.readByDegreeCode(degreeCode);

		if (mwDegreeTranslation != null)
		{
			String degreeCurricularPlanName = "PAST-" + mwDegreeTranslation.getDegree().getSigla();
			ICurso degree = mwDegreeTranslation.getDegree();
			IDegreeCurricularPlan degreeCurricularPlan =
				degreeCurricularPlanDAO.readByNameAndDegree(degreeCurricularPlanName, degree);
			return degreeCurricularPlan;
		} else
		{
			return null;
		}
	}

	/**
	 * @param degreeCurricularPlan
	 * @param student
	 * @param mwStudent
	 * @return IStudentCurricularPlan
	 * @throws Throwable
	 */
	private IStudentCurricularPlan getStudentCurricularPlan(
		IDegreeCurricularPlan degreeCurricularPlan,
		IStudent student,
		MWStudent mwStudent)
		throws Throwable
	{
		IStudentCurricularPlanPersistente studentCurricularPlanDAO = super.persistentSuport.getIStudentCurricularPlanPersistente();

		List result = studentCurricularPlanDAO.readAllByStudentAntState(student, StudentCurricularPlanState.PAST_OBJ);
		if ((result == null) || (result.isEmpty()))
		{

			IStudentCurricularPlan studentCurricularPlanToObtainKey = new StudentCurricularPlan();
			studentCurricularPlanToObtainKey.setStudent(student);
			studentCurricularPlanToObtainKey.setDegreeCurricularPlan(degreeCurricularPlan);
			studentCurricularPlanToObtainKey.setCurrentState(StudentCurricularPlanState.PAST_OBJ);
			String key = super.getStudentCurricularPlanKey(studentCurricularPlanToObtainKey);

			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) this.studentCurricularPlansCreated.get(key);

			if (studentCurricularPlan == null)
			{

				IBranch branch = this.getBranch(mwStudent.getDegreecode(), mwStudent.getBranchcode(), degreeCurricularPlan);
				if (branch == null)
				{
					super.out.println(
						"[ERROR 205] No record of Branch with code: ["
							+ mwStudent.getBranchcode()
							+ "] for Degree with code: ["
							+ mwStudent.getDegreecode()
							+ "]!");
					return null;
				}

				Date startDate = null;
				if ((mwStudent.getEnrolments() == null) || (mwStudent.getEnrolments().isEmpty()))
				{
					startDate = new Date();
				} else
				{
					ComparatorChain comparatorChain = new ComparatorChain();
					comparatorChain.addComparator(new BeanComparator("enrolmentyear"));
					Collections.sort(mwStudent.getEnrolments(), comparatorChain);
					MWEnrolment mwEnrolment = (MWEnrolment) mwStudent.getEnrolments().get(0);
					startDate = super.getExecutionPeriodForThisMWEnrolment(mwEnrolment).getBeginDate();
				}

				studentCurricularPlan = new StudentCurricularPlan();

				studentCurricularPlanDAO.simpleLockWrite(studentCurricularPlan);

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

				this.studentCurricularPlansCreated.put(key, studentCurricularPlan);
			}

			return studentCurricularPlan;
		} else if (!result.isEmpty())
		{
			return (IStudentCurricularPlan) result.get(0);
		} else
		{
			return null;
		}
	}

	/**
	 * @param degreeCode
	 * @param branchCode
	 * @param degreeCurricularPlan
	 * @return IBranch
	 * @throws Throwable
	 */
	private IBranch getBranch(Integer degreeCode, Integer branchCode, IDegreeCurricularPlan degreeCurricularPlan) throws Throwable
	{
		IBranch branch = null;

		IPersistentMWBranch mwBranchDAO = super.persistentMiddlewareSuport.getIPersistentMWBranch();
		IPersistentBranch branchDAO = super.persistentSuport.getIPersistentBranch();

		MWBranch mwBranch = mwBranchDAO.readByDegreeCodeAndBranchCode(degreeCode, branchCode);

		if (mwBranch != null)
		{
			String realBranchCode = null;

			if (mwBranch.getDescription().startsWith("CURSO DE "))
			{
				realBranchCode = new String("");
			} else
			{
				realBranchCode =
					new String(
						mwBranch.getDegreecode().toString()
							+ mwBranch.getBranchcode().toString()
							+ mwBranch.getOrientationcode().toString());
			}

			branch = branchDAO.readByDegreeCurricularPlanAndCode(degreeCurricularPlan, realBranchCode);

		} else
		{
			branch =
				CreateAndUpdateAllPastCurriculums.solveBranchesProblemsForDegrees1And4And6And51And53And54And64(
					degreeCode,
					branchCode,
					degreeCurricularPlan,
					branchDAO);
		}

		return branch;
	}

	/**
	 * @param mwStudent
	 * @param studentCurricularPlan
	 * @throws Throwable
	 */
	private void writeAndUpdateEnrolments(MWStudent mwStudent, IStudentCurricularPlan studentCurricularPlan) throws Throwable
	{
		List mwEnrolments = mwStudent.getEnrolments();
		Iterator iterator = mwEnrolments.iterator();
		while (iterator.hasNext())
		{
			final MWEnrolment mwEnrolment = (MWEnrolment) iterator.next();

			IDegreeCurricularPlan degreeCurricularPlan = this.getDegreeCurricularPlan(mwEnrolment.getDegreecode());
			if (degreeCurricularPlan == null)
			{
				// This can only happen if the CreateAndUpdateAllPastCurriculums migration was not runed before this one!
				super.out.println("[ERROR 206] No record of Degree with code: [" + mwEnrolment.getDegreecode() + "]!");
				return;
			}

			ICurricularCourse curricularCourse = this.getCurricularCourse(mwEnrolment, degreeCurricularPlan);

			if (curricularCourse == null)
			{
				super.out.println("[ERROR 207] Couldn't create CurricularCourse with code [" + mwEnrolment.getCoursecode() + "]!");
				ReportAllPastEnrollmentMigration.addUnknownCurricularCourse(mwEnrolment);
				continue;
			}

			IEnrolment enrolment = this.createAndUpdateEnrolment(mwEnrolment, studentCurricularPlan, curricularCourse);

			if (enrolment == null)
			{
				super.out.println("[ERROR 208] Couldn't create nor update enrolment!");
				continue;
			}
		}
	}

	/**
	 * @param mwEnrolment
	 * @param degreeCurricularPlan
	 * @return ICurricularCourse
	 * @throws Throwable
	 */
	private ICurricularCourse getCurricularCourse(MWEnrolment mwEnrolment, IDegreeCurricularPlan degreeCurricularPlan)
		throws Throwable
	{
		IPersistentCurricularCourse curricularCourseDAO = super.persistentSuport.getIPersistentCurricularCourse();

		ICurricularCourse curricularCourseToObtainKey = new CurricularCourse();
		curricularCourseToObtainKey.setDegreeCurricularPlan(degreeCurricularPlan);
		curricularCourseToObtainKey.setCode(mwEnrolment.getCoursecode());
		curricularCourseToObtainKey.setName(this.getCurricularCourseName(mwEnrolment.getCoursecode()));
		String key = super.getCurricularCourseKey(curricularCourseToObtainKey);

		ICurricularCourse curricularCourse = (ICurricularCourse) this.curricularCoursesCreated.get(key);
		if (curricularCourse == null)
		{
			curricularCourse = this.getCurricularCourse(mwEnrolment.getCoursecode(), degreeCurricularPlan, key);
		}

		if ((curricularCourse != null) && (curricularCourse.getUniversity() == null))
		{
			IUniversity university = this.getUniversity(mwEnrolment.getUniversitycode());
			if (university == null)
			{
				super.out.println("[ERROR 209] No record of University with code: [" + mwEnrolment.getUniversitycode() + "]!");
			} else
			{
				curricularCourseDAO.simpleLockWrite(curricularCourse);
				curricularCourse.setUniversity(university);
			}
		}
		return curricularCourse;
	}

	/**
	 * @param universityCode
	 * @return IUniversity
	 * @throws Throwable
	 */
	private IUniversity getUniversity(String universityCode) throws Throwable
	{
		IPersistentMWUniversity mwUniversityDAO = super.persistentMiddlewareSuport.getIPersistentMWUniversity();
		IPersistentUniversity universityDAO = super.persistentSuport.getIPersistentUniversity();

		MWUniversity mwUniversity = mwUniversityDAO.readByCode(universityCode);

		if (mwUniversity == null)
		{
			return null;
		}

		IUniversity university = universityDAO.readByNameAndCode(mwUniversity.getUniversityName(), mwUniversity.getUniversityCode());

		return university;
	}

	/**
	 * @param mwEnrolment
	 * @param studentCurricularPlan
	 * @param curricularCourseScope
	 * @return IEnrolment
	 * @throws Throwable
	 */
	private IEnrolment createAndUpdateEnrolment(
		MWEnrolment mwEnrolment,
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse)
		throws Throwable
	{
		IPersistentEnrolment enrollmentDAO = super.persistentSuport.getIPersistentEnrolment();
		IPersistentEnrolmentEvaluation enrollmentEvaluationDAO = super.persistentSuport.getIPersistentEnrolmentEvaluation();

		IExecutionPeriod executionPeriod = super.getExecutionPeriodForThisMWEnrolment(mwEnrolment);

		EnrolmentEvaluationType enrolmentEvaluationType = this.getEvaluationType(mwEnrolment);

		IEnrolment enrolment =
			enrollmentDAO.readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
				studentCurricularPlan,
				curricularCourse,
				executionPeriod);

		if (enrolment == null)
		{
			IEnrolment enrolmentToObtainKey = new Enrolment();
			enrolmentToObtainKey.setStudentCurricularPlan(studentCurricularPlan);
			enrolmentToObtainKey.setCurricularCourse(curricularCourse);
			enrolmentToObtainKey.setExecutionPeriod(executionPeriod);
			String key = super.getEnrollmentKey(enrolmentToObtainKey);

			enrolment = (IEnrolment) this.enrollmentsCreated.get(key);

			if (enrolment == null)
			{
				// Create the Enrolment.
				enrolment = new Enrolment();

				enrollmentDAO.simpleLockWrite(enrolment);

				enrolment.setCurricularCourse(curricularCourse);
				enrolment.setExecutionPeriod(executionPeriod);
				enrolment.setStudentCurricularPlan(studentCurricularPlan);

				enrolment.setEnrolmentEvaluationType(enrolmentEvaluationType);
				enrolment.setEnrolmentState(super.getEnrollmentStateByGrade(mwEnrolment));

				this.enrollmentsCreated.put(key, enrolment);
			}
		}

		// Create the EnrolmentEvaluation.
		Date whenAltered = null;
		if (mwEnrolment.getExamdate() == null)
		{
			Calendar calendar = Calendar.getInstance();
			calendar.set(mwEnrolment.getEnrolmentyear().intValue(), 9, 1);
			whenAltered = new Date(calendar.getTimeInMillis());
		} else
		{
			whenAltered = mwEnrolment.getExamdate();
		}
		long dateInLongFormat = whenAltered.getTime();
		dateInLongFormat = dateInLongFormat + (mwEnrolment.getIdinternal().longValue() * 1000);
		Date newDate = new Date(dateInLongFormat);

		String grade = super.getAcurateGrade(mwEnrolment);

		IEnrolmentEvaluation enrolmentEvaluation =
			enrollmentEvaluationDAO.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
				enrolment,
				enrolmentEvaluationType,
				grade);

		IEnrolmentEvaluation enrolmentEvaluationToObtainKey = new EnrolmentEvaluation();
		enrolmentEvaluationToObtainKey.setEnrolment(enrolment);
		enrolmentEvaluationToObtainKey.setGrade(grade);
		enrolmentEvaluationToObtainKey.setEnrolmentEvaluationType(enrolmentEvaluationType);
		enrolmentEvaluationToObtainKey.setWhen(whenAltered);
		String key = super.getEnrollmentEvaluationKey(enrolmentEvaluationToObtainKey);

		if (enrolmentEvaluation == null)
		{

			enrolmentEvaluation = (IEnrolmentEvaluation) this.enrollmentEvaluationsCreated.get(key);

			if (enrolmentEvaluation == null)
			{
				enrolmentEvaluation = new EnrolmentEvaluation();

				enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);

				enrolmentEvaluation.setEnrolment(enrolment);
				enrolmentEvaluation.setEnrolmentEvaluationType(enrolmentEvaluationType);
				enrolmentEvaluation.setGrade(grade);

				enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
				enrolmentEvaluation.setExamDate(mwEnrolment.getExamdate());
				enrolmentEvaluation.setObservation(mwEnrolment.getRemarks());
				enrolmentEvaluation.setPersonResponsibleForGrade(super.getPersonResponsibleForGrade(mwEnrolment));
				enrolmentEvaluation.setGradeAvailableDate(mwEnrolment.getExamdate());
				enrolmentEvaluation.setWhen(newDate);
				enrolmentEvaluation.setEmployee(super.getEmployee(mwEnrolment));
				enrolmentEvaluation.setCheckSum(null);
				enrolmentEvaluation.setAckOptLock(new Integer(1));
				
				this.enrollmentEvaluationsCreated.put(key, enrolmentEvaluation);

				super.updateEnrollmentStateAndEvaluationType(enrolment, enrolmentEvaluation);

				super.writeTreatedMWEnrollment(mwEnrolment);

			} else
			{
				if (this.toWriteEnrollments)
				{
					enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);
					enrolmentEvaluation.setExamDate(mwEnrolment.getExamdate());
					enrolmentEvaluation.setObservation(mwEnrolment.getRemarks());
					enrolmentEvaluation.setPersonResponsibleForGrade(super.getPersonResponsibleForGrade(mwEnrolment));
					enrolmentEvaluation.setGradeAvailableDate(mwEnrolment.getExamdate());
					enrolmentEvaluation.setWhen(newDate);
					enrolmentEvaluation.setEmployee(super.getEmployee(mwEnrolment));
					enrolmentEvaluation.setCheckSum(null);
					enrolmentEvaluation.setAckOptLock(new Integer(1));
					super.updateEnrollmentStateAndEvaluationType(enrolment, enrolmentEvaluation);

					super.writeTreatedMWEnrollment(mwEnrolment);

				}
			}
		} else
		{
			if (this.toWriteEnrollments)
			{
				enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);
				enrolmentEvaluation.setExamDate(mwEnrolment.getExamdate());
				enrolmentEvaluation.setObservation(mwEnrolment.getRemarks());
				enrolmentEvaluation.setPersonResponsibleForGrade(super.getPersonResponsibleForGrade(mwEnrolment));
				enrolmentEvaluation.setGradeAvailableDate(mwEnrolment.getExamdate());
				enrolmentEvaluation.setWhen(newDate);
				enrolmentEvaluation.setEmployee(super.getEmployee(mwEnrolment));
				enrolmentEvaluation.setCheckSum(null);
				enrolmentEvaluation.setAckOptLock(new Integer(1));
				super.updateEnrollmentStateAndEvaluationType(enrolment, enrolmentEvaluation);

				super.writeTreatedMWEnrollment(mwEnrolment);

			} else
			{
				if (enrolment.getEvaluations().size() == 1)
				{
					enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);
					enrollmentEvaluationDAO.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());
					ReportAllPastEnrollmentMigration.addEnrollmentEvaluationsDeleted();
					enrollmentDAO.simpleLockWrite(enrolment);
					enrollmentDAO.deleteByOID(Enrolment.class, enrolment.getIdInternal());
					ReportAllPastEnrollmentMigration.addEnrollmentsDeleted();

					super.writeTreatedMWEnrollment(mwEnrolment);

				} else if (enrolment.getEvaluations().size() > 1)
				{
					enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);
					enrollmentEvaluationDAO.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());
					ReportAllPastEnrollmentMigration.addEnrollmentEvaluationsDeleted();

					super.writeTreatedMWEnrollment(mwEnrolment);
				}
			}
		}

		return enrolment;
	}

	/**
	 * @param mwEnrolment
	 * @return EnrolmentEvaluationType
	 */
	private EnrolmentEvaluationType getEvaluationType(MWEnrolment mwEnrolment)
	{
		EnrolmentEvaluationType enrolmentEvaluationType = null;

		int season = mwEnrolment.getSeason().intValue();

		switch (season)
		{
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
				super.out.println("[ERROR 210] No record of EnrolmentEvaluationType with code: [" + mwEnrolment.getSeason() + "]!");
				break;
		}

		return enrolmentEvaluationType;
	}

	/**
	 * @param curricularCourseCode
	 * @return String
	 * @throws Throwable
	 */
	private String getCurricularCourseName(String curricularCourseCode) throws Throwable
	{
		IPersistentMWCurricularCourse persistentMWCurricualrCourse =
			super.persistentMiddlewareSuport.getIPersistentMWCurricularCourse();
		
		MWCurricularCourse mwCurricularCourse = persistentMWCurricualrCourse.readByCode(curricularCourseCode);
		
		if (mwCurricularCourse != null) {
			return mwCurricularCourse.getCoursename();
		} else {
			return null;
		}
	}

	/**
	 * @param courseCode
	 * @param degreeCurricularPlan
	 * @return ICurricularCourse
	 * @throws Throwable
	 */
	private ICurricularCourse getCurricularCourse(String courseCode, IDegreeCurricularPlan degreeCurricularPlan, String key)
		throws Throwable
	{
		IPersistentCurricularCourse persistentCurricularCourse = super.persistentSuport.getIPersistentCurricularCourse();

		List curricularCourses =
			persistentCurricularCourse.readbyCourseCodeAndDegreeCurricularPlan(StringUtils.trim(courseCode), degreeCurricularPlan);

		if (curricularCourses == null)
		{
			curricularCourses = new ArrayList();
		}

		if (curricularCourses.size() > 1)
		{
			System.out.println(
				"[ERROR 211] Several Fenix CurricularCourses with code ["
					+ StringUtils.trim(courseCode)
					+ "] were found for Degree ["
					+ degreeCurricularPlan.getDegree().getNome()
					+ "]!");
			return null;
		} else if (curricularCourses.size() < 1)
		{
			// In fact this can only be curricularCourses.size() == 0 but better safe than sorry :)
			// This means no CurricularCourse was found with that code, for that DegreeCurricularPlan.

			String curricularCourseName = this.getCurricularCourseName(StringUtils.trim(courseCode));
			if (curricularCourseName == null)
			{
				System.out.println(
					"[ERROR 212] Couldn't find name for CurricularCourse with code [" + StringUtils.trim(courseCode) + "]!");
				return null;
			}

			ICurricularCourse curricularCourse = new CurricularCourse();

			persistentCurricularCourse.simpleLockWrite(curricularCourse);

			curricularCourse.setCode(StringUtils.trim(courseCode));
			curricularCourse.setDegreeCurricularPlan(degreeCurricularPlan);
			curricularCourse.setName(curricularCourseName);
			curricularCourse.setType(CurricularCourseType.NORMAL_COURSE_OBJ);

			curricularCourse.setUniversity(null);
			curricularCourse.setCurricularCourseExecutionScope(null);
			curricularCourse.setMandatory(null);
			curricularCourse.setScopes(null);
			curricularCourse.setAssociatedExecutionCourses(null);
			curricularCourse.setBasic(null);
			curricularCourse.setCredits(null);
			curricularCourse.setDepartmentCourse(null);

			this.curricularCoursesCreated.put(key, curricularCourse);
			
			return curricularCourse;
		} else
		{
			// It has been found exactly ONE CurricularCourse already in the Fenix DB with that code, for that DegreeCurricularPlan.
			return (ICurricularCourse) curricularCourses.get(0);
		}
	}

}