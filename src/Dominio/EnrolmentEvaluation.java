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
	private Date when;
	private String checkSum;
	private String observation;
	
	private IEnrolment enrolment;
	private IPessoa personResponsibleForGrade;
	private IEmployee employee; 
	
	private Integer enrolmentKey;
	private Integer personResponsibleForGradeKey;
	private Integer employeeKey;
		
	public EnrolmentEvaluation() {
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if(obj instanceof IEnrolmentEvaluation)
		{
			IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) obj;
			result = this.getEnrolment().equals(enrolmentEvaluation.getEnrolment()) &&
					this.getEnrolmentEvaluationType().equals(enrolmentEvaluation.getEnrolmentEvaluationType()) &&
					this.getGrade().equals(enrolmentEvaluation.getGrade()) &&
					this.getWhen().equals(enrolmentEvaluation.getWhen());
		}
		return result;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "grade = " + this.grade + "; ";
		result += "enrolmentEvaluationType = " + this.enrolmentEvaluationType + "; ";
		result += "examDate = " + this.examDate + "; ";
		result += "personResponsibleForGrade = " + this.personResponsibleForGrade + "; ";
		result += "enrolmentEvaluationState = " + this.enrolmentEvaluationState + "; ";
		result += "when = " + this.when + "; ";
		result += "checkSum = " + this.checkSum + "; ";
		result += "enrolment = " + this.enrolment + "; ";
		result += "gradeAvailableDate = " + this.gradeAvailableDate + "]\n";
		result += "employee = " + this.employee + "; ";
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

	public IEmployee getEmployee() {
		return employee;
	}

	public Integer getEmployeeKey() {
		return employeeKey;
	}

	public void setEmployee(IEmployee employee) {
		this.employee = employee;
	}

	public void setEmployeeKey(Integer integer) {
		employeeKey = integer;
	}

	public String getCheckSum() {
		return checkSum;
	}

	public Date getWhen() {
		return when;
	}

	public void setCheckSum(String string) {
		checkSum = string;
	}

	public void setWhen(Date date) {
		when = date;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String string) {
		observation = string;
	}

}