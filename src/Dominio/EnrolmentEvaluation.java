package Dominio;

import java.util.Date;

import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;


/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class EnrolmentEvaluation extends DomainObject implements IEnrolmentEvaluation {

	private String grade;
	private EnrolmentEvaluationType enrolmentEvaluationType;
	private Date examDate;
	private Date gradeAvailableDate;
	private EnrolmentEvaluationState enrolmentEvaluationState;
	private IEnrolment enrolment;
	private IPessoa personResponsibleForGrade;
	
	private Integer enrolmentKey;
	private Integer personResponsibleForGradeKey;
		
	public EnrolmentEvaluation() {
	}

	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof IEnrolmentEvaluation) {
			IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) obj;

			resultado =	this.getEnrolment().equals(enrolmentEvaluation.getEnrolment())
					&& this.getEnrolmentEvaluationType().equals(enrolmentEvaluation.getEnrolmentEvaluationType());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "grade = " + this.grade + "; ";
		result += "enrolmentEvaluationType = " + this.enrolmentEvaluationType + "; ";
		result += "examDate = " + this.examDate + "; ";
		result += "personResponsibleForGrade = " + this.personResponsibleForGrade + "; ";
		result += "enrolmentEvaluationState = " + this.enrolmentEvaluationState + "; ";
		result += "enrolment = " + this.enrolment + "; ";
		result += "gradeAvailableDate = " + this.gradeAvailableDate + "]\n";
		return result;
	}
	public IEnrolment getEnrolment() {
		return enrolment;
	}

	public Integer getEnrolmentKey() {
		return enrolmentKey;
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

	public EnrolmentEvaluationState getEnrolmentEvaluationState() {
		return enrolmentEvaluationState;
	}

	public void setEnrolment(IEnrolment enrolment) {
		this.enrolment = enrolment;
	}

	public void setEnrolmentKey(Integer integer) {
		enrolmentKey = integer;
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

	public void setEnrolmentEvaluationState(EnrolmentEvaluationState state) {
		this.enrolmentEvaluationState = state;
	}

	public IPessoa getPersonResponsibleForGrade() {
		return personResponsibleForGrade;
	}

	public Integer getPersonResponsibleForGradeKey() {
		return personResponsibleForGradeKey;
	}

	public void setPersonResponsibleForGrade(IPessoa person) {
		personResponsibleForGrade = person;
	}

	public void setPersonResponsibleForGradeKey(Integer integer) {
		personResponsibleForGradeKey = integer;
	}

}