package Dominio;
import java.util.Date;
import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;
import Util.Specialization;
import Util.StudentCurricularPlanState;
import Util.enrollment.EnrollmentRuleType;

/**
 * @author David Santos in Jun 24, 2004
 */

public interface IStudentCurricularPlan extends IDomainObject
{
	public IBranch getBranch();
	public Double getClassification();
	public Integer getCompletedCourses();
	public StudentCurricularPlanState getCurrentState();
	public IDegreeCurricularPlan getDegreeCurricularPlan();
	public IEmployee getEmployee();
	public Integer getEnrolledCourses();
	public List getEnrolments();
	public Double getGivenCredits();
	public String getObservations();
	public IBranch getSecundaryBranch();
	public Specialization getSpecialization();
	public Date getStartDate();
	public IStudent getStudent();
	public Date getWhen();

	public void setBranch(IBranch branch);
	public void setClassification(Double classification);
	public void setCompletedCourses(Integer completedCourses);
	public void setCurrentState(StudentCurricularPlanState currentState);
	public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan);
	public void setEmployee(IEmployee employee);
	public void setEnrolledCourses(Integer enrolledCourses);
	public void setEnrolments(List enrolments);
	public void setGivenCredits(Double givenCredits);
	public void setObservations(String observations);
	public void setSecundaryBranch(IBranch secundaryBranch);
	public void setSpecialization(Specialization specialization);
	public void setStartDate(Date startDate);
	public void setStudent(IStudent student);
	public void setWhen(Date when);


	// -------------------------------------------------------------
	// BEGIN: Only for enrollment purposes
	// -------------------------------------------------------------
	public List getCurricularCoursesToEnroll(IExecutionPeriod executionPeriod, EnrollmentRuleType enrollmentRuleType) throws ExcepcaoPersistencia;
	public List getStudentApprovedEnrollments();
	public List getStudentEnrolledEnrollments();
	public List getStudentEnrolledEnrollmentsInExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
	public List getStudentTemporarilyEnrolledEnrollments();
	public Integer getMinimumNumberOfCoursesToEnroll();
	public Integer getMaximumNumberOfCoursesToEnroll();
	public Integer getMaximumNumberOfAcumulatedEnrollments();
	public int getNumberOfApprovedCurricularCourses();
	public int getNumberOfEnrolledCurricularCourses();
	public boolean isCurricularCourseApproved(ICurricularCourse curricularCourse);
	public boolean isCurricularCourseEnrolled(ICurricularCourse curricularCourse);
    public void calculateStudentAcumulatedEnrollments();
    public Integer getCurricularCourseAcumulatedEnrolments(ICurricularCourse curricularCourse);
	public List getNotNeedToEnrollCurricularCourses();
	public void setNotNeedToEnrollCurricularCourses(List notNeedToEnrollCurricularCourses);
	// -------------------------------------------------------------
	// END: Only for enrollment purposes
	// -------------------------------------------------------------

}