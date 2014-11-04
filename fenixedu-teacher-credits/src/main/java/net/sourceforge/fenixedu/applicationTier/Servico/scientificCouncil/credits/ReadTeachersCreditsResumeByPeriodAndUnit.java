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
/**
 * Jan 23, 2006
 */
package org.fenixedu.academic.service.services.scientificCouncil.credits;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.TeacherCredits;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.personnelSection.contracts.PersonProfessionalData;
import org.fenixedu.academic.domain.personnelSection.contracts.ProfessionalCategory;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.predicate.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadTeachersCreditsResumeByPeriodAndUnit {

    @Atomic
    public static List<TeacherCreditsReportDTO> run(Unit department, ExecutionSemester fromExecutionPeriod,
            ExecutionSemester untilExecutionPeriod) throws FenixServiceException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);
        try {
            SortedSet<ExecutionSemester> executionPeriodsBetween =
                    getExecutionPeriodsBetween(fromExecutionPeriod, untilExecutionPeriod);

            List<Teacher> teachers =
                    TeacherCredits.getAllTeachersFromUnit(department, fromExecutionPeriod.getBeginDateYearMonthDay(),
                            untilExecutionPeriod.getEndDateYearMonthDay());

            List<TeacherCreditsReportDTO> creditLines = new ArrayList<TeacherCreditsReportDTO>();
            for (Teacher teacher : teachers) {
                if (!ProfessionalCategory.isMonitor(teacher, executionPeriodsBetween.last())
                        && !PersonProfessionalData.isTeacherInactive(teacher, executionPeriodsBetween.last())) {
                    Unit workingUnit =
                            teacher.getPerson().getEmployee() != null ? teacher.getPerson().getEmployee().getLastWorkingPlace(untilExecutionPeriod.getBeginDateYearMonthDay(), untilExecutionPeriod.getEndDateYearMonthDay()) : null;
                    Unit workingUnitDepartment = (workingUnit != null) ? workingUnit.getDepartmentUnit() : null;
                    if (workingUnitDepartment != null && workingUnitDepartment.getDepartment().equals(department.getDepartment())) {
                        TeacherCreditsReportDTO creditsReportDTO = new TeacherCreditsReportDTO();
                        creditsReportDTO.setTeacher(teacher);
                        for (ExecutionSemester executionSemester : executionPeriodsBetween) {
                            updateCreditLine(teacher, executionSemester, creditsReportDTO, true);
                        }
                        creditsReportDTO.setUnit(workingUnit);
                        creditsReportDTO.setPastCredits(TeacherCredits.calculateBalanceOfCreditsUntil(teacher,
                                fromExecutionPeriod.getPreviousExecutionPeriod()));
                        creditLines.add(creditsReportDTO);
                    }
                }
            }
            return creditLines;
        } catch (ParseException e) {
            throw new FenixServiceException(e);
        }
    }

    private static void updateCreditLine(Teacher teacher, ExecutionSemester executionSemester,
            TeacherCreditsReportDTO creditLine, boolean countCredits) throws ParseException {

        double totalCredits = 0.0;
        if (countCredits && !ProfessionalCategory.isMonitor(teacher, executionSemester)) {
            TeacherCredits teacherCredits = TeacherCredits.readTeacherCredits(executionSemester, teacher);
            if (teacherCredits != null && teacherCredits.getTeacherCreditsState().isCloseState()) {
                totalCredits += teacherCredits.getTotalCredits().subtract(teacherCredits.getMandatoryLessonHours()).doubleValue();
            } else {
                TeacherService teacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester);
                if (teacherService != null) {
                    totalCredits = teacherService.getCredits();
                }
                totalCredits -= TeacherCredits.calculateMandatoryLessonHours(teacher, executionSemester);
                totalCredits += TeacherCredits.calculateManagementFunctionsCredits(teacher, executionSemester);
                totalCredits += TeacherCredits.calculateServiceExemptionCredits(teacher, executionSemester);
                totalCredits += TeacherCredits.calculateThesesCredits(teacher, executionSemester);
            }
        }
        creditLine.getCreditsByExecutionPeriod().put(executionSemester, totalCredits);
    }

    private static SortedSet<ExecutionSemester> getExecutionPeriodsBetween(ExecutionSemester fromExecutionPeriod,
            ExecutionSemester untilExecutionPeriod) {

        SortedSet<ExecutionSemester> executionPeriodsBetween =
                new TreeSet<ExecutionSemester>(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);

        ExecutionSemester tempExecutionPeriod = fromExecutionPeriod;
        while (tempExecutionPeriod != untilExecutionPeriod) {
            executionPeriodsBetween.add(tempExecutionPeriod);
            tempExecutionPeriod = tempExecutionPeriod.getNextExecutionPeriod();
        }
        executionPeriodsBetween.add(untilExecutionPeriod);
        return executionPeriodsBetween;
    }

    public static class TeacherCreditsReportDTO {

        Map<ExecutionSemester, Double> creditsByExecutionPeriod = new TreeMap<ExecutionSemester, Double>(
                ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);

        Teacher teacher;

        Unit unit;

        double pastCredits;

        public Teacher getTeacher() {
            return teacher;
        }

        public void setTeacher(Teacher teacher) {
            this.teacher = teacher;
        }

        public Unit getUnit() {
            return unit;
        }

        public void setUnit(Unit unit) {
            this.unit = unit;
        }

        public Map<ExecutionSemester, Double> getCreditsByExecutionPeriod() {
            return creditsByExecutionPeriod;
        }

        public double getPastCredits() {
            return pastCredits;
        }

        public void setPastCredits(double pastCredits) {
            this.pastCredits = pastCredits;
        }
    }
}