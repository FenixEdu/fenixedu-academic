package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
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
	}
	
	public ExecutionPeriodStatisticsBean(ExecutionPeriod executionPeriod, List<Enrolment> enrolmentsWithinExecutionPeriod) {
		setExecutionPeriod(executionPeriod);
		setEnrolmentsWithinExecutionPeriod(enrolmentsWithinExecutionPeriod);
		
		fillRequiredInfo();
	}

	private void fillRequiredInfo() {
		setTotalEnrolmentsNumber();
		setApprovedEnrolmentsNumber();
		calculateApprovedRatio();
		calculateAritmeticAverage();
	}
	
	public ExecutionPeriod getExecutionPeriod() {
		return (executionPeriod == null ? null : executionPeriod.getObject());
	}

	public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
		this.executionPeriod = new DomainReference<ExecutionPeriod>(executionPeriod);
	}
	
	private List<Enrolment> getEnrolmentsWithinExecutionPeriod() {
		return this.enrolmentsWithinExecutionPeriod;
	}

	public void setEnrolmentsWithinExecutionPeriod(List<Enrolment> enrolmentsWithinExecutionPeriod){
		this.enrolmentsWithinExecutionPeriod = enrolmentsWithinExecutionPeriod;
	}
	
	public Integer getTotalEnrolmentsNumber() {
		return totalEnrolmentsNumber;
	}

	public void setTotalEnrolmentsNumber() {
		this.totalEnrolmentsNumber = getEnrolmentsWithinExecutionPeriod().size();
	}
	
	public Integer getApprovedEnrolmentsNumber() {
		return approvedEnrolmentsNumber;
	}

	public void setApprovedEnrolmentsNumber() {
		int approvedEnrolmentsNumber = 0;
		for(Enrolment enrolment : getEnrolmentsWithinExecutionPeriod()) {
			if(enrolment.isApproved()) {
				approvedEnrolmentsNumber++;
			}
		}
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

}
