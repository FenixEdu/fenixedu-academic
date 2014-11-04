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
package org.fenixedu.academic.domain.credits.util;

import java.io.Serializable;
import java.math.BigDecimal;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.DepartmentCreditsPool;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.personnelSection.contracts.ProfessionalCategory;
import org.fenixedu.academic.domain.teacher.ReductionService;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.domain.teacher.evaluation.ApprovedTeacherEvaluationProcessMark;
import org.fenixedu.academic.domain.teacher.evaluation.FacultyEvaluationProcessYear;
import org.fenixedu.academic.domain.teacher.evaluation.TeacherEvaluationMark;
import org.fenixedu.academic.domain.teacher.evaluation.TeacherEvaluationProcess;
import org.fenixedu.academic.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class ReductionServiceBean implements Serializable {
    private Teacher teacher;
    private ReductionService reductionService;
    private ExecutionSemester executionSemester;

    public ReductionServiceBean(Teacher teacher, ExecutionSemester executionSemester) {
        this.teacher = teacher;
        this.executionSemester = executionSemester;
    }

    public ReductionServiceBean(ReductionService reductionService) {
        this.reductionService = reductionService;
        this.teacher = reductionService.getTeacherService().getTeacher();
        this.executionSemester = reductionService.getTeacherService().getExecutionPeriod();
    }

    public ReductionServiceBean() {
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ReductionService getReductionService() {
        return reductionService;
    }

    public void setReductionService(ReductionService reductionService) {
        this.reductionService = reductionService;
    }

    public ExecutionSemester getExecutionSemester() {
        if (executionSemester == null) {
            executionSemester = ExecutionSemester.readActualExecutionSemester();
        }
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public String getTeacherCategory() {
<<<<<<< HEAD
        ProfessionalCategory category = getTeacher().getCategoryByPeriod(getExecutionSemester());
=======
        ProfessionalCategory category = ProfessionalCategory.getCategoryByPeriod(getTeacher(), ExecutionSemester.readActualExecutionSemester());
>>>>>>> b544671... Dependency cleanup: Isolated giaf contracts
        return category != null ? category.getName().getContent() : null;
    }

    @Atomic
    public TeacherService getTeacherService() {
        ExecutionSemester executionSemester = getExecutionSemester();
        TeacherService teacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester);
        if (teacherService == null) {
            teacherService = new TeacherService(teacher, executionSemester);
        }
        return teacherService;
    }

    public FacultyEvaluationProcessYear getFacultyEvaluationProcessYear() {
        FacultyEvaluationProcessYear lastFacultyEvaluationProcessYear = null;
        for (final FacultyEvaluationProcessYear facultyEvaluationProcessYear : Bennu.getInstance()
                .getFacultyEvaluationProcessYearSet()) {
            if (facultyEvaluationProcessYear.getApprovedTeacherEvaluationProcessMarkSet().size() != 0
                    && (lastFacultyEvaluationProcessYear == null || facultyEvaluationProcessYear.getYear().compareTo(
                            lastFacultyEvaluationProcessYear.getYear()) > 0)) {
                lastFacultyEvaluationProcessYear = facultyEvaluationProcessYear;
            }
        }
        return lastFacultyEvaluationProcessYear;
    }

    public String getTeacherEvaluationMarkString() {
        FacultyEvaluationProcessYear lastFacultyEvaluationProcessYear = getFacultyEvaluationProcessYear();
        TeacherEvaluationProcess lastTeacherEvaluationProcess = null;
        for (TeacherEvaluationProcess teacherEvaluationProcess : getTeacher().getPerson()
                .getTeacherEvaluationProcessFromEvalueeSet()) {
            if (teacherEvaluationProcess.getFacultyEvaluationProcess().equals(
                    lastFacultyEvaluationProcessYear.getFacultyEvaluationProcess())) {
                lastTeacherEvaluationProcess = teacherEvaluationProcess;
                break;
            }
        }
        TeacherEvaluationMark approvedEvaluationMark = null;
        if (lastTeacherEvaluationProcess != null) {
            for (ApprovedTeacherEvaluationProcessMark approvedTeacherEvaluationProcessMark : lastTeacherEvaluationProcess
                    .getApprovedTeacherEvaluationProcessMarkSet()) {
                if (approvedTeacherEvaluationProcessMark.getFacultyEvaluationProcessYear().equals(
                        lastFacultyEvaluationProcessYear)) {
                    approvedEvaluationMark = approvedTeacherEvaluationProcessMark.getApprovedEvaluationMark();
                    if (approvedEvaluationMark != null) {
                        return BundleUtil.getString(Bundle.ENUMERATION, approvedEvaluationMark.name());
                    } else {
                        return "N/A";
                    }
                }
            }
        }
        return null;
    }

    public BigDecimal getMaxCreditsFromEvaluationAndAge() {
        BigDecimal maxCreditsFromEvaluation = getTeacherEvaluationMark();
        BigDecimal maxCreditsFromAge = getTeacherMaxCreditsFromAge();
        BigDecimal maxCreditsFromEvaluationAndAge = maxCreditsFromEvaluation.add(maxCreditsFromAge);
        BigDecimal maxCreditsReduction = getMaxCreditsReduction();
        return maxCreditsReduction.min(maxCreditsFromEvaluationAndAge);
    }

    public BigDecimal getTeacherEvaluationMark() {
        FacultyEvaluationProcessYear lastFacultyEvaluationProcessYear = getFacultyEvaluationProcessYear();
        TeacherEvaluationProcess lastTeacherEvaluationProcess = null;
        for (TeacherEvaluationProcess teacherEvaluationProcess : getTeacherService().getTeacher().getPerson()
                .getTeacherEvaluationProcessFromEvalueeSet()) {
            if (teacherEvaluationProcess.getFacultyEvaluationProcess().equals(
                    lastFacultyEvaluationProcessYear.getFacultyEvaluationProcess())) {
                lastTeacherEvaluationProcess = teacherEvaluationProcess;
                break;
            }
        }
        TeacherEvaluationMark approvedEvaluationMark = null;

        BigDecimal maxCreditsReduction = getMaxCreditsReduction();
        if (lastTeacherEvaluationProcess != null) {
            for (ApprovedTeacherEvaluationProcessMark approvedTeacherEvaluationProcessMark : lastTeacherEvaluationProcess
                    .getApprovedTeacherEvaluationProcessMarkSet()) {
                if (approvedTeacherEvaluationProcessMark.getFacultyEvaluationProcessYear().equals(
                        lastFacultyEvaluationProcessYear)) {
                    approvedEvaluationMark = approvedTeacherEvaluationProcessMark.getApprovedEvaluationMark();
                    if (approvedEvaluationMark != null) {
                        switch (approvedEvaluationMark) {
                        case EXCELLENT:
                            return maxCreditsReduction;
                        case VERY_GOOD:
                            return BigDecimal.ZERO.max(maxCreditsReduction.subtract(BigDecimal.ONE));
                        case GOOD:
                            return BigDecimal.ZERO.max(maxCreditsReduction.subtract(new BigDecimal(2)));
                        default:
                            return BigDecimal.ZERO;
                        }
                    } else {
                        return BigDecimal.ZERO;
                    }
                }
            }
        } else {
            return maxCreditsReduction;
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getMaxCreditsReduction() {
        Department department =
                getTeacherService().getTeacher().getLastWorkingDepartment(
                        getTeacherService().getExecutionPeriod().getBeginDateYearMonthDay(),
                        getTeacherService().getExecutionPeriod().getEndDateYearMonthDay());
        DepartmentCreditsPool departmentCreditsPool =
                DepartmentCreditsPool.getDepartmentCreditsPool(department, getTeacherService().getExecutionPeriod()
                        .getExecutionYear());

        return departmentCreditsPool == null || departmentCreditsPool.getMaximumCreditsReduction() == null ? new BigDecimal(3) : departmentCreditsPool
                .getMaximumCreditsReduction();
    }

    public BigDecimal getTeacherMaxCreditsFromAge() {
        YearMonthDay dateOfBirthYearMonthDay = getTeacherService().getTeacher().getPerson().getDateOfBirthYearMonthDay();
        if (dateOfBirthYearMonthDay != null) {
            Interval interval =
                    new Interval(dateOfBirthYearMonthDay.toLocalDate().toDateTimeAtStartOfDay(), getTeacherService()
                            .getExecutionPeriod().getEndDateYearMonthDay().plusDays(1).toLocalDate().toDateTimeAtStartOfDay());
            int age = interval.toPeriod(PeriodType.years()).getYears();
            if (age >= 65) {
                return BigDecimal.ONE;
            }
        }
        return BigDecimal.ZERO;
    }
}
