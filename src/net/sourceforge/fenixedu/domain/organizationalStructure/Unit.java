/*
 * Created on Sep 16, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Contract;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.TeacherLegalRegimen;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class Unit extends Unit_Base {

    public static final Comparator UNIT_COMPARATOR_BY_NAME = new BeanComparator("name", Collator
            .getInstance());

    private Unit() {
        super();
    }

    public List<Unit> getTopUnits() {
        Unit unit = this;
        List<Unit> allTopUnits = new ArrayList<Unit>();
        if (unit.hasAnyParentUnits()) {
            for (Unit parentUnit : this.getParentUnits()) {
                if (!parentUnit.hasAnyParentUnits() && !allTopUnits.contains(parentUnit)) {
                    allTopUnits.add(parentUnit);
                } else if (parentUnit.hasAnyParentUnits()) {
                    for (Unit parentUnit2 : parentUnit.getTopUnits()) {
                        if (!allTopUnits.contains(parentUnit2)) {
                            allTopUnits.add(parentUnit2);
                        }
                    }
                }
            }
        }
        return allTopUnits;
    }

    public Unit getDepartmentUnit() {
        List<Unit> parentUnits = this.getParentUnits();
        if (isUnitDepartment(this)) {
            return this;
        } else if (!parentUnits.isEmpty()) {
            for (Unit parentUnit : parentUnits) {
                if (isUnitDepartment(parentUnit)) {
                    return parentUnit;
                } else if (parentUnit.hasAnyParentUnits()) {
                    Unit departmentUnit = parentUnit.getDepartmentUnit();
                    if (departmentUnit == null) {
                        continue;
                    } else {
                        return departmentUnit;
                    }
                }
            }
        }
        return null;
    }

    private boolean isUnitDepartment(Unit unit) {
        return (unit.getType() != null && unit.getType().equals(PartyTypeEnum.DEPARTMENT) && unit
                .getDepartment() != null);
    }

    public List<Unit> getInactiveSubUnits(YearMonthDay currentDate) {
        List<Unit> allInactiveSubUnits = new ArrayList<Unit>();
        for (Unit subUnit : this.getSubUnits()) {
            if (!subUnit.isActive(currentDate)) {
                allInactiveSubUnits.add(subUnit);
            }
        }
        return allInactiveSubUnits;
    }

    public List<Unit> getInactiveSubUnits(YearMonthDay currentDate, AccountabilityTypeEnum accountabilityTypeEnum) {
        List<Unit> allInactiveSubUnits = new ArrayList<Unit>();
        for (Unit subUnit : this.getSubUnits(accountabilityTypeEnum)) {
            if (!subUnit.isActive(currentDate)) {
                allInactiveSubUnits.add(subUnit);
            }
        }
        return allInactiveSubUnits;
    }
    
    public List<Unit> getActiveSubUnits(YearMonthDay currentDate) {
        List<Unit> allActiveSubUnits = new ArrayList<Unit>();
        for (Unit subUnit : this.getSubUnits()) {
            if (subUnit.isActive(currentDate)) {
                allActiveSubUnits.add(subUnit);
            }
        }
        return allActiveSubUnits;
    }
    
    public List<Unit> getActiveSubUnits(YearMonthDay currentDate, AccountabilityTypeEnum accountabilityTypeEnum) {
        List<Unit> allActiveSubUnits = new ArrayList<Unit>();
        for (Unit subUnit : this.getSubUnits(accountabilityTypeEnum)) {
            if (subUnit.isActive(currentDate)) {
                allActiveSubUnits.add(subUnit);
            }
        }
        return allActiveSubUnits;
    }  

    public List<Unit> getActiveSubUnits(YearMonthDay currentDate, List<AccountabilityTypeEnum> accountabilityTypeEnums) {
        List<Unit> allActiveSubUnits = new ArrayList<Unit>();
        for (Unit subUnit : this.getSubUnits(accountabilityTypeEnums)) {
            if (subUnit.isActive(currentDate)) {
                allActiveSubUnits.add(subUnit);
            }
        }
        return allActiveSubUnits;
    }
    
    public List<Unit> getAllInactiveSubUnits(YearMonthDay currentDate) {
        Set<Unit> allInactiveSubUnits = new HashSet<Unit>();
        List<Unit> inactiveSubUnits = getInactiveSubUnits(currentDate);
        allInactiveSubUnits.addAll(inactiveSubUnits);
        for (Unit subUnit : inactiveSubUnits) {
            allInactiveSubUnits.addAll(subUnit.getAllInactiveSubUnits(currentDate));
        }
        return new ArrayList<Unit>(allInactiveSubUnits);
    }
        
    public List<Unit> getAllActiveSubUnits(YearMonthDay currentDate) {
        Set<Unit> allActiveSubUnits = new HashSet<Unit>();
        List<Unit> activeSubUnits = getActiveSubUnits(currentDate);
        allActiveSubUnits.addAll(activeSubUnits);
        for (Unit subUnit : activeSubUnits) {
            allActiveSubUnits.addAll(subUnit.getAllActiveSubUnits(currentDate));
        }
        return new ArrayList<Unit>(allActiveSubUnits);
    }

    public List<Unit> getAllActiveSubUnits(YearMonthDay currentDate, AccountabilityTypeEnum accountabilityTypeEnum) {
        Set<Unit> allActiveSubUnits = new HashSet<Unit>();
        List<Unit> activeSubUnits = getActiveSubUnits(currentDate, accountabilityTypeEnum);
        allActiveSubUnits.addAll(activeSubUnits);
        for (Unit subUnit : activeSubUnits) {
            allActiveSubUnits.addAll(subUnit.getAllActiveSubUnits(currentDate));
        }
        return new ArrayList<Unit>(allActiveSubUnits);
    }

    public List<Unit> getAllInactiveSubUnits(YearMonthDay currentDate, AccountabilityTypeEnum accountabilityTypeEnum) {
        Set<Unit> allInactiveSubUnits = new HashSet<Unit>();
        List<Unit> inactiveSubUnits = getInactiveSubUnits(currentDate, accountabilityTypeEnum);
        allInactiveSubUnits.addAll(inactiveSubUnits);
        for (Unit subUnit : inactiveSubUnits) {
            allInactiveSubUnits.addAll(subUnit.getAllInactiveSubUnits(currentDate));
        }
        return new ArrayList<Unit>(allInactiveSubUnits);
    }
    
    private void checkCostCenterCode(Integer costCenterCode) {
        if (costCenterCode != null) {
            List<Unit> allUnits = readAllUnits();
            for (Unit unit : allUnits) {
                if (!unit.equals(this) && unit.getCostCenterCode() != null
                        && unit.getCostCenterCode().equals(costCenterCode)) {
                    throw new DomainException("error.costCenter.alreadyExists");
                }
            }
        }
    }

    public void edit(String unitName, Integer unitCostCenter, String acronym, Date beginDate,
            Date endDate, PartyTypeEnum type, String webAddress) {

        checkCostCenterCode(unitCostCenter);
        checkUnitDates(beginDate, endDate);
        if (acronym != null && !acronym.equals("") && this.getType() != null) {
            checkAcronym(acronym, this.getType());
        }
        this.setName(unitName);
        this.setBeginDate(beginDate);
        this.setEndDate(endDate);
        this.setType(type);
        this.setCostCenterCode(unitCostCenter);
        this.setWebAddress(webAddress);
        this.setAcronym(acronym);
    }

    private void checkUnitDates(Date beginDate, Date endDate) {
        if(beginDate == null) {
            throw new DomainException("error.unit.no.beginDate");
        }
        if (endDate != null && endDate.before(beginDate)) {
            throw new DomainException("error.unit.endDateBeforeBeginDate");
        }
    }

    private void checkAcronym(String acronym, PartyTypeEnum partyTypeEnum) {
        Unit unit = readUnitByAcronymAndType(acronym, partyTypeEnum);
        if (unit != null && !unit.equals(this)) {
            throw new DomainException("error.existent.acronym");
        }
    }

    public boolean isActive(YearMonthDay currentDate) {
        return (!this.getBeginDateYearMonthDay().isAfter(currentDate) && (this.getEndDateYearMonthDay() == null || !this
                .getEndDateYearMonthDay().isBefore(currentDate)));
    }

    public void delete() {
        if (!canBeDeleted()) {
            throw new DomainException("error.unit.cannot.be.deleted");
        }

        if (hasAnyParentUnits()) {
            this.getParents().get(0).delete();
        }
        for (; !getParticipatingAnyCurricularCourseCurricularRules().isEmpty(); getParticipatingAnyCurricularCourseCurricularRules()
                .get(0).delete())
            ;
        removeDepartment();
        removeDegree();
        super.delete();
    }

    private boolean canBeDeleted() {
        return (!hasAnyParents() || (this.getParentUnits().size() == 1 && this.getParents().size() == 1))
                && !hasAnyFunctions()
                && !hasAnyWorkingContracts()
                && !hasAnyMailingContracts()
                && !hasAnySalaryContracts()
                && !hasAnyCompetenceCourses()
                && !hasAnyExternalPersons()
                && !hasAnyAssociatedNonAffiliatedTeachers()
                && !hasAnyPayedGuides()
                && !hasAnyPayedReceipts();
    }

    public List<Contract> getWorkingContracts(YearMonthDay begin, YearMonthDay end) {
        List<Contract> contracts = new ArrayList<Contract>();
        for (Contract contract : this.getWorkingContracts()) {
            if (contract.belongsToPeriod(begin, end)) {
                contracts.add(contract);
            }
        }
        return contracts;
    }

    // begin SCIENTIFIC AREA UNITS, COMPETENCE COURSE GROUP UNITS AND RELATED
    public List<Unit> getScientificAreaUnits() {
        final SortedSet<Unit> result = new TreeSet<Unit>(Unit.UNIT_COMPARATOR_BY_NAME);

        for (Unit unit : this.getSubUnits()) {
            if (unit.getType() != null && unit.getType().equals(PartyTypeEnum.SCIENTIFIC_AREA)) {
                result.add(unit);
            }
        }

        return new ArrayList<Unit>(result);
    }

    public Double getScientificAreaUnitEctsCredits() {
        double result = 0.0;
        for (Unit competenceCourseGroupUnit : getCompetenceCourseGroupUnits()) {
            for (CompetenceCourse competenceCourse : competenceCourseGroupUnit.getCompetenceCourses()) {
                result += competenceCourse.getEctsCredits();
            }
        }
        return result;
    }

    public Double getScientificAreaUnitEctsCredits(List<Context> contexts) {
        double result = 0.0;
        for (Context context : contexts) {
            if (context.getChildDegreeModule().isLeaf()) {
                CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();

                if (!curricularCourse.isOptional()
                        && curricularCourse.getCompetenceCourse().getScientificAreaUnit().equals(this)) {
                    result += curricularCourse.getCompetenceCourse().getEctsCredits();
                }
            }
        }
        return result;
    }

    public List<Unit> getCompetenceCourseGroupUnits() {
        final SortedSet<Unit> result = new TreeSet<Unit>(Unit.UNIT_COMPARATOR_BY_NAME);

        for (Unit unit : this.getSubUnits()) {
            if (unit.getType() != null && unit.getType().equals(PartyTypeEnum.COMPETENCE_COURSE_GROUP)) {
                result.add(unit);
            }
        }

        return new ArrayList<Unit>(result);
    }

    @Override
    public List<CompetenceCourse> getCompetenceCourses() {
        final SortedSet<CompetenceCourse> result = new TreeSet<CompetenceCourse>(
                CompetenceCourse.COMPETENCE_COURSE_COMPARATOR_BY_NAME);
        result.addAll(super.getCompetenceCourses());
        return new ArrayList<CompetenceCourse>(result);
    }

    // end SCIENTIFIC AREA UNITS, COMPETENCE COURSE GROUP UNITS AND RELATED

    public List<Teacher> getAllTeachers(YearMonthDay begin, YearMonthDay end) {
        List<Teacher> teachers = new ArrayList<Teacher>();
        List<Employee> employees = getAllWorkingEmployees(begin, end);
        for (Employee employee : employees) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null
                    && !teacher.getAllLegalRegimensWithoutEndSituations(begin, end).isEmpty()) {
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<Teacher>();
        List<Employee> employees = getAllWorkingEmployees();
        for (Employee employee : employees) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null && !teacher.getAllLegalRegimensWithoutEndSituations().isEmpty()) {
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    public List<Teacher> getAllCurrentTeachers() {
        YearMonthDay currentDate = new YearMonthDay();
        List<Teacher> teachers = new ArrayList<Teacher>();
        List<Employee> employees = getAllCurrentActiveWorkingEmployees();
        for (Employee employee : employees) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null) {
                TeacherLegalRegimen legalRegimen = teacher.getLastLegalRegimenWithoutEndSituations();
                if (legalRegimen != null && legalRegimen.isActive(currentDate)) {
                    teachers.add(teacher);
                }
            }
        }
        return teachers;
    }

    public Teacher getTeacherByPeriod(Integer teacherNumber, YearMonthDay begin, YearMonthDay end) {
        for (Employee employee : getAllWorkingEmployees(begin, end)) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null && teacher.getTeacherNumber().equals(teacherNumber)
                    && !teacher.getAllLegalRegimensWithoutEndSituations(begin, end).isEmpty()) {
                return teacher;
            }
        }
        return null;
    }

    public List<Employee> getAllWorkingEmployees() {
        Set<Employee> employees = new HashSet<Employee>();
        for (Contract contract : getWorkingContracts()) {
            employees.add(contract.getEmployee());
        }
        for (Unit subUnit : getSubUnits()) {
            employees.addAll(subUnit.getAllWorkingEmployees());
        }
        return new ArrayList<Employee>(employees);
    }

    public List<Employee> getAllWorkingEmployees(YearMonthDay begin, YearMonthDay end) {
        Set<Employee> employees = new HashSet<Employee>();
        for (Contract contract : getWorkingContracts(begin, end)) {
            employees.add(contract.getEmployee());
        }
        for (Unit subUnit : getSubUnits()) {
            employees.addAll(subUnit.getAllWorkingEmployees(begin, end));
        }
        return new ArrayList<Employee>(employees);
    }

    public List<Employee> getAllCurrentActiveWorkingEmployees() {
        Set<Employee> employees = new HashSet<Employee>();
        YearMonthDay currentDate = new YearMonthDay();
        for (Contract contract : getWorkingContracts()) {
            Employee employee = contract.getEmployee();
            if (employee.getActive().booleanValue() && contract.isActive(currentDate)) {
                employees.add(employee);
            }
        }
        for (Unit subUnit : getSubUnits()) {
            employees.addAll(subUnit.getAllCurrentActiveWorkingEmployees());
        }
        return new ArrayList<Employee>(employees);
    }

    public List<Unit> getParentUnits() {
        return new ArrayList(getParentParties(getClass()));        
    }

    public List<Unit> getParentUnits(AccountabilityTypeEnum accountabilityTypeEnum) {
        return new ArrayList(getParentParties(accountabilityTypeEnum, getClass()));        
    }
    
    public List<Unit> getSubUnits() {
        return new ArrayList(getChildParties(getClass()));
    }
    
    public List<Unit> getSubUnits(AccountabilityTypeEnum accountabilityTypeEnum) {
        return new ArrayList(getChildParties(accountabilityTypeEnum, getClass()));        
    }
    
    public List<Unit> getSubUnits(List<AccountabilityTypeEnum> accountabilityTypeEnums) {
        return new ArrayList(getChildParties(accountabilityTypeEnums, getClass()));        
    }
     
    public boolean hasAnyParentUnits() {
        return !getParentUnits().isEmpty();
    }    
    
    public boolean hasAnySubUnits() {
        return !getSubUnits().isEmpty();
    }

    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
        if (parentUnit == null) {
            throw new DomainException("error.unit.inexistent.parentUnit");
        }
        if (parentUnit.equals(this)) {
            throw new DomainException("error.unit.equals.parentUnit");
        }
   
        checkParentUnit(parentUnit);
        return new Accountability(parentUnit, this, accountabilityType);
    }

    private void checkParentUnit(Unit parentUnit) {
        if(getParentUnits().contains(parentUnit)) {
            throw new DomainException("error.unit.parentUnit.is.already.parentUnit");
        }
        YearMonthDay currentDate = new YearMonthDay();
        List<Unit> subUnits = (parentUnit.isActive(currentDate)) ? getAllActiveSubUnits(currentDate) : getAllInactiveSubUnits(currentDate);
        if(subUnits.contains(parentUnit)) {
           throw new DomainException("error.unit.parentUnit.is.already.subUnit"); 
        }
    }

    public Accountability addSubUnit(Unit childUnit, AccountabilityType accountabilityType) {
        return new Accountability(this, childUnit, accountabilityType);
    }

    public void removeParentUnit(Unit parentUnit) {
        for (Accountability accountability : this.getParents()) {
            if (accountability.getParentParty().equals(parentUnit)) {
                accountability.delete();
                break;
            }
        }
    }

    public void removeSubUnit(Unit childUnit) {
        for (Accountability accountability : this.getChilds()) {
            if (accountability.getChildParty().equals(childUnit)) {
                accountability.delete();
                break;
            }
        }
    }

    public NonAffiliatedTeacher findNonAffiliatedTeacherByName(final String name) {
        for (final NonAffiliatedTeacher nonAffiliatedTeacher : getAssociatedNonAffiliatedTeachersSet()) {
            if (nonAffiliatedTeacher.getName().equalsIgnoreCase(name)) {
                return nonAffiliatedTeacher;
            }
        }
        return null;
    }
   
    public Unit getChildUnitByAcronym(String acronym) {
        for (Unit subUnit : getSubUnits()) {
            if ((subUnit.getAcronym() != null) && (subUnit.getAcronym().equals(acronym))) {
                return subUnit;
            }
        }
        return null;
    }

    public static List<Unit> readAllUnits() {
        List<Unit> allUnits = new ArrayList<Unit>();
        for (Party party : RootDomainObject.getInstance().getPartys()) {
            if (party instanceof Unit) {
                allUnits.add((Unit) party);
            }
        }
        return allUnits;
    }

    /**
     * This method should be used only for Degree and Department Unit types,
     * because acronyms of this two types are unique. *
     */
    public static Unit readUnitByAcronymAndType(String acronym, PartyTypeEnum partyTypeEnum) {
        if (partyTypeEnum.equals(PartyTypeEnum.DEGREE_UNIT)
                || partyTypeEnum.equals(PartyTypeEnum.DEPARTMENT)
                || partyTypeEnum.equals(PartyTypeEnum.ACADEMIC_SERVICES_SUPERVISION)) {
            for (Unit unit : readAllUnits()) {
                if (unit.getAcronym() != null && unit.getAcronym().equals(acronym)
                        && unit.getType() != null && unit.getType().equals(partyTypeEnum)) {
                    return unit;
                }
            }
        }
        return null;
    }

    public List<CompetenceCourse> getDepartmentUnitCompetenceCourses(CurricularStage curricularStage) {
        List<CompetenceCourse> result = new ArrayList<CompetenceCourse>();
        if (isUnitDepartment(this)) {
            for (Unit scientificAreaUnit : this.getScientificAreaUnits()) {
                for (Unit competenceCourseGroupUnit : scientificAreaUnit.getCompetenceCourseGroupUnits()) {
                    for (CompetenceCourse competenceCourse : competenceCourseGroupUnit
                            .getCompetenceCourses()) {
                        if (competenceCourse.getCurricularStage().equals(curricularStage)) {
                            result.add(competenceCourse);
                        }
                    }
                }
            }
        }
        return result;
    }

    public static Unit createNewUnit(String unitName, Integer costCenterCode, String acronym,
            Date beginDate, Date endDate, PartyTypeEnum type, Unit parentUnit,
            AccountabilityType accountabilityType, String webAddress) throws FenixFilterException,
            FenixServiceException {

        if (unitName == null) {
            throw new DomainException("error.unit.empty.name");
        }

        Unit unit = new Unit();
        unit.edit(unitName, costCenterCode, acronym, beginDate, endDate, type, webAddress);
        if (parentUnit != null && accountabilityType != null) {
            unit.addParentUnit(parentUnit, accountabilityType);
        }
        return unit;
    }

    public static Unit createNewExternalInstitution(String unitName) {

        if (unitName == null) {
            throw new DomainException("error.unit.empty.name");
        }

        Unit externalInstitutionUnit = UnitUtils
                .readUnitWithoutParentstByName(UnitUtils.EXTERNAL_INSTITUTION_UNIT_NAME);
        if (externalInstitutionUnit == null) {
            throw new DomainException("error.exception.commons.institution.rootInstitutionNotFound");
        }

        Unit institutionUnit = new Unit();
        institutionUnit.setName(unitName);
        institutionUnit.setBeginDate(Calendar.getInstance().getTime());
        institutionUnit.setType(PartyTypeEnum.EXTERNAL_INSTITUTION);
        institutionUnit.addParentUnit(externalInstitutionUnit, AccountabilityType
                .readAccountabilityTypeByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));

        return institutionUnit;
    }

    public Set<Unit> getParentByOrganizationalStructureAccountabilityType() {
    	return (Set) getParentParties(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE, getClass());
    }

    public static Party createContributor(String contributorName, String contributorNumber,
            String contributorAddress, String areaCode, String areaOfAreaCode, String area,
            String parishOfResidence, String districtSubdivisionOfResidence, String districtOfResidence) {

        if (Party.readByContributorNumber(contributorNumber) != null) {
            throw new DomainException("EXTERNAL_INSTITUTION_UNIT.createContributor.existing.contributor.number");
        }
        
        Unit contributor = Unit.createNewExternalInstitution(contributorName);

        contributor.setSocialSecurityNumber(contributorNumber);
        contributor.setAddress(contributorAddress);
        contributor.setAreaCode(areaCode);
        contributor.setAreaOfAreaCode(areaOfAreaCode);
        contributor.setArea(area);
        contributor.setParishOfResidence(parishOfResidence);
        contributor.setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
        contributor.setDistrictOfResidence(districtOfResidence);
        
        return contributor;
    }
    
}
