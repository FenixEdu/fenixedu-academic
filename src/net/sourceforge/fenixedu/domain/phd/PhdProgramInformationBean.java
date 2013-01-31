package net.sourceforge.fenixedu.domain.phd;

import java.math.BigDecimal;

import org.joda.time.LocalDate;

public class PhdProgramInformationBean implements java.io.Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private LocalDate beginDate;
	private BigDecimal minThesisEctsCredits;
	private BigDecimal maxThesisEctsCredits;
	private BigDecimal minStudyPlanEctsCredits;
	private BigDecimal maxStudyPlanEctsCredits;
	private Integer numberOfYears;
	private Integer numberOfSemesters;

	private PhdProgram phdProgram;

	public PhdProgramInformationBean(final PhdProgram phdProgram) {
		this.phdProgram = phdProgram;
	}

	public PhdProgramInformationBean(final PhdProgramInformation phdProgramInformation) {
		this.phdProgram = phdProgramInformation.getPhdProgram();
		this.beginDate = phdProgramInformation.getBeginDate();
		this.minThesisEctsCredits = phdProgramInformation.getMinThesisEctsCredits();
		this.maxThesisEctsCredits = phdProgramInformation.getMaxThesisEctsCredits();
		this.minStudyPlanEctsCredits = phdProgramInformation.getMinStudyPlanEctsCredits();
		this.maxStudyPlanEctsCredits = phdProgramInformation.getMaxStudyPlanEctsCredits();
		this.numberOfYears = phdProgramInformation.getNumberOfYears();
		this.numberOfSemesters = phdProgramInformation.getNumberOfSemesters();
	}

	public LocalDate getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(LocalDate beginDate) {
		this.beginDate = beginDate;
	}

	public BigDecimal getMinThesisEctsCredits() {
		return minThesisEctsCredits;
	}

	public void setMinThesisEctsCredits(BigDecimal minThesisEctsCredits) {
		this.minThesisEctsCredits = minThesisEctsCredits;
	}

	public BigDecimal getMaxThesisEctsCredits() {
		return maxThesisEctsCredits;
	}

	public void setMaxThesisEctsCredits(BigDecimal maxThesisEctsCredits) {
		this.maxThesisEctsCredits = maxThesisEctsCredits;
	}

	public BigDecimal getMinStudyPlanEctsCredits() {
		return minStudyPlanEctsCredits;
	}

	public void setMinStudyPlanEctsCredits(BigDecimal minStudyPlanEctsCredits) {
		this.minStudyPlanEctsCredits = minStudyPlanEctsCredits;
	}

	public BigDecimal getMaxStudyPlanEctsCredits() {
		return maxStudyPlanEctsCredits;
	}

	public void setMaxStudyPlanEctsCredits(BigDecimal maxStudyPlanEctsCredits) {
		this.maxStudyPlanEctsCredits = maxStudyPlanEctsCredits;
	}

	public PhdProgram getPhdProgram() {
		return phdProgram;
	}

	public Integer getNumberOfYears() {
		return numberOfYears;
	}

	public void setNumberOfYears(Integer numberOfYears) {
		this.numberOfYears = numberOfYears;
	}

	public Integer getNumberOfSemesters() {
		return numberOfSemesters;
	}

	public void setNumberOfSemesters(Integer numberOfSemesters) {
		this.numberOfSemesters = numberOfSemesters;
	}
}
