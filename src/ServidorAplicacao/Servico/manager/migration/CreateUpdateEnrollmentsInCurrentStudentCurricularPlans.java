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
import middleware.studentMigration.enrollments.ReportEnrolment;
import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.enrolment.WriteEnrolment;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author David Santos in Feb 7, 2004
 */

public class CreateUpdateEnrollmentsInCurrentStudentCurricularPlans
	extends CreateUpdateDeleteEnrollmentsInCurrentStudentCurricularPlans
	implements IService
{
	private HashMap enrollmentsCreated;
	private HashMap enrollmentEvaluationsCreated;

	public CreateUpdateEnrollmentsInCurrentStudentCurricularPlans()
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
		super.executionPeriod = null;
		super.out = null;
		super.numberOfElementsInSpan = 150;
		super.maximumNumberOfElementsToConsider = 10000;
	}

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
			super.out.println("[INFO] Updating a total of [" + numberOfStudents.intValue() + "] student curriculums.");

			int numberOfSpans = numberOfStudents.intValue() / super.numberOfElementsInSpan;
			numberOfSpans = numberOfStudents.intValue() % super.numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
			int totalElementsConsidered = 0;

			for (int span = 0; span < numberOfSpans && totalElementsConsidered < super.maximumNumberOfElementsToConsider; span++)
			{
				super.out.println("[INFO] Reading MWStudents...");

				Iterator iterator = mwAlunoDAO.readAllBySpanIterator(new Integer(span), new Integer(super.numberOfElementsInSpan));

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
			super.out.println("[ERROR 101.1] Exception migrating student [" + mwStudent.getNumber() + "] enrolments!");
			super.out.println("[ERROR 101.1] Number: [" + mwStudent.getNumber() + "]");
			super.out.println("[ERROR 101.1] Degree: [" + mwStudent.getDegreecode() + "]");
			super.out.println("[ERROR 101.1] Branch: [" + mwStudent.getBranchcode() + "]");
			e.printStackTrace(super.out);
			throw new FenixServiceException(e);
		}

		WriteEnrolment.resetAttends();
		ReportEnrolment.report(super.out);
		super.out.close();
	}

	/**
	 * @param mwStudent
	 * @throws Throwable
	 */
	private void writeUpdateStudentEnrollments(MWStudent mwStudent) throws Throwable
	{
		IPersistentStudent studentDAO = super.persistentSuport.getIPersistentStudent();

		IStudent student = studentDAO.readStudentByNumberAndDegreeType(mwStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

		if (student == null)
		{
			super.out.println("[ERROR 102.1] Cannot find Fenix student with number [" + mwStudent.getNumber() + "]!");
			return;
		}

		IStudentCurricularPlan studentCurricularPlan =
			super.persistentSuport.getIStudentCurricularPlanPersistente().readActiveByStudentNumberAndDegreeType(
				student.getNumber(),
				TipoCurso.LICENCIATURA_OBJ);

		if (studentCurricularPlan == null)
		{
			super.out.println(
				"[ERROR 103.1] Cannot find Fenix StudentCurricularPlan for student number [" + mwStudent.getNumber() + "]!");
			return;
		}

		List studentEnrolments = mwStudent.getEnrolments();

		this.writeUpdateEnrollments(studentEnrolments, studentCurricularPlan, mwStudent);
	}

	/**
	 * @param enrolments2Write
	 * @param studentCurricularPlan
	 * @param mwStudent
	 * @throws Throwable
	 */
	private void writeUpdateEnrollments(List enrolments2Write, IStudentCurricularPlan studentCurricularPlan, MWStudent mwStudent)
		throws Throwable
	{
		Iterator iterator = enrolments2Write.iterator();
		while (iterator.hasNext())
		{
			MWEnrolment mwEnrolment = (MWEnrolment) iterator.next();

			super.executionPeriod = super.getExecutionPeriodForThisMWEnrolment(mwEnrolment);

			IDegreeCurricularPlan degreeCurricularPlan =
				super.getDegreeCurricularPlan(mwEnrolment.getDegreecode(), studentCurricularPlan);

			if (degreeCurricularPlan == null)
			{
				super.out.println("[ERROR 104.1] Degree Curricular Plan Not Found!");
				continue;
			}

			ICurricularCourse curricularCourse = super.getCurricularCourse(mwEnrolment, degreeCurricularPlan, true);

			if (curricularCourse == null)
			{
				curricularCourse = super.getCurricularCourse(mwEnrolment, studentCurricularPlan.getDegreeCurricularPlan(), true);

				if (curricularCourse == null)
				{
					continue;
				}
			}

			IExecutionPeriod currentExecutionPeriod =
				super.persistentSuport.getIPersistentExecutionPeriod().readActualExecutionPeriod();

			if (super.executionPeriod.equals(currentExecutionPeriod))
			{
				if (!this.hasExecutionInGivenPeriod(curricularCourse, super.executionPeriod))
				{
					ReportEnrolment.addExecutionCourseNotFound(
						mwEnrolment.getCoursecode(),
						mwEnrolment.getDegreecode().toString(),
						mwEnrolment.getNumber().toString());
//					continue;
				}
			}

			IEnrolment enrolment = this.writeUpdateEnrollment(mwEnrolment, studentCurricularPlan, curricularCourse);

			if (super.executionPeriod.equals(currentExecutionPeriod))
			{
				// Update the corresponding Fenix Attend if it exists.
				IFrequenta attend = this.updateAttend(curricularCourse, enrolment, mwEnrolment);
				if (attend == null)
				{
					// NOTE [DAVID]: This kind of report is only presented the first time the migration
					// process is executed.
					// This happens because although this is a situation of error report, this kind of error
					// doesn't forbid the enrolment to be created in the Fenix DB. Thus the second time the process
					// is executed, the enrolment for this particular CurricularCourse will have already been created in the
					// DB so this CurricularCourse is no longer considered for this execution of the process.
					// This is for information only.
					ReportEnrolment.addAttendNotFound(
						mwEnrolment.getCoursecode(),
						mwEnrolment.getDegreecode().toString(),
						mwEnrolment.getNumber().toString());
					this.createAttend(curricularCourse, enrolment, mwEnrolment);
				}
			}
		}
	}

	/**
	 * @param curricularCourse
	 * @param executionPeriod
	 * @param mwEnrolment
	 * @return true/false
	 * @throws Throwable
	 */
	private boolean hasExecutionInGivenPeriod(ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) throws Throwable
	{
		IExecutionCourse executionCourse =
			super.persistentSuport.getIPersistentExecutionCourse().readbyCurricularCourseAndExecutionPeriod(
				curricularCourse,
				executionPeriod);
		if (executionCourse == null)
		{
			return false;
		} else
		{
			return true;
		}
	}

	/**
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

		IEnrolment enrolment =
			enrollmentDAO.readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
				studentCurricularPlan,
				curricularCourse,
				this.executionPeriod);

		if (enrolment == null)
		{
			IEnrolment enrolmentToObtainKey = new Enrolment();
			enrolmentToObtainKey.setStudentCurricularPlan(studentCurricularPlan);
			enrolmentToObtainKey.setCurricularCourse(curricularCourse);
			enrolmentToObtainKey.setExecutionPeriod(this.executionPeriod);
			String key = super.getEnrollmentKey(enrolmentToObtainKey);

			enrolment = (IEnrolment) this.enrollmentsCreated.get(key);

			if (enrolment == null)
			{
				enrolment = new Enrolment();
				enrollmentDAO.simpleLockWrite(enrolment);
				enrolment.setCurricularCourse(curricularCourse);
				enrolment.setExecutionPeriod(this.executionPeriod);
				enrolment.setStudentCurricularPlan(studentCurricularPlan);
				enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);

				EnrolmentState enrolmentState = null;
				if ((mwEnrolment.getGrade() == null) || (mwEnrolment.getGrade().equals("")))
				{
					enrolmentState = EnrolmentState.ENROLED;
				} else
				{
					enrolmentState = super.getEnrollmentStateByGrade(mwEnrolment);
				}
				enrolment.setEnrolmentState(enrolmentState);

				this.enrollmentsCreated.put(key, enrolment);
				ReportEnrolment.addEnrolmentMigrated();
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

		IExecutionPeriod currentExecutionPeriod = super.persistentSuport.getIPersistentExecutionPeriod().readActualExecutionPeriod();

		if (((mwEnrolment.getGrade() == null) || (mwEnrolment.getGrade().equals("")))
			&& super.executionPeriod.equals(currentExecutionPeriod))
		{
			IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
			enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);
			enrolmentEvaluation.setCheckSum(null);
			enrolmentEvaluation.setEmployee(null);
			enrolmentEvaluation.setEnrolment(enrolment);
			enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
			enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
			enrolmentEvaluation.setExamDate(null);
			enrolmentEvaluation.setGrade(null);
			enrolmentEvaluation.setGradeAvailableDate(null);
			enrolmentEvaluation.setObservation(null);
			enrolmentEvaluation.setPersonResponsibleForGrade(null);
			enrolmentEvaluation.setWhen(null);

			ReportEnrolment.addEnrolmentEvaluationMigrated();
			super.writeTreatedMWEnrollment(mwEnrolment);
		} else
		{
			Date whenAltered = this.getWhenAlteredDate(mwEnrolment);
			String grade = super.getAcurateGrade(mwEnrolment);

			IEnrolmentEvaluation enrolmentEvaluation =
				enrollmentEvaluationDAO.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
					enrolment,
					EnrolmentEvaluationType.NORMAL_OBJ,
					grade);

			if (enrolmentEvaluation == null)
			{
				IEnrolmentEvaluation enrolmentEvaluationToObtainKey = new EnrolmentEvaluation();
				enrolmentEvaluationToObtainKey.setEnrolment(enrolment);
				enrolmentEvaluationToObtainKey.setGrade(grade);
				enrolmentEvaluationToObtainKey.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
				enrolmentEvaluationToObtainKey.setWhen(whenAltered);
				String key = super.getEnrollmentEvaluationKey(enrolmentEvaluationToObtainKey);

				enrolmentEvaluation = (IEnrolmentEvaluation) this.enrollmentEvaluationsCreated.get(key);

				if (enrolmentEvaluation == null)
				{
					enrolmentEvaluation = new EnrolmentEvaluation();
					enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);
					enrolmentEvaluation.setEnrolment(enrolment);
					enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
					enrolmentEvaluation.setGrade(grade);

					this.setEnrollmentEvaluationValues(mwEnrolment, enrolment, whenAltered, enrolmentEvaluation);

					this.enrollmentEvaluationsCreated.put(key, enrolmentEvaluation);

					super.updateEnrollmentStateAndEvaluationType(enrolment, enrolmentEvaluation);

					ReportEnrolment.addEnrolmentEvaluationMigrated();
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
		if (enrolment.getEnrolmentState().equals(EnrolmentState.APROVED))
		{
			enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
		} else
		{
			enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
		}
		enrolmentEvaluation.setExamDate(mwEnrolment.getExamdate());
		enrolmentEvaluation.setObservation(mwEnrolment.getRemarks());
		enrolmentEvaluation.setPersonResponsibleForGrade(super.getPersonResponsibleForGrade(mwEnrolment));
		enrolmentEvaluation.setGradeAvailableDate(mwEnrolment.getExamdate());
		enrolmentEvaluation.setWhen(newDate);
		enrolmentEvaluation.setEmployee(super.getEmployee(mwEnrolment));
		enrolmentEvaluation.setCheckSum(null);
	}

	/**
	 * @param curricularCourse
	 * @param enrolment
	 * @param mwEnrolment
	 * @return IFrequenta
	 * @throws Throwable
	 */
	private IFrequenta updateAttend(ICurricularCourse curricularCourse, IEnrolment enrolment, MWEnrolment mwEnrolment)
		throws Throwable
	{
		IExecutionCourse executionCourse =
			super.persistentSuport.getIPersistentExecutionCourse().readbyCurricularCourseAndExecutionPeriod(
				curricularCourse,
				this.executionPeriod);

		IFrequenta attend = null;

		if (executionCourse == null)
		{
			// NOTE [DAVID]: This should not happen at this point.
			ReportEnrolment.addExecutionCourseNotFound(
				mwEnrolment.getCoursecode(),
				mwEnrolment.getDegreecode().toString(),
				mwEnrolment.getNumber().toString());
		} else
		{
			IStudent student =
				super.persistentSuport.getIPersistentStudent().readStudentByNumberAndDegreeType(
					mwEnrolment.getNumber(),
					TipoCurso.LICENCIATURA_OBJ);
			attend = super.persistentSuport.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(student, executionCourse);

			if (attend != null)
			{
				super.persistentSuport.getIFrequentaPersistente().simpleLockWrite(attend);
				attend.setEnrolment(enrolment);
			}

		}
		return attend;
	}

	/**
	 * @param curricularCourse
	 * @param enrolment
	 * @param mwEnrolment
	 * @throws Throwable
	 */
	private void createAttend(ICurricularCourse curricularCourse, IEnrolment enrolment, MWEnrolment mwEnrolment) throws Throwable
	{
		IExecutionCourse executionCourse =
			super.persistentSuport.getIPersistentExecutionCourse().readbyCurricularCourseAndExecutionPeriod(
				curricularCourse,
				this.executionPeriod);

		if (executionCourse == null)
		{
			// NOTE [DAVID]: This error report can be added here even if it was added before in the
			// updateAttend() method
			// because this addition wont repeat same occurrences. This should not happen at this point.
			ReportEnrolment.addExecutionCourseNotFound(
				mwEnrolment.getCoursecode(),
				mwEnrolment.getDegreecode().toString(),
				mwEnrolment.getNumber().toString());
		} else
		{
			IStudent student =
				super.persistentSuport.getIPersistentStudent().readStudentByNumberAndDegreeType(
					mwEnrolment.getNumber(),
					TipoCurso.LICENCIATURA_OBJ);
			WriteEnrolment.createAttend(student, curricularCourse, this.executionPeriod, enrolment);
			// NOTE [DAVID]: This is for information only.
			ReportEnrolment.addCreatedAttend(
				mwEnrolment.getCoursecode(),
				mwEnrolment.getDegreecode().toString(),
				mwEnrolment.getNumber().toString());
		}
	}

}