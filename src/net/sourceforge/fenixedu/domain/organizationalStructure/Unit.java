/*
 * Created on Sep 16, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.parking.ParkingPartyClassification;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.domain.util.FunctionalityPrinters;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.util.ContractType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class Unit extends Unit_Base {

    private final static ResourceBundle applicationResourcesBundle;
    public final static Comparator<Unit> UNIT_COMPARATOR_BY_NAME = new ComparatorChain();
    static {
	((ComparatorChain) UNIT_COMPARATOR_BY_NAME).addComparator(new BeanComparator("name", Collator.getInstance()));
	((ComparatorChain) UNIT_COMPARATOR_BY_NAME).addComparator(new BeanComparator("idInternal"));
	applicationResourcesBundle = ResourceBundle.getBundle("resources.ApplicationResources", new Locale("pt"));
    }

    protected Unit() {	
	super();	
    }

    protected void init(String name, Integer costCenterCode, String acronym, YearMonthDay beginDate, YearMonthDay endDate, 
	    String webAddress, UnitClassification classification, Boolean canBeResponsibleOfSpaces) {
	
	setName(name);	
	setAcronym(acronym);
	setCostCenterCode(costCenterCode);	
	setBeginDateYearMonthDay(beginDate);
	setEndDateYearMonthDay(endDate);
	setClassification(classification);
	setCanBeResponsibleOfSpaces(canBeResponsibleOfSpaces);
	updateDefaultWebAddress(webAddress);
    }
    
    @Override
    public void setName(String name) {
	super.setName(name);
	UnitName unitName = getUnitName();
	if (unitName == null) {
	    unitName = new UnitName(this);
	}
	unitName.setName(name);
    }

    public void edit(String unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
	    YearMonthDay endDate, String webAddress, UnitClassification classification, 
	    Department department, Degree degree, Boolean canBeResponsibleOfSpaces) {
	
	init(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, canBeResponsibleOfSpaces);	
    }

    @Override
    public void setCanBeResponsibleOfSpaces(Boolean canBeResponsibleOfSpaces) {
        super.setCanBeResponsibleOfSpaces(canBeResponsibleOfSpaces != null ? canBeResponsibleOfSpaces : Boolean.FALSE);
    }
    
    @Override
    public void setCostCenterCode(Integer costCenterCode) {
	Unit unit = readByCostCenterCode(costCenterCode);
	if (unit != null && !unit.equals(this)) {
	    throw new DomainException("error.costCenter.alreadyExists");
	}
	super.setCostCenterCode(costCenterCode);
    }

    @Override
    public void setBeginDateYearMonthDay(YearMonthDay beginDateYearMonthDay) {
	if (beginDateYearMonthDay == null) {
	    throw new DomainException("error.unit.no.beginDate");
	}
	super.setBeginDateYearMonthDay(beginDateYearMonthDay);
    }

    @Override
    public void setEndDateYearMonthDay(YearMonthDay endDateYearMonthDay) {
	if (getBeginDateYearMonthDay() == null
		|| (endDateYearMonthDay != null && endDateYearMonthDay
			.isBefore(getBeginDateYearMonthDay()))) {
	    throw new DomainException("error.unit.endDateBeforeBeginDate");
	}
	super.setEndDateYearMonthDay(endDateYearMonthDay);
    }
    
    public void delete() {
	if (!canBeDeleted()) {
	    throw new DomainException("error.unit.cannot.be.deleted");
	}
	if (hasAnyParentUnits()) {
	    getParents().get(0).delete();
	}
	if (hasSite()) {
	    getSite().delete();
    	}
		
	getUnitName().delete();		
	super.delete();
    }

    private boolean canBeDeleted() {
	return (!hasAnyParents() || (getParentsCount() == 1 && getParentUnits().size() == 1))
		&& !hasAnyChilds()
		&& !hasAnyFunctions()				
		&& !hasAnySpaceResponsibility()
		&& !hasAnyMaterials()
		&& !hasAnyVigilantGroups()		
		&& !hasAnyAssociatedNonAffiliatedTeachers()
		&& !hasAnyPayedGuides()
		&& !hasAnyPayedReceipts()
		&& !hasAnyExtraPayingUnitAuthorizations()
                && !hasAnyExtraWorkingUnitAuthorizations()		
		&& !hasAnyExternalCurricularCourses()
		&& !hasAnyResultUnitAssociations()		
		&& !hasUnitServiceAgreementTemplate()	
		&& !hasAnyResearchInterests()
		&& !hasAnyProjectParticipations() 
		&& !hasAnyParticipations()
		&& !hasAnyBoards()
		&& (!hasSite() || getSite().canBeDeleted())
		&& !hasAnyOwnedReceipts()
		&& !hasAnyCreatedReceipts()
                && !hasAnyProtocols()
                && !hasAnyPartnerProtocols();
    }

    public boolean isInternal() {
	if(this.equals(UnitUtils.readInstitutionUnit())){
	    return true;
	}
		
	for (final Unit parentUnit : getParentUnits()) {
	    if(parentUnit.isInternal()) {
		return true;
	    }
	}
		
	return false;
    }
    
    public boolean isNoOfficialExternal() {
	if(this.equals(UnitUtils.readExternalInstitutionUnit())){
	    return true;
	}	
	for (final Unit parentUnit : getParentUnits()) {
	    if(parentUnit.isNoOfficialExternal()) {
		return true;
	    }
	}	
	return false;
    }
    
    public boolean isOfficialExternal() {
	return !isInternal() && !isNoOfficialExternal(); 
    }
    
    public boolean isActive(YearMonthDay currentDate) {
	return (!this.getBeginDateYearMonthDay().isAfter(currentDate) && (this.getEndDateYearMonthDay() == null || !this
		.getEndDateYearMonthDay().isBefore(currentDate)));
    }
    
    @Override
    public boolean isUnit(){
    	return true;    
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

    public Department getDepartment() {
	return null;
    }
    
    public Degree getDegree() {
	return null;
    }
         
    public DepartmentUnit getDepartmentUnit() {	
	if (this.isDepartmentUnit()) {
	    return (DepartmentUnit) this;
	} else {	    
	    for (Unit parentUnit : getParentUnits()) {
		if (parentUnit.isDepartmentUnit()) {
		    return (DepartmentUnit) parentUnit;
		} else if (parentUnit.hasAnyParentUnits()) {
		    Unit departmentUnit = parentUnit.getDepartmentUnit();
		    if (departmentUnit == null) {
			continue;
		    } else {
			return (DepartmentUnit) departmentUnit;
		    }
		}
	    }
	}
	return null;
    }
   
    public List<Unit> getInactiveSubUnits(YearMonthDay currentDate) {
	return getSubUnitsByState(currentDate, false);
    }

    public List<Unit> getActiveSubUnits(YearMonthDay currentDate) {
	return getSubUnitsByState(currentDate, true);
    }

    private List<Unit> getSubUnitsByState(YearMonthDay currentDate, boolean state) {
	List<Unit> allSubUnits = new ArrayList<Unit>();
	for (Unit subUnit : this.getSubUnits()) {
	    if (subUnit.isActive(currentDate) == state) {
		allSubUnits.add(subUnit);
	    }
	}
	return allSubUnits;
    }

    public List<Unit> getInactiveParentUnits(YearMonthDay currentDate) {
	return getParentUnitsByState(currentDate, false);
    }

    public List<Unit> getActiveParentUnits(YearMonthDay currentDate) {
	return getParentUnitsByState(currentDate, true);
    }

    private List<Unit> getParentUnitsByState(YearMonthDay currentDate, boolean state) {
	List<Unit> allParentUnits = new ArrayList<Unit>();
	for (Unit subUnit : this.getParentUnits()) {
	    if (subUnit.isActive(currentDate) == state) {
		allParentUnits.add(subUnit);
	    }
	}
	return allParentUnits;
    }

    public List<Unit> getInactiveSubUnits(YearMonthDay currentDate, AccountabilityTypeEnum accountabilityTypeEnum) {
	return getSubUnitsByState(currentDate, accountabilityTypeEnum, false);
    }

    public List<Unit> getActiveSubUnits(YearMonthDay currentDate, AccountabilityTypeEnum accountabilityTypeEnum) {
	return getSubUnitsByState(currentDate, accountabilityTypeEnum, true);
    }

    private List<Unit> getSubUnitsByState(YearMonthDay currentDate, AccountabilityTypeEnum accountabilityTypeEnum, boolean state) {
	List<Unit> allSubUnits = new ArrayList<Unit>();
	for (Unit subUnit : getSubUnits(accountabilityTypeEnum)) {
	    if (subUnit.isActive(currentDate) == state) {
		allSubUnits.add(subUnit);
	    }
	}
	return allSubUnits;
    }

    public List<Unit> getActiveSubUnits(YearMonthDay currentDate, List<AccountabilityTypeEnum> accountabilityTypeEnums) {
	return getSubUnitsByState(currentDate, accountabilityTypeEnums, true);
    }

    public List<Unit> getInactiveSubUnits(YearMonthDay currentDate, List<AccountabilityTypeEnum> accountabilityTypeEnums) {
	return getSubUnitsByState(currentDate, accountabilityTypeEnums, false);
    }

    private List<Unit> getSubUnitsByState(YearMonthDay currentDate, List<AccountabilityTypeEnum> accountabilityTypeEnums, boolean state) {
	List<Unit> allSubUnits = new ArrayList<Unit>();
	for (Unit subUnit : this.getSubUnits(accountabilityTypeEnums)) {
	    if (subUnit.isActive(currentDate) == state) {
		allSubUnits.add(subUnit);
	    }
	}
	return allSubUnits;
    }

    public List<Unit> getAllInactiveParentUnits(YearMonthDay currentDate) {
	Set<Unit> allInactiveParentUnits = new HashSet<Unit>();	
	allInactiveParentUnits.addAll(getInactiveParentUnits(currentDate));
	for (Unit subUnit : getParentUnits()) {
	    allInactiveParentUnits.addAll(subUnit.getAllInactiveParentUnits(currentDate));
	}
	return new ArrayList<Unit>(allInactiveParentUnits);
    }

    public List<Unit> getAllActiveParentUnits(YearMonthDay currentDate) {
	Set<Unit> allActiveParentUnits = new HashSet<Unit>();
	allActiveParentUnits.addAll(getActiveParentUnits(currentDate));
	for (Unit subUnit : getParentUnits()) {
	    allActiveParentUnits.addAll(subUnit.getAllActiveParentUnits(currentDate));
	}
	return new ArrayList<Unit>(allActiveParentUnits);
    }

    public List<Unit> getAllInactiveSubUnits(YearMonthDay currentDate) {
	Set<Unit> allInactiveSubUnits = new HashSet<Unit>();
	allInactiveSubUnits.addAll(getInactiveSubUnits(currentDate));
	for (Unit subUnit : getSubUnits()) {
	    allInactiveSubUnits.addAll(subUnit.getAllInactiveSubUnits(currentDate));
	}
	return new ArrayList<Unit>(allInactiveSubUnits);
    }

    public List<Unit> getAllActiveSubUnits(YearMonthDay currentDate) {
	Set<Unit> allActiveSubUnits = new HashSet<Unit>();	
	allActiveSubUnits.addAll(getActiveSubUnits(currentDate));
	for (Unit subUnit : getSubUnits()) {
	    allActiveSubUnits.addAll(subUnit.getAllActiveSubUnits(currentDate));
	}
	return new ArrayList<Unit>(allActiveSubUnits);
    }

    public List<Unit> getAllActiveSubUnits(YearMonthDay currentDate, AccountabilityTypeEnum accountabilityTypeEnum) {
	Set<Unit> allActiveSubUnits = new HashSet<Unit>();	
	allActiveSubUnits.addAll(getActiveSubUnits(currentDate, accountabilityTypeEnum));
	for (Unit subUnit : getSubUnits(accountabilityTypeEnum)) {
	    allActiveSubUnits.addAll(subUnit.getAllActiveSubUnits(currentDate));
	}
	return new ArrayList<Unit>(allActiveSubUnits);
    }

    public List<Unit> getAllInactiveSubUnits(YearMonthDay currentDate, AccountabilityTypeEnum accountabilityTypeEnum) {
	Set<Unit> allInactiveSubUnits = new HashSet<Unit>();	
	allInactiveSubUnits.addAll(getInactiveSubUnits(currentDate, accountabilityTypeEnum));
	for (Unit subUnit : getSubUnits(accountabilityTypeEnum)) {
	    allInactiveSubUnits.addAll(subUnit.getAllInactiveSubUnits(currentDate));
	}
	return new ArrayList<Unit>(allInactiveSubUnits);
    }

    public Collection<Unit> getAllSubUnits() {
	Set<Unit> allSubUnits = new HashSet<Unit>();
	Collection<Unit> subUnits = getSubUnits();
	allSubUnits.addAll(subUnits);
	for (Unit subUnit : subUnits) {
	    allSubUnits.addAll(subUnit.getAllSubUnits());
	}
	return allSubUnits;
    }

    public Collection<Unit> getAllParentUnits() {
	Set<Unit> allParentUnits = new HashSet<Unit>();
	Collection<Unit> parentUnits = getSubUnits();
	allParentUnits.addAll(parentUnits);
	for (Unit subUnit : parentUnits) {
	    allParentUnits.addAll(subUnit.getAllParentUnits());
	}
	return allParentUnits;
    }   

    public Collection<ExternalContract> getExternalPersons() {
	return (Collection<ExternalContract>) getChildAccountabilities(AccountabilityTypeEnum.EMPLOYEE_CONTRACT, ExternalContract.class);
    }
    
    public Collection<Contract> getEmployeeContracts() {
	return (Collection<Contract>) getChildAccountabilities(AccountabilityTypeEnum.EMPLOYEE_CONTRACT, Contract.class);
    }

    public List<Contract> getWorkingContracts(YearMonthDay begin, YearMonthDay end) {
	List<Contract> contracts = new ArrayList<Contract>();
	for (Contract contract : getEmployeeContracts()) {
	    if (contract.getContractType().equals(ContractType.WORKING) && contract.belongsToPeriod(begin, end)) {
		contracts.add(contract);
	    }
	}
	return contracts;
    }

    public List<Contract> getWorkingContracts() {
	return getContractsByContractType(ContractType.WORKING);
    }
    
    public List<Contract> getContractsByContractType(ContractType contractType) {
	List<Contract> contracts = new ArrayList<Contract>();
	for (Contract contract : getEmployeeContracts()) {
	    if (contract.getContractType().equals(contractType)) {
		contracts.add(contract);
	    }
	}
	return contracts;
    }

    public List<Teacher> getAllTeachers() {
	List<Teacher> teachers = new ArrayList<Teacher>();
	List<Employee> employees = getAllWorkingEmployees();
	for (Employee employee : employees) {
	    Teacher teacher = employee.getPerson().getTeacher();
	    if (teacher != null && !teacher.getAllLegalRegimensWithoutSpecialSituations().isEmpty()) {
		teachers.add(teacher);
	    }
	}
	return teachers;
    }

    public List<Teacher> getAllTeachers(YearMonthDay begin, YearMonthDay end) {
	List<Teacher> teachers = new ArrayList<Teacher>();
	List<Employee> employees = getAllWorkingEmployees(begin, end);
	for (Employee employee : employees) {
	    Teacher teacher = employee.getPerson().getTeacher();
	    if (teacher != null && !teacher.getAllLegalRegimensWithoutSpecialSituations(begin, end).isEmpty()) {
		teachers.add(teacher);
	    }
	}
	return teachers;
    }

    public List<Teacher> getAllCurrentTeachers() {
	List<Teacher> teachers = new ArrayList<Teacher>();
	List<Employee> employees = getAllCurrentActiveWorkingEmployees();
	for (Employee employee : employees) {
	    Teacher teacher = employee.getPerson().getTeacher();
	    if (teacher != null && teacher.getCurrentLegalRegimenWithoutSpecialSitutions() != null) {		
		teachers.add(teacher);		
	    }
	}
	return teachers;
    }

    public List<Employee> getAllCurrentNonTeacherEmployees() {
        List<Employee> employees = getAllCurrentActiveWorkingEmployees();
        for (Iterator<Employee> iter = employees.iterator(); iter.hasNext();) {
            Employee employee = iter.next();            
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null && teacher.getCurrentLegalRegimenWithoutSpecialSitutions() != null) {
                iter.remove();
            }
        }        
        return employees;
    }
    
    public Teacher getTeacherByPeriod(Integer teacherNumber, YearMonthDay begin, YearMonthDay end) {
	for (Employee employee : getAllWorkingEmployees(begin, end)) {
	    Teacher teacher = employee.getPerson().getTeacher();
	    if (teacher != null && teacher.getTeacherNumber().equals(teacherNumber)
		    && !teacher.getAllLegalRegimensWithoutSpecialSituations(begin, end).isEmpty()) {
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

    public Collection<Unit> getParentUnits() {
	return (Collection<Unit>) getParentParties(Unit.class);
    }

    public Collection<Unit> getParentUnits(AccountabilityTypeEnum accountabilityTypeEnum) {
	return (Collection<Unit>) getParentParties(accountabilityTypeEnum, Unit.class);
    }

    public Collection<Unit> getParentUnits(List<AccountabilityTypeEnum> accountabilityTypeEnums) {
	return (Collection<Unit>) getParentParties(accountabilityTypeEnums, Unit.class);
    }

    public Collection<Unit> getSubUnits() {
	return (Collection<Unit>) getChildParties(Unit.class);
    }

    public Collection<Unit> getSubUnits(AccountabilityTypeEnum accountabilityTypeEnum) {
	return (Collection<Unit>) getChildParties(accountabilityTypeEnum, Unit.class);
    }

    public Collection<Unit> getSubUnits(List<AccountabilityTypeEnum> accountabilityTypeEnums) {
	return (Collection<Unit>) getChildParties(accountabilityTypeEnums, Unit.class);
    }

    public Collection<Unit> getSubUnits(final PartyTypeEnum type) {
	return (Collection<Unit>) getChildParties(type, Unit.class);
    }

    public boolean hasAnyParentUnits() {
	return !getParentUnits().isEmpty();
    }

    public boolean hasAnySubUnits() {
	return !getSubUnits().isEmpty();
    }

    public Collection<Unit> getParentByOrganizationalStructureAccountabilityType() {
	return (Collection<Unit>) getParentParties(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE, Unit.class);
    }
    
    public int getUnitDepth() {
        int depth = 0;
        
        for (Unit unit : getParentUnits()) {
            depth = Math.max(depth, 1 + unit.getUnitDepth());
        }
        
        return depth;
    }

    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
	if (this.equals(parentUnit)) {
	    throw new DomainException("error.unit.equals.parentUnit");
	}
	if (getParentUnits(accountabilityType.getType()).contains(parentUnit)) {
	    throw new DomainException("error.unit.parentUnit.is.already.parentUnit");
	}
	
	YearMonthDay currentDate = new YearMonthDay();
	List<Unit> subUnits = (parentUnit.isActive(currentDate)) ? getAllActiveSubUnits(currentDate) : getAllInactiveSubUnits(currentDate);
	if (subUnits.contains(parentUnit)) {
	    throw new DomainException("error.unit.parentUnit.is.already.subUnit");
	}
	
	return new Accountability(parentUnit, this, accountabilityType);
    }
           
    public AdministrativeOffice getAdministrativeOffice() {	
	for (Unit parentUnit : getParentUnits(AccountabilityTypeEnum.ADMINISTRATIVE_STRUCTURE)) {
	    if (parentUnit.isAdministrativeOfficeUnit()) {
		return (parentUnit).getAdministrativeOffice();
	    }
	}	
	return null;
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
	final List<Unit> allUnits = new ArrayList<Unit>();
	for (final Party party : RootDomainObject.getInstance().getPartys()) {
	    if (party instanceof Unit) {
		allUnits.add((Unit) party);
	    }
	}
	return allUnits;
    }

    /**
     *  This method should be used only for Unit types where acronyms are unique.
     **/
    public static Unit readUnitByAcronymAndType(String acronym, PartyTypeEnum partyTypeEnum) {
	if (acronym != null
		&& !acronym.equals("")
		&& partyTypeEnum != null
		&& (partyTypeEnum.equals(PartyTypeEnum.DEGREE_UNIT)
			|| partyTypeEnum.equals(PartyTypeEnum.DEPARTMENT)			
			|| partyTypeEnum.equals(PartyTypeEnum.PLANET)
			|| partyTypeEnum.equals(PartyTypeEnum.COUNTRY)
			|| partyTypeEnum.equals(PartyTypeEnum.DEPARTMENT)
			|| partyTypeEnum.equals(PartyTypeEnum.UNIVERSITY) 
			|| partyTypeEnum.equals(PartyTypeEnum.SCHOOL))) {

	    for (Unit unit : readAllUnits()) {
		if (unit.getAcronym() != null && unit.getAcronym().equals(acronym)
			&& unit.getType() != null && unit.getType().equals(partyTypeEnum)) {
		    return unit;
		}
	    }
	}
	return null;
    }

    public static List<Unit> readUnitsByAcronym(String acronym) {
	List<Unit> result = new ArrayList<Unit>();
	if (!StringUtils.isEmpty(acronym.trim())) {
	    for (Party party : RootDomainObject.getInstance().getPartys()) {
		if (party.isUnit() && ((Unit) party).getAcronym() != null
			&& ((Unit) party).getAcronym().equals(acronym)) {
		    result.add((Unit) party);
		}
	    }
	}
	return result;
    }

    public static Unit readByCostCenterCode(Integer costCenterCode) {
	if (costCenterCode != null) {
	    for (Party party : RootDomainObject.getInstance().getPartys()) {
		if (party.isUnit() && ((Unit) party).getCostCenterCode() != null
			&& ((Unit) party).getCostCenterCode().equals(costCenterCode)) {
		    return (Unit) party;
		}
	    }
	}
	return null;
    }    
    
    public Collection<Unit> getParentUnitsByOrganizationalStructureAccountabilityType() {
	return (Collection<Unit>) getParentParties(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE, Unit.class);
    }

    public static Unit createNewUnit(String unitName, Integer costCenterCode, String acronym,
	    YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
	    AccountabilityType accountabilityType, String webAddress, UnitClassification classification,
	    Boolean canBeResponsibleOfSpaces) {

	Unit unit = new Unit();
	unit.init(unitName, costCenterCode, acronym, beginDate, endDate, webAddress, classification, canBeResponsibleOfSpaces);	
	if (parentUnit != null && accountabilityType != null) {
	    unit.addParentUnit(parentUnit, accountabilityType);
	}
	return unit;
    }

    public static Unit createNewNoOfficialExternalInstitution(String unitName) {
	Unit externalInstitutionUnit = UnitUtils.readExternalInstitutionUnit();		
	Unit noOfficialExternalInstitutionUnit = new Unit();
	noOfficialExternalInstitutionUnit.init(unitName, null, null, new YearMonthDay(), null, null, null, null);
	noOfficialExternalInstitutionUnit.addParentUnit(externalInstitutionUnit,
		AccountabilityType.readAccountabilityTypeByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));

	return noOfficialExternalInstitutionUnit;
    }   

    public static Party createContributor(final String contributorName, final String contributorNumber,
	    final PhysicalAddressData data) {

	if (Party.readByContributorNumber(contributorNumber) != null) {
	    throw new DomainException("EXTERNAL_INSTITUTION_UNIT.createContributor.existing.contributor.number");
	}

	final Unit contributor = Unit.createNewNoOfficialExternalInstitution(contributorName);
	contributor.setSocialSecurityNumber(contributorNumber);
	contributor.createDefaultPhysicalAddress(data);

	return contributor;
    }

    public List<VigilantGroup> getVigilantGroupsForGivenExecutionYear(ExecutionYear executionYear) {
	List<VigilantGroup> vigilantGroups = this.getVigilantGroups();
	List<VigilantGroup> vigilantGroupsInExecutionYear = new ArrayList<VigilantGroup>();

	for (VigilantGroup group : vigilantGroups) {
	    if (group.getExecutionYear().equals(executionYear)) {
		vigilantGroupsInExecutionYear.add(group);
	    }
	}

	return vigilantGroupsInExecutionYear;
    }
  
    public List<ExamCoordinator> getExamCoordinatorsForGivenYear(ExecutionYear executionYear) {
	List<ExamCoordinator> examCoordinators = new ArrayList<ExamCoordinator>();
	for (ExamCoordinator coordinator : this.getExamCoordinators()) {
	    if (coordinator.getExecutionYear().equals(executionYear)) {
		examCoordinators.add(coordinator);
	    }
	}
	return examCoordinators;
    }

    @Override
    public ParkingPartyClassification getPartyClassification() {
	return ParkingPartyClassification.UNIT;
    }

    public static Unit findFirstExternalUnitByName(final String unitName) {
	if (unitName == null || unitName.length() == 0) {
	    return null;
	}
	for (final Party party : RootDomainObject.getInstance().getExternalInstitutionUnit().getSubUnits()) {
	    if (!party.isPerson() && unitName.equalsIgnoreCase(party.getName())) {
		final Unit unit = (Unit) party;
		return unit;
	    }
	}
	return null;
    }

    public static Unit findFirstUnitByName(final String unitName) {
        if (StringUtils.isEmpty(unitName)) {
            return null;
        }
        for (final Party party : RootDomainObject.getInstance().getPartys()) {
            if (party.isUnit() && unitName.equalsIgnoreCase(party.getName())) {
                final Unit unit = (Unit) party;
                return unit;
            }
        }
        return null;
    }
    
    public String getNameWithAcronym() {
	String name = super.getName().trim();
	return (getAcronym() == null || StringUtils.isEmpty(getAcronym().trim())) ? name : name + " ("
		+ getAcronym().trim() + ")";
    }

    public String getPresentationName() {
	StringBuilder builder = new StringBuilder();
	builder.append(getNameWithAcronym());
	if (getCostCenterCode() != null) {
	    builder.append(" [c.c. ").append(getCostCenterCode()).append("]");
	}
	return builder.toString();
    }

    public String getPresentationNameWithParents() {
	String parentUnits = getParentUnitsPresentationName();
	return (!StringUtils.isEmpty(parentUnits.trim())) ? parentUnits + " - " + getPresentationName() : getPresentationName();
    }

    public String getPresentationNameWithParentsAndBreakLine() {
	String parentUnits = getParentUnitsPresentationNameWithBreakLine();		
	return (!StringUtils.isEmpty(parentUnits.trim())) ? parentUnits + applicationResourcesBundle.getString("label.html.breakLine") + getPresentationName() : getPresentationName();
    }

    public String getParentUnitsPresentationNameWithBreakLine() {
	return getParentUnitsPresentationName(applicationResourcesBundle.getString("label.html.breakLine"));
    }

    public String getParentUnitsPresentationName() {
	return getParentUnitsPresentationName(" - ");
    }

    public String getDirectParentUnitsPresentationName() {
    	StringBuilder builder = new StringBuilder();
    	for(Unit unit : getParentUnits()) {
    	    if(!unit.isAggregateUnit()) {
    		builder.append(unit.getNameWithAcronym());
    	    }
    	}
    	return builder.toString();
    }
    
    private String getParentUnitsPresentationName(String separator) {
	StringBuilder builder = new StringBuilder();
	List<Unit> parentUnits = getParentUnitsPath();	
	int index = 1;
	
	for (Unit unit : parentUnits) {
	    if(!unit.isAggregateUnit()) {
		if (index == 1) {
		    builder.append(unit.getNameWithAcronym());
	    	} else {
	    	    builder.append(separator + unit.getNameWithAcronym());
	    	}
	    }
	    index++;
	}
	
	return builder.toString();
    }
    
    public List<Unit> getParentUnitsPath(){
	
	List<Unit> parentUnits = new ArrayList<Unit>();
	Unit searchedUnit = this;
	Unit externalInstitutionUnit = UnitUtils.readExternalInstitutionUnit();
	Unit institutionUnit = UnitUtils.readInstitutionUnit();
	Unit earthUnit = UnitUtils.readEarthUnit();
	
	while (searchedUnit.getParentUnits().size() == 1) {
	    Unit parentUnit = searchedUnit.getParentUnits().iterator().next();
	    parentUnits.add(0, parentUnit);
	    if (parentUnit != institutionUnit && parentUnit != externalInstitutionUnit && parentUnit != earthUnit) {				
		searchedUnit = parentUnit;
		continue;
	    } 		
	    break;	    
	}

	if (searchedUnit.getParentUnits().size() > 1) {
	    if(searchedUnit.isInternal()) {
		parentUnits.add(0, institutionUnit);
	    } else if (searchedUnit.isNoOfficialExternal()) {
		parentUnits.add(0, externalInstitutionUnit);		
	    } else {
		parentUnits.add(0, earthUnit);
	    }
	}
	
	return parentUnits;
    }
    
    public SortedSet<Unit> getSortedExternalChilds() {
	final SortedSet<Unit> result = new TreeSet<Unit>(new BeanComparator("name"));	
	for (final Unit unit : getSubUnits()) {
	    if (!unit.isInternal()) {
		result.add(unit);
	    }
	}
	return result;
    }    
    
    public List<ExternalCurricularCourse> getAllExternalCurricularCourses() {
	return new ArrayList<ExternalCurricularCourse>(getExternalCurricularCourses());		
    }   
    
    public static void mergeExternalUnits(Unit fromUnit, Unit destinationUnit) {
	
	if(fromUnit == null || destinationUnit == null || fromUnit.equals(destinationUnit)) {
	    throw new DomainException("error.merge.external.units.equals.units");
	}
	
	if(!fromUnit.isNoOfficialExternal() || destinationUnit.isInternal()) {
	    throw new DomainException("error.merge.external.units.invalid.units");
	}
           	    
	Collection<? extends Accountability> externalContracts = fromUnit.getChildAccountabilitiesByAccountabilityClass(ExternalContract.class);
        List<NonAffiliatedTeacher> nonAffiliatedTeachers = fromUnit.getAssociatedNonAffiliatedTeachers();        	        	
        List<ResultUnitAssociation> resultUnitAssociations = fromUnit.getResultUnitAssociations();            
        List<Guide> payedGuides = fromUnit.getPayedGuides();
        List<Receipt> payedReceipts = fromUnit.getPayedReceipts();
        
        destinationUnit.getPayedReceipts().addAll(payedReceipts);
        destinationUnit.getPayedGuides().addAll(payedGuides);
        destinationUnit.getResultUnitAssociations().addAll(resultUnitAssociations);
        destinationUnit.getAssociatedNonAffiliatedTeachers().addAll(nonAffiliatedTeachers);
        destinationUnit.getChilds().addAll(externalContracts);
                               
        fromUnit.delete();        		
    }
    
    public String[] getPrinterNamesByFunctionalityName(final String name) {
	for (FunctionalityPrinters functionalityPrinters : getFunctionalityPrintersSet()) {
	    if(functionalityPrinters.getFunctionality().equals(name)) {
		return functionalityPrinters.getPrinterNames();
	    }
	}
	return new String[0];
    }
}