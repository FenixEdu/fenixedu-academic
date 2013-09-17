package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class ReadDepartmentTotalCreditsByPeriod {

    @Atomic
    public static Map<ExecutionYear, PeriodCreditsReportDTO> run(Unit department, ExecutionSemester fromExecutionPeriod,
            ExecutionSemester untilExecutionPeriod) throws ParseException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        List<ExecutionSemester> executionPeriodsBetween = getExecutionPeriodsBetween(fromExecutionPeriod, untilExecutionPeriod);

        List<Teacher> teachers =
                department.getAllTeachers(fromExecutionPeriod.getBeginDateYearMonthDay(),
                        untilExecutionPeriod.getEndDateYearMonthDay());

        SortedMap<ExecutionYear, PeriodCreditsReportDTO> departmentGlobalCredits =
                new TreeMap<ExecutionYear, PeriodCreditsReportDTO>(ExecutionYear.COMPARATOR_BY_YEAR);

        ExecutionSemester lasExecutionPeriod =
                (!executionPeriodsBetween.isEmpty()) ? executionPeriodsBetween.get(executionPeriodsBetween.size() - 1) : null;
        for (Teacher teacher : teachers) {
            if (!teacher.isMonitor(lasExecutionPeriod) && !teacher.isInactive(lasExecutionPeriod)) {
                Unit workingUnit =
                        teacher.getLastWorkingUnit(untilExecutionPeriod.getBeginDateYearMonthDay(),
                                untilExecutionPeriod.getEndDateYearMonthDay());
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

        double teacherPeriodTotalCredits = teacher.getBalanceOfCreditsUntil(executionSemester);

        if (!departmentCredits.containsKey(executionSemester.getExecutionYear())) {
            departmentCredits.put(executionSemester.getExecutionYear(), new PeriodCreditsReportDTO());
        }

        PeriodCreditsReportDTO reportDTO = departmentCredits.get(executionSemester.getExecutionYear());
        reportDTO.setCredits(round(reportDTO.getCredits() + teacherPeriodTotalCredits));

        boolean careerTeacher = teacher.isTeacherCareerCategory(executionSemester);
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