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
package org.fenixedu.academic.service.services.scientificCouncil.credits;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.TeacherCredits;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.personnelSection.contracts.PersonProfessionalData;
import org.fenixedu.academic.domain.personnelSection.contracts.ProfessionalCategory;
import org.fenixedu.academic.predicate.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class ReadDepartmentTotalCreditsByPeriod {

    @Atomic
    public static Map<ExecutionYear, PeriodCreditsReportDTO> run(Unit department, ExecutionSemester fromExecutionPeriod,
            ExecutionSemester untilExecutionPeriod) throws ParseException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        List<ExecutionSemester> executionPeriodsBetween = getExecutionPeriodsBetween(fromExecutionPeriod, untilExecutionPeriod);

        List<Teacher> teachers =
                TeacherCredits.getAllTeachersFromUnit(department, fromExecutionPeriod.getBeginDateYearMonthDay(),
                        untilExecutionPeriod.getEndDateYearMonthDay());

        SortedMap<ExecutionYear, PeriodCreditsReportDTO> departmentGlobalCredits =
                new TreeMap<ExecutionYear, PeriodCreditsReportDTO>(ExecutionYear.COMPARATOR_BY_YEAR);

        ExecutionSemester lasExecutionPeriod =
                (!executionPeriodsBetween.isEmpty()) ? executionPeriodsBetween.get(executionPeriodsBetween.size() - 1) : null;
        for (Teacher teacher : teachers) {
            if (!ProfessionalCategory.isMonitor(teacher, lasExecutionPeriod)
                    && !PersonProfessionalData.isTeacherInactive(teacher, lasExecutionPeriod)) {
                Unit workingUnit =
                        teacher.getPerson().getEmployee() != null ? teacher.getPerson().getEmployee().getLastWorkingPlace(untilExecutionPeriod.getBeginDateYearMonthDay(), untilExecutionPeriod.getEndDateYearMonthDay()) : null;
                Unit workingUnitDepartment = (workingUnit != null) ? workingUnit.getDepartmentUnit() : null;
                if (workingUnitDepartment != null && workingUnitDepartment.getDepartment().equals(department.getDepartment())) {
                    for (ExecutionSemester executionSemester : executionPeriodsBetween) {
                        if (executionSemester.getSemester().intValue() == 2) {
                            updateCredits(teacher, executionSemester, departmentGlobalCredits, untilExecutionPeriod);
                        }
                    }
                }
            }
        }
        return departmentGlobalCredits;
    }

    private static void updateCredits(Teacher teacher, ExecutionSemester executionSemester,
            Map<ExecutionYear, PeriodCreditsReportDTO> departmentCredits, ExecutionSemester untilExecutionPeriod)
            throws ParseException {

        double teacherPeriodTotalCredits = TeacherCredits.calculateBalanceOfCreditsUntil(teacher, executionSemester);

        if (!departmentCredits.containsKey(executionSemester.getExecutionYear())) {
            departmentCredits.put(executionSemester.getExecutionYear(), new PeriodCreditsReportDTO());
        }

        PeriodCreditsReportDTO reportDTO = departmentCredits.get(executionSemester.getExecutionYear());
        reportDTO.setCredits(round(reportDTO.getCredits() + teacherPeriodTotalCredits));

        boolean careerTeacher = ProfessionalCategory.isTeacherCareerCategory(teacher, executionSemester);
        if (careerTeacher) {
            reportDTO.setCareerCategoryTeacherCredits(round(reportDTO.getCareerCategoryTeacherCredits()
                    + teacherPeriodTotalCredits));
        } else {
            reportDTO.setNotCareerCategoryTeacherCredits(round(reportDTO.getNotCareerCategoryTeacherCredits()
                    + teacherPeriodTotalCredits));
        }

        if (executionSemester.equals(untilExecutionPeriod)) {
            if (careerTeacher) {
                reportDTO.setCareerTeachersSize(reportDTO.getCareerTeachersSize() + 1);
                reportDTO.setCareerTeachersBalance(round(reportDTO.getCareerCategoryTeacherCredits()
                        / reportDTO.getCareerTeachersSize()));
            } else {
                reportDTO.setNotCareerTeachersSize(reportDTO.getNotCareerTeachersSize() + 1);
                reportDTO.setNotCareerTeachersBalance(round(reportDTO.getNotCareerCategoryTeacherCredits()
                        / reportDTO.getNotCareerTeachersSize()));
            }
            reportDTO.setTeachersSize(reportDTO.getTeachersSize() + 1);
            reportDTO.setBalance(round(reportDTO.getCredits() / reportDTO.getTeachersSize()));
        }
    }

    private static List<ExecutionSemester> getExecutionPeriodsBetween(ExecutionSemester fromExecutionPeriod,
            ExecutionSemester untilExecutionPeriod) {

        List<ExecutionSemester> executionPeriodsBetween = new ArrayList<ExecutionSemester>();

        ExecutionSemester tempExecutionPeriod = fromExecutionPeriod;
        while (tempExecutionPeriod != untilExecutionPeriod) {
            executionPeriodsBetween.add(tempExecutionPeriod);
            tempExecutionPeriod = tempExecutionPeriod.getNextExecutionPeriod();
        }
        executionPeriodsBetween.add(untilExecutionPeriod);
        return executionPeriodsBetween;
    }

    private static Double round(double n) {
        return Math.round((n * 100.0)) / 100.0;
    }

    public static class PeriodCreditsReportDTO {
        private double credits;
        private double balance;
        private double careerTeachersBalance;
        private double notCareerTeachersBalance;
        private double careerCategoryTeacherCredits;
        private double notCareerCategoryTeacherCredits;
        private int teachersSize;
        private int careerTeachersSize;
        private int notCareerTeachersSize;

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public double getCredits() {
            return credits;
        }

        public void setCredits(double credits) {
            this.credits = credits;
        }

        public int getTeachersSize() {
            return teachersSize;
        }

        public void setTeachersSize(int teachersSize) {
            this.teachersSize = teachersSize;
        }

        public double getCareerCategoryTeacherCredits() {
            return careerCategoryTeacherCredits;
        }

        public void setCareerCategoryTeacherCredits(double careerCategoryTeacherCredits) {
            this.careerCategoryTeacherCredits = careerCategoryTeacherCredits;
        }

        public double getNotCareerCategoryTeacherCredits() {
            return notCareerCategoryTeacherCredits;
        }

        public void setNotCareerCategoryTeacherCredits(double notCareerCategoryTeacherCredits) {
            this.notCareerCategoryTeacherCredits = notCareerCategoryTeacherCredits;
        }

        public double getCareerTeachersBalance() {
            return careerTeachersBalance;
        }

        public void setCareerTeachersBalance(double careerTeachersBalance) {
            this.careerTeachersBalance = careerTeachersBalance;
        }

        public int getCareerTeachersSize() {
            return careerTeachersSize;
        }

        public void setCareerTeachersSize(int careerTeachersSize) {
            this.careerTeachersSize = careerTeachersSize;
        }

        public double getNotCareerTeachersBalance() {
            return notCareerTeachersBalance;
        }

        public void setNotCareerTeachersBalance(double notCareerTeachersBalance) {
            this.notCareerTeachersBalance = notCareerTeachersBalance;
        }

        public int getNotCareerTeachersSize() {
            return notCareerTeachersSize;
        }

        public void setNotCareerTeachersSize(int notCareerTeachersSize) {
            this.notCareerTeachersSize = notCareerTeachersSize;
        }
    }
}