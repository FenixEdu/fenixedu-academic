package ServidorAplicacao.Servico.manager.migration.withBroker;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWEnrolment;
import middleware.middlewareDomain.MWStudent;
import middleware.persistentMiddlewareSupport.PersistanceBrokerForMigrationProcess;
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
import Util.EnrolmentEvaluationType;
import Util.TipoCurso;

/**
 * @author David Santos in Feb 7, 2004
 */

public class DeleteEnrollmentsInPastStudentCurricularPlans
	extends CreateUpdateDeleteEnrollmentsInPastStudentCurricularPlans
	implements IService
{
	public DeleteEnrollmentsInPastStudentCurricularPlans(PersistanceBrokerForMigrationProcess pb)
	{
		if (pb == null)
		{
			super.persistentSuport = new PersistanceBrokerForMigrationProcess();
		} else
		{
			super.persistentSuport = pb;
		}
		
		super.studentCurricularPlansCreated = new HashMap();
		super.curricularCoursesCreated = new HashMap();
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

			super.out.println("[INFO] Total number of student curriculums to update [" + numberOfStudents + "].");

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
				super.out.println("[ERROR 028] Exception migrating student [" + mwStudent.getNumber() + "] enrolments!");
				super.out.println("[ERROR 028] Number: [" + mwStudent.getNumber() + "]");
				super.out.println("[ERROR 028] Degree: [" + mwStudent.getDegreecode() + "]");
				super.out.println("[ERROR 028] Branch: [" + mwStudent.getBranchcode() + "]");
			}
			e.printStackTrace(super.out);
			throw new Throwable(e);
		}

		ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.report(super.out);
		super.out.close();
	}

	/**
	 * @param mwStudent
	 */
	private void deleteStudentEnrollments(MWStudent mwStudent)
	{
		// Read Fenix Student.
		IStudent student =
			super.persistentSuport.readStudentByNumberAndDegreeType(mwStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

		if (student == null)
		{
			// This can only happen if the Students/Persons migration was not runed before this one!
			super.out.println("[ERROR 029] Can't find Student in Fenix DB with number: [" + mwStudent.getNumber() + "]!");
			return;
		}

		IDegreeCurricularPlan degreeCurricularPlan = super.getDegreeCurricularPlan(mwStudent.getDegreecode());

		if (degreeCurricularPlan == null)
		{
			// This can only happen if the CreateAndUpdateAllPastCurriculums migration was not runed before this one!
			super.out.println("[ERROR 030] No record of Degree with code: [" + mwStudent.getDegreecode() + "]!");
			return;
		}

		IStudentCurricularPlan studentCurricularPlan = super.getStudentCurricularPlan(degreeCurricularPlan, student, mwStudent);

		if (studentCurricularPlan == null)
		{
			super.out.println(
				"[ERROR 031] Could not obtain StudentCurricularPlan for Student with number: ["
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
	 */
	private void deleteEnrollments(MWStudent mwStudent, IStudentCurricularPlan studentCurricularPlan)
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
				super.out.println("[ERROR 032] No record of Degree with code: [" + mwEnrolment.getDegreecode() + "]!");
				return;
			}

			ICurricularCourse curricularCourse = super.getCurricularCourse(mwEnrolment, degreeCurricularPlan);

			if (curricularCourse == null)
			{
				super.out.println("[ERROR 033] Couldn't create CurricularCourse with code [" + mwEnrolment.getCoursecode() + "]!");
				ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.addUnknownCurricularCourse(mwEnrolment);
				continue;
			}

			this.deleteEnrollment(mwEnrolment, studentCurricularPlan, curricularCourse);
		}
	}

	/**
	 * @param mwEnrolment
	 * @param studentCurricularPlan
	 * @param curricularCourseScope
	 */
	private void deleteEnrollment(
		MWEnrolment mwEnrolment,
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse)
	{
		IExecutionPeriod executionPeriod = super.getExecutionPeriodForThisMWEnrolment(mwEnrolment);

		EnrolmentEvaluationType enrolmentEvaluationType = super.getEvaluationType(mwEnrolment);

		IEnrolment enrolment =
			super.persistentSuport.readEnrolmentByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
				studentCurricularPlan,
				curricularCourse,
				executionPeriod);

		if (enrolment == null)
		{
			/*
			 * super.out.println( "[ERROR 034] Could not find Enrolment for student [" +
			 * studentCurricularPlan.getStudent().getNumber() + "] in curricular course with name [" + curricularCourse.getName() + "]
			 * and code [" + curricularCourse.getCode() + "] from degree with name [" +
			 * curricularCourse.getDegreeCurricularPlan().getName() + "] and code [" + mwEnrolment.getDegreecode() + "] for
			 * execution period [" + executionPeriod.getExecutionYear().getYear() + " - " + executionPeriod.getName()
			 */
			super.markMWEnrollmentAsTreated(mwEnrolment);
			return;
		}

		String grade = super.getAcurateGrade(mwEnrolment);

		IEnrolmentEvaluation enrolmentEvaluation =
			super.persistentSuport.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
				enrolment,
				enrolmentEvaluationType,
				grade);

		if (enrolmentEvaluation == null)
		{
			/*
			 * super.out.println( "[ERROR 035] Could not find EnrolmentEvaluation for student [" +
			 * studentCurricularPlan.getStudent().getNumber() + "] in curricular course with name [" + curricularCourse.getName() + "]
			 * and code [" + curricularCourse.getCode() + "] with grade [" + grade + "] from degree with name [" +
			 * curricularCourse.getDegreeCurricularPlan().getName() + "] and code [" + mwEnrolment.getDegreecode() + "] for
			 * execution period [" + executionPeriod.getExecutionYear().getYear() + " - " + executionPeriod.getName()
			 */
			super.markMWEnrollmentAsTreated(mwEnrolment);
			return;
		}

		if (enrolment.getEvaluations().size() == 1)
		{
			super.persistentSuport.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());

			ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.addEnrollmentEvaluationsDeleted();

			super.persistentSuport.deleteByOID(Enrolment.class, enrolment.getIdInternal());

			ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.addEnrollmentsDeleted();

			super.markMWEnrollmentAsTreated(mwEnrolment);
		} else if (enrolment.getEvaluations().size() > 1)
		{
			super.persistentSuport.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());

			ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.addEnrollmentEvaluationsDeleted();

			super.markMWEnrollmentAsTreated(mwEnrolment);
		}
	}

}