package Dominio;
import java.util.Date;
import java.util.List;

import ServidorAplicacao.Servico.exceptions.BothAreasAreTheSameServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.Specialization;
import Util.StudentCurricularPlanState;
import Util.enrollment.CurricularCourseEnrollmentType;

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
	public List getCurricularCoursesToEnroll(IExecutionPeriod executionPeriod);
	public List getAllStudentEnrolledEnrollments();
	public List getAllStudentEnrolledEnrollmentsInExecutionPeriod(IExecutionPeriod executionPeriod);
//	public List getStudentTemporarilyEnrolledEnrollments();
	public Integer getMinimumNumberOfCoursesToEnroll();
	public Integer getMaximumNumberOfCoursesToEnroll();
	public Integer getMaximumNumberOfAcumulatedEnrollments();
	public int getNumberOfApprovedCurricularCourses();
	public int getNumberOfEnrolledCurricularCourses();
	public boolean isCurricularCourseApproved(ICurricularCourse curricularCourse);
	public boolean isCurricularCourseEnrolled(ICurricularCourse curricularCourse);
    public Integer getCurricularCourseAcumulatedEnrollments(ICurricularCourse curricularCourse);
	public List getNotNeedToEnrollCurricularCourses();
	public void setNotNeedToEnrollCurricularCourses(List notNeedToEnrollCurricularCourses);
	public boolean areNewAreasCompatible(IBranch specializationArea,IBranch secundaryArea) throws ExcepcaoPersistencia, BothAreasAreTheSameServiceException, InvalidArgumentsServiceException;
    public boolean getCanChangeSpecializationArea();
    public CurricularCourseEnrollmentType getCurricularCourseEnrollmentType(ICurricularCourse curricularCourse,
            IExecutionPeriod currentExecutionPeriod) throws ExcepcaoPersistencia;
    public Integer getCreditsInSecundaryArea() ;
    public void setCreditsInSecundaryArea(Integer creditsInSecundaryArea) ;
    public Integer getCreditsInSpecializationArea() ;
    public void setCreditsInSpecializationArea(
            Integer creditsInSpecializationArea) ;
	// -------------------------------------------------------------
	// END: Only for enrollment purposes
	// -------------------------------------------------------------
}