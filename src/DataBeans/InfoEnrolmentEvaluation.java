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
public class InfoEnrolmentEvaluation implements Serializable {
	private String grade;
	private EnrolmentEvaluationType evaluationType;
	private Date examDate;
	private Date gradeAvailableDate;
	private EnrolmentEvaluationState state;
	private InfoEnrolment infoEnrolment;
	private InfoPerson infoPersonResponsibleForGrade;
	
	public InfoEnrolmentEvaluation() {
	}

	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof InfoEnrolmentEvaluation) {
			InfoEnrolmentEvaluation InfoEnrolmentEvaluation = (InfoEnrolmentEvaluation) obj;

			resultado =	this.getInfoEnrolment().equals(InfoEnrolmentEvaluation.getInfoEnrolment())
					&& this.getEvaluationType().equals(InfoEnrolmentEvaluation.getEvaluationType());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "grade = " + this.grade + "; ";
		result += "evaluationType = " + this.evaluationType + "; ";
		result += "examDate = " + this.examDate + "; ";
		result += "infoPersonResponsibleForGrade = " + this.infoPersonResponsibleForGrade + "; ";
		result += "state = " + this.state + "; ";
		result += "infoEnrolment = " + this.infoEnrolment + "; ";
		result += "gradeAvailableDate = " + this.gradeAvailableDate + "]\n";
		return result;
	}


	public EnrolmentEvaluationType getEvaluationType() {
		return evaluationType;
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


	public void setEvaluationType(EnrolmentEvaluationType type) {
		evaluationType = type;
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

}