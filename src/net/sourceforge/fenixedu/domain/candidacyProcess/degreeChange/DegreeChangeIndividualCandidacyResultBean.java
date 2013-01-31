package net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacySeriesGradeState;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;

public class DegreeChangeIndividualCandidacyResultBean implements Serializable {

	private DegreeChangeIndividualCandidacyProcess candidacyProcess;
	private BigDecimal affinity;
	private Integer degreeNature;
	private BigDecimal approvedEctsRate;
	private BigDecimal gradeRate;
	private BigDecimal seriesCandidacyGrade;
	private IndividualCandidacyState state;
	private IndividualCandidacySeriesGradeState seriesGradeState;
	private Degree degree;
	private List<Degree> degrees;

	public DegreeChangeIndividualCandidacyResultBean(final DegreeChangeIndividualCandidacyProcess process) {
		setCandidacyProcess(process);
		setAffinity(process.getCandidacyAffinity());
		setDegreeNature(process.getCandidacyDegreeNature());
		setApprovedEctsRate(process.getCandidacyApprovedEctsRate());
		setGradeRate(process.getCandidacyGradeRate());
		setSeriesCandidacyGrade(process.getCandidacySeriesCandidacyGrade());
		if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
			setState(process.getCandidacyState());
		}
		List<Degree> d = new ArrayList<Degree>();
		d.add(process.getCandidacy().getSelectedDegree());
		setDegrees(d);

	}

	public DegreeChangeIndividualCandidacyResultBean(final DegreeChangeIndividualCandidacyProcess process, Degree degree) {
		setCandidacyProcess(process);
		DegreeChangeIndividualCandidacySeriesGrade seriesGradeForDegree =
				process.getCandidacy().getDegreeChangeIndividualCandidacySeriesGradeForDegree(degree);
		setAffinity(seriesGradeForDegree.getAffinity());
		setDegreeNature(seriesGradeForDegree.getDegreeNature());
		setApprovedEctsRate(seriesGradeForDegree.getApprovedEctsRate());
		setGradeRate(seriesGradeForDegree.getGradeRate());
		setSeriesCandidacyGrade(seriesGradeForDegree.getSeriesCandidacyGrade());

		if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
			setState(process.getCandidacyState());
		}
		List<Degree> d = new ArrayList<Degree>();
		d.add(degree);
		setDegrees(d);
		setDegree(degree);
		setSeriesGradeState(seriesGradeForDegree.getState());
	}

	public DegreeChangeIndividualCandidacyProcess getCandidacyProcess() {
		return this.candidacyProcess;
	}

	public void setCandidacyProcess(DegreeChangeIndividualCandidacyProcess candidacyProcess) {
		this.candidacyProcess = candidacyProcess;
	}

	public String getStudentNumber() {
		return getCandidacyProcess().getPersonalDetails().hasStudent() ? getCandidacyProcess().getPersonalDetails().getStudent()
				.getNumber().toString() : null;
	}

	public String getCandidacyPersonName() {
		return getCandidacyProcess().getPersonalDetails().getName();
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
