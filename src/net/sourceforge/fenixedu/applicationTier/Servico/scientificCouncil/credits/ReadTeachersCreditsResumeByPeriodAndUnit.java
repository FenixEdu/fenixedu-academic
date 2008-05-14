/**
 * Jan 23, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadTeachersCreditsResumeByPeriodAndUnit extends Service {

    public List<TeacherCreditsReportDTO> run(Unit department, ExecutionSemester fromExecutionPeriod,
            ExecutionSemester untilExecutionPeriod) throws ExcepcaoPersistencia, ParseException {

        SortedSet<ExecutionSemester> executionPeriodsBetween = getExecutionPeriodsBetween(fromExecutionPeriod,
                untilExecutionPeriod);
        
        List<Teacher> teachers = department.getAllTeachers(fromExecutionPeriod.getBeginDateYearMonthDay(),
                untilExecutionPeriod.getEndDateYearMonthDay());
        
        List<TeacherCreditsReportDTO> creditLines = new ArrayList<TeacherCreditsReportDTO>();
        for (Teacher teacher : teachers) {            
            if (!teacher.isMonitor(executionPeriodsBetween.last())
                    && !teacher.isInactive(executionPeriodsBetween.last()) 
                    && !teacher.isDeceased()) {                
                Unit workingUnit = teacher.getLastWorkingUnit(untilExecutionPeriod.getBeginDateYearMonthDay(), untilExecutionPeriod.getEndDateYearMonthDay());
                Unit workingUnitDepartment = (workingUnit != null) ? workingUnit.getDepartmentUnit() : null;
                if(workingUnitDepartment != null && workingUnitDepartment.getDepartment().equals(department.getDepartment())) {               
                    TeacherCreditsReportDTO creditsReportDTO = new TeacherCreditsReportDTO();
                    creditsReportDTO.setTeacher(teacher);
                    for (ExecutionSemester executionSemester : executionPeriodsBetween) {
                        updateCreditLine(teacher, executionSemester, creditsReportDTO, true);
                    }                    
                    creditsReportDTO.setUnit(workingUnit);
                    creditsReportDTO.setPastCredits(teacher.getBalanceOfCreditsUntil(fromExecutionPeriod.getPreviousExecutionPeriod()));
                    creditLines.add(creditsReportDTO);
                }
            }
        }
        return creditLines;
    }

    private void updateCreditLine(Teacher teacher, ExecutionSemester executionSemester,
            TeacherCreditsReportDTO creditLine, boolean countCredits) throws ParseException {

        double totalCredits = 0.0;
        if (countCredits) {
            TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
            if (teacherService != null) {
                totalCredits = teacherService.getCredits();
            }
            totalCredits -= teacher.getMandatoryLessonHours(executionSemester);
            totalCredits += teacher.getManagementFunctionsCredits(executionSemester);
            totalCredits += teacher.getServiceExemptionCredits(executionSemester);
        }
        creditLine.getCreditsByExecutionPeriod().put(executionSemester, totalCredits);
    }

    private SortedSet<ExecutionSemester> getExecutionPeriodsBetween(ExecutionSemester fromExecutionPeriod,
            ExecutionSemester untilExecutionPeriod) {
        
        SortedSet<ExecutionSemester> executionPeriodsBetween = new TreeSet<ExecutionSemester>(ExecutionSemester.
                EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR);
        
        ExecutionSemester tempExecutionPeriod = fromExecutionPeriod;
        while (tempExecutionPeriod != untilExecutionPeriod) {
            executionPeriodsBetween.add(tempExecutionPeriod);
            tempExecutionPeriod = tempExecutionPeriod.getNextExecutionPeriod();
        }
        executionPeriodsBetween.add(untilExecutionPeriod);
        return executionPeriodsBetween;
    }

    public static class TeacherCreditsReportDTO {
                     
        Map<ExecutionSemester, Double> creditsByExecutionPeriod = new TreeMap<ExecutionSemester, Double>(ExecutionSemester.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR);

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
