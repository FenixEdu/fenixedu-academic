/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.credits.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.domain.teacher.OtherService;
import net.sourceforge.fenixedu.domain.teacher.ReductionService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceLog;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.Interval;

public class AnnualTeachingCreditsByPeriodBean implements Serializable {
    private ExecutionSemester executionPeriod;
    private Teacher teacher;
    private Boolean showTeacherCreditsLockedMessage = false;
    private Boolean showTeacherCreditsUnlockedMessage = false;
    private Boolean canLockTeacherCredits = false;
    private Boolean canUnlockTeacherCredits = false;
    private Boolean canEditTeacherCredits = false;
    private Boolean canEditTeacherCreditsReductions = false;
    private Boolean canEditTeacherManagementFunctions = false;

    public AnnualTeachingCreditsByPeriodBean(ExecutionSemester executionPeriod, Teacher teacher, RoleType roleType) {
        super();
        this.executionPeriod = executionPeriod;
        this.teacher = teacher;
        if (roleType != null) {
            TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
            boolean inValidCreditsPeriod = executionPeriod.isInValidCreditsPeriod(roleType);
            boolean isLocked = teacherService != null && teacherService.getTeacherServiceLock() != null;
            if (roleType.equals(RoleType.DEPARTMENT_MEMBER)) {
                boolean canLockAndEditTeacherCredits = inValidCreditsPeriod && !isLocked;
                setCanLockTeacherCredits(canLockAndEditTeacherCredits);
                setCanEditTeacherCredits(canLockAndEditTeacherCredits);
            } else if (roleType.equals(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE) || roleType.equals(RoleType.SCIENTIFIC_COUNCIL)) {
                boolean inValidTeacherCreditsPeriod = executionPeriod.isInValidCreditsPeriod(RoleType.DEPARTMENT_MEMBER);
                setCanUnlockTeacherCredits(inValidCreditsPeriod && inValidTeacherCreditsPeriod && isLocked);
                setCanEditTeacherCredits(roleType.equals(RoleType.SCIENTIFIC_COUNCIL)
                        || (inValidCreditsPeriod && (isLocked || !inValidTeacherCreditsPeriod)));
            }
            setShowTeacherCreditsLockedMessage(isLocked);
            setShowTeacherCreditsUnlockedMessage(!isLocked);
            ReductionService creditsReductionService = getCreditsReductionService();
            setCanEditTeacherCreditsReductions(roleType.equals(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE) ? false : getCanEditTeacherCredits()
                    && (creditsReductionService == null || creditsReductionService.getAttributionDate() == null));
            setCanEditTeacherManagementFunctions(roleType.equals(RoleType.DEPARTMENT_MEMBER) ? false : getCanEditTeacherCredits());
        }
    }

    public List<Professorship> getProfessorships() {
        List<Professorship> professorships = new ArrayList<Professorship>();
        for (Professorship professorship : getTeacher().getProfessorships()) {
            if (professorship.getExecutionCourse().getExecutionPeriod().equals(executionPeriod)
                    && professorship.getExecutionCourse().hasAnyLesson()
                    && (!professorship.getExecutionCourse().isDissertation())) {
                professorships.add(professorship);
            }
        }

        Collections.sort(professorships, new BeanComparator("executionCourse.name"));
        return professorships;
    }

    public List<InstitutionWorkTime> getInstitutionWorkTime() {
        List<InstitutionWorkTime> institutionWorkingTimes = new ArrayList<InstitutionWorkTime>();
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService != null && !teacherService.getInstitutionWorkTimes().isEmpty()) {
            institutionWorkingTimes.addAll(teacherService.getInstitutionWorkTimes());
        }

