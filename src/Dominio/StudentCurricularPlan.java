/*
 * StudentCurricularPlan.java
 *
 * Created on 21 de December de 2002, 16:39
 */

package Dominio;

import java.util.Date;

import Util.StudentCurricularPlanState;

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

public class StudentCurricularPlan implements IStudentCurricularPlan {

  protected Integer internalCode;
  protected Integer studentKey;
  protected Integer courseCurricularPlanKey;

  protected IStudent student;
  protected IDegreeCurricularPlan courseCurricularPlan;
  protected Date startDate;
  protected StudentCurricularPlanState currentState;
    
    
  /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
  public StudentCurricularPlan() { }
    
  public StudentCurricularPlan(IStudent student, IDegreeCurricularPlan courseCurricularPlan, 
  			Date startDate, StudentCurricularPlanState currentState) {

	this.student = student;
	this.courseCurricularPlan = courseCurricularPlan;
	this.startDate = startDate;
	this.currentState = currentState;
  }
    

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof IStudentCurricularPlan) {
      IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan)obj;
      resultado = 
      ((getStudent().getNumber() == studentCurricularPlan.getStudent().getNumber()) &&
       (getCourseCurricularPlan().getDegree().getSigla().equals(studentCurricularPlan.getCourseCurricularPlan().getDegree().getSigla())) &&
       (getCurrentState().getState().intValue() == studentCurricularPlan.getCurrentState().getState().intValue()));
    } 
    return resultado;
  }
  
  public String toString() {
    String result = "[STUDENT_CURRICULAR_PLAN";
    result += ", Internal Code=" + internalCode;
    result += ", Student=" + student;
    result += ", CourseCurricularPlan=" + courseCurricularPlan;
    result += ", Start Date=" + startDate;
    result += ", State=" + currentState.toString();

    result += "]";
    return result;
  }
   
    
	/**
	 * Returns the courseCurricularPlan.
	 * @return IDegreeCurricularPlan
	 */
	public IDegreeCurricularPlan getCourseCurricularPlan() {
		return courseCurricularPlan;
	}
	
	/**
	 * Returns the courseCurricularPlanKey.
	 * @return Integer
	 */
	public Integer getCourseCurricularPlanKey() {
		return courseCurricularPlanKey;
	}
	
	/**
	 * Returns the currentState.
	 * @return StudentCurricularPlanState
	 */
	public StudentCurricularPlanState getCurrentState() {
		return currentState;
	}
	
	/**
	 * Returns the internalCode.
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
	}
	
	/**
	 * Returns the startDate.
	 * @return Date
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * Returns the student.
	 * @return IStudent
	 */
	public IStudent getStudent() {
		return student;
	}
	
	/**
	 * Returns the studentKey.
	 * @return Integer
	 */
	public Integer getStudentKey() {
		return studentKey;
	}
	
	/**
	 * Sets the courseCurricularPlan.
	 * @param courseCurricularPlan The courseCurricularPlan to set
	 */
	public void setCourseCurricularPlan(IDegreeCurricularPlan courseCurricularPlan) {
		this.courseCurricularPlan = courseCurricularPlan;
	}
	
	/**
	 * Sets the courseCurricularPlanKey.
	 * @param courseCurricularPlanKey The courseCurricularPlanKey to set
	 */
	public void setCourseCurricularPlanKey(Integer courseCurricularPlanKey) {
		this.courseCurricularPlanKey = courseCurricularPlanKey;
	}
	
	/**
	 * Sets the currentState.
	 * @param currentState The currentState to set
	 */
	public void setCurrentState(StudentCurricularPlanState currentState) {
		this.currentState = currentState;
	}
	
	/**
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}
	
	/**
	 * Sets the startDate.
	 * @param startDate The startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * Sets the student.
	 * @param student The student to set
	 */
	public void setStudent(IStudent student) {
		this.student = student;
	}
	
	/**
	 * Sets the studentKey.
	 * @param studentKey The studentKey to set
	 */
	public void setStudentKey(Integer studentKey) {
		this.studentKey = studentKey;
	}

}
