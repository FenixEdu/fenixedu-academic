package DataBeans.equivalence;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;

public class InfoEquivalenceContext {
	private List infoEnrolmentsToGiveEquivalence;
	private List infoCurricularCourseScopesToGetEquivalence;
	private InfoExecutionPeriod currentInfoExecutionPeriod;
	private InfoStudentCurricularPlan infoStudentCurricularPlan;
	private List chosenInfoEnrolmentsToGiveEquivalence;
	private List chosenInfoCurricularCourseScopesToGetEquivalence;
	private IUserView responsible;
	private boolean success;
	private List errorMessages;
	private List chosenInfoCurricularCourseScopesToGetEquivalenceWithGrade;
	
	public InfoEquivalenceContext() {
	}

	/**
	 * @return
	 */
	public List getInfoCurricularCourseScopesToGetEquivalence() {
		return infoCurricularCourseScopesToGetEquivalence;
	}

	/**
	 * @param list
	 */
	public void setInfoCurricularCourseScopesToGetEquivalence(List list) {
		infoCurricularCourseScopesToGetEquivalence = list;
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
	 * @param list
	 */
	public void setChosenInfoCurricularCourseScopesToGetEquivalence(List list) {
		chosenInfoCurricularCourseScopesToGetEquivalence = list;
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

	/**
	 * @return
	 */
	public IUserView getResponsible() {
		return responsible;
	}

	/**
	 * @param view
	 */
	public void setResponsible(IUserView view) {
		responsible = view;
	}

	/**
	 * @return
	 */
	public List getInfoEnrolmentsToGiveEquivalence() {
		return infoEnrolmentsToGiveEquivalence;
	}

	/**
	 * @param list
	 */
	public void setInfoEnrolmentsToGiveEquivalence(List list) {
		infoEnrolmentsToGiveEquivalence = list;
	}

	/**
	 * @return
	 */
	public List getChosenInfoEnrolmentsToGiveEquivalence() {
		return chosenInfoEnrolmentsToGiveEquivalence;
	}

	/**
	 * @param list
	 */
	public void setChosenInfoEnrolmentsToGiveEquivalence(List list) {
		chosenInfoEnrolmentsToGiveEquivalence = list;
	}

	/**
	 * @return
	 */
	public List getChosenInfoCurricularCourseScopesToGetEquivalenceWithGrade() {
		return chosenInfoCurricularCourseScopesToGetEquivalenceWithGrade;
	}

	/**
	 * @param list
	 */
	public void setChosenInfoCurricularCourseScopesToGetEquivalenceWithGrade(List list) {
		chosenInfoCurricularCourseScopesToGetEquivalenceWithGrade = list;
	}

}