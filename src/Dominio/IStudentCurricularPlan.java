/*
 * IStudentCurricularPlan.java
 *
 * Created on 21 de December de 2002, 16:31
 */
package Dominio;
import java.util.Date;
import java.util.List;

import Util.Specialization;
import Util.StudentCurricularPlanState;
import Util.enrollment.EnrollmentRuleType;
/**
 *
 * @author  Nuno Nunes & Joana Mota
 */
public interface IStudentCurricularPlan extends IDomainObject
{
	public IStudent getStudent();
	public IBranch getBranch();
	public IDegreeCurricularPlan getDegreeCurricularPlan();
	public StudentCurricularPlanState getCurrentState();
	public Date getStartDate();
	public Specialization getSpecialization();
	public Double getGivenCredits();
	public Integer getCompletedCourses();
	public Integer getEnrolledCourses();
	public List getEnrolments();
	public Double getClassification();
	public IEmployee getEmployee();
	public Date getWhen();
    public String getObservations();
    public IBranch getSecundaryBranch();

    public void setSecundaryBranch(IBranch secundaryBranch);
    public void setObservations(String observations);
	public void setStudent(IStudent student);
	public void setBranch(IBranch branch);
	public void setDegreeCurricularPlan(IDegreeCurricularPlan courseCurricularPlan);
	public void setCurrentState(StudentCurricularPlanState state);
	public void setStartDate(Date startDate);
	public void setSpecialization(Specialization specialization);
	public void setGivenCredits(Double givenCredits);
	public void setEnrolments(List enrolments);
	public void setCompletedCourses(Integer integer);
	public void setEnrolledCourses(Integer integer);
	public void setClassification(Double double1);
	public void setEmployee(IEmployee funcionario);
	public void setWhen(Date date);


	// -------------------------------------------------------------
	// BEGIN: Only for enrollment purposes
	// -------------------------------------------------------------
	public List getCurricularCoursesToEnroll(IExecutionPeriod executionPeriod, EnrollmentRuleType enrollmentRuleType);
	public List getStudentApprovedEnrollments();
	public List getStudentEnrolledEnrollments();
	public List getStudentTemporarilyEnrolledEnrollments();
	public List getAllEnrollmentsInCoursesWhereStudentIsEnrolledAtTheMoment();
	public Integer getMinimumNumberOfCoursesToEnroll();
	public Integer getMaximumNumberOfCoursesToEnroll();
	public Integer getMaximumNumberOfAcumulatedEnrollments();
	public int getNumberOfApprovedCurricularCourses();
	public int getNumberOfEnrolledCurricularCourses();
	public boolean isCurricularCourseApproved(ICurricularCourse curricularCourse);
	public boolean isCurricularCourseEnrolled(ICurricularCourse curricularCourse);
    public void calculateStudentAcumulatedEnrollments();
    public Integer getCurricularCourseAcumulatedEnrolments(ICurricularCourse curricularCourse);
	// -------------------------------------------------------------
	// END: Only for enrollment purposes
	// -------------------------------------------------------------

}