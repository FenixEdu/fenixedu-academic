package ServidorAplicacao.Servico.manager.migration;

import java.io.FileOutputStream;
import java.io.PrintWriter;
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
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.enrolment.DeleteEnrolment;
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

public class DeleteEnrollmentsInCurrentStudentCurricularPlans
	extends CreateUpdateDeleteEnrollmentsInCurrentStudentCurricularPlans
	implements IService
{
	public DeleteEnrollmentsInCurrentStudentCurricularPlans()
	{
		try
		{
			super.persistentSuport = SuportePersistenteOJB.getInstance();
			super.persistentMiddlewareSuport = PersistentMiddlewareSupportOJB.getInstance();
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace(System.out);
		}

		super.executionPeriod = null;
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

					this.deleteStudentEnrollments(mwStudent);

					totalElementsConsidered++;
				}
			}
		} catch (Throwable e)
		{
			super.out.println("[ERROR 101.2] Exception migrating student [" + mwStudent.getNumber() + "] enrolments!");
			super.out.println("[ERROR 101.2] Number: [" + mwStudent.getNumber() + "]");
			super.out.println("[ERROR 101.2] Degree: [" + mwStudent.getDegreecode() + "]");
			super.out.println("[ERROR 101.2] Branch: [" + mwStudent.getBranchcode() + "]");
			e.fillInStackTrace();
			e.printStackTrace(super.out);
			throw new FenixServiceException(e);
		}

		ReportEnrolment.report(super.out);
		super.out.close();
	}

	/**
	 * @param mwStudent
	 * @throws Throwable
	 */
	private void deleteStudentEnrollments(MWStudent mwStudent) throws Throwable
	{
		IPersistentStudent studentDAO = super.persistentSuport.getIPersistentStudent();

		IStudent student = studentDAO.readStudentByNumberAndDegreeType(mwStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

		if (student == null)
		{
			super.out.println("[ERROR 102.2] Cannot find Fenix student with number [" + mwStudent.getNumber() + "]!");
			return;
		}

		IStudentCurricularPlan studentCurricularPlan =
			super.persistentSuport.getIStudentCurricularPlanPersistente().readActiveByStudentNumberAndDegreeType(
				student.getNumber(),
				TipoCurso.LICENCIATURA_OBJ);

		if (studentCurricularPlan == null)
		{
			super.out.println(
				"[ERROR 103.2] Cannot find Fenix StudentCurricularPlan for student number [" + mwStudent.getNumber() + "]!");
			return;
		}

		List studentEnrolments = mwStudent.getEnrolments();

		this.deleteEnrollments(studentEnrolments, studentCurricularPlan, mwStudent);
	}

	/**
	 * @param enrolments2Write
	 * @param studentCurricularPlan
	 * @param mwStudent
	 * @throws Throwable
	 */
	private void deleteEnrollments(List enrolments2Delete, IStudentCurricularPlan studentCurricularPlan, MWStudent mwStudent)
		throws Throwable
	{
		Iterator iterator = enrolments2Delete.iterator();
		while (iterator.hasNext())
		{
			MWEnrolment mwEnrolment = (MWEnrolment) iterator.next();

			super.executionPeriod = super.getExecutionPeriodForThisMWEnrolment(mwEnrolment);

			IDegreeCurricularPlan degreeCurricularPlan =
				super.getDegreeCurricularPlan(mwEnrolment.getDegreecode(), studentCurricularPlan);

			if (degreeCurricularPlan == null)
			{
				super.out.println("[ERROR 104.2] Degree Curricular Plan Not Found!");
				continue;
			}

			ICurricularCourse curricularCourse = super.getCurricularCourse(mwEnrolment, degreeCurricularPlan, true);

			if (curricularCourse == null)
			{
				continue;
			}

			this.deleteEnrollment(mwEnrolment, studentCurricularPlan, curricularCourse);
		}
	}

	/**
	 * @param studentCurricularPlan
	 * @param curricularCourse
	 * @return IEnrolment
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

		IEnrolment enrolment =
			enrollmentDAO.readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
				studentCurricularPlan,
				curricularCourse,
				super.executionPeriod);

		if (enrolment == null)
		{
//			super.out.println(
//				"[ERROR 105.2] Could not find Enrolment for student ["
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
//					+ super.executionPeriod.getExecutionYear().getYear()
//					+ " - "
//					+ super.executionPeriod.getName()
//					+ "]");
			super.writeTreatedMWEnrollment(mwEnrolment);
			return;
		}

		IEnrolmentEvaluation enrolmentEvaluation = null;
		String grade = super.getAcurateGrade(mwEnrolment);

		if ((mwEnrolment.getGrade() == null) || (mwEnrolment.getGrade().equals("")))
		{
			enrolmentEvaluation =
				enrollmentEvaluationDAO.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
					enrolment,
					EnrolmentEvaluationType.NORMAL_OBJ,
					null);
		} else
		{
			enrolmentEvaluation =
				enrollmentEvaluationDAO.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
					enrolment,
					EnrolmentEvaluationType.NORMAL_OBJ,
					grade);
		}

		if (enrolmentEvaluation == null)
		{
//			super.out.println(
//				"[ERROR 106.2] Could not find EnrolmentEvaluation for student ["
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
//					+ super.executionPeriod.getExecutionYear().getYear()
//					+ " - "
//					+ super.executionPeriod.getName()
//					+ "]");
			super.writeTreatedMWEnrollment(mwEnrolment);
			return;
		}

		if (enrolment.getEvaluations().size() == 1)
		{
			enrollmentEvaluationDAO.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());

			ReportEnrolment.addEnrolmentEvaluationDeleted();

			enrollmentDAO.deleteByOID(Enrolment.class, enrolment.getIdInternal());

			this.cleanEnrollmentRelations(enrolment);

			ReportEnrolment.addEnrolmentDeleted();

			super.writeTreatedMWEnrollment(mwEnrolment);
		} else if (enrolment.getEvaluations().size() > 1)
		{
			enrollmentEvaluationDAO.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());

			ReportEnrolment.addEnrolmentEvaluationDeleted();

			super.writeTreatedMWEnrollment(mwEnrolment);
		}
	}

	/**
	 * @param enrolment
	 * @throws Throwable
	 */
	protected void cleanEnrollmentRelations(IEnrolment enrolment) throws Throwable
	{
		DeleteEnrolment.deleteAttend(enrolment);
	}

}