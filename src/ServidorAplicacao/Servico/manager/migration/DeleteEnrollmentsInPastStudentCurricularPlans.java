package ServidorAplicacao.Servico.manager.migration;

import java.io.FileOutputStream;
import java.io.PrintWriter;
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
import Util.EnrolmentEvaluationType;
import Util.TipoCurso;

/**
 * @author David Santos in Feb 7, 2004
 */

public class DeleteEnrollmentsInPastStudentCurricularPlans
	extends CreateUpdateDeleteEnrollmentsInPastStudentCurricularPlans
	implements IService
{
	public DeleteEnrollmentsInPastStudentCurricularPlans()
	{
		try
		{
			super.persistentSuport = SuportePersistenteOJB.getInstance();
			super.persistentMiddlewareSuport = PersistentMiddlewareSupportOJB.getInstance();
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace(System.out);
		}

		super.studentCurricularPlansCreated = new HashMap();
		super.curricularCoursesCreated = new HashMap();
		super.out = null;
		super.numberOfElementsInSpan = 50;
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

			super.out.println("[INFO] Total number of student curriculums to update [" + numberOfStudents + "].");

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

					this.deleteStudentEnrollments(mwStudent);

					totalElementsConsidered++;
				}
			}
		} catch (Throwable e)
		{
			super.out.println("[ERROR 201.2] Exception migrating student [" + mwStudent.getNumber() + "] enrolments!");
			super.out.println("[ERROR 201.2] Number: [" + mwStudent.getNumber() + "]");
			super.out.println("[ERROR 201.2] Degree: [" + mwStudent.getDegreecode() + "]");
			super.out.println("[ERROR 201.2] Branch: [" + mwStudent.getBranchcode() + "]");
			e.fillInStackTrace();
			e.printStackTrace(super.out);
			throw new FenixServiceException(e);
		}

		ReportAllPastEnrollmentMigration.report(super.out);
		super.out.close();
	}

	/**
	 * @param mwStudent
	 * @throws Throwable
	 */
	private void deleteStudentEnrollments(MWStudent mwStudent) throws Throwable
	{
		IPersistentStudent persistentStudent = super.persistentSuport.getIPersistentStudent();

		// Read Fenix Student.
		IStudent student = persistentStudent.readStudentByNumberAndDegreeType(mwStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

		if (student == null)
		{
			// This can only happen if the Students/Persons migration was not runed before this one!
			super.out.println("[ERROR 202.2] Can't find Student in Fenix DB with number: [" + mwStudent.getNumber() + "]!");
			return;
		}

		IDegreeCurricularPlan degreeCurricularPlan = super.getDegreeCurricularPlan(mwStudent.getDegreecode());

		if (degreeCurricularPlan == null)
		{
			// This can only happen if the CreateAndUpdateAllPastCurriculums migration was not runed before this one!
			super.out.println("[ERROR 203.2] No record of Degree with code: [" + mwStudent.getDegreecode() + "]!");
			return;
		}

		IStudentCurricularPlan studentCurricularPlan = super.getStudentCurricularPlan(degreeCurricularPlan, student, mwStudent);

		if (studentCurricularPlan == null)
		{
			super.out.println(
				"[ERROR 204.2] Could not obtain StudentCurricularPlan for Student with number: ["
					+ mwStudent.getNumber()
					+ "] in Degree with code: ["
					+ mwStudent.getDegreecode()
					+ "]!");
			return;
		}

		this.deleteEnrollments(mwStudent, studentCurricularPlan);
	}

	/**
	 * @param mwStudent
	 * @param studentCurricularPlan
	 * @throws Throwable
	 */
	private void deleteEnrollments(MWStudent mwStudent, IStudentCurricularPlan studentCurricularPlan) throws Throwable
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
				super.out.println("[ERROR 205.2] No record of Degree with code: [" + mwEnrolment.getDegreecode() + "]!");
				return;
			}

			ICurricularCourse curricularCourse = super.getCurricularCourse(mwEnrolment, degreeCurricularPlan);

			if (curricularCourse == null)
			{
				super.out.println("[ERROR 206.2] Couldn't create CurricularCourse with code [" + mwEnrolment.getCoursecode() + "]!");
				ReportAllPastEnrollmentMigration.addUnknownCurricularCourse(mwEnrolment);
				continue;
			}

			this.deleteEnrollment(mwEnrolment, studentCurricularPlan, curricularCourse);
		}
	}

	/**
	 * @param mwEnrolment
	 * @param studentCurricularPlan
	 * @param curricularCourseScope
	 * @throws Throwable
	 */
	private void deleteEnrollment(
		MWEnrolment mwEnrolment,
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse)
		throws Throwable
	{
		IPersistentEnrolment enrollmentDAO = super.persistentSuport.getIPersistentEnrolment();
		IPersistentEnrolmentEvaluation enrollmentEvaluationDAO = super.persistentSuport.getIPersistentEnrolmentEvaluation();

		IExecutionPeriod executionPeriod = super.getExecutionPeriodForThisMWEnrolment(mwEnrolment);

		EnrolmentEvaluationType enrolmentEvaluationType = super.getEvaluationType(mwEnrolment);

		IEnrolment enrolment =
			enrollmentDAO.readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
				studentCurricularPlan,
				curricularCourse,
				executionPeriod);

		if (enrolment == null)
		{
//			super.out.println(
//				"[ERROR 207.2] Could not find Enrolment for student ["
//					+ studentCurricularPlan.getStudent().getNumber()
//					+ "] in curricular course with name ["
//					+ curricularCourse.getName()
//					+ "] and code ["
//					+ curricularCourse.getCode()
//					+ "] from degree with name ["
//					+ curricularCourse.getDegreeCurricularPlan().getName()
//					+ "] and code ["
//					+ mwEnrolment.getDegreecode()
//					+ "] for execution period ["
//					+ executionPeriod.getExecutionYear().getYear()
//					+ " - "
//					+ executionPeriod.getName()
//					+ "]");
			super.writeTreatedMWEnrollment(mwEnrolment);
			return;
		}

		String grade = super.getAcurateGrade(mwEnrolment);

		IEnrolmentEvaluation enrolmentEvaluation =
			enrollmentEvaluationDAO.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
				enrolment,
				enrolmentEvaluationType,
				grade);

		if (enrolmentEvaluation == null)
		{
//			super.out.println(
//				"[ERROR 208.2] Could not find EnrolmentEvaluation for student ["
//					+ studentCurricularPlan.getStudent().getNumber()
//					+ "] in curricular course with name ["
//					+ curricularCourse.getName()
//					+ "] and code ["
//					+ curricularCourse.getCode()
//					+ "] with grade ["
//					+ grade
//					+ "] from degree with name ["
//					+ curricularCourse.getDegreeCurricularPlan().getName()
//					+ "] and code ["
//					+ mwEnrolment.getDegreecode()
//					+ "] for execution period ["
//					+ executionPeriod.getExecutionYear().getYear()
//					+ " - "
//					+ executionPeriod.getName()
//					+ "]");
			super.writeTreatedMWEnrollment(mwEnrolment);
			return;
		}

		if (enrolment.getEvaluations().size() == 1)
		{
			enrollmentEvaluationDAO.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());

			ReportAllPastEnrollmentMigration.addEnrollmentEvaluationsDeleted();

			enrollmentDAO.deleteByOID(Enrolment.class, enrolment.getIdInternal());

			ReportAllPastEnrollmentMigration.addEnrollmentsDeleted();

			super.writeTreatedMWEnrollment(mwEnrolment);
		} else if (enrolment.getEvaluations().size() > 1)
		{
			enrollmentEvaluationDAO.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());

			ReportAllPastEnrollmentMigration.addEnrollmentEvaluationsDeleted();

			super.writeTreatedMWEnrollment(mwEnrolment);
		}
	}

}