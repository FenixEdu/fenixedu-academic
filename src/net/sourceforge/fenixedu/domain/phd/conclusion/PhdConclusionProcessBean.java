package net.sourceforge.fenixedu.domain.phd.conclusion;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisFinalGrade;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.joda.time.LocalDate;

public class PhdConclusionProcessBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private PhdIndividualProgramProcess phdIndividualProgramProcess;
	private LocalDate conclusionDate;
	private PhdThesisFinalGrade grade;
	private BigDecimal thesisEctsCredits;
	private BigDecimal studyPlanEctsCredits;

	private Integer numberOfCurricularYears;
	private Integer numberOfCurricularSemesters;

	public PhdConclusionProcessBean(final PhdIndividualProgramProcess process) {
		setPhdIndividualProgramProcess(process);
		setConclusionDate(process.getThesisProcess().getConclusionDate());
		setGrade(process.getThesisProcess().getFinalGrade());

		if (!process.getCandidacyProcess().isStudyPlanExempted()) {
			StudentCurricularPlan lastStudentCurricularPlan = process.getRegistration().getLastStudentCurricularPlan();
			CycleCurriculumGroup cycleCurriculumGroup =
					lastStudentCurricularPlan.getRoot().getCycleCurriculumGroup(CycleType.THIRD_CYCLE);
			setStudyPlanEctsCredits(cycleCurriculumGroup.getConclusionProcess().getCredits());
		}
	}

	public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
		return phdIndividualProgramProcess;
	}

	public void setPhdIndividualProgramProcess(PhdIndividualProgramProcess phdIndividualProgramProcess) {
		this.phdIndividualProgramProcess = phdIndividualProgramProcess;
	}

	public LocalDate getConclusionDate() {
		return conclusionDate;
	}

	public void setConclusionDate(LocalDate conclusionDate) {
		this.conclusionDate = conclusionDate;
	}

	public PhdThesisFinalGrade getGrade() {
		return grade;
	}

	public void setGrade(PhdThesisFinalGrade grade) {
		this.grade = grade;
	}

	public BigDecimal getThesisEctsCredits() {
		return thesisEctsCredits;
	}

	public void setThesisEctsCredits(BigDecimal thesisEctsCredits) {
		this.thesisEctsCredits = thesisEctsCredits;
	}

	public BigDecimal getStudyPlanEctsCredits() {
		return studyPlanEctsCredits;
	}

	public void setStudyPlanEctsCredits(BigDecimal studyPlanEctsCredits) {
		this.studyPlanEctsCredits = studyPlanEctsCredits;
	}

	public Integer getNumberOfCurricularYears() {
		return numberOfCurricularYears;
	}

	public void setNumberOfCurricularYears(Integer numberOfCurricularYears) {
		this.numberOfCurricularYears = numberOfCurricularYears;
	}

	public Integer getNumberOfCurricularSemesters() {
		return numberOfCurricularSemesters;
	}

	public void setNumberOfCurricularSemesters(Integer numberOfCurricularSemesters) {
		this.numberOfCurricularSemesters = numberOfCurricularSemesters;
	}
}
