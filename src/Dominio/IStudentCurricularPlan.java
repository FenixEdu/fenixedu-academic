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


/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

public interface IStudentCurricularPlan extends IDomainObject {
  public IStudent getStudent();
  public IBranch getBranch();
  public IDegreeCurricularPlan getDegreeCurricularPlan();
  public StudentCurricularPlanState getCurrentState();
  public Date getStartDate();
  public Specialization getSpecialization();
  public Double getGivenCredits();
  public List getEnrolments();
  public Double getClassification();
  public Integer getCompletedCourses();
  public Integer getEnrolledCourses();
  
  public void setStudent(IStudent student);
  public void setBranch(IBranch branch);
  public void setDegreeCurricularPlan(IDegreeCurricularPlan courseCurricularPlan);
  public void setCurrentState(StudentCurricularPlanState state);
  public void setStartDate(Date startDate);
  public void setSpecialization(Specialization specialization);
  public void setGivenCredits(Double givenCredits);
  public void setEnrolments(List enrolments);
  public void setClassification(Double double1);
  public void setCompletedCourses(Integer integer);
  public void setEnrolledCourses(Integer integer);

}
