package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacySeriesGradeState;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;

public class SecondCycleIndividualCandidacyResultBean implements Serializable {

    private SecondCycleIndividualCandidacyProcess candidacyProcess;
    private Integer professionalExperience;
    private BigDecimal affinity;
    private Integer degreeNature;
    private BigDecimal grade;
    private String interviewGrade; // NA or value
    private BigDecimal seriesGrade;
    private IndividualCandidacyState state;
    private IndividualCandidacySeriesGradeState seriesGradeState;
    private String notes;
    private Degree degree;
    private List<Degree> degrees;

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
        List<Degree> d = new ArrayList<Degree>();
        d.addAll(process.getCandidacy().getSelectedDegrees());
        setDegrees(d);
        setNotes(process.getCandidacyNotes());
    }

    public SecondCycleIndividualCandidacyResultBean(final SecondCycleIndividualCandidacyProcess process, Degree degree) {
        setCandidacyProcess(process);
        SecondCycleIndividualCandidacySeriesGrade seriesGradeForDegree =
                process.getCandidacy().getSecondCycleIndividualCandidacySeriesGradeForDegree(degree);
        setProfessionalExperience(seriesGradeForDegree.getProfessionalExperience());
        setAffinity(seriesGradeForDegree.getAffinity());
        setDegreeNature(seriesGradeForDegree.getDegreeNature());
        setGrade(seriesGradeForDegree.getCandidacyGrade());
        setInterviewGrade(seriesGradeForDegree.getInterviewGrade());
        setSeriesGrade(seriesGradeForDegree.getSeriesCandidacyGrade());
        if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
            setState(process.getCandidacyState());
        }
        List<Degree> d = new ArrayList<Degree>();
        d.addAll(process.getCandidacy().getSelectedDegrees());
        setDegrees(d);
        setNotes(seriesGradeForDegree.getNotes());
        setSeriesGradeState(seriesGradeForDegree.getState());
        setDegree(degree);
    }

    public SecondCycleIndividualCandidacyProcess getCandidacyProcess() {
        return this.candidacyProcess;
    }

    public void setCandidacyProcess(final SecondCycleIndividualCandidacyProcess candidacyProcess) {
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

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public IndividualCandidacySeriesGradeState getSeriesGradeState() {
        return seriesGradeState;
    }

    public void setSeriesGradeState(IndividualCandidacySeriesGradeState seriesGradeState) {
        this.seriesGradeState = seriesGradeState;
    }

    public List<Degree> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<Degree> degrees) {
        this.degrees = degrees;
    }
}