        ComparatorChain comparatorChain = new ComparatorChain();
        BeanComparator semesterComparator = new BeanComparator("teacherService.executionPeriod");
        BeanComparator weekDayComparator = new BeanComparator("weekDay");
        BeanComparator startTimeComparator = new BeanComparator("startTime");
        comparatorChain.addComparator(semesterComparator);
        comparatorChain.addComparator(weekDayComparator);
        comparatorChain.addComparator(startTimeComparator);
        Collections.sort(institutionWorkingTimes, comparatorChain);
        return institutionWorkingTimes;
    }

    public List<PersonFunction> getPersonFunctions() {
        List<PersonFunction> personFunctions = new ArrayList<PersonFunction>();
        for (PersonFunction personFunction : teacher.getPerson().getPersonFunctions()) {
            if (personFunction.belongsToPeriod(executionPeriod.getBeginDateYearMonthDay(),
                    executionPeriod.getEndDateYearMonthDay())
                    && !personFunction.getFunction().isVirtual()) {
                personFunctions.add(personFunction);
            }
        }
        return personFunctions;
    }

    public List<OtherService> getOtherServices() {
        List<OtherService> otherServices = new ArrayList<OtherService>();
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService != null && !teacherService.getOtherServices().isEmpty()) {
            otherServices.addAll(teacherService.getOtherServices());
        }
        return otherServices;
    }

    public List<PersonContractSituation> getServiceExemptions() {
        Interval executionYearInterval =
                new Interval(executionPeriod.getBeginDateYearMonthDay().toDateTimeAtMidnight(), executionPeriod
                        .getEndDateYearMonthDay().plusDays(1).toDateTimeAtMidnight());
        return new ArrayList<PersonContractSituation>(teacher.getValidTeacherServiceExemptions(executionYearInterval));
    }

    public ReductionService getCreditsReductionService() {
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        return teacherService != null ? teacherService.getReductionService() : null;
    }

    public BigDecimal getCreditsReduction() {
        ReductionService reductionService = getCreditsReductionService();
        return reductionService != null ? reductionService.getCreditsReduction() : null;
    }

    public Boolean getRequestCreditsReduction() {
        ReductionService reductionService = getCreditsReductionService();
        return reductionService != null ? reductionService.getRequestCreditsReduction() : false;
    }

    public String getCreditsReductionServiceAttribute() {
        ReductionService reductionService = getCreditsReductionService();
        return reductionService != null ? reductionService.getAttributionDate() != null ? reductionService
                .getCreditsReductionAttributed().toString() : null : "-";
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionPeriod;
    }

    public Boolean getCanEditTeacherCredits() {
        return canEditTeacherCredits;
    }

    public void setCanEditTeacherCredits(Boolean canEditTeacherCredits) {
        this.canEditTeacherCredits = canEditTeacherCredits;
    }

    public Boolean getCanEditTeacherCreditsReductions() {
        return canEditTeacherCreditsReductions;
    }

    public void setCanEditTeacherCreditsReductions(Boolean canEditTeacherCreditsReductions) {
        this.canEditTeacherCreditsReductions = canEditTeacherCreditsReductions;
    }

    public Boolean getCanEditTeacherManagementFunctions() {
        return canEditTeacherManagementFunctions;
    }

    public void setCanEditTeacherManagementFunctions(Boolean canEditTeacherManagementFunctions) {
        this.canEditTeacherManagementFunctions = canEditTeacherManagementFunctions;
    }

    public TeacherService getTeacherService() {
        return teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
    }

    public Boolean getCanLockTeacherCredits() {
        return canLockTeacherCredits;
    }

    public void setCanLockTeacherCredits(Boolean canLockTeacherCredits) {
        this.canLockTeacherCredits = canLockTeacherCredits;
    }

    public Boolean getCanUnlockTeacherCredits() {
        return canUnlockTeacherCredits;
    }

    public void setCanUnlockTeacherCredits(Boolean canUnlockTeacherCredits) {
        this.canUnlockTeacherCredits = canUnlockTeacherCredits;
    }

    public Set<TeacherServiceLog> getLogs() {
        final TeacherService teacherService = getTeacherService();
        return teacherService == null ? Collections.EMPTY_SET : teacherService.getSortedLogs();
    }

    public Boolean getShowTeacherCreditsLockedMessage() {
        return showTeacherCreditsLockedMessage;
    }

    public void setShowTeacherCreditsLockedMessage(Boolean showTeacherCreditsLockedMessage) {
        this.showTeacherCreditsLockedMessage = showTeacherCreditsLockedMessage;
    }

    public Boolean getShowTeacherCreditsUnlockedMessage() {
        return showTeacherCreditsUnlockedMessage;
    }

    public void setShowTeacherCreditsUnlockedMessage(Boolean showTeacherCreditsUnlockedMessage) {
        this.showTeacherCreditsUnlockedMessage = showTeacherCreditsUnlockedMessage;
    }

}
