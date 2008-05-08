package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;

public class SecondCycleIndividualCandidacyResultBean implements Serializable {

    private DomainReference<SecondCycleIndividualCandidacyProcess> candidacyProcess;

    private Integer professionalExperience;

    private Double affinity;

    private Integer degreeNature;

    private Double grade;

    private String interviewGrade; // NA or value

    private Double seriesGrade;

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

    public Double getAffinity() {
	return affinity;
    }

    public void setAffinity(Double affinity) {
	this.affinity = affinity;
    }

    public Integer getDegreeNature() {
	return degreeNature;
    }

    public void setDegreeNature(Integer degreeNature) {
	this.degreeNature = degreeNature;
    }

    public Double getGrade() {
	return grade;
    }

    public void setGrade(Double grade) {
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

    public Double getSeriesGrade() {
	return seriesGrade;
    }

    public void setSeriesGrade(Double seriesGrade) {
	this.seriesGrade = seriesGrade;
    }

    public IndividualCandidacyState getState() {
	return state;
    }

    public void setState(IndividualCandidacyState state) {
	this.state = state;
    }

    public Double calculateGrade() {
	final CandidacyPrecedentDegreeInformation information = getCandidacyProcess().getCandidacyPrecedentDegreeInformation();
	final Double mfc = Double.valueOf(information.getConclusionGrade());
	return (0.4 * getAffinity() + 0.3 * getDegreeNature() / 5 + 0.3 * (mfc * 10 + getProfessionalExperience()) / 200) * 200;
    }

    public Double calculateSeriesGrade() {
	final Double grade = calculateGrade();
	return hasInterviewGrade() ? (0.7 * grade + 0.3 * Double.valueOf(getInterviewGrade())) : grade;
    }

    private boolean hasInterviewGrade() {
	return getInterviewGrade() != null && getInterviewGrade().matches("[0-9]+(\\.[0-9]+)?")
		&& Double.valueOf(getInterviewGrade()) != 0d;
    }
}
