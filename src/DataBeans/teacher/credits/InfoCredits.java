package DataBeans.teacher.credits;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoObject;
import DataBeans.InfoTeacher;

/**
 * @author Tânia Pousão
 *
 */
public class InfoCredits extends InfoObject {
	private InfoTeacher infoTeacher = null;
	private InfoExecutionPeriod infoExecutionPeriod = null;
	private Integer tfcStudentsNumber = null;
	private Double credits = null;
	
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
