/*
 * InfoExecutionCourse.java
 *
 * Created on 28 de Novembro de 2002, 3:41
 */

package DataBeans;

import java.io.Serializable;
import java.util.Date;

import Util.Specialization;
import Util.StudentCurricularPlanState;

/**
 *
 * @author  tfc130
 */
public class InfoStudentCurricularPlan extends InfoObject implements Serializable {

	protected InfoStudent infoStudent;
	protected InfoBranch infoBranch;
	protected InfoDegreeCurricularPlan infoDegreeCurricularPlan;
	protected Date startDate;
	protected StudentCurricularPlanState currentState;
	protected Specialization specialization;

	public InfoStudentCurricularPlan() {
		setInfoDegreeCurricularPlan(null);
		setInfoStudent(null);
		setInfoBranch(null);
		setStartDate(null);
		setCurrentState(null);
		setSpecialization(null);
	}

	public InfoStudentCurricularPlan(
		InfoStudent student,
		InfoDegreeCurricularPlan degreeCurricularPlan,
		InfoBranch branch,
		Date startDate,
		StudentCurricularPlanState currentState, Specialization specialization) {
		this();
		setInfoStudent(student);
		setInfoDegreeCurricularPlan(degreeCurricularPlan);
		setInfoBranch(branch);
		setStartDate(startDate);
		setCurrentState(currentState);
		setSpecialization(specialization);

	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoStudentCurricularPlan) {
			InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) obj;
			resultado =
				this.getInfoStudent().equals(infoStudentCurricularPlan.getInfoStudent())
					&& this.getInfoDegreeCurricularPlan().equals(infoStudentCurricularPlan.getInfoDegreeCurricularPlan())
					&& this.getCurrentState().equals(infoStudentCurricularPlan.getCurrentState());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "student = " + this.infoStudent + "; ";
		result += "degreeCurricularPlan = " + this.infoDegreeCurricularPlan + "; ";
		result += "startDate = " + this.startDate + "; ";
		result += "specialization = " + this.specialization + "; ";
		result += "currentState = " + this.currentState + "]\n";
		return result;
	}

	/**
	 * @return StudentCurricularPlanState
	 */
	public StudentCurricularPlanState getCurrentState() {
		return currentState;
	}

	/**
	 * @return InfoBranch
	 */
	public InfoBranch getInfoBranch() {
		return infoBranch;
	}

	/**
	 * @return InfoDegreeCurricularPlan
	 */
	public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
		return infoDegreeCurricularPlan;
	}

	/**
	 * @return InfoStudent
	 */
	public InfoStudent getInfoStudent() {
		return infoStudent;
	}

	/**
	 * @return Date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the currentState.
	 * @param currentState The currentState to set
	 */
	public void setCurrentState(StudentCurricularPlanState currentState) {
		this.currentState = currentState;
	}

	/**
	 * Sets the infoBranch.
	 * @param infoBranch The infoBranch to set
	 */
	public void setInfoBranch(InfoBranch infoBranch) {
		this.infoBranch = infoBranch;
	}

	/**
	 * Sets the infoDegreeCurricularPlan.
	 * @param infoDegreeCurricularPlan The infoDegreeCurricularPlan to set
	 */
	public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
		this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
	}

	/**
	 * Sets the infoStudent.
	 * @param infoStudent The infoStudent to set
	 */
	public void setInfoStudent(InfoStudent infoStudent) {
		this.infoStudent = infoStudent;
	}

	/**
	 * Sets the startDate.
	 * @param startDate The startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return
	 */
	public Specialization getSpecialization() {
		return specialization;
	}

	/**
	 * @param specialization
	 */
	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

}
