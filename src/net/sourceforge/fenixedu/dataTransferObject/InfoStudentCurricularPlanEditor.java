package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;

public class InfoStudentCurricularPlanEditor extends InfoObject implements Serializable {

    protected InfoStudent infoStudent;

    protected InfoBranch infoBranch;

    protected InfoDegreeCurricularPlan infoDegreeCurricularPlan;

    protected Date startDate;

    protected StudentCurricularPlanState currentState;

    protected Specialization specialization;

    protected Double givenCredits;

    protected Double classification;

    protected Integer enrolledCourses;

    protected Integer completedCourses;

    protected Date when;

    protected InfoPerson infoEmployee;

    protected String observations;

    protected List infoEnrolments;

    protected InfoBranch infoSecundaryBranch;

    public InfoStudentCurricularPlanEditor() {
    }

	public Double getClassification() {
		return classification;
	}

	public void setClassification(Double classification) {
		this.classification = classification;
	}

	public Integer getCompletedCourses() {
		return completedCourses;
	}

	public void setCompletedCourses(Integer completedCourses) {
		this.completedCourses = completedCourses;
	}

	public StudentCurricularPlanState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(StudentCurricularPlanState currentState) {
		this.currentState = currentState;
	}

	public Integer getEnrolledCourses() {
		return enrolledCourses;
	}

	public void setEnrolledCourses(Integer enrolledCourses) {
		this.enrolledCourses = enrolledCourses;
	}

	public Double getGivenCredits() {
		return givenCredits;
	}

	public void setGivenCredits(Double givenCredits) {
		this.givenCredits = givenCredits;
	}

	public InfoBranch getInfoBranch() {
		return infoBranch;
	}

	public void setInfoBranch(InfoBranch infoBranch) {
		this.infoBranch = infoBranch;
	}

	public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
		return infoDegreeCurricularPlan;
	}

	public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
		this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
	}

	public InfoPerson getInfoEmployee() {
		return infoEmployee;
	}

	public void setInfoEmployee(InfoPerson infoEmployee) {
		this.infoEmployee = infoEmployee;
	}

	public List getInfoEnrolments() {
		return infoEnrolments;
	}

	public void setInfoEnrolments(List infoEnrolments) {
		this.infoEnrolments = infoEnrolments;
	}

	public InfoBranch getInfoSecundaryBranch() {
		return infoSecundaryBranch;
	}

	public void setInfoSecundaryBranch(InfoBranch infoSecundaryBranch) {
		this.infoSecundaryBranch = infoSecundaryBranch;
	}

	public InfoStudent getInfoStudent() {
		return infoStudent;
	}

	public void setInfoStudent(InfoStudent infoStudent) {
		this.infoStudent = infoStudent;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public Specialization getSpecialization() {
		return specialization;
	}

	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getWhen() {
		return when;
	}

	public void setWhen(Date when) {
		this.when = when;
	}

}