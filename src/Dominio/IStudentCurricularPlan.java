/*
 * IStudentCurricularPlan.java
 *
 * Created on 21 de December de 2002, 16:31
 */

package Dominio;

import java.util.Date;

import Util.StudentCurricularPlanState;


/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

public interface IStudentCurricularPlan {
  public IStudent getStudent();
  public IPlanoCurricularCurso getCourseCurricularPlan();
  public StudentCurricularPlanState getCurrentState();
  public Date getStartDate();

  public void setStudent(IStudent student);
  public void setCourseCurricularPlan(IPlanoCurricularCurso courseCurricularPlan);
  public void setCurrentState(StudentCurricularPlanState state);
  public void setStartDate(Date startDate);
}
