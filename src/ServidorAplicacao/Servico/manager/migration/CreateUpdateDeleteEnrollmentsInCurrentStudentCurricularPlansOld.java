package ServidorAplicacao.Servico.manager.migration;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWCurricularCourseOutsideStudentDegree;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWEnrolment;
import middleware.middlewareDomain.MWStudent;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourseOutsideStudentDegree;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWEnrolment;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.studentMigration.enrollments.ReportEnrolment;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.enrolment.DeleteEnrolment;
import ServidorAplicacao.Servico.enrolment.WriteEnrolment;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
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

public class CreateUpdateDeleteEnrollmentsInCurrentStudentCurricularPlansOld extends CreateUpdateDeleteEnrollments implements IService
{
	private boolean toWriteEnrollments;
	private HashMap enrollmentsCreated;
	private HashMap enrollmentEvaluationsCreated;
	private IExecutionPeriod executionPeriod;
	private int numberOfElementsInSpan;
	private int maximumNumberOfElementsToConsider;

	public CreateUpdateDeleteEnrollmentsInCurrentStudentCurricularPlansOld()
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
		this.enrollmentsCreated = new HashMap();
		this.enrollmentEvaluationsCreated = new HashMap();
		this.executionPeriod = null;
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

//			this.executionPeriod = super.persistentSuport.getIPersistentExecutionPeriod().readActualExecutionPeriod();
			Integer numberOfStudents = mwAlunoDAO.countAll();

			super.out.println("[INFO] Updating a total of [" + numberOfStudents.intValue() + "] student curriculums.");

			int numberOfSpans = numberOfStudents.intValue() / this.numberOfElementsInSpan;
			numberOfSpans = numberOfStudents.intValue() % this.numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
			int totalElementsConsidered = 0;

