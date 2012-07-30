package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacySeriesGradeState;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;

public class DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean implements Serializable {

    private DegreeCandidacyForGraduatedPersonIndividualProcess candidacyProcess;
    private BigDecimal affinity;
    private Integer degreeNature;
    private BigDecimal grade;
    private IndividualCandidacyState state;
    private IndividualCandidacySeriesGradeState seriesGradeState;
    private Degree degree;
    private List<Degree> degrees;
    
    public DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean(
	    final DegreeCandidacyForGraduatedPersonIndividualProcess process) {
	setCandidacyProcess(process);
	setAffinity(process.getCandidacyAffinity());
	setDegreeNature(process.getCandidacyDegreeNature());
	setGrade(process.getCandidacyGrade());
	if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
	    setState(process.getCandidacyState());
	}
	List<Degree> d = new ArrayList<Degree>();
	d.add(process.getCandidacy().getSelectedDegree());
	setDegrees(d);
    }
    
    public DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean(final DegreeCandidacyForGraduatedPersonIndividualProcess process, Degree degree) {
	setCandidacyProcess(process);
	DegreeCandidacyForGraduatedPersonSeriesGade seriesGradeForDegree = process.getCandidacy().getDegreeCandidacyForGraduatedPersonSeriesGadeForDegree(degree);

	setAffinity(seriesGradeForDegree.getAffinity());
	setDegreeNature(seriesGradeForDegree.getDegreeNature());
	setGrade(seriesGradeForDegree.getCandidacyGrade());
	
	if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
	    setState(process.getCandidacyState());
	}
	List<Degree> d = new ArrayList<Degree>();
	d.add(degree);
	setDegrees(d);
	setDegree(degree);
	setSeriesGradeState(seriesGradeForDegree.getState());
    }


    public DegreeCandidacyForGraduatedPersonIndividualProcess getCandidacyProcess() {
	return this.candidacyProcess;
    }

    public void setCandidacyProcess(DegreeCandidacyForGraduatedPersonIndividualProcess candidacyProcess) {
	this.candidacyProcess = candidacyProcess;
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

    public IndividualCandidacySeriesGradeState getSeriesGradeState() {
	return seriesGradeState;
    }

    public void setSeriesGradeState(IndividualCandidacySeriesGradeState seriesGradeState) {
	this.seriesGradeState = seriesGradeState;
    }

    public Degree getDegree() {
	return degree;
    }

    public void setDegree(Degree degree) {
	this.degree = degree;
    }

    public List<Degree> getDegrees() {
	return degrees;
    }

    public void setDegrees(List<Degree> degrees) {
	this.degrees = degrees;
    }
}
