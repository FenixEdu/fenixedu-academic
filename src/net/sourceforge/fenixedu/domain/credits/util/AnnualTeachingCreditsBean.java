package net.sourceforge.fenixedu.domain.credits.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.AnnualTeachingCredits;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.teacher.ReductionService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;

import org.apache.commons.beanutils.BeanComparator;

public class AnnualTeachingCreditsBean implements Serializable {
    private ExecutionYear executionYear;
    private Teacher teacher;
    private BigDecimal teachingCredits;
    private BigDecimal masterDegreeThesesCredits;
    private BigDecimal phdDegreeThesesCredits;
    private BigDecimal projectsTutorialsCredits;
    private BigDecimal managementFunctionCredits;
    private BigDecimal othersCredits;
    private BigDecimal creditsReduction;
    private BigDecimal serviceExemptionCredits;
    private BigDecimal annualTeachingLoad;
    private BigDecimal yearCredits;
    private BigDecimal finalCredits;
    private BigDecimal accumulatedCredits;

    private List<AnnualTeachingCreditsByPeriodBean> annualTeachingCreditsByPeriodBeans = new ArrayList<AnnualTeachingCreditsByPeriodBean>();

    public AnnualTeachingCreditsBean(AnnualTeachingCredits annualTeachingCredits) {
	super();
	this.executionYear = annualTeachingCredits.getAnnualCreditsState().getExecutionYear();
	this.teacher = annualTeachingCredits.getTeacher();
	this.teachingCredits = annualTeachingCredits.getTeachingCredits();
	this.masterDegreeThesesCredits = annualTeachingCredits.getMasterDegreeThesesCredits();
	this.phdDegreeThesesCredits = annualTeachingCredits.getPhdDegreeThesesCredits();
	this.projectsTutorialsCredits = annualTeachingCredits.getProjectsTutorialsCredits();
	this.managementFunctionCredits = annualTeachingCredits.getManagementFunctionCredits();
	this.othersCredits = annualTeachingCredits.getOthersCredits();
	this.creditsReduction = annualTeachingCredits.getCreditsReduction();
	this.serviceExemptionCredits = annualTeachingCredits.getServiceExemptionCredits();
	this.annualTeachingLoad = annualTeachingCredits.getAnnualTeachingLoad();
	this.yearCredits = annualTeachingCredits.getYearCredits();
	this.finalCredits = annualTeachingCredits.getFinalCredits();
	this.accumulatedCredits = annualTeachingCredits.getAccumulatedCredits();
	for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
	    annualTeachingCreditsByPeriodBeans.add(new AnnualTeachingCreditsByPeriodBean(executionSemester, teacher));
	}
    }

    public AnnualTeachingCreditsBean(ExecutionYear executionYear, Teacher teacher) {
	this.executionYear = executionYear;
	this.teacher = teacher;
	this.teachingCredits = BigDecimal.ZERO;
	this.masterDegreeThesesCredits = BigDecimal.ZERO;
	this.phdDegreeThesesCredits = BigDecimal.ZERO;
	this.projectsTutorialsCredits = BigDecimal.ZERO;
	this.managementFunctionCredits = BigDecimal.ZERO;
	this.othersCredits = BigDecimal.ZERO;
	this.creditsReduction = BigDecimal.ZERO;
	this.serviceExemptionCredits = BigDecimal.ZERO;
	this.annualTeachingLoad = BigDecimal.ZERO;
	this.yearCredits = BigDecimal.ZERO;
	this.finalCredits = BigDecimal.ZERO;
	this.accumulatedCredits = BigDecimal.ZERO;
	for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
	    annualTeachingCreditsByPeriodBeans.add(new AnnualTeachingCreditsByPeriodBean(executionSemester, teacher));
	}
    }

    public List<AnnualTeachingCreditsByPeriodBean> getAnnualTeachingCreditsByPeriodBeans() {
	Collections.sort(annualTeachingCreditsByPeriodBeans, new BeanComparator("executionPeriod"));
	return annualTeachingCreditsByPeriodBeans;
    }

    public String getProfessionalCategoryName() {
	ProfessionalCategory professionalCategory = teacher.getLastCategory(executionYear.getBeginDateYearMonthDay()
		.toLocalDate(), executionYear.getEndDateYearMonthDay().toLocalDate());
	return professionalCategory == null ? null : professionalCategory.getName().getContent();
    }

    public String getDepartmentName() {
	Department department = teacher.getLastWorkingDepartment(executionYear.getBeginDateYearMonthDay(),
		executionYear.getEndDateYearMonthDay());
	return department == null ? null : department.getName();
    }

    public List<ThesisEvaluationParticipant> getMasterDegreeThesis() {
	ArrayList<ThesisEvaluationParticipant> participants = new ArrayList<ThesisEvaluationParticipant>();
	// for (ThesisEvaluationParticipant participant :
	// teacher.getPerson().getThesisEvaluationParticipants()) {
	// Thesis thesis = participant.getThesis();
	// if (thesis.isEvaluated()
	// && thesis.hasFinalEnrolmentEvaluation()
	// && thesis.getEvaluation().getYear() ==
	// executionYear.getBeginCivilYear()
	// && (participant.getType() == ThesisParticipationType.ORIENTATOR ||
	// participant.getType() == ThesisParticipationType.COORIENTATOR)) {
	// participants.add(participant);
	// }
	// }
	Collections.sort(participants, ThesisEvaluationParticipant.COMPARATOR_BY_STUDENT_NUMBER);
	return participants;
    }

    public List<ThesisEvaluationParticipant> getPhdDegreeTheses() {
	// PhdIndividualProgramProcess.
	ArrayList<ThesisEvaluationParticipant> participants = new ArrayList<ThesisEvaluationParticipant>();
	return participants;
    }

    public List<Professorship> getProjectAndTutorialProfessorships() {
	List<Professorship> professorships = new ArrayList<Professorship>();
	ExecutionYear previousExecutionYear = executionYear.getPreviousExecutionYear();
	for (Professorship professorship : getTeacher().getPerson().getProfessorshipsSet()) {
	    if (professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear().equals(previousExecutionYear)
		    && professorship.getExecutionCourse().getProjectTutorialCourse()) {
		professorships.add(professorship);
	    }
	}
	return professorships;
    }

    public List<ReductionService> getCreditsReductionService() {
	List<ReductionService> creditsReductions = new ArrayList<ReductionService>();
	for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
	    TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
	    if (teacherService != null) {
		ReductionService reductionService = teacherService.getReductionService();
		if (reductionService != null) {
		    creditsReductions.add(reductionService);
		}
	    }
	}
	Collections.sort(creditsReductions, new BeanComparator("teacherService.executionPeriod"));
	return creditsReductions;
    }

    public ExecutionYear getExecutionYear() {
	return executionYear;
    }

    public Teacher getTeacher() {
	return teacher;
    }

    public BigDecimal getTeachingCredits() {
	return teachingCredits;
    }

    public BigDecimal getMasterDegreeThesesCredits() {
	return masterDegreeThesesCredits;
    }

    public BigDecimal getPhdDegreeThesesCredits() {
	return phdDegreeThesesCredits;
    }

    public BigDecimal getProjectsTutorialsCredits() {
	return projectsTutorialsCredits;
    }

    public BigDecimal getOthersCredits() {
	return othersCredits;
    }

    public BigDecimal getManagementFunctionCredits() {
	return managementFunctionCredits;
    }

    public BigDecimal getCreditsReduction() {
	return creditsReduction;
    }

    public BigDecimal getServiceExemptionCredits() {
	return serviceExemptionCredits;
    }

    public BigDecimal getAnnualTeachingLoad() {
	return annualTeachingLoad;
    }

    public BigDecimal getYearCredits() {
	return yearCredits;
    }

    public BigDecimal getFinalCredits() {
	return finalCredits;
    }

    public BigDecimal getAccumulatedCredits() {
	return accumulatedCredits;
    }

}
