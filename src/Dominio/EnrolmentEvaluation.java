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
	private EnrolmentEvaluationType evaluationType;
	private Date examDate;
	private Date gradeAvailableDate;
	private EnrolmentEvaluationState state;
	private IEnrolment enrolment;
	private ITeacher responsibleTeacher;
	
	private Integer enrolmentKey;
	private Integer responsibleTeacherKey;
		
	public EnrolmentEvaluation() {
	}

	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof IEnrolmentEvaluation) {
			IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) obj;

			resultado =	this.getEnrolment().equals(enrolmentEvaluation.getEnrolment())
					&& this.getEvaluationType().equals(enrolmentEvaluation.getEvaluationType());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "grade = " + this.grade + "; ";
		result += "evaluationType = " + this.evaluationType + "; ";
		result += "examDate = " + this.examDate + "; ";
		result += "responsibleTeacher = " + this.responsibleTeacher + "; ";
		result += "state = " + this.state + "; ";
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

	public void setEnrolment(IEnrolment enrolment) {
		this.enrolment = enrolment;
	}

	public void setEnrolmentKey(Integer integer) {
		enrolmentKey = integer;
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

	public ITeacher getResponsibleTeacher() {
		return responsibleTeacher;
	}

	public Integer getResponsibleTeacherKey() {
		return responsibleTeacherKey;
	}

	public void setResponsibleTeacher(ITeacher teacher) {
		responsibleTeacher = teacher;
	}

	public void setResponsibleTeacherKey(Integer integer) {
		responsibleTeacherKey = integer;
	}

}