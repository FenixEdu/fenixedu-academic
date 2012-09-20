package net.sourceforge.fenixedu.domain.credits.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.AnnualTeachingCredits;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.teacher.OtherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceComment;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;

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
    private Boolean hasAnyLimitation = false;
    private Boolean areCreditsCalculated = false;
    private boolean canEditTeacherCredits;
    private boolean canEditTeacherCreditsInAnyPeriod = false;
    private boolean canSeeCreditsReduction = false;

    public boolean isCanSeeCreditsReduction() {
	return canSeeCreditsReduction;
    }

    public void setCanSeeCreditsReduction(boolean canSeeCreditsReduction) {
	this.canSeeCreditsReduction = canSeeCreditsReduction;
    }

    private RoleType roleType;
    private Set<ExecutionYear> correctionInYears = new TreeSet<ExecutionYear>(ExecutionYear.COMPARATOR_BY_YEAR);

    private List<AnnualTeachingCreditsByPeriodBean> annualTeachingCreditsByPeriodBeans = new ArrayList<AnnualTeachingCreditsByPeriodBean>();

    public AnnualTeachingCreditsBean(AnnualTeachingCredits annualTeachingCredits, RoleType roleType) {
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
	this.hasAnyLimitation = annualTeachingCredits.getHasAnyLimitation();
	setAreCreditsCalculated(annualTeachingCredits.getAnnualCreditsState().getIsFinalCreditsCalculated());
	setAnnualTeachingCreditsByPeriod(executionYear, teacher, roleType);
	for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
	    if (!annualTeachingCredits.isPastResume()) {
		for (OtherService otherService : executionSemester.getOtherServicesCorrections()) {
		    if (otherService.getTeacherService().getTeacher().equals(teacher)
			    && !otherService.getCorrectedExecutionSemester().equals(
				    otherService.getTeacherService().getExecutionPeriod())) {
			correctionInYears.add(otherService.getTeacherService().getExecutionPeriod().getExecutionYear());
		    }
		}
	    }
	}
    }

    public AnnualTeachingCreditsBean(ExecutionYear executionYear, Teacher teacher, RoleType roleType) {
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
	setAnnualTeachingCreditsByPeriod(executionYear, teacher, roleType);
    }

    protected void setAnnualTeachingCreditsByPeriod(ExecutionYear executionYear, Teacher teacher, RoleType roleType) {
	setRoleType(roleType);
	if (roleType.equals(RoleType.SCIENTIFIC_COUNCIL) || roleType.equals(RoleType.DEPARTMENT_MEMBER)) {
	    setCanSeeCreditsReduction(true);
	}
	for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
	    annualTeachingCreditsByPeriodBeans.add(new AnnualTeachingCreditsByPeriodBean(executionSemester, teacher, roleType));
	    if (executionSemester.isInValidCreditsPeriod(roleType)) {
		setCanEditTeacherCreditsInAnyPeriod(true);
	    }
	}
	setCanEditTeacherCredits(executionYear.getFirstExecutionPeriod().isInValidCreditsPeriod(roleType));
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
	if (!executionYear.getYear().equals("2011/2012")) {
	    for (ThesisEvaluationParticipant participant : teacher.getPerson().getThesisEvaluationParticipants()) {
		Thesis thesis = participant.getThesis();
		if (thesis.isEvaluated()
			&& thesis.hasFinalEnrolmentEvaluation()
			&& thesis.getEvaluation().getYear() == executionYear.getBeginCivilYear()
			&& (participant.getType() == ThesisParticipationType.ORIENTATOR || participant.getType() == ThesisParticipationType.COORIENTATOR)) {
		    participants.add(participant);
		}
	    }
	}
	Collections.sort(participants, ThesisEvaluationParticipant.COMPARATOR_BY_STUDENT_NUMBER);
	return participants;
    }

    public List<InternalPhdParticipant> getPhdDegreeTheses() {
	ExecutionYear previousExecutionYear = executionYear.getPreviousExecutionYear();
	ArrayList<InternalPhdParticipant> participants = new ArrayList<InternalPhdParticipant>();
	if (!executionYear.getYear().equals("2011/2012")) {
	    for (InternalPhdParticipant internalPhdParticipant : teacher.getPerson().getInternalParticipants()) {
		ExecutionYear conclusionYear = internalPhdParticipant.getIndividualProcess().getConclusionYear();
		if (conclusionYear != null
			&& conclusionYear.equals(previousExecutionYear)
			&& (internalPhdParticipant.getProcessForGuiding() != null || internalPhdParticipant
				.getProcessForAssistantGuiding() != null)) {
		    participants.add(internalPhdParticipant);
		}
	    }
	}
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

    public List<TeacherServiceComment> getTeacherServiceComments() {
	List<TeacherServiceComment> teacherServiceComments = new ArrayList<TeacherServiceComment>();
	for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
	    TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
	    if (teacherService != null) {
		teacherServiceComments.addAll(teacherService.getTeacherServiceComments());
	    }
	}
	Collections.sort(teacherServiceComments, new BeanComparator("lastModifiedDate"));
	return teacherServiceComments;
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

    public boolean isCanEditTeacherCredits() {
	return canEditTeacherCredits;
    }

    public void setCanEditTeacherCredits(boolean canEditTeacherCredits) {
	this.canEditTeacherCredits = canEditTeacherCredits;
    }

    public boolean isCanEditTeacherCreditsInAnyPeriod() {
	return canEditTeacherCreditsInAnyPeriod;
    }

    public void setCanEditTeacherCreditsInAnyPeriod(boolean canEditTeacherCreditsInAnyPeriod) {
	this.canEditTeacherCreditsInAnyPeriod = canEditTeacherCreditsInAnyPeriod;
    }

    public Boolean getHasAnyLimitation() {
	return hasAnyLimitation;
    }

    public void setHasAnyLimitation(Boolean hasAnyLimitation) {
	this.hasAnyLimitation = hasAnyLimitation;
    }

    public Boolean getAreCreditsCalculated() {
	return areCreditsCalculated;
    }

    public void setAreCreditsCalculated(Boolean areCreditsCalculated) {
	this.areCreditsCalculated = areCreditsCalculated;
    }

    public RoleType getRoleType() {
	return roleType;
    }

    public void setRoleType(RoleType roleType) {
	this.roleType = roleType;
    }

    public String getCorrections() {
	StringBuilder result = new StringBuilder();
	for (ExecutionYear executionTear : correctionInYears) {
	    result.append("(** ").append(executionTear.getName()).append(") ");
	}
	if (hasAnyLimitation) {
	    result.append("(*)");
	}
	return result.toString();
    }

    public Set<ExecutionYear> getCorrectionInYears() {
	return correctionInYears;
    }

    public void setCorrectionInYears(Set<ExecutionYear> correctionInYears) {
	this.correctionInYears = correctionInYears;
    }

}
