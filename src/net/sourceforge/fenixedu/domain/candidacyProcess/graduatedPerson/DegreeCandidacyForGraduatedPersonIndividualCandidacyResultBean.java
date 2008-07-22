package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;

public class DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean implements Serializable {

    private DomainReference<DegreeCandidacyForGraduatedPersonIndividualProcess> candidacyProcess;
    private BigDecimal affinity;
    private Integer degreeNature;
    private BigDecimal grade;
    private IndividualCandidacyState state;

    public DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean(
	    final DegreeCandidacyForGraduatedPersonIndividualProcess process) {
	setCandidacyProcess(process);
	setAffinity(process.getCandidacyAffinity());
	setDegreeNature(process.getCandidacyDegreeNature());
	setGrade(process.getCandidacyGrade());
	if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
	    setState(process.getCandidacyState());
	}
    }

    public DegreeCandidacyForGraduatedPersonIndividualProcess getCandidacyProcess() {
	return (this.candidacyProcess != null) ? this.candidacyProcess.getObject() : null;
    }

    public void setCandidacyProcess(DegreeCandidacyForGraduatedPersonIndividualProcess candidacyProcess) {
	this.candidacyProcess = (candidacyProcess != null) ? new DomainReference<DegreeCandidacyForGraduatedPersonIndividualProcess>(
		candidacyProcess)
		: null;
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

    public IndividualCandidacyState getState() {
	return state;
    }

    public void setState(IndividualCandidacyState state) {
	this.state = state;
    }
}
