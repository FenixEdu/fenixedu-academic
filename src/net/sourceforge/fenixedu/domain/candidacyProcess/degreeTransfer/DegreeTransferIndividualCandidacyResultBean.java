package net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;

public class DegreeTransferIndividualCandidacyResultBean implements Serializable {

    private DomainReference<DegreeTransferIndividualCandidacyProcess> candidacyProcess;
    private BigDecimal affinity;
    private Integer degreeNature;
    private BigDecimal approvedEctsRate;
    private BigDecimal gradeRate;
    private BigDecimal seriesCandidacyGrade;
    private IndividualCandidacyState state;

    public DegreeTransferIndividualCandidacyResultBean(final DegreeTransferIndividualCandidacyProcess process) {
	setCandidacyProcess(process);
	setAffinity(process.getCandidacyAffinity());
	setDegreeNature(process.getCandidacyDegreeNature());
	setApprovedEctsRate(process.getCandidacyApprovedEctsRate());
	setGradeRate(process.getCandidacyGradeRate());
	setSeriesCandidacyGrade(process.getCandidacySeriesCandidacyGrade());
	if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
	    setState(process.getCandidacyState());
	}
    }

    public DegreeTransferIndividualCandidacyProcess getCandidacyProcess() {
	return (this.candidacyProcess != null) ? this.candidacyProcess.getObject() : null;
    }

    public void setCandidacyProcess(DegreeTransferIndividualCandidacyProcess candidacyProcess) {
	this.candidacyProcess = (candidacyProcess != null) ? new DomainReference<DegreeTransferIndividualCandidacyProcess>(
		candidacyProcess) : null;
    }

    public String getStudentNumber() {
	return getCandidacyPerson().hasStudent() ? getCandidacyPerson().getStudent().getNumber().toString() : null;
    }

    private Person getCandidacyPerson() {
	return getCandidacyProcess().getCandidacyPerson();
    }

    public String getCandidacyPersonName() {
	return getCandidacyPerson().getName();
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

    public BigDecimal getApprovedEctsRate() {
	return approvedEctsRate;
    }

    public void setApprovedEctsRate(BigDecimal approvedEctsRate) {
	this.approvedEctsRate = approvedEctsRate;
    }

    public BigDecimal getGradeRate() {
	return gradeRate;
    }

    public void setGradeRate(BigDecimal gradeRate) {
	this.gradeRate = gradeRate;
    }

    public BigDecimal getSeriesCandidacyGrade() {
	return seriesCandidacyGrade;
    }

    public void setSeriesCandidacyGrade(BigDecimal seriesCandidacyGrade) {
	this.seriesCandidacyGrade = seriesCandidacyGrade;
    }

    public IndividualCandidacyState getState() {
	return state;
    }

    public void setState(IndividualCandidacyState state) {
	this.state = state;
    }
}
