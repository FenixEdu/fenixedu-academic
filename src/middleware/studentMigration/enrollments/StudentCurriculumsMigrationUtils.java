package middleware.studentMigration.enrollments;

import java.util.Calendar;

import middleware.middlewareDomain.MWEnrolment;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;


/**
 * @author David Santos in Mar 15, 2004
 */

public class StudentCurriculumsMigrationUtils
{
	/**
	 * @param mwEnrolment
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	public static IExecutionPeriod getExecutionPeriodForThisMWEnrolment(MWEnrolment mwEnrolment, ISuportePersistente fenixPersistentSuport) throws Throwable
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
	 * @param person
	 * @return
	 */
	public static String getPersonKey(IPessoa person)
	{
		return person.getNumeroDocumentoIdentificacao() + person.getUsername() + person.getTipoDocumentoIdentificacao().toString();
	}

	/**
	 * @param student
	 * @return
	 */
	public static String getStudentKey(IStudent student)
	{
		return StudentCurriculumsMigrationUtils.getPersonKey(student.getPerson()) + student.getNumber().toString() + student.getDegreeType().toString();
	}

	/**
	 * @param degree
	 * @return
	 */
	public static String getDegreeKey(ICurso degree)
	{
		return degree.getSigla() + degree.getNome() + degree.getTipoCurso().toString();
	}

	/**
	 * @param degreeCurricularPlan
	 * @return
	 */
	public static String getDegreeCurricularPlanKey(IDegreeCurricularPlan degreeCurricularPlan)
	{
		return StudentCurriculumsMigrationUtils.getDegreeKey(degreeCurricularPlan.getDegree()) + degreeCurricularPlan.getName();
	}

	/**
	 * @param studentCurricularPlan
	 * @return
	 */
	public static String getStudentCurricularPlanKey(IStudentCurricularPlan studentCurricularPlan)
	{
		return StudentCurriculumsMigrationUtils.getStudentKey(studentCurricularPlan.getStudent()) + 
			StudentCurriculumsMigrationUtils.getDegreeCurricularPlanKey(studentCurricularPlan.getDegreeCurricularPlan()) +
			studentCurricularPlan.getCurrentState().toString();
	}

//	/**
//	 * @param curricularYear
//	 * @return
//	 */
//	public static String getCurricularYearKey(ICurricularYear curricularYear)
//	{
//		return curricularYear.getYear().toString();
//	}

//	/**
//	 * @param curricularSemester
//	 * @return
//	 */
//	public static String getCurricularSemesterKey(ICurricularSemester curricularSemester)
//	{
//		return StudentCurriculumsMigrationUtils.getCurricularYearKey(curricularSemester.getCurricularYear()) + curricularSemester.getSemester().toString();
//	}

	/**
	 * @param curricularCourse
	 * @return
	 */
	public static String getCurricularCourseKey(ICurricularCourse curricularCourse)
	{
		return StudentCurriculumsMigrationUtils.getDegreeCurricularPlanKey(curricularCourse.getDegreeCurricularPlan()) + 
			curricularCourse.getName() + curricularCourse.getCode();
	}

//	/**
//	 * @param branch
//	 * @return
//	 */
//	public static String getBranchKey(IBranch branch)
//	{
//		return StudentCurriculumsMigrationUtils.getDegreeCurricularPlanKey(branch.getDegreeCurricularPlan()) + 
//			branch.getCode();
//	}

//	/**
//	 * @param curricularCourseScope
//	 * @return
//	 */
//	public static String getCurricularCourseScopeKey(ICurricularCourseScope curricularCourseScope)
//	{
//		return StudentCurriculumsMigrationUtils.getCurricularSemesterKey(curricularCourseScope.getCurricularSemester()) +
//			StudentCurriculumsMigrationUtils.getCurricularCourseKey(curricularCourseScope.getCurricularCourse()) +
//			StudentCurriculumsMigrationUtils.getBranchKey(curricularCourseScope.getBranch()) +
//			curricularCourseScope.getBeginDate().get(Calendar.YEAR) + "-" +
//			curricularCourseScope.getBeginDate().get(Calendar.MONTH) + "-" +
//			curricularCourseScope.getBeginDate().get(Calendar.DAY_OF_MONTH); 
//	}

	/**
	 * @param executionYear
	 * @return
	 */
	public static String getExecutionYearKey(IExecutionYear executionYear)
	{
		return executionYear.getYear();
	}

	/**
	 * @param executionPeriod
	 * @return
	 */
	public static String getExecutionPeriodKey(IExecutionPeriod executionPeriod)
	{
		return StudentCurriculumsMigrationUtils.getExecutionYearKey(executionPeriod.getExecutionYear()) + executionPeriod.getName();
	}

	/**
	 * @param enrolment
	 * @return
	 */
	public static String getEnrollmentKey(IEnrolment enrolment)
	{
		return StudentCurriculumsMigrationUtils.getStudentCurricularPlanKey(enrolment.getStudentCurricularPlan()) +
			StudentCurriculumsMigrationUtils.getCurricularCourseKey(enrolment.getCurricularCourse()) +
			StudentCurriculumsMigrationUtils.getExecutionPeriodKey(enrolment.getExecutionPeriod());
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

		return StudentCurriculumsMigrationUtils.getEnrollmentKey(enrolmentEvaluation.getEnrolment()) +
			enrolmentEvaluation.getGrade() +
			enrolmentEvaluation.getEnrolmentEvaluationType().toString() +
			calendar.get(Calendar.YEAR) + "-" +
			calendar.get(Calendar.MONTH) + "-" +
			calendar.get(Calendar.DAY_OF_MONTH); 
	}

}