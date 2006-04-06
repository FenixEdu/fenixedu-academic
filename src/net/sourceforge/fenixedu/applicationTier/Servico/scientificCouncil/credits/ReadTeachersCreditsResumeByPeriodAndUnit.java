/**
 * Jan 23, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Contract;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.TeacherLegalRegimen;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.LegalRegimenType;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadTeachersCreditsResumeByPeriodAndUnit extends Service {

    public List<TeacherCreditsReportDTO> run(Unit unit, ExecutionPeriod fromExecutionPeriod,
            ExecutionPeriod untilExecutionPeriod) throws ExcepcaoPersistencia {

        List<ExecutionPeriod> executionPeriodsBetween = getExecutionPeriodsBetween(fromExecutionPeriod,
                untilExecutionPeriod);
        List<Teacher> teachers = unit.getTeachers(fromExecutionPeriod.getBeginDate(),
                untilExecutionPeriod.getEndDate());
        List<TeacherCreditsReportDTO> creditLines = new ArrayList<TeacherCreditsReportDTO>();
        for (Teacher teacher : teachers) {
            if (!verifyIfTeacherIsMonitor(teacher, executionPeriodsBetween)
                    && !verifyIfTeacherIsInactive(teacher, executionPeriodsBetween)
                    && !verifyIfTeacherIsDeath(teacher)) {
                TeacherCreditsReportDTO creditsReportDTO = new TeacherCreditsReportDTO();
                creditsReportDTO.setTeacher(teacher);
                for (ExecutionPeriod executionPeriod : executionPeriodsBetween) {
                    if (unit.getTeacherByPeriod(teacher.getTeacherNumber(), executionPeriod
                            .getBeginDate(), executionPeriod.getEndDate()) != null) {
                        updateCreditLine(teacher, executionPeriod, creditsReportDTO, true);
                    } else {

                        updateCreditLine(teacher, executionPeriod, creditsReportDTO, false);
                    }
                }
                List<Contract> contracts = teacher.getPerson().getEmployee().getContractsByPeriod(
                        fromExecutionPeriod.getBeginDate(), untilExecutionPeriod.getEndDate());
                Collections.sort(contracts, new BeanComparator("beginDate"));
                // if it has more than one contract in the given period, return
                // the
                // first working unit contract
                Unit workingUnit = contracts.get(contracts.size() - 1).getWorkingUnit();
                Unit displayUnit = getDisplayUnit(workingUnit);
                creditsReportDTO.setUnit(displayUnit);
                setPastCredits(teacher, creditsReportDTO);
                creditLines.add(creditsReportDTO);
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
                    .getAllLegalRegimensWithoutEndSituations(lastExecutionPeriod
                            .getBeginDate(), lastExecutionPeriod.getEndDate());
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

    private Unit getDisplayUnit(Unit unit) {
        Set<Unit> displayUnits = new HashSet<Unit>();
        setAllTopDisplayUnits(unit, displayUnits);
        if (displayUnits.isEmpty()) {
            return unit.getDepartmentUnit();
        } else {
            // if a unit has more than one top unit, return the first
            return displayUnits.iterator().next();
        }
    }

    private void setAllTopDisplayUnits(Unit unit, Set<Unit> displayUnits) {
        if (unit.getType() != null
                && (unit.getType().equals(PartyTypeEnum.SCIENTIFIC_AREA) || unit.getType().equals(
                        PartyTypeEnum.SECTION)) && unit.getCostCenterCode() != null) {
            displayUnits.add(unit);
        }
        for (Unit displayUnit : unit.getTopUnits()) {
            setAllTopDisplayUnits(displayUnit, displayUnits);
        }
    }

    private void updateCreditLine(Teacher teacher, ExecutionPeriod executionPeriod,
            TeacherCreditsReportDTO creditLine, boolean countCredits) {
        double totalCredits = 0;
        if (countCredits) {
            TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
            // Category category =
            // teacher.getCategoryForCreditsByPeriod(executionPeriod);
            // if (category != null
            // // ignore if it has a monitor category
            // && (!category.getCode().equalsIgnoreCase("MNT") ||
            // category.getCode()
            // .equalsIgnoreCase("MNL"))) {
            if (teacherService != null) {
                totalCredits = teacherService.getCredits();
            }
            totalCredits -= teacher.getMandatoryLessonHours(executionPeriod);
            totalCredits += teacher.getManagementFunctionsCredits(executionPeriod);
            totalCredits += teacher.getServiceExemptionCredits(executionPeriod);
            // }
        }
        creditLine.getCreditsByExecutionPeriod().put(executionPeriod, totalCredits);
    }

    private void setPastCredits(Teacher teacher, TeacherCreditsReportDTO creditLine) throws ExcepcaoPersistencia {
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(1);
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService != null) {
            creditLine.setPastCredits(teacherService.getPastServiceCredits());
        }
    }

    /**
     * @param fromExecutionPeriod
     * @param untilExecutionPeriod
     * @return
     */
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
        Map<ExecutionPeriod, Double> creditsByExecutionPeriod = new TreeMap<ExecutionPeriod, Double>(new BeanComparator("beginDate"));

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
