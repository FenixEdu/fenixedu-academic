package DataBeans;

import java.io.Serializable;
import java.util.Date;

import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;


/**
 * @author dcs-rjao
 *
 * 22/Abr/2003
 */
public class InfoEnrolmentEvaluation extends InfoObject {
	private String grade;
	private EnrolmentEvaluationType enrolmentEvaluationType;
	private Date examDate;
	private Date gradeAvailableDate;
	private String observation;
	private Date when;
	private EnrolmentEvaluationState state;
	private InfoEnrolment infoEnrolment;
	private InfoPerson infoPersonResponsibleForGrade;
	private InfoPerson infoEmployee;
	
	public InfoEnrolmentEvaluation() {
	}

	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof InfoEnrolmentEvaluation) {
			InfoEnrolmentEvaluation InfoEnrolmentEvaluation = (InfoEnrolmentEvaluation) obj;

			resultado =	this.getInfoEnrolment().equals(InfoEnrolmentEvaluation.getInfoEnrolment())
					&& this.getEnrolmentEvaluationType().equals(InfoEnrolmentEvaluation.getEnrolmentEvaluationType());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "grade = " + this.grade + "; ";
		result += "evaluationType = " + this.enrolmentEvaluationType + "; ";
		result += "examDate = " + this.examDate + "; ";
		result += "infoPersonResponsibleForGrade = " + this.infoPersonResponsibleForGrade + "; ";
		result += "state = " + this.state + "; ";
		result += "infoEnrolment = " + this.infoEnrolment + "; ";
		result += "gradeAvailableDate = " + this.gradeAvailableDate + "]\n";
		result += "employee = " + this.infoEmployee + "]\n";
		result += "when = " + this.when + "]\n";
		return result;
	}


	public EnrolmentEvaluationType getEnrolmentEvaluationType() {
		return enrolmentEvaluationType;
	}

	public Date getExamDate() {
		return examDate;
	}

	public String getGrade() {
		return grade;
	}

	public Date getGradeAvailableDate() {
		return gradeAvailableDate;
	}

	public EnrolmentEvaluationState getState() {
		return state;
	}


	public void setEnrolmentEvaluationType(EnrolmentEvaluationType type) {
		enrolmentEvaluationType = type;
	}

	public void setExamDate(Date date) {
		examDate = date;
	}

	public void setGrade(String string) {
		grade = string;
	}

	public void setGradeAvailableDate(Date date) {
		gradeAvailableDate = date;
	}

	public void setState(EnrolmentEvaluationState state) {
		this.state = state;
	}

	public InfoEnrolment getInfoEnrolment() {
		return infoEnrolment;
	}

	public void setInfoEnrolment(InfoEnrolment enrolment) {
		infoEnrolment = enrolment;
	}

	public InfoPerson getInfoPersonResponsibleForGrade() {
		return infoPersonResponsibleForGrade;
	}

	public void setInfoPersonResponsibleForGrade(InfoPerson person) {
		infoPersonResponsibleForGrade = person;
	}

	/**
	 * @return
	 */
	public String getObservation() {
		return observation;
	}

	/**
	 * @param string
	 */
	public void setObservation(String string) {
		observation = string;
	}

	/**
	 * @return
	 */
	public InfoPerson getInfoEmployee() {
		return infoEmployee;
	}

	/**
	 * @param person
	 */
	public void setInfoEmployee(InfoPerson person) {
		infoEmployee = person;
	}

	/**
	 * @return
	 */
	public Date getWhen() {
		return when;
	}

	/**
	 * @param date
	 */
	public void setWhen(Date date) {
		when = date;
	}

}