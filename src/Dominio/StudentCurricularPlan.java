package Dominio;

import java.util.Date;
import java.util.List;

import Util.StudentCurricularPlanState;

public class StudentCurricularPlan implements IStudentCurricularPlan {

	protected Integer internalCode;
	protected Integer studentKey;
	protected Integer degreeCurricularPlanKey;

	protected IStudent student;
	protected IDegreeCurricularPlan degreeCurricularPlan;
	protected Date startDate;
	protected StudentCurricularPlanState currentState;

	private List associatedBranches;

	public StudentCurricularPlan() {
		setInternalCode(null);
		setStudent(null);
		setDegreeCurricularPlan(null);
		setStartDate(null);
		setCurrentState(null);
		setDegreeCurricularPlanKey(null);
		setStudentKey(null);
	}

	public StudentCurricularPlan(IStudent student, IDegreeCurricularPlan degreeCurricularPlan, Date startDate, StudentCurricularPlanState currentState) {
		setStudent(student);
		setDegreeCurricularPlan(degreeCurricularPlan);
		setStartDate(startDate);
		setCurrentState(currentState);
		setInternalCode(null);
		setDegreeCurricularPlanKey(null);
		setStudentKey(null);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof IStudentCurricularPlan) {
			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) obj;
			resultado = this.getStudent().equals(studentCurricularPlan.getStudent()) &&
									this.getDegreeCurricularPlan().equals(studentCurricularPlan.getDegreeCurricularPlan()) &&
									this.getCurrentState().equals(studentCurricularPlan.getCurrentState());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "internalCode = " + this.internalCode + "; ";
		result += "student = " + this.student + "; ";
		result += "degreeCurricularPlan = " + this.degreeCurricularPlan + "; ";
		result += "startDate = " + this.startDate + "; ";
		result += "currentState = " + this.currentState + "]\n";
		return result;
	}

	/**
	 * Returns the degreeCurricularPlan.
	 * @return IDegreeCurricularPlan
	 */
	public IDegreeCurricularPlan getDegreeCurricularPlan() {
		return degreeCurricularPlan;
	}

	/**
	 * Returns the degreeCurricularPlanKey.
	 * @return Integer
	 */
	public Integer getDegreeCurricularPlanKey() {
		return degreeCurricularPlanKey;
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
	 * Sets the degreeCurricularPlan.
	 * @param degreeCurricularPlan The degreeCurricularPlan to set
	 */
	public void setDegreeCurricularPlan(IDegreeCurricularPlan courseCurricularPlan) {
		this.degreeCurricularPlan = courseCurricularPlan;
	}

	/**
	 * Sets the degreeCurricularPlanKey.
	 * @param degreeCurricularPlanKey The degreeCurricularPlanKey to set
	 */
	public void setDegreeCurricularPlanKey(Integer courseCurricularPlanKey) {
		this.degreeCurricularPlanKey = courseCurricularPlanKey;
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

	/**
	 * @return List
	 */
	public List getAssociatedBranches() {
		return associatedBranches;
	}

	/**
	 * Sets the associatedBranches.
	 * @param associatedBranches The associatedBranches to set
	 */
	public void setAssociatedBranches(List associatedBranches) {
		this.associatedBranches = associatedBranches;
	}

}
