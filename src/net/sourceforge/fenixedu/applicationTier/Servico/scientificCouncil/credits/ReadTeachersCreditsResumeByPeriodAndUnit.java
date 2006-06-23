/**
 * Jan 23, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.TeacherLegalRegimen;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.LegalRegimenType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadTeachersCreditsResumeByPeriodAndUnit extends Service {

    public List<TeacherCreditsReportDTO> run(Unit department, ExecutionPeriod fromExecutionPeriod,
            ExecutionPeriod untilExecutionPeriod) throws ExcepcaoPersistencia {

        List<ExecutionPeriod> executionPeriodsBetween = getExecutionPeriodsBetween(fromExecutionPeriod,
                untilExecutionPeriod);
        
        List<Teacher> teachers = department.getTeachers(fromExecutionPeriod.getBeginDateYearMonthDay(),
                untilExecutionPeriod.getEndDateYearMonthDay());
        
        List<TeacherCreditsReportDTO> creditLines = new ArrayList<TeacherCreditsReportDTO>();
        for (Teacher teacher : teachers) {            
            if (!verifyIfTeacherIsMonitor(teacher, executionPeriodsBetween)
                    && !verifyIfTeacherIsInactive(teacher, executionPeriodsBetween) 
                    && !verifyIfTeacherIsDeath(teacher)) {                
                Unit workingUnit = teacher.getLastWorkingUnit(untilExecutionPeriod.getBeginDateYearMonthDay(), untilExecutionPeriod.getEndDateYearMonthDay());
                Unit workingUnitDepartment = (workingUnit != null) ? workingUnit.getDepartmentUnit() : null;
                if(workingUnitDepartment != null && workingUnitDepartment.getDepartment().equals(department.getDepartment())) {               
                    TeacherCreditsReportDTO creditsReportDTO = new TeacherCreditsReportDTO();
                    creditsReportDTO.setTeacher(teacher);
                    for (ExecutionPeriod executionPeriod : executionPeriodsBetween) {
                        updateCreditLine(teacher, executionPeriod, creditsReportDTO, true);
                    }                    
                    creditsReportDTO.setUnit(workingUnit);
                    creditsReportDTO.setPastCredits(teacher.getBalanceOfCreditsUntil(fromExecutionPeriod.getPreviousExecutionPeriod()));
                    creditLines.add(creditsReportDTO);
                }
            }
        }
        return creditLines;
    }
    
    private boolean verifyIfTeacherIsDeath(Teacher teacher) {
        for (TeacherLegalRegimen legalRegimen : teacher.getLegalRegimens()) {
            if (legalRegimen.getLegalRegimenType().equals(LegalRegimenType.DEATH)) {
                return true;
            }
        }
        return false;
    }

    private boolean verifyIfTeacherIsInactive(Teacher teacher,
            List<ExecutionPeriod> executionPeriodsBetween) {

        if (!executionPeriodsBetween.isEmpty()) {
            ExecutionPeriod lastExecutionPeriod = executionPeriodsBetween.get(executionPeriodsBetween
                    .size() - 1);
            List<TeacherLegalRegimen> allLegalRegimens = teacher
                    .getAllLegalRegimensWithoutEndSituations(lastExecutionPeriod.getBeginDateYearMonthDay(),
                            lastExecutionPeriod.getEndDateYearMonthDay());
            if (allLegalRegimens.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean verifyIfTeacherIsMonitor(Teacher teacher,
            List<ExecutionPeriod> executionPeriodsBetween) {

        if (!executionPeriodsBetween.isEmpty()) {
            Category category = teacher.getCategoryForCreditsByPeriod(executionPeriodsBetween
                    .get(executionPeriodsBetween.size() - 1));
            if (category != null
                    && (category.getCode().equalsIgnoreCase("MNT") || category.getCode()
                            .equalsIgnoreCase("MNL"))) {
                return true;
            }
        }
        return false;
    }

    private void updateCreditLine(Teacher teacher, ExecutionPeriod executionPeriod,
            TeacherCreditsReportDTO creditLine, boolean countCredits) {

        double totalCredits = 0.0;
        if (countCredits) {
            TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
            if (teacherService != null) {
                totalCredits = teacherService.getCredits();
            }
            totalCredits -= teacher.getMandatoryLessonHours(executionPeriod);
            totalCredits += teacher.getManagementFunctionsCredits(executionPeriod);
            totalCredits += teacher.getServiceExemptionCredits(executionPeriod);
        }
        creditLine.getCreditsByExecutionPeriod().put(executionPeriod, totalCredits);
    }

    private List<ExecutionPeriod> getExecutionPeriodsBetween(ExecutionPeriod fromExecutionPeriod,
            ExecutionPeriod untilExecutionPeriod) {
        List<ExecutionPeriod> executionPeriodsBetween = new ArrayList<ExecutionPeriod>();
        ExecutionPeriod tempExecutionPeriod = fromExecutionPeriod;
        while (tempExecutionPeriod != untilExecutionPeriod) {
            executionPeriodsBetween.add(tempExecutionPeriod);
            tempExecutionPeriod = tempExecutionPeriod.getNextExecutionPeriod();
        }
        executionPeriodsBetween.add(untilExecutionPeriod);
        return executionPeriodsBetween;
    }

    public static class TeacherCreditsReportDTO {
        private static final Comparator comparator_ = new ComparatorChain();                
        static {
            ((ComparatorChain)comparator_).addComparator(new BeanComparator("executionYear.year"));
            ((ComparatorChain)comparator_).addComparator(new BeanComparator("semester"));            
        }
             
        Map<ExecutionPeriod, Double> creditsByExecutionPeriod = new TreeMap<ExecutionPeriod, Double>(comparator_);

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

        public Map<ExecutionPeriod, Double> getCreditsByExecutionPeriod() {
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
