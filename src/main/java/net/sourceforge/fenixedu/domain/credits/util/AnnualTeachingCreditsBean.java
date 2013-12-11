package net.sourceforge.fenixedu.domain.credits.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import pt.ist.bennu.core.domain.User;
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
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceLog;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.LocalDate;

import pt.ist.bennu.core.security.Authenticate;

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

    private List<AnnualTeachingCreditsByPeriodBean> annualTeachingCreditsByPeriodBeans =
            new ArrayList<AnnualTeachingCreditsByPeriodBean>();

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
            AnnualTeachingCreditsByPeriodBean annualTeachingCreditsByPeriodBean =
                    new AnnualTeachingCreditsByPeriodBean(executionSemester, teacher, roleType);
            annualTeachingCreditsByPeriodBeans.add(annualTeachingCreditsByPeriodBean);
            if (annualTeachingCreditsByPeriodBean.getCanEditTeacherCredits()) {
                setCanEditTeacherCreditsInAnyPeriod(true);
            }
            if (executionSemester.isFirstOfYear()) {
                setCanEditTeacherCredits(annualTeachingCreditsByPeriodBean.getCanEditTeacherCredits());
            }
        }
    }

    public List<AnnualTeachingCreditsByPeriodBean> getAnnualTeachingCreditsByPeriodBeans() {
        Collections.sort(annualTeachingCreditsByPeriodBeans, new BeanComparator("executionPeriod"));
        return annualTeachingCreditsByPeriodBeans;
    }

    public String getProfessionalCategoryName() {
        ProfessionalCategory professionalCategory =
                teacher.getLastCategory(executionYear.getBeginDateYearMonthDay().toLocalDate(), executionYear
                        .getEndDateYearMonthDay().toLocalDate());
        return professionalCategory == null ? null : professionalCategory.getName().getContent();
    }

    public String getDepartmentName() {
        Department department =
                teacher.getLastWorkingDepartment(executionYear.getBeginDateYearMonthDay(), executionYear.getEndDateYearMonthDay());
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
        ArrayList<InternalPhdParticipant> participants = new ArrayList<InternalPhdParticipant>();
        if (!executionYear.getYear().equals("2011/2012")) {
            for (InternalPhdParticipant internalPhdParticipant : teacher.getPerson().getInternalParticipants()) {
                LocalDate conclusionDate = internalPhdParticipant.getIndividualProcess().getConclusionDate();
                if (conclusionDate != null
                        && conclusionDate.getYear() == executionYear.getBeginCivilYear()
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
                    && professorship.getExecutionCourse().getProjectTutorialCourse()
                    && !professorship.getExecutionCourse().isDissertation()) {
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

    public SortedSet<TeacherServiceLog> getLogs() {
        final SortedSet<TeacherServiceLog> logs = new TreeSet<TeacherServiceLog>();
        for (final AnnualTeachingCreditsByPeriodBean bean : annualTeachingCreditsByPeriodBeans) {
            logs.addAll(bean.getLogs());
        }
        return logs;
    }

    public boolean getCanUserSeeTeacherServiceLogs() {
        User userView = Authenticate.getUser();
        Teacher loggedTeacher = userView.getPerson().getTeacher();
        Department department = getTeacher().getCurrentWorkingDepartment();
        return userView.getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL)
                || (loggedTeacher != null && loggedTeacher.equals(getTeacher()))
                || (department != null && department.isCurrentUserCurrentDepartmentPresident());
    }

    public void calculateCredits() {
        masterDegreeThesesCredits = teacher.getMasterDegreeThesesCredits(executionYear);
        phdDegreeThesesCredits = teacher.getPhdDegreeThesesCredits(executionYear);
        projectsTutorialsCredits = teacher.getProjectsTutorialsCredits(executionYear);

        BigDecimal yearCreditsForFinalCredits = BigDecimal.ZERO;
        BigDecimal annualTeachingLoadFinalCredits = BigDecimal.ZERO;

        boolean hasOrientantionCredits = false;
        boolean hasFinalAndAccumulatedCredits = false;

        for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
            if (getTeacher().isActiveForSemester(executionSemester) || getTeacher().hasTeacherAuthorization(executionSemester)) {
                BigDecimal thisSemesterManagementFunctionCredits =
                        new BigDecimal(getTeacher().getManagementFunctionsCredits(executionSemester));
                managementFunctionCredits = managementFunctionCredits.add(thisSemesterManagementFunctionCredits);
                serviceExemptionCredits =
                        serviceExemptionCredits.add(new BigDecimal(getTeacher().getServiceExemptionCredits(executionSemester)));
                BigDecimal thisSemesterTeachingLoad = new BigDecimal(getTeacher().getMandatoryLessonHours(executionSemester));
                annualTeachingLoad = annualTeachingLoad.add(thisSemesterTeachingLoad).setScale(2, BigDecimal.ROUND_HALF_UP);
                TeacherService teacherService = getTeacher().getTeacherServiceByExecutionPeriod(executionSemester);
                BigDecimal thisSemesterCreditsReduction = BigDecimal.ZERO;
                if (teacherService != null) {
                    teachingCredits = teachingCredits.add(new BigDecimal(teacherService.getTeachingDegreeCredits()));
                    thisSemesterCreditsReduction = teacherService.getReductionServiceCredits();
                    othersCredits = othersCredits.add(new BigDecimal(teacherService.getOtherServiceCredits()));
                }
                creditsReduction = creditsReduction.add(thisSemesterCreditsReduction);
                BigDecimal reductionAndManagement = thisSemesterManagementFunctionCredits.add(thisSemesterCreditsReduction);
                BigDecimal thisSemesterYearCredits = thisSemesterTeachingLoad;
                if (thisSemesterTeachingLoad.compareTo(reductionAndManagement) > 0) {
                    thisSemesterYearCredits = reductionAndManagement;
                } else {
                    setHasAnyLimitation(true);
                }
                yearCredits = yearCredits.add(thisSemesterYearCredits);
                if (getTeacher().isActiveForSemester(executionSemester) && !getTeacher().isMonitor(executionSemester)) {
                    yearCreditsForFinalCredits = yearCreditsForFinalCredits.add(thisSemesterYearCredits);
                    annualTeachingLoadFinalCredits = annualTeachingLoadFinalCredits.add(thisSemesterTeachingLoad);
                    if (executionSemester.getSemester() == 2) {
                        hasFinalAndAccumulatedCredits = true;
                    } else {
                        hasOrientantionCredits = true;
                    }
                }
            }
        }
        yearCredits = yearCredits.add(teachingCredits).add(serviceExemptionCredits).add(othersCredits);
        yearCreditsForFinalCredits =
                yearCreditsForFinalCredits.add(teachingCredits).add(serviceExemptionCredits).add(othersCredits);
        if (hasOrientantionCredits) {
            yearCredits =
                    yearCredits.add(getMasterDegreeThesesCredits()).add(getPhdDegreeThesesCredits())
                            .add(getProjectsTutorialsCredits()).setScale(2, BigDecimal.ROUND_HALF_UP);
            yearCreditsForFinalCredits =
                    yearCreditsForFinalCredits.add(getMasterDegreeThesesCredits()).add(getPhdDegreeThesesCredits())
                            .add(getProjectsTutorialsCredits());
        }
        if (hasFinalAndAccumulatedCredits) {
            finalCredits = yearCreditsForFinalCredits.subtract(annualTeachingLoadFinalCredits);
            BigDecimal lastYearAccumulated = getPreviousAccumulatedCredits();
            accumulatedCredits = (finalCredits.add(lastYearAccumulated)).setScale(2, BigDecimal.ROUND_HALF_UP);
            finalCredits = finalCredits.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }

    private BigDecimal getPreviousAccumulatedCredits() {
        AnnualTeachingCredits previousAnnualTeachingCredits =
                AnnualTeachingCredits.readByYearAndTeacher(getExecutionYear().getPreviousExecutionYear(), getTeacher());
        return previousAnnualTeachingCredits != null ? previousAnnualTeachingCredits.getAccumulatedCredits() : BigDecimal.ZERO;
    }

    public Boolean getAreCreditsOpen() {
        return !getAreCreditsCalculated();
    }
}
