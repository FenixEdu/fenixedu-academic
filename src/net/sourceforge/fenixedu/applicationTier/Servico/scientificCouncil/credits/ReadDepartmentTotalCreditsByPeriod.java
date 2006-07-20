package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadDepartmentTotalCreditsByPeriod extends Service {

    public Map<ExecutionYear, PeriodCreditsReportDTO>  run(Unit department, ExecutionPeriod fromExecutionPeriod,
            ExecutionPeriod untilExecutionPeriod) throws ExcepcaoPersistencia, ParseException {

        List<ExecutionPeriod> executionPeriodsBetween = getExecutionPeriodsBetween(fromExecutionPeriod,
                untilExecutionPeriod);
        
        List<Teacher> teachers = department.getAllTeachers(fromExecutionPeriod.getBeginDateYearMonthDay(),
                untilExecutionPeriod.getEndDateYearMonthDay());
        
        SortedMap<ExecutionYear, PeriodCreditsReportDTO> departmentGlobalCredits = new TreeMap<ExecutionYear, PeriodCreditsReportDTO>(ExecutionYear.EXECUTION_YEAR_COMPARATOR_BY_YEAR);
                
        ExecutionPeriod lasExecutionPeriod = (!executionPeriodsBetween.isEmpty()) ? executionPeriodsBetween.get(executionPeriodsBetween.size() - 1) : null;
        for (Teacher teacher : teachers) {            
            if (!teacher.isMonitor(lasExecutionPeriod)
                    && !teacher.isInactive(lasExecutionPeriod) 
                    && !teacher.isDeceased()) {                
                
                Unit workingUnit = teacher.getLastWorkingUnit(untilExecutionPeriod.getBeginDateYearMonthDay(), untilExecutionPeriod.getEndDateYearMonthDay());
                Unit workingUnitDepartment = (workingUnit != null) ? workingUnit.getDepartmentUnit() : null;
                if(workingUnitDepartment != null && workingUnitDepartment.getDepartment().equals(department.getDepartment())) {                                                       
                    for (ExecutionPeriod executionPeriod : executionPeriodsBetween) {
                        if(executionPeriod.getSemester().intValue() == 2) {
                            updateCredits(teacher, executionPeriod, departmentGlobalCredits, untilExecutionPeriod);
                        }                        
                    }                    
                }
            }
        }
        return departmentGlobalCredits;
    }
    
    private void updateCredits(Teacher teacher, ExecutionPeriod executionPeriod,
            Map<ExecutionYear, PeriodCreditsReportDTO> departmentCredits, ExecutionPeriod untilExecutionPeriod) throws ParseException {

        double teacherPeriodTotalCredits = teacher.getBalanceOfCreditsUntil(executionPeriod);
        
        if(!departmentCredits.containsKey(executionPeriod.getExecutionYear())) {
            departmentCredits.put(executionPeriod.getExecutionYear(), new PeriodCreditsReportDTO());
        }
        
        PeriodCreditsReportDTO reportDTO = departmentCredits.get(executionPeriod.getExecutionYear());
        reportDTO.setCredits(round(reportDTO.getCredits() + teacherPeriodTotalCredits));
        
        Category category = teacher.getCategoryForCreditsByPeriod(executionPeriod);
        boolean careerTeacher = (category != null && category.isCareerCategory());
        if(careerTeacher) {
            reportDTO.setCareerCategoryTeacherCredits(round(reportDTO.getCareerCategoryTeacherCredits() + teacherPeriodTotalCredits));
        } else {
            reportDTO.setNotCareerCategoryTeacherCredits(round(reportDTO.getNotCareerCategoryTeacherCredits() + teacherPeriodTotalCredits));
        }
        
        if(executionPeriod.equals(untilExecutionPeriod)) {
            if(careerTeacher) {
                reportDTO.setCareerTeachersSize(reportDTO.getCareerTeachersSize() + 1);
                reportDTO.setCareerTeachersBalance(round(reportDTO.getCareerCategoryTeacherCredits() / reportDTO.getCareerTeachersSize()));
            } else {
                reportDTO.setNotCareerTeachersSize(reportDTO.getNotCareerTeachersSize() + 1);
                reportDTO.setNotCareerTeachersBalance(round(reportDTO.getNotCareerCategoryTeacherCredits() / reportDTO.getNotCareerTeachersSize()));
            }
            reportDTO.setTeachersSize(reportDTO.getTeachersSize() + 1);
            reportDTO.setBalance(round(reportDTO.getCredits() / reportDTO.getTeachersSize()));            
        }
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
    
    private Double round(double n) {
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