			for (int span = 0; span < numberOfSpans && totalElementsConsidered < this.maximumNumberOfElementsToConsider; span++)
			{
				super.out.println("[INFO] Reading MWStudents...");
//				List result = mwAlunoDAO.readAllBySpan(new Integer(span), new Integer(this.numberOfElementsInSpan));
//				super.out.println("[INFO] Updating [" + result.size() + "] student curriculums...");

//				Iterator iterator = result.iterator();

				Iterator iterator = mwAlunoDAO.readAllBySpanIterator(new Integer(span), new Integer(this.numberOfElementsInSpan));
				
				while (iterator.hasNext())
				{
//					mwStudent = (MWStudent) iterator.next();

					mwStudent = (MWStudent) mwAlunoDAO.lockIteratorNextObj(iterator);
					
					mwStudent.setEnrolments(mwEnrollmentDAO.readByStudentNumber(mwStudent.getNumber()));

					this.updateStudentEnrolment(mwStudent);

//					this.enrollmentsCreated.clear();
//					this.enrollmentEvaluationsCreated.clear();

					totalElementsConsidered++;
				}
			}
		} catch (Throwable e)
		{
			super.out.println("[ERROR 106] Exception migrating student [" + mwStudent.getNumber() + "] enrolments!");
			super.out.println("[ERROR 106] Number: [" + mwStudent.getNumber() + "]");
			super.out.println("[ERROR 106] Degree: [" + mwStudent.getDegreecode() + "]");
			super.out.println("[ERROR 106] Branch: [" + mwStudent.getBranchcode() + "]");
			e.fillInStackTrace();
			e.printStackTrace(super.out);
		}

		ReportEnrolment.report(super.out);
		super.out.close();
	}

	/**
	 * @param mwStudent
	 * @throws Throwable
	 */
	private void updateStudentEnrolment(MWStudent mwStudent) throws Throwable
	{
		IPersistentStudent studentDAO = super.persistentSuport.getIPersistentStudent();

		IStudent student = studentDAO.readStudentByNumberAndDegreeType(mwStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

		if (student == null)
		{
			super.out.println("[ERROR 101] Cannot find Fenix student with number [" + mwStudent.getNumber() + "]!");
			return;
		}

		IStudentCurricularPlan studentCurricularPlan =
			super.persistentSuport.getIStudentCurricularPlanPersistente().readActiveByStudentNumberAndDegreeType(
				student.getNumber(),
				TipoCurso.LICENCIATURA_OBJ);

		if (studentCurricularPlan == null)
		{
			super.out.println(
				"[ERROR 102] Cannot find Fenix StudentCurricularPlan for student number [" + mwStudent.getNumber() + "]!");
			return;
		}

		List studentEnrolments = mwStudent.getEnrolments();

		this.writeEnrolments(studentEnrolments, studentCurricularPlan, mwStudent);
	}

	/**
	 * @param enrolments2Write
	 * @param studentCurricularPlan
	 * @param mwStudent
	 * @throws Throwable
	 */
	private void writeEnrolments(List enrolments2Write, IStudentCurricularPlan studentCurricularPlan, MWStudent mwStudent)
		throws Throwable
	{
		Iterator iterator = enrolments2Write.iterator();
		while (iterator.hasNext())
		{
			MWEnrolment mwEnrolment = (MWEnrolment) iterator.next();

			this.executionPeriod = super.getExecutionPeriodForThisMWEnrolment(mwEnrolment);
			
			IDegreeCurricularPlan degreeCurricularPlan =
				this.getDegreeCurricularPlan(mwEnrolment.getDegreecode(), studentCurricularPlan);

			if (degreeCurricularPlan == null)
			{
				super.out.println("[ERROR 103] Degree Curricular Plan Not Found!");
				continue;
			}

			ICurricularCourse curricularCourse = this.getCurricularCourse(mwEnrolment, degreeCurricularPlan, true);

			if (curricularCourse == null)
			{
				continue;
			}

			IExecutionPeriod currentExecutionPeriod =
				super.persistentSuport.getIPersistentExecutionPeriod().readActualExecutionPeriod();

			if (this.executionPeriod.equals(currentExecutionPeriod) && this.toWriteEnrollments)
			{
				if (!this.hasExecutionInGivenPeriod(curricularCourse, this.executionPeriod))
				{
					ReportEnrolment.addExecutionCourseNotFound(
							mwEnrolment.getCoursecode(),
							mwEnrolment.getDegreecode().toString(),
							mwEnrolment.getNumber().toString());
					continue;
				}
			}

			IEnrolment enrolment = this.createEnrolment(mwEnrolment, studentCurricularPlan, curricularCourse);

			if (this.executionPeriod.equals(currentExecutionPeriod) && this.toWriteEnrollments)
			{
				// Update the corresponding Fenix Attend if it exists.
				IFrequenta attend = this.updateAttend(curricularCourse, enrolment, mwEnrolment);
				if (attend == null)
				{
					// NOTE [DAVID]: This kind of report is only pesented the first time the migration
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
	 * @param studentCurricularPlan
	 * @param curricularCourse
	 * @return IEnrolment
	 * @throws Throwable
	 */
	private IEnrolment createEnrolment(
		MWEnrolment mwEnrolment,
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse)
		throws Throwable
	{
		IEnrolment enrolment = this.createAndUpdateEnrolment(mwEnrolment, studentCurricularPlan, curricularCourse);
		this.createAndUpdateEnrolmentEvaluation(mwEnrolment, enrolment);
		return enrolment;
	}

	/**
	 * @param mwEnrolment
	 * @param studentCurricularPlan
	 * @param curricularCourse
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
	private void createAndUpdateEnrolmentEvaluation(MWEnrolment mwEnrolment, IEnrolment enrolment) throws Throwable
	{
		IPersistentEnrolmentEvaluation enrollmentEvaluationDAO = super.persistentSuport.getIPersistentEnrolmentEvaluation();
		IPersistentEnrolment enrollmentDAO = super.persistentSuport.getIPersistentEnrolment();

		if (((mwEnrolment.getGrade() == null) || (mwEnrolment.getGrade().equals(""))) && this.toWriteEnrollments)
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
			enrolmentEvaluation.setAckOptLock(new Integer(1));
			
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

			IEnrolmentEvaluation enrolmentEvaluationToObtainKey = new EnrolmentEvaluation();
			enrolmentEvaluationToObtainKey.setEnrolment(enrolment);
			enrolmentEvaluationToObtainKey.setGrade(grade);
			enrolmentEvaluationToObtainKey.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
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
					enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
					enrolmentEvaluation.setGrade(grade);

					this.updateEnrollmentEvaluation(mwEnrolment, enrolment, whenAltered, enrolmentEvaluation);

					this.enrollmentEvaluationsCreated.put(key, enrolmentEvaluation);

					super.updateEnrollmentStateAndEvaluationType(enrolment, enrolmentEvaluation);

					ReportEnrolment.addEnrolmentEvaluationMigrated();

					super.writeTreatedMWEnrollment(mwEnrolment);

				} else
				{
					if (this.toWriteEnrollments)
					{
						enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);

						this.updateEnrollmentEvaluation(mwEnrolment, enrolment, whenAltered, enrolmentEvaluation);
						
						super.updateEnrollmentStateAndEvaluationType(enrolment, enrolmentEvaluation);

						super.writeTreatedMWEnrollment(mwEnrolment);
					}
				}
			} else
			{
				if (this.toWriteEnrollments)
				{
					enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);

					this.updateEnrollmentEvaluation(mwEnrolment, enrolment, whenAltered, enrolmentEvaluation);
					
					super.updateEnrollmentStateAndEvaluationType(enrolment, enrolmentEvaluation);

					super.writeTreatedMWEnrollment(mwEnrolment);
				} else
				{
					if (enrolment.getEvaluations().size() == 1)
					{
						enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);
						enrollmentEvaluationDAO.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());

						ReportEnrolment.addEnrolmentEvaluationDeleted();
						
						enrollmentDAO.simpleLockWrite(enrolment);
						enrollmentDAO.deleteByOID(Enrolment.class, enrolment.getIdInternal());
						
						this.cleanEnrollmentRelations(enrolment);
						
						ReportEnrolment.addEnrolmentDeleted();

						super.writeTreatedMWEnrollment(mwEnrolment);
					} else if (enrolment.getEvaluations().size() > 1)
					{
						enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);
						enrollmentEvaluationDAO.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());

						ReportEnrolment.addEnrolmentEvaluationDeleted();

						super.writeTreatedMWEnrollment(mwEnrolment);
					}
				}
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
	private void updateEnrollmentEvaluation(
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
		enrolmentEvaluation.setAckOptLock(new Integer(1));
	}

	/**
	 * @param enrolment
	 * @throws Throwable
	 */
	private void cleanEnrollmentRelations(IEnrolment enrolment) throws Throwable
	{
		DeleteEnrolment.deleteAttend(enrolment);
	}

	/**
	 * @param mwEnrolment
	 * @param degreeCurricularPlan
	 * @return ICurricularCourse
	 * @throws Throwable
	 */
	private ICurricularCourse getCurricularCourseFromAnotherDegree(
		final MWEnrolment mwEnrolment,
		IDegreeCurricularPlan degreeCurricularPlan)
		throws Throwable
	{
		ICurricularCourse curricularCourse = null;

		IPersistentMWCurricularCourseOutsideStudentDegree persistentMWCurricularCourseOutsideStudentDegree =
			super.persistentMiddlewareSuport.getIPersistentMWCurricularCourseOutsideStudentDegree();

		// First of all we look in the MWCurricularCourseOutsideStudentDegree table to see if there is
		// already a
		// correspondence between this CurricularCourse and this Degree.
		MWCurricularCourseOutsideStudentDegree mwCurricularCourseOutsideStudentDegree =
			persistentMWCurricularCourseOutsideStudentDegree.readByCourseCodeAndDegreeCode(
				mwEnrolment.getCoursecode(),
				mwEnrolment.getDegreecode());
		if (mwCurricularCourseOutsideStudentDegree != null)
		{
			curricularCourse = mwCurricularCourseOutsideStudentDegree.getCurricularCourse();

			// If there is no correspondence yet, let us look if this CurricularCourse has only one
			// ExecuitonCourse in the given period.
		} else if (this.curricularCourseHasOnlyOneExecutionInGivenPeriod(this.executionPeriod, mwEnrolment, degreeCurricularPlan))
		{

			// If there is only one ExecutionCourse for all the existing CurricularCourses then we can
			// choose
			// any CurricularCourse but this choice is registred in table
			// MWCurricularCourseOutsideStudentDegree
			// to keep coherence of choice and to make the next similar choice quicker.
			IPersistentCurricularCourse persistentCurricularCourse = super.persistentSuport.getIPersistentCurricularCourse();
			List curricularCourses =
				persistentCurricularCourse.readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(
					StringUtils.trim(mwEnrolment.getCoursecode()),
					degreeCurricularPlan.getDegree().getTipoCurso(),
					degreeCurricularPlan.getState());
			curricularCourse = (ICurricularCourse) curricularCourses.get(0);

			MWCurricularCourseOutsideStudentDegree mwCurricularCourseOutsideStudentDegreeToWrite =
				new MWCurricularCourseOutsideStudentDegree();
			persistentMWCurricularCourseOutsideStudentDegree.simpleLockWrite(mwCurricularCourseOutsideStudentDegreeToWrite);
			mwCurricularCourseOutsideStudentDegreeToWrite.setCourseCode(mwEnrolment.getCoursecode());
			mwCurricularCourseOutsideStudentDegreeToWrite.setDegreeCode(mwEnrolment.getDegreecode());
			mwCurricularCourseOutsideStudentDegreeToWrite.setCurricularCourse(curricularCourse);

		} else
		{

			IFrequentaPersistente attendDAO = super.persistentSuport.getIFrequentaPersistente();
			List attendList = attendDAO.readByStudentNumberInCurrentExecutionPeriod(mwEnrolment.getNumber());
			List attendsWithCurricularCourseCode = (List) CollectionUtils.select(attendList, new Predicate()
			{
				public boolean evaluate(Object input)
				{
					IFrequenta attend = (IFrequenta) input;

					String courseCode = mwEnrolment.getCoursecode();

					List associatedCurricularCourses = attend.getDisciplinaExecucao().getAssociatedCurricularCourses();
					Iterator iterator = associatedCurricularCourses.iterator();
					while (iterator.hasNext())
					{
						ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
						if (curricularCourse.getCode().equals(courseCode))
						{
							return true;
						}
					}
					return false;
				}
			});

			if (attendsWithCurricularCourseCode.size() > 0)
			{
				List associatedCurricularCourses =
					((IFrequenta) attendsWithCurricularCourseCode.get(0)).getDisciplinaExecucao().getAssociatedCurricularCourses();

				curricularCourse = (ICurricularCourse) associatedCurricularCourses.get(0);

				MWCurricularCourseOutsideStudentDegree mwCurricularCourseOutsideStudentDegreeToWrite =
					new MWCurricularCourseOutsideStudentDegree();
				persistentMWCurricularCourseOutsideStudentDegree.simpleLockWrite(mwCurricularCourseOutsideStudentDegreeToWrite);
				mwCurricularCourseOutsideStudentDegreeToWrite.setCourseCode(mwEnrolment.getCoursecode());
				mwCurricularCourseOutsideStudentDegreeToWrite.setDegreeCode(mwEnrolment.getDegreecode());
				mwCurricularCourseOutsideStudentDegreeToWrite.setCurricularCourse(curricularCourse);

			} else
			{
				ReportEnrolment.addFoundCurricularCourseInOtherDegrees(
					mwEnrolment.getCoursecode(),
					mwEnrolment.getDegreecode().toString(),
					mwEnrolment.getNumber().toString());
			}
		}

		return curricularCourse;
	}

	/**
	 * @param curricularCourses
	 * @return true/false
	 */
	private boolean hasDiferentDegrees(List curricularCourses)
	{
		int numberOfDegrees = CollectionUtils.getCardinalityMap(curricularCourses).size();
		return (numberOfDegrees > 1);
	}

	/**
	 * @param degreeCode
	 * @param studentCurricularPlan
	 * @throws Throwable
	 */
	private IDegreeCurricularPlan getDegreeCurricularPlan(Integer degreeCode, IStudentCurricularPlan studentCurricularPlan)
		throws Throwable
	{
		IPersistentMWDegreeTranslation persistentMWDegreeTranslation =
			super.persistentMiddlewareSuport.getIPersistentMWDegreeTranslation();

		MWDegreeTranslation mwDegreeTranslation = persistentMWDegreeTranslation.readByDegreeCode(degreeCode);

		ICursoExecucao executionDegree =
			super.persistentSuport.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYearAndDegreeType(
				mwDegreeTranslation.getDegree().getNome(),
				this.executionPeriod.getExecutionYear(),
				TipoCurso.LICENCIATURA_OBJ);

		if (executionDegree == null)
		{
			super.out.println("[ERROR 104] the degree has no execution in [" + this.executionPeriod.getExecutionYear().getYear() + "]!");
			return null;
		} else
		{
			if (!studentCurricularPlan.getDegreeCurricularPlan().equals(executionDegree.getCurricularPlan()))
			{
				super.out.println(
					"[INFO] the student [" + studentCurricularPlan.getStudent().getNumber() + "] has changed his degree!");
				return studentCurricularPlan.getDegreeCurricularPlan();
			} else
			{
				return executionDegree.getCurricularPlan();
			}
		}
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
	 * @param executionPeriod
	 * @param mwEnrolment
	 * @param degreeCurricularPlan
	 * @return true/false
	 * @throws Throwable
	 */
	private boolean curricularCourseHasOnlyOneExecutionInGivenPeriod(
		IExecutionPeriod executionPeriod,
		MWEnrolment mwEnrolment,
		IDegreeCurricularPlan degreeCurricularPlan)
		throws Throwable
	{
		IPersistentCurricularCourse persistentCurricularCourse = super.persistentSuport.getIPersistentCurricularCourse();

		List curricularCourses =
			persistentCurricularCourse.readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(
				StringUtils.trim(mwEnrolment.getCoursecode()),
				degreeCurricularPlan.getDegree().getTipoCurso(),
				degreeCurricularPlan.getState());

		List executionCourses = new ArrayList();

		Iterator iterator1 = curricularCourses.iterator();
		while (iterator1.hasNext())
		{
			ICurricularCourse curricularCourse = (ICurricularCourse) iterator1.next();
			List associatedExecutionCourses = curricularCourse.getAssociatedExecutionCourses();
			Iterator iterator2 = associatedExecutionCourses.iterator();
			while (iterator2.hasNext())
			{
				IExecutionCourse executionCourse = (IExecutionCourse) iterator2.next();
				if (executionCourse.getExecutionPeriod().equals(executionPeriod))
				{
					if (!executionCourses.contains(executionCourse))
					{
						executionCourses.add(executionCourse);
					}
				}
			}
		}

		if (executionCourses.size() == 1)
		{
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * @param mwEnrolment
	 * @param degreeCurricularPlan
	 * @param solveSomeProblems
	 * @return ICurricularCourse
	 * @throws Throwable
	 */
	private ICurricularCourse getCurricularCourse(
		MWEnrolment mwEnrolment,
		IDegreeCurricularPlan degreeCurricularPlan,
		boolean solveSomeProblems)
		throws Throwable
	{
		String courseCode = null;
		if (solveSomeProblems)
		{
			courseCode = this.getRealCurricularCourseCodeForCodesAZx(mwEnrolment);
		} else
		{
			courseCode = mwEnrolment.getCoursecode();
		}

		List curricularCourses =
			super.persistentSuport.getIPersistentCurricularCourse().readbyCourseCodeAndDegreeCurricularPlan(
				StringUtils.trim(courseCode),
				degreeCurricularPlan);

		ICurricularCourse curricularCourse = null;

		// Ideally we find only one CurricularCourse but we never know, we may find more or even less.
		if (curricularCourses.size() != 1)
		{
			if (curricularCourses.size() > 1)
			{
				super.out.println(
					"[ERROR 105] Several Fenix CurricularCourses with code ["
						+ courseCode
						+ "] were found for Degree ["
						+ mwEnrolment.getDegreecode()
						+ "]!");
				return null;
			} else // size == 0
				{
				// We did not find any CurricularCourse with that code in that DegreeCurricularPlan.
				// Now we try to read by the CurricularCourse code only.
				IPersistentCurricularCourse curricularCourseDAO = super.persistentSuport.getIPersistentCurricularCourse();
				curricularCourses =
					curricularCourseDAO.readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(
						StringUtils.trim(courseCode),
						degreeCurricularPlan.getDegree().getTipoCurso(),
						degreeCurricularPlan.getState());

				if (curricularCourses.size() == 1)
				{
					curricularCourse = (ICurricularCourse) curricularCourses.get(0);
				} else if (curricularCourses.size() > 1)
				{
					if (this.hasDiferentDegrees(curricularCourses))
					{
						curricularCourse = this.getCurricularCourseFromAnotherDegree(mwEnrolment, degreeCurricularPlan);
						if (curricularCourse == null)
						{
							// NOTE [DAVID]: This is for information only.
							ReportEnrolment.addReplicatedCurricularCourses(courseCode, curricularCourses);
							return null;
						}
					} else
					{
						curricularCourse = (ICurricularCourse) curricularCourses.get(0);
					}
				} else
				{
					ReportEnrolment.addCurricularCourseNotFound(
						courseCode,
						mwEnrolment.getDegreecode().toString(),
						mwEnrolment.getNumber().toString());
					return null;
				}
			}
		} else // curricularCourses.size() == 1
			{
			curricularCourse = (ICurricularCourse) curricularCourses.get(0);
		}
		return curricularCourse;
	}

	// -----------------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------------
	// ------------------------------- METHODS TO SOLVE SPECIFIC PROBLEMS ----------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param mwEnrolment
	 * @return String
	 */
	private String getRealCurricularCourseCodeForCodesAZx(MWEnrolment mwEnrolment)
	{
		if (mwEnrolment.getCoursecode().equals("AZ2") && mwEnrolment.getCurricularcoursesemester().intValue() == 2)
		{
			return "QN";
		} else if (mwEnrolment.getCoursecode().equals("AZ3") && mwEnrolment.getCurricularcoursesemester().intValue() == 2)
		{
			return "PY";
		} else if (mwEnrolment.getCoursecode().equals("AZ4") && mwEnrolment.getCurricularcoursesemester().intValue() == 1)
		{
			return "P5";
		} else if (mwEnrolment.getCoursecode().equals("AZ5") && mwEnrolment.getCurricularcoursesemester().intValue() == 2)
		{
			return "UN";
		} else if (mwEnrolment.getCoursecode().equals("AZ6") && mwEnrolment.getCurricularcoursesemester().intValue() == 1)
		{
			return "U8";
		} else
		{
			return mwEnrolment.getCoursecode();
		}
	}

}