package DataBeans.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudentCurricularPlan;

public class InfoEquivalenceContext {
	private List infoCurricularCourseScopesToGiveEquivalence;
	private List infoCurricularCourseScopesToGetEquivalence;
	private InfoExecutionPeriod currentInfoExecutionPeriod;
	private InfoStudentCurricularPlan infoStudentCurricularPlan;
	private List chosenInfoCurricularCourseScopesToGiveEquivalence;
	private List chosenInfoCurricularCourseScopesToGetEquivalence;
	private boolean success;
	private List errorMessages;
	
	public InfoEquivalenceContext() {
	}

	/**
	 * @return
	 */
	public List getInfoCurricularCourseScopesToGetEquivalence() {
		return infoCurricularCourseScopesToGetEquivalence;
	}

	/**
	 * @return
	 */
	public List getInfoCurricularCourseScopesToGiveEquivalence() {
		return infoCurricularCourseScopesToGiveEquivalence;
	}

	/**
	 * @param list
	 */
	public void setInfoCurricularCourseScopesToGetEquivalence(List list) {
		infoCurricularCourseScopesToGetEquivalence = list;
	}

	/**
	 * @param list
	 */
	public void setInfoCurricularCourseScopesToGiveEquivalence(List list) {
		infoCurricularCourseScopesToGiveEquivalence = list;
	}

	/**
	 * @return
	 */
	public InfoExecutionPeriod getCurrentInfoExecutionPeriod() {
		return currentInfoExecutionPeriod;
	}

	/**
	 * @return
	 */
	public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
		return infoStudentCurricularPlan;
	}

	/**
	 * @param period
	 */
	public void setCurrentInfoExecutionPeriod(InfoExecutionPeriod period) {
		currentInfoExecutionPeriod = period;
	}

	/**
	 * @param plan
	 */
	public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan plan) {
		infoStudentCurricularPlan = plan;
	}

	/**
	 * @return
	 */
	public List getChosenInfoCurricularCourseScopesToGetEquivalence() {
		return chosenInfoCurricularCourseScopesToGetEquivalence;
	}

	/**
	 * @return
	 */
	public List getChosenInfoCurricularCourseScopesToGiveEquivalence() {
		return chosenInfoCurricularCourseScopesToGiveEquivalence;
	}

	/**
	 * @param list
	 */
	public void setChosenInfoCurricularCourseScopesToGetEquivalence(List list) {
		chosenInfoCurricularCourseScopesToGetEquivalence = list;
	}

	/**
	 * @param list
	 */
	public void setChosenInfoCurricularCourseScopesToGiveEquivalence(List list) {
		chosenInfoCurricularCourseScopesToGiveEquivalence = list;
	}

	/**
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param b
	 */
	public void setSuccess(boolean b) {
		success = b;
	}

	/**
	 * @return
	 */
	public List getErrorMessages() {
		return errorMessages;
	}

	/**
	 * @param list
	 */
	public void setErrorMessages(List list) {
		errorMessages = list;
	}

	/**
	 * @param string
	 */
	public void setErrorMessage(String string) {
		if(errorMessages == null) {
			errorMessages = new ArrayList();
		}
		errorMessages.add(string);
	}

}