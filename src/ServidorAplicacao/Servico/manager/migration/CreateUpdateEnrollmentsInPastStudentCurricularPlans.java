package ServidorAplicacao.Servico.manager.migration;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWEnrolment;
import middleware.middlewareDomain.MWStudent;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWEnrolment;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.studentMigration.enrollments.ReportAllPastEnrollmentMigration;
import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.TipoCurso;

/**
 * @author David Santos in Feb 7, 2004
 */

public class CreateUpdateEnrollmentsInPastStudentCurricularPlans
	extends CreateUpdateDeleteEnrollmentsInPastStudentCurricularPlans
	implements IService
{
	private HashMap enrollmentsCreated;
	private HashMap enrollmentEvaluationsCreated;

	public CreateUpdateEnrollmentsInPastStudentCurricularPlans()
	{
		try
		{
			super.persistentSuport = SuportePersistenteOJB.getInstance();
			super.persistentMiddlewareSuport = PersistentMiddlewareSupportOJB.getInstance();
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace(System.out);
		}

		this.enrollmentsCreated = new HashMap();
		this.enrollmentEvaluationsCreated = new HashMap();
		super.studentCurricularPlansCreated = new HashMap();
		super.curricularCoursesCreated = new HashMap();
		super.out = null;
		super.numberOfElementsInSpan = 150;
		super.maximumNumberOfElementsToConsider = 10000;
	}

	/**
	 * @param toLogToFile
	 * @param fileName
	 */
	public void run(Boolean toLogToFile, String fileName) throws FenixServiceException
	{
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

			int numberOfSpans = numberOfStudents.intValue() / super.numberOfElementsInSpan;
			numberOfSpans = numberOfStudents.intValue() % super.numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
			int totalElementsConsidered = 0;

			for (int span = 0; span < numberOfSpans && totalElementsConsidered < super.maximumNumberOfElementsToConsider; span++)
			{
				super.out.println("[INFO] Reading MWStudents...");

				Iterator iterator = mwAlunoDAO.readAllBySpanIterator(new Integer(span), new Integer(numberOfElementsInSpan));
				while (iterator.hasNext())
				{
					mwStudent = (MWStudent) mwAlunoDAO.lockIteratorNextObj(iterator);

					mwStudent.setEnrolments(mwEnrollmentDAO.readByStudentNumber(mwStudent.getNumber()));

					this.writeUpdateStudentEnrollments(mwStudent);

					totalElementsConsidered++;
				}
			}
		} catch (Throwable e)
		{
			if (mwStudent != null)
			{
				super.out.println("[ERROR 201.1] Exception migrating student [" + mwStudent.getNumber() + "] enrolments!");
				super.out.println("[ERROR 201.1] Number: [" + mwStudent.getNumber() + "]");
				super.out.println("[ERROR 201.1] Degree: [" + mwStudent.getDegreecode() + "]");
				super.out.println("[ERROR 201.1] Branch: [" + mwStudent.getBranchcode() + "]");
			}
			e.printStackTrace(super.out);
			throw new FenixServiceException(e);
		}

		ReportAllPastEnrollmentMigration.addStudentCurricularPlansMigrated(super.studentCurricularPlansCreated.size());
		ReportAllPastEnrollmentMigration.addEnrolmentsMigrated(this.enrollmentsCreated.size());
		ReportAllPastEnrollmentMigration.addEnrollmentEvaluationsMigrated(this.enrollmentEvaluationsCreated.size());
		ReportAllPastEnrollmentMigration.addCurricularCoursesMigrated(super.curricularCoursesCreated.size());

		ReportAllPastEnrollmentMigration.report(super.out);
		super.out.close();
	}

	/**
	 * @param mwStudent
	 * @throws Throwable
	 */
	private void writeUpdateStudentEnrollments(MWStudent mwStudent) throws Throwable
	{
		IPersistentStudent persistentStudent = super.persistentSuport.getIPersistentStudent();

		// Read Fenix Student.
		IStudent student = persistentStudent.readStudentByNumberAndDegreeType(mwStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

		if (student == null)
		{
			// This can only happen if the Students/Persons migration was not runed before this one!
			super.out.println("[ERROR 202.1] Can't find Student in Fenix DB with number: [" + mwStudent.getNumber() + "]!");
			return;
		}

		IDegreeCurricularPlan degreeCurricularPlan = super.getDegreeCurricularPlan(mwStudent.getDegreecode());

		if (degreeCurricularPlan == null)
		{
			// This can only happen if the CreateAndUpdateAllPastCurriculums migration was not runed before this one!
			super.out.println("[ERROR 203.1] No record of Degree with code: [" + mwStudent.getDegreecode() + "]!");
			return;
		}

		IStudentCurricularPlan studentCurricularPlan = super.getStudentCurricularPlan(degreeCurricularPlan, student, mwStudent);

		if (studentCurricularPlan == null)
		{
			super.out.println(
				"[ERROR 204.1] Could not obtain StudentCurricularPlan for Student with number: ["
					+ mwStudent.getNumber()
					+ "] in Degree with code: ["
					+ mwStudent.getDegreecode()
					+ "]!");
			return;
		}

		this.writeUpdateEnrollments(mwStudent, studentCurricularPlan);
	}

	/**
	 * @param mwStudent
	 * @param studentCurricularPlan
	 * @throws Throwable
	 */
	private void writeUpdateEnrollments(MWStudent mwStudent, IStudentCurricularPlan studentCurricularPlan) throws Throwable
	{
		List mwEnrolments = mwStudent.getEnrolments();
		Iterator iterator = mwEnrolments.iterator();
		while (iterator.hasNext())
		{
			final MWEnrolment mwEnrolment = (MWEnrolment) iterator.next();

			IDegreeCurricularPlan degreeCurricularPlan = super.getDegreeCurricularPlan(mwEnrolment.getDegreecode());
			if (degreeCurricularPlan == null)
			{
				// This can only happen if the CreateAndUpdateAllPastCurriculums migration was not runed before this one!
				super.out.println("[ERROR 205.1] No record of Degree with code: [" + mwEnrolment.getDegreecode() + "]!");
				continue;
			}

			ICurricularCourse curricularCourse = super.getCurricularCourse(mwEnrolment, degreeCurricularPlan);

			if (curricularCourse == null)
			{
				super.out.println("[ERROR 206.1] Couldn't create CurricularCourse with code [" + mwEnrolment.getCoursecode() + "]!");
				ReportAllPastEnrollmentMigration.addUnknownCurricularCourse(mwEnrolment);
				continue;
			}

			IEnrolment enrolment = this.writeUpdateEnrollment(mwEnrolment, studentCurricularPlan, curricularCourse);

			if (enrolment == null)
			{
				super.out.println("[ERROR 207.1] Couldn't create nor update enrolment!");
				continue;
			}
		}
	}

	/**
	 * @param mwEnrolment
	 * @param studentCurricularPlan
	 * @param curricularCourse
	 * @return IEnrolment
	 * @throws Throwable
	 */
	private IEnrolment writeUpdateEnrollment(
		MWEnrolment mwEnrolment,
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse)
		throws Throwable
	{
		IEnrolment enrolment = this.getEnrollment(mwEnrolment, studentCurricularPlan, curricularCourse);
		this.writeUpdateEnrollmentEvaluation(mwEnrolment, enrolment);
		return enrolment;
	}

	/**
	 * @param mwEnrolment
	 * @param studentCurricularPlan
	 * @param curricularCourse
	 * @return IEnrolment
	 * @throws Throwable
	 */
	private IEnrolment getEnrollment(
		MWEnrolment mwEnrolment,
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse)
		throws Throwable
	{
		IPersistentEnrolment enrollmentDAO = super.persistentSuport.getIPersistentEnrolment();

		IExecutionPeriod executionPeriod = super.getExecutionPeriodForThisMWEnrolment(mwEnrolment);

		EnrolmentEvaluationType enrolmentEvaluationType = super.getEvaluationType(mwEnrolment);

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

		return enrolment;
	}

	/**
	 * @param mwEnrolment
	 * @param enrolment
	 * @throws Throwable
	 */
	private void writeUpdateEnrollmentEvaluation(MWEnrolment mwEnrolment, IEnrolment enrolment) throws Throwable
	{
		IPersistentEnrolmentEvaluation enrollmentEvaluationDAO = super.persistentSuport.getIPersistentEnrolmentEvaluation();

		EnrolmentEvaluationType enrolmentEvaluationType = super.getEvaluationType(mwEnrolment);

		Date whenAltered = this.getWhenAlteredDate(mwEnrolment);

		String grade = super.getAcurateGrade(mwEnrolment);

		IEnrolmentEvaluation enrolmentEvaluation =
			enrollmentEvaluationDAO.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
				enrolment,
				enrolmentEvaluationType,
				grade);

		if (enrolmentEvaluation == null)
		{
			IEnrolmentEvaluation enrolmentEvaluationToObtainKey = new EnrolmentEvaluation();
			enrolmentEvaluationToObtainKey.setEnrolment(enrolment);
			enrolmentEvaluationToObtainKey.setGrade(grade);
			enrolmentEvaluationToObtainKey.setEnrolmentEvaluationType(enrolmentEvaluationType);
			enrolmentEvaluationToObtainKey.setWhen(whenAltered);
			String key = super.getEnrollmentEvaluationKey(enrolmentEvaluationToObtainKey);

			enrolmentEvaluation = (IEnrolmentEvaluation) this.enrollmentEvaluationsCreated.get(key);

			if (enrolmentEvaluation == null)
			{
				enrolmentEvaluation = new EnrolmentEvaluation();
				enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);
				enrolmentEvaluation.setEnrolment(enrolment);
				enrolmentEvaluation.setEnrolmentEvaluationType(enrolmentEvaluationType);
				enrolmentEvaluation.setGrade(grade);
				enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
				this.setEnrollmentEvaluationValues(mwEnrolment, enrolment, whenAltered, enrolmentEvaluation);

				this.enrollmentEvaluationsCreated.put(key, enrolmentEvaluation);

				super.updateEnrollmentStateAndEvaluationType(enrolment, enrolmentEvaluation);

				super.writeTreatedMWEnrollment(mwEnrolment);
			} else
			{
				enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);
				this.setEnrollmentEvaluationValues(mwEnrolment, enrolment, whenAltered, enrolmentEvaluation);
				super.updateEnrollmentStateAndEvaluationType(enrolment, enrolmentEvaluation);
				super.writeTreatedMWEnrollment(mwEnrolment);
			}
		} else
		{
			enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);
			this.setEnrollmentEvaluationValues(mwEnrolment, enrolment, whenAltered, enrolmentEvaluation);
			super.updateEnrollmentStateAndEvaluationType(enrolment, enrolmentEvaluation);
			super.writeTreatedMWEnrollment(mwEnrolment);
		}
	}

	/**
	 * @param mwEnrolment
	 * @return Date
	 */
	private Date getWhenAlteredDate(MWEnrolment mwEnrolment)
	{
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
		return newDate;
	}

	/**
	 * @param mwEnrolment
	 * @param enrolment
	 * @param newDate
	 * @param enrolmentEvaluation
	 * @throws Throwable
	 */
	private void setEnrollmentEvaluationValues(
		MWEnrolment mwEnrolment,
		IEnrolment enrolment,
		Date newDate,
		IEnrolmentEvaluation enrolmentEvaluation)
		throws Throwable
	{
		enrolmentEvaluation.setExamDate(mwEnrolment.getExamdate());
		enrolmentEvaluation.setObservation(mwEnrolment.getRemarks());
		enrolmentEvaluation.setPersonResponsibleForGrade(super.getPersonResponsibleForGrade(mwEnrolment));
		enrolmentEvaluation.setGradeAvailableDate(mwEnrolment.getExamdate());
		enrolmentEvaluation.setWhen(newDate);
		enrolmentEvaluation.setEmployee(super.getEmployee(mwEnrolment));
		enrolmentEvaluation.setCheckSum(null);
		enrolmentEvaluation.setAckOptLock(new Integer(1));
	}

}