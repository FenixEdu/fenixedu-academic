package DataBeans.teacher.credits;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoObject;
import DataBeans.InfoTeacher;

/**
 * @author Tânia Pousão
 *
 */
public class InfoCredits extends InfoObject {
	private InfoTeacher infoTeacher;
	private InfoExecutionPeriod infoExecutionPeriod;
	private Integer tfcStudentsNumber;
	private Double credits;
	private Double additionalCredits;
	private String additionalCreditsJustification;
	
	/**
	 * @return
	 */
	public Double getAdditionalCredits() {
		return this.additionalCredits;
	}

	/**
	 * @param additionalCredits
	 */
	public void setAdditionalCredits(Double additionalCredits) {
		this.additionalCredits = additionalCredits;
	}

	/**
	 * @return
	 */
	public String getAdditionalCreditsJustification() {
		return this.additionalCreditsJustification;
	}

	/**
	 * @param additionalCreditsJustification
	 */
	public void setAdditionalCreditsJustification(String additionalCreditsJustification) {
		this.additionalCreditsJustification = additionalCreditsJustification;
	}

	/**
	 * @return
	 */
	public Double getCredits() {
		return credits;
	}

	/**
	 * @return
	 */
	public InfoExecutionPeriod getInfoExecutionPeriod() {
		return infoExecutionPeriod;
	}

	/**
	 * @return
	 */
	public InfoTeacher getInfoTeacher() {
		return infoTeacher;
	}

	/**
	 * @return
	 */
	public Integer getTfcStudentsNumber() {
		return tfcStudentsNumber;
	}

	/**
	 * @param Doubler1
	 */
	public void setCredits(Double double1) {
		credits = double1;
	}

	/**
	 * @param period
	 */
	public void setInfoExecutionPeriod(InfoExecutionPeriod period) {
		infoExecutionPeriod = period;
	}

	/**
	 * @param teacher
	 */
	public void setInfoTeacher(InfoTeacher teacher) {
		infoTeacher = teacher;
	}

	/**
	 * @param Integer1
	 */
	public void setTfcStudentsNumber(Integer integer1) {
		tfcStudentsNumber = integer1;
	}

}
