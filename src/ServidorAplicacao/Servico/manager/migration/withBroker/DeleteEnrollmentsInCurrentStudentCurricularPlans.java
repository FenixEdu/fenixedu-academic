package ServidorAplicacao.Servico.manager.migration.withBroker;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWEnrolment;
import middleware.middlewareDomain.MWStudent;
import middleware.persistentMiddlewareSupport.PersistanceBrokerForMigrationProcess;
import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.Frequenta;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IStudentGroupAttend;
import Util.EnrolmentEvaluationType;
import Util.TipoCurso;

/**
 * @author David Santos in Feb 7, 2004
 */

public class DeleteEnrollmentsInCurrentStudentCurricularPlans
	extends CreateUpdateDeleteEnrollmentsInCurrentStudentCurricularPlans
	implements IService
{
	public DeleteEnrollmentsInCurrentStudentCurricularPlans(PersistanceBrokerForMigrationProcess pb)
	{
		if (pb == null)
		{
			super.persistentSuport = new PersistanceBrokerForMigrationProcess();
		} else
		{
			super.persistentSuport = pb;
		}
		
		super.executionPeriod = null;
		super.out = null;
		super.numberOfElementsInSpan = 50;
		super.maximumNumberOfElementsToConsider = 10000;
	}

	public void run(Boolean toLogToFile, String fileName) throws Throwable
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

			Integer numberOfStudents = super.persistentSuport.countAllMWStudents();
			super.out.println("[INFO] Updating a total of [" + numberOfStudents.intValue() + "] student curriculums.");

			int numberOfSpans = numberOfStudents.intValue() / super.numberOfElementsInSpan;
			numberOfSpans = numberOfStudents.intValue() % super.numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
			int totalElementsConsidered = 0;

			for (int span = 0; span < numberOfSpans && totalElementsConsidered < super.maximumNumberOfElementsToConsider; span++)
			{
				super.out.println("[INFO] Reading MWStudents...");

				Iterator iterator =
					super.persistentSuport.readAllMWStudentsBySpanIterator(
						new Integer(span),
						new Integer(super.numberOfElementsInSpan));
				while (iterator.hasNext())
				{
					mwStudent = (MWStudent) iterator.next();

					mwStudent.setEnrolments(super.persistentSuport.readMWEnrolmentsByStudentNumber(mwStudent.getNumber()));

					this.deleteStudentEnrollments(mwStudent);

					totalElementsConsidered++;
				}
			}
		} catch (Throwable e)
		{
			if (mwStudent != null)
			{
				super.out.println("[ERROR 022] Exception migrating student [" + mwStudent.getNumber() + "] enrolments!");
				super.out.println("[ERROR 022] Number: [" + mwStudent.getNumber() + "]");
				super.out.println("[ERROR 022] Degree: [" + mwStudent.getDegreecode() + "]");
				super.out.println("[ERROR 022] Branch: [" + mwStudent.getBranchcode() + "]");
			}
			e.printStackTrace(super.out);
			throw new Throwable(e);
		}

		ReportForCreateUpdateEnrollmentsInCurrentStudentCurricularPlans.report(super.out);
		super.out.close();
	}

	/**
	 * @param mwStudent
	 */
	private void deleteStudentEnrollments(MWStudent mwStudent)
	{
		IStudent student =
			super.persistentSuport.readStudentByNumberAndDegreeType(mwStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

		if (student == null)
		{
			super.out.println("[ERROR 023] Cannot find Fenix student with number [" + mwStudent.getNumber() + "]!");
			return;
		}

		IStudentCurricularPlan studentCurricularPlan =
			super.persistentSuport.readActiveStudentCurricularPlanByStudentNumberAndDegreeType(
				student.getNumber(),
				TipoCurso.LICENCIATURA_OBJ);

		if (studentCurricularPlan == null)
		{
			super.out.println(
				"[ERROR 024] Cannot find Fenix StudentCurricularPlan for student number [" + mwStudent.getNumber() + "]!");
			return;
		}

		List studentEnrolments = mwStudent.getEnrolments();

		this.deleteEnrollments(studentEnrolments, studentCurricularPlan, mwStudent);
	}

	/**
	 * @param enrolments2Delete
	 * @param studentCurricularPlan
	 * @param mwStudent
	 */
	private void deleteEnrollments(List enrolments2Delete, IStudentCurricularPlan studentCurricularPlan, MWStudent mwStudent)
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
				super.out.println("[ERROR 025] Degree Curricular Plan Not Found!");
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
	 * @param mwEnrolment
	 * @param studentCurricularPlan
	 * @param curricularCourse
	 */
	private void deleteEnrollment(
		MWEnrolment mwEnrolment,
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse)
	{
		IEnrolment enrolment =
			super.persistentSuport.readEnrolmentByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
				studentCurricularPlan,
				curricularCourse,
				super.executionPeriod);

		if (enrolment == null)
		{
			/*
			 * super.out.println( "[ERROR 026] Could not find Enrolment for student [" +
			 * studentCurricularPlan.getStudent().getNumber() + "] in curricular course with name [" + curricularCourse.getName() + "]
			 * and code [" + curricularCourse.getCode() + "] from degree with name [" +
			 * curricularCourse.getDegreeCurricularPlan().getName() + "] and code [" + mwEnrolment.getDegreecode() + "] for
			 * execution period [" + super.executionPeriod.getExecutionYear().getYear() + " - " + super.executionPeriod.getName()
			 */
			super.markMWEnrollmentAsTreated(mwEnrolment);
			return;
		}

		IEnrolmentEvaluation enrolmentEvaluation = null;
		String grade = super.getAcurateGrade(mwEnrolment);

		if ((mwEnrolment.getGrade() == null) || (mwEnrolment.getGrade().equals("")))
		{
			enrolmentEvaluation =
				super.persistentSuport.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
					enrolment,
					EnrolmentEvaluationType.NORMAL_OBJ,
					null);
		} else
		{
			enrolmentEvaluation =
				super.persistentSuport.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
					enrolment,
					EnrolmentEvaluationType.NORMAL_OBJ,
					grade);
		}

		if (enrolmentEvaluation == null)
		{
			/*
			 * super.out.println( "[ERROR 027] Could not find EnrolmentEvaluation for student [" +
			 * studentCurricularPlan.getStudent().getNumber() + "] in curricular course with name [" + curricularCourse.getName() + "]
			 * and code [" + curricularCourse.getCode() + "] with grade [" + grade + "] from degree with name [" +
			 * curricularCourse.getDegreeCurricularPlan().getName() + "] and code [" + mwEnrolment.getDegreecode() + "] for
			 * execution period [" + super.executionPeriod.getExecutionYear().getYear() + " - " + super.executionPeriod.getName()
			 */
			super.markMWEnrollmentAsTreated(mwEnrolment);
			return;
		}

		if (enrolment.getEvaluations().size() == 1)
		{
			super.persistentSuport.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());

			ReportForCreateUpdateEnrollmentsInCurrentStudentCurricularPlans.addEnrolmentEvaluationDeleted();

			super.persistentSuport.deleteByOID(Enrolment.class, enrolment.getIdInternal());

			this.cleanEnrollmentRelations(enrolment);

			ReportForCreateUpdateEnrollmentsInCurrentStudentCurricularPlans.addEnrolmentDeleted();

			super.markMWEnrollmentAsTreated(mwEnrolment);
		} else if (enrolment.getEvaluations().size() > 1)
		{
			super.persistentSuport.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());

			ReportForCreateUpdateEnrollmentsInCurrentStudentCurricularPlans.addEnrolmentEvaluationDeleted();

			super.markMWEnrollmentAsTreated(mwEnrolment);
		}
	}

	/**
	 * @param enrolment
	 */
	protected void cleanEnrollmentRelations(IEnrolment enrolment)
	{
		this.deleteAttend(enrolment);
	}

	/**
	 * @param enrolment
	 */
	public void deleteAttend(IEnrolment enrolment)
	{
		IExecutionCourse executionCourse =
			super.persistentSuport.readExecutionCourseByCurricularCourseAndExecutionPeriod(
				enrolment.getCurricularCourse(),
				enrolment.getExecutionPeriod());

		if (executionCourse != null)
		{
			IFrequenta attend =
				super.persistentSuport.readAttendByStudentAndExecutionCourse(
					enrolment.getStudentCurricularPlan().getStudent(),
					executionCourse);

			if (attend != null)
			{
				List marks = super.persistentSuport.readMarksByAttend(attend);
				if (marks == null || marks.isEmpty())
				{
					IStudentGroupAttend studentGroupAttend = super.persistentSuport.readStudentGroupAttendByAttend(attend);
					if (studentGroupAttend == null)
					{
						List shiftsStudentIsIn =
							super.persistentSuport.readShiftStudentByStudent(enrolment.getStudentCurricularPlan().getStudent());

						if (shiftsStudentIsIn == null || shiftsStudentIsIn.isEmpty())
						{

							super.persistentSuport.deleteByOID(Frequenta.class, attend.getIdInternal());
						} else
						{
							attend.setEnrolment(null);
							super.persistentSuport.store(attend);
						}
					} else
					{
						attend.setEnrolment(null);
						super.persistentSuport.store(attend);
					}
				} else
				{
					attend.setEnrolment(null);
					super.persistentSuport.store(attend);
				}
			}
		}
	}
}