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
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherCredits;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.predicates.RolePredicates;
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
                    department.getAllTeachers(fromExecutionPeriod.getBeginDateYearMonthDay(),
                            untilExecutionPeriod.getEndDateYearMonthDay());

            List<TeacherCreditsReportDTO> creditLines = new ArrayList<TeacherCreditsReportDTO>();
            for (Teacher teacher : teachers) {
                if (!teacher.isMonitor(executionPeriodsBetween.last()) && !teacher.isInactive(executionPeriodsBetween.last())) {
                    Unit workingUnit =
                            teacher.getLastWorkingUnit(untilExecutionPeriod.getBeginDateYearMonthDay(),
                                    untilExecutionPeriod.getEndDateYearMonthDay());
                    Unit workingUnitDepartment = (workingUnit != null) ? workingUnit.getDepartmentUnit() : null;
                    if (workingUnitDepartment != null && workingUnitDepartment.getDepartment().equals(department.getDepartment())) {
                        TeacherCreditsReportDTO creditsReportDTO = new TeacherCreditsReportDTO();
                        creditsReportDTO.setTeacher(teacher);
                        for (ExecutionSemester executionSemester : executionPeriodsBetween) {
                            updateCreditLine(teacher, executionSemester, creditsReportDTO, true);
                        }
                        creditsReportDTO.setUnit(workingUnit);
                        creditsReportDTO.setPastCredits(teacher.getBalanceOfCreditsUntil(fromExecutionPeriod
                                .getPreviousExecutionPeriod()));
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
        if (countCredits && !teacher.isMonitor(executionSemester)) {
            TeacherCredits teacherCredits = TeacherCredits.readTeacherCredits(executionSemester, teacher);
            if (teacherCredits != null && teacherCredits.getTeacherCreditsState().isCloseState()) {
                totalCredits += teacherCredits.getTotalCredits().subtract(teacherCredits.getMandatoryLessonHours()).doubleValue();
            } else {
                TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
                if (teacherService != null) {
                    totalCredits = teacherService.getCredits();
                }
                totalCredits -= teacher.getMandatoryLessonHours(executionSemester);
                totalCredits += teacher.getManagementFunctionsCredits(executionSemester);
                totalCredits += teacher.getServiceExemptionCredits(executionSemester);
                totalCredits += teacher.getThesesCredits(executionSemester);
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