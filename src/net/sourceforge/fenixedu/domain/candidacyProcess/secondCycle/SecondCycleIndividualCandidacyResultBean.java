package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;

public class SecondCycleIndividualCandidacyResultBean implements Serializable {

    private DomainReference<SecondCycleIndividualCandidacyProcess> candidacyProcess;
    private Integer professionalExperience;
    private BigDecimal affinity;
    private Integer degreeNature;
    private BigDecimal grade;
    private String interviewGrade; // NA or value
    private BigDecimal seriesGrade;
    private IndividualCandidacyState state;
    private String notes;

    public SecondCycleIndividualCandidacyResultBean(final SecondCycleIndividualCandidacyProcess process) {
	setCandidacyProcess(process);
	setProfessionalExperience(process.getCandidacyProfessionalExperience());
	setAffinity(process.getCandidacyAffinity());
	setDegreeNature(process.getCandidacyDegreeNature());
	setGrade(process.getCandidacyGrade());
	setInterviewGrade(process.getCandidacyInterviewGrade());
	setSeriesGrade(process.getCandidacySeriesGrade());
	if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
	    setState(process.getCandidacyState());
	}
	setNotes(process.getCandidacyNotes());
    }

    public SecondCycleIndividualCandidacyProcess getCandidacyProcess() {
	return (this.candidacyProcess != null) ? this.candidacyProcess.getObject() : null;
    }

    public void setCandidacyProcess(final SecondCycleIndividualCandidacyProcess candidacyProcess) {
	this.candidacyProcess = (candidacyProcess != null) ? new DomainReference<SecondCycleIndividualCandidacyProcess>(
		candidacyProcess) : null;
    }

    public BigDecimal getAffinity() {
	return affinity;
    }

    public void setAffinity(BigDecimal affinity) {
	this.affinity = affinity;
    }

    public Integer getDegreeNature() {
	return degreeNature;
    }

    public void setDegreeNature(Integer degreeNature) {
	this.degreeNature = degreeNature;
    }

    public BigDecimal getGrade() {
	return grade;
    }

    public void setGrade(BigDecimal grade) {
	this.grade = grade;
    }

    public String getInterviewGrade() {
	return interviewGrade;
    }

    public void setInterviewGrade(String interviewGrade) {
	this.interviewGrade = interviewGrade;
    }

    public String getNotes() {
	return notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    public Integer getProfessionalExperience() {
	return professionalExperience;
    }

    public void setProfessionalExperience(Integer professionalExperience) {
	this.professionalExperience = professionalExperience;
    }

    public BigDecimal getSeriesGrade() {
	return seriesGrade;
    }

    public void setSeriesGrade(BigDecimal seriesGrade) {
	this.seriesGrade = seriesGrade;
    }

    public IndividualCandidacyState getState() {
	return state;
    }

    public void setState(IndividualCandidacyState state) {
	this.state = state;
    }
}
