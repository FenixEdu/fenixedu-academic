package ServidorAplicacao.Servico.manager.migration.withBroker;

import java.io.PrintWriter;
import java.util.Calendar;

import middleware.middlewareDomain.MWEnrolment;
import middleware.persistentMiddlewareSupport.PersistanceBrokerForMigrationProcess;
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
import Util.EnrolmentState;

/**
 * @author David Santos in Feb 7, 2004
 */

public abstract class CreateUpdateDeleteEnrollments
{
	protected PrintWriter out = null;
	protected PersistanceBrokerForMigrationProcess persistentSuport = null;

	/**
	 * @param mwEnrolment
	 */
	protected void markMWEnrollmentAsTreated(MWEnrolment mwEnrolment)
	{
		this.persistentSuport.delete(mwEnrolment);
	}

	/**
	 * @param mwEnrolment
	 * @return EnrolmentState
	 */
	protected EnrolmentState getEnrollmentStateByGrade(MWEnrolment mwEnrolment)
	{
		String grade = mwEnrolment.getGrade();

		if (mwEnrolment.getRemarks() != null)
		{
			if (mwEnrolment.getRemarks().equals("ANULADO"))
			{
				return EnrolmentState.ANNULED;
			}
		}

		if (grade == null)
		{
			return EnrolmentState.NOT_EVALUATED;
		}

		if (grade.equals(""))
		{
			return EnrolmentState.NOT_EVALUATED;
		}

		if (grade.equals("0"))
		{
			return EnrolmentState.NOT_EVALUATED;
		}

		if (grade.equals("NA"))
		{
			return EnrolmentState.NOT_EVALUATED;
		}

		if (grade.equals("RE"))
		{
			return EnrolmentState.NOT_APROVED;
		}

		if (grade.equals("AP"))
		{
			return EnrolmentState.APROVED;
		}

		int intGrade;

		try
		{
			intGrade = new Integer(grade).intValue();
		} catch (NumberFormatException e)
		{
			out.println("[ERROR 001] Grade from MWEnrolment is not a number: [" + mwEnrolment.getGrade() + "]!");
			return null;
		}

		if ((intGrade > 20) || (intGrade < 0))
		{
			out.println("[ERROR 002] Grade from MWEnrolment is not valid: [" + mwEnrolment.getGrade() + "]!");
			return null;
		} else if (intGrade < 10)
		{
			return EnrolmentState.NOT_APROVED;
		}

		return EnrolmentState.APROVED;
	}

	/**
	 * @param mwEnrolment
	 * @return IPessoa
	 */
	protected IPessoa getPersonResponsibleForGrade(MWEnrolment mwEnrolment)
	{
		ITeacher teacher = this.persistentSuport.readTeacherByNumber(mwEnrolment.getTeachernumber());

		if (teacher == null)
		{
			if (mwEnrolment.getTeachernumber().intValue() != 0)
			{
				out.println(
					"[WARNING 001] No Teacher with number: [" + mwEnrolment.getTeachernumber() + "] was found in the Fenix DB!");
				ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.addUnknownTeachersAndEmployees(mwEnrolment);
			}
			return null;
		}

		return teacher.getPerson();
	}

	/**
	 * @param mwEnrolment
	 * @return IEmployee
	 */
	protected IEmployee getEmployee(MWEnrolment mwEnrolment)
	{
		IEmployee employee = this.persistentSuport.readEmployeeByNumber(mwEnrolment.getTeachernumber());

		if (employee == null)
		{
			if (mwEnrolment.getTeachernumber().intValue() != 0)
			{
				ReportForCreateUpdateEnrollmentsInPastStudentCurricularPlans.addUnknownTeachersAndEmployees(mwEnrolment);
				out.println(
					"[WARNING 002] No Employee with number: [" + mwEnrolment.getTeachernumber() + "] was found in the Fenix DB!");
			}
			return null;
		}

		return employee;
	}

	/**
	 * @param mwEnrolment
	 * @return IExecutionPeriod
	 */
	protected IExecutionPeriod getExecutionPeriodForThisMWEnrolment(MWEnrolment mwEnrolment)
	{
		String executionYearName = mwEnrolment.getEnrolmentyear().intValue() + "/" + (mwEnrolment.getEnrolmentyear().intValue() + 1);
		IExecutionYear executionYear = this.persistentSuport.readExecutionYearByName(executionYearName);
		String executionPeriodName = mwEnrolment.getCurricularcoursesemester() + " Semestre";
		IExecutionPeriod executionPeriod =
			this.persistentSuport.readExecutionPeriodByNameAndExecutionYear(executionPeriodName, executionYear);

		return executionPeriod;
	}

	/**
	 * @param mwEnrolment
	 * @return String
	 */
	protected String getAcurateGrade(MWEnrolment mwEnrolment)
	{
		String grade = mwEnrolment.getGrade();

		if (grade == null)
		{
			return "NA";
		}

		if (grade.equals(""))
		{
			return "NA";
		}

		if (grade.equals("RE"))
		{
			return grade;
		}

		if (grade.equals("AP"))
		{
			return grade;
		}

		int intGrade;

		try
		{
			intGrade = new Integer(grade).intValue();
		} catch (NumberFormatException e)
		{
			out.println("[ERROR 003] Grade from MWEnrolment is not a number: [" + mwEnrolment.getGrade() + "]!");
			return "0";
		}

		if ((intGrade > 20) || (intGrade < 0))
		{
			out.println("[ERROR 004] Grade from MWEnrolment is not valid: [" + mwEnrolment.getGrade() + "]!");
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
	protected void updateEnrollmentStateAndEvaluationType(IEnrolment enrolment, IEnrolmentEvaluation enrolmentEvaluation)
	{
		MWEnrolment mwEnrolment = new MWEnrolment();
		mwEnrolment.setGrade(enrolmentEvaluation.getGrade());
		EnrolmentState enrolmentStateFromEnrolmentEvaluation = getEnrollmentStateByGrade(mwEnrolment);

		if (!enrolment.getEnrolmentState().equals(enrolmentStateFromEnrolmentEvaluation))
		{
			if (enrolment.getEnrolmentState().equals(EnrolmentState.NOT_APROVED)
				&& enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.APROVED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			} else if (
				enrolment.getEnrolmentState().equals(EnrolmentState.NOT_APROVED)
					&& enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.ANNULED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			} else if (
				enrolment.getEnrolmentState().equals(EnrolmentState.NOT_EVALUATED)
					&& enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.NOT_APROVED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			} else if (
				enrolment.getEnrolmentState().equals(EnrolmentState.NOT_EVALUATED)
					&& enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.APROVED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			} else if (
				enrolment.getEnrolmentState().equals(EnrolmentState.NOT_EVALUATED)
					&& enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.ANNULED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			} else if (
				enrolment.getEnrolmentState().equals(EnrolmentState.APROVED)
					&& enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.ANNULED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			} else if (
				enrolment.getEnrolmentState().equals(EnrolmentState.ENROLED)
					&& enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.ANNULED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			} else if (
				enrolment.getEnrolmentState().equals(EnrolmentState.ENROLED)
					&& enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.APROVED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			} else if (
				enrolment.getEnrolmentState().equals(EnrolmentState.ENROLED)
					&& enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.NOT_APROVED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			} else if (
				enrolment.getEnrolmentState().equals(EnrolmentState.ENROLED)
					&& enrolmentStateFromEnrolmentEvaluation.equals(EnrolmentState.NOT_EVALUATED))
			{
				enrolment.setEnrolmentState(enrolmentStateFromEnrolmentEvaluation);
				enrolment.setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType());
			}

			this.persistentSuport.store(enrolment);
		}
	}

	/**
	 * @param person
	 * @return String
	 */
	protected String getPersonKey(IPessoa person)
	{
		return person.getNumeroDocumentoIdentificacao()
			+ "-"
			+ person.getUsername()
			+ "-"
			+ person.getTipoDocumentoIdentificacao().toString();
	}

	/**
	 * @param student
	 * @return String
	 */
	protected String getStudentKey(IStudent student)
	{
		return this.getPersonKey(student.getPerson())
			+ "-"
			+ student.getNumber().toString()
			+ "-"
			+ student.getDegreeType().toString();
	}

	/**
	 * @param degree
	 * @return String
	 */
	protected String getDegreeKey(ICurso degree)
	{
		return degree.getSigla() + "-" + degree.getNome() + "-" + degree.getTipoCurso().toString();
	}

	/**
	 * @param degreeCurricularPlan
	 * @return String
	 */
	protected String getDegreeCurricularPlanKey(IDegreeCurricularPlan degreeCurricularPlan)
	{
		return this.getDegreeKey(degreeCurricularPlan.getDegree()) + "-" + degreeCurricularPlan.getName();
	}

	/**
	 * @param studentCurricularPlan
	 * @return String
	 */
	protected String getStudentCurricularPlanKey(IStudentCurricularPlan studentCurricularPlan)
	{
		return this.getStudentKey(studentCurricularPlan.getStudent())
			+ "-"
			+ this.getDegreeCurricularPlanKey(studentCurricularPlan.getDegreeCurricularPlan())
			+ "-"
			+ studentCurricularPlan.getCurrentState().toString();
	}

	/**
	 * @param curricularYear
	 * @return String
	 */
	protected String getCurricularYearKey(ICurricularYear curricularYear)
	{
		return curricularYear.getYear().toString();
	}

	/**
	 * @param curricularSemester
	 * @return String
	 */
	protected String getCurricularSemesterKey(ICurricularSemester curricularSemester)
	{
		return this.getCurricularYearKey(curricularSemester.getCurricularYear()) + "-" + curricularSemester.getSemester().toString();
	}

	/**
	 * @param curricularCourse
	 * @return String
	 */
	protected String getCurricularCourseKey(ICurricularCourse curricularCourse)
	{
		return this.getDegreeCurricularPlanKey(curricularCourse.getDegreeCurricularPlan())
			+ "-"
			+ curricularCourse.getName()
			+ "-"
			+ curricularCourse.getCode();
	}

	/**
	 * @param branch
	 * @return String
	 */
	protected String getBranchKey(IBranch branch)
	{
		return this.getDegreeCurricularPlanKey(branch.getDegreeCurricularPlan()) + "-" + branch.getCode();
	}

	/**
	 * @param curricularCourseScope
	 * @return String
	 */
	protected String getCurricularCourseScopeKey(ICurricularCourseScope curricularCourseScope)
	{
		return this.getCurricularSemesterKey(curricularCourseScope.getCurricularSemester())
			+ this.getCurricularCourseKey(curricularCourseScope.getCurricularCourse())
			+ "-"
			+ this.getBranchKey(curricularCourseScope.getBranch())
			+ "-"
			+ curricularCourseScope.getBeginDate().get(Calendar.YEAR)
			+ "-"
			+ curricularCourseScope.getBeginDate().get(Calendar.MONTH)
			+ "-"
			+ curricularCourseScope.getBeginDate().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @param executionYear
	 * @return String
	 */
	protected String getExecutionYearKey(IExecutionYear executionYear)
	{
		return executionYear.getYear();
	}

	/**
	 * @param executionPeriod
	 * @return String
	 */
	protected String getExecutionPeriodKey(IExecutionPeriod executionPeriod)
	{
		return this.getExecutionYearKey(executionPeriod.getExecutionYear()) + "-" + executionPeriod.getName();
	}

	/**
	 * @param enrolment
	 * @return String
	 */
	protected String getEnrollmentKey(IEnrolment enrolment)
	{
		return this.getStudentCurricularPlanKey(enrolment.getStudentCurricularPlan())
			+ "-"
			+ this.getCurricularCourseKey(enrolment.getCurricularCourse())
			+ "-"
			+ this.getExecutionPeriodKey(enrolment.getExecutionPeriod());
	}

	/**
	 * @param enrolmentEvaluation
	 * @return String
	 */
	protected String getEnrollmentEvaluationKey(IEnrolmentEvaluation enrolmentEvaluation)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTimeInMillis(enrolmentEvaluation.getWhen().getTime());

		return this.getEnrollmentKey(enrolmentEvaluation.getEnrolment())
			+ enrolmentEvaluation.getGrade()
			+ "-"
			+ enrolmentEvaluation.getEnrolmentEvaluationType().toString()
			+ "-"
			+ calendar.get(Calendar.YEAR)
			+ "-"
			+ calendar.get(Calendar.MONTH)
			+ "-"
			+ calendar.get(Calendar.DAY_OF_MONTH);
	}

}