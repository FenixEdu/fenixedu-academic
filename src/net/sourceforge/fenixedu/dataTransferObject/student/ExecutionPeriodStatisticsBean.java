package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;

public class ExecutionPeriodStatisticsBean implements Serializable {
	
	private DomainReference<ExecutionPeriod> executionPeriod;
	private List<Enrolment> enrolmentsWithinExecutionPeriod;
	private Integer totalEnrolmentsNumber;
	private Integer approvedEnrolmentsNumber;
	private Integer approvedRatio;
	private Double aritmeticAverage;
	
	
	public ExecutionPeriodStatisticsBean() {
		setExecutionPeriod(null);
		this.totalEnrolmentsNumber = 0;
		this.approvedEnrolmentsNumber = 0;
		this.approvedRatio = 0;
		this.aritmeticAverage = 0.0;
	}
	
	public ExecutionPeriodStatisticsBean(ExecutionPeriod executionPeriod) {
		setExecutionPeriod(executionPeriod);
		this.enrolmentsWithinExecutionPeriod = new ArrayList<Enrolment>();
	}
	
	public ExecutionPeriod getExecutionPeriod() {
		return (executionPeriod == null ? null : executionPeriod.getObject());
	}

	public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
		this.executionPeriod = new DomainReference<ExecutionPeriod>(executionPeriod);
	}
	
	public List<Enrolment> getEnrolmentsWithinExecutionPeriod() {
		return this.enrolmentsWithinExecutionPeriod;
	}

	public void setEnrolmentsWithinExecutionPeriod(List<Enrolment> enrolmentsWithinExecutionPeriod){
		this.enrolmentsWithinExecutionPeriod = enrolmentsWithinExecutionPeriod;
	}
	
	public Integer getTotalEnrolmentsNumber() {
		return totalEnrolmentsNumber;
	}

	public void setTotalEnrolmentsNumber(Integer totalEnrolmentsNumber) {
		this.totalEnrolmentsNumber = totalEnrolmentsNumber;
	}
	
	public Integer getApprovedEnrolmentsNumber() {
		return approvedEnrolmentsNumber;
	}

	public void setApprovedEnrolmentsNumber(Integer approvedEnrolmentsNumber) {
		this.approvedEnrolmentsNumber = approvedEnrolmentsNumber;
	}
	
	public Integer getApprovedRatio() {
		return approvedRatio;
	}

	public void setApprovedRatio(Integer approvedRatio) {
		this.approvedRatio = approvedRatio;
	}
	
	public Double getAritmeticAverage() {
		return aritmeticAverage;
	}

	public void setAritmeticAverage(Double aritmeticAverage) {
		this.aritmeticAverage = aritmeticAverage;
	}
	
	private void calculateTotalEnrolmentsNumber() {
		setTotalEnrolmentsNumber(getEnrolmentsWithinExecutionPeriod().size());
	}
	
	private void calculateApprovedEnrolmentsNumber() {
		int approvedEnrolmentsNumber = 0;
		for(Enrolment enrolment : getEnrolmentsWithinExecutionPeriod()) {
			if(enrolment.isApproved()) {
				approvedEnrolmentsNumber++;
			}
		}
		setApprovedEnrolmentsNumber(approvedEnrolmentsNumber);
	}
	
	private void calculateAritmeticAverage() {
		int concludedCurricularCourses = 0, gradesAcumulator = 0;
		double aritmeticAverage = 0;
		
		for(Enrolment enrolment : getEnrolmentsWithinExecutionPeriod()) {
			if(enrolment.isApproved() && enrolment.getFinalGrade() != null) {
				concludedCurricularCourses++;
				gradesAcumulator+=enrolment.getFinalGrade();
			}
		}
		aritmeticAverage = ((double)gradesAcumulator / (double)concludedCurricularCourses);
		setAritmeticAverage((int)(aritmeticAverage*100)/100.0);		
	}
	
	private void calculateApprovedRatio() {
		int concludedEnrolments = 0;

		for(Enrolment enrolment : getEnrolmentsWithinExecutionPeriod()) {			
			if(enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
				concludedEnrolments++;
			}
		}
		int ratio = Math.round(((float)concludedEnrolments / (float)getTotalEnrolmentsNumber()) * 100);	
		setApprovedRatio(ratio);
	}
	
	public void addEnrolmentsWithinExecutionPeriod(List<Enrolment> enrolments) {
		//Checks if already exists enrolments for this execution period
		if(!this.enrolmentsWithinExecutionPeriod.isEmpty()) {
			for (Enrolment newEnrolment : enrolments) {
				StudentCurricularPlan newEnrolmentSCP = newEnrolment.getStudentCurricularPlan();
				Enrolment previousAddedEnrolment = getPreviousEnrolmentGivenCurricularCourse(newEnrolment.getCurricularCourse());
				//Checks if exists a repeated enrolment
				if(previousAddedEnrolment != null) {
					StudentCurricularPlan previousEnrolmentSCP = previousAddedEnrolment.getStudentCurricularPlan();
					if(newEnrolmentSCP.getStartExecutionYear().isAfterOrEquals(previousEnrolmentSCP.getStartExecutionYear())) {
						this.enrolmentsWithinExecutionPeriod.remove(previousAddedEnrolment);
						this.enrolmentsWithinExecutionPeriod.add(newEnrolment);
					}
				}
				else {
					this.enrolmentsWithinExecutionPeriod.add(newEnrolment);
				}
			}
		}
		else {
			this.enrolmentsWithinExecutionPeriod = enrolments;
		}
		recalculateStatistics();
	}
	
	private Enrolment getPreviousEnrolmentGivenCurricularCourse(CurricularCourse curricular) {
		for (Enrolment enrolment : this.enrolmentsWithinExecutionPeriod) {
			if(enrolment.getCurricularCourse().equals(curricular)) {
				return enrolment;
			}
		}
		return null;
	}
	
	private void recalculateStatistics() {
		calculateTotalEnrolmentsNumber();
		calculateApprovedEnrolmentsNumber();
		calculateApprovedRatio();
		calculateAritmeticAverage();
	}

}
