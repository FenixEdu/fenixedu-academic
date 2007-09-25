/*
 * Created on Sep 16, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.InstitutionSite;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.UnitFileTag;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.assiduousness.UnitExtraWorkAmount;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.util.FunctionalityPrinters;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.domain.OrderedRelationAdapter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class Unit extends Unit_Base {

    public static OrderedRelationAdapter<Unit, Function> FUNCTION_ORDERED_ADAPTER;
    static {
	FUNCTION_ORDERED_ADAPTER = new OrderedRelationAdapter<Unit, Function>("activeFunctions", "functionOrder"); 
	UnitFunction.addListener(FUNCTION_ORDERED_ADAPTER);
    }	
    static final private ResourceBundle applicationResourcesBundle = ResourceBundle.getBundle("resources.ApplicationResources", new Locale("pt"));

    protected Unit() {
	super();
    }

    protected void init(MultiLanguageString name, Integer costCenterCode, String acronym, YearMonthDay beginDate,
	    YearMonthDay endDate, String webAddress, UnitClassification classification,
	    Boolean canBeResponsibleOfSpaces, Campus campus) {

	setPartyName(name);
	setAcronym(acronym);
	if(getCostCenterCode() == null || !getCostCenterCode().equals(costCenterCode)) {
	    setCostCenterCode(costCenterCode);
	}
	setBeginDateYearMonthDay(beginDate);
	setEndDateYearMonthDay(endDate);
	setClassification(classification);
	setCanBeResponsibleOfSpaces(canBeResponsibleOfSpaces);
	setCampus(campus);
	updateDefaultWebAddress(webAddress);
    }
    
    @Override
    public void setPartyName(MultiLanguageString partyName) {               
	super.setPartyName(partyName);
	setName(partyName.getPreferedContent());
    }
    
    public String getName() {
	return getPartyName().getPreferedContent();
    }
    
    public void setName(String name) {
	
	if(name == null || StringUtils.isEmpty(name.trim())) {
	    throw new DomainException("error.person.empty.name");
	}
	
	MultiLanguageString partyName = getPartyName();
	partyName = partyName == null ? new MultiLanguageString() : partyName;
	partyName.setContent(LanguageUtils.getSystemLanguage(), name);
	
	super.setPartyName(partyName);
	
	UnitName unitName = getUnitName();
	unitName = unitName == null ? new UnitName(this) : unitName;	
	unitName.setName(name);
    }

    public void edit(MultiLanguageString name, String acronym) {
	setPartyName(name);
	setAcronym(acronym);
    }

    public void edit(MultiLanguageString unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
	    YearMonthDay endDate, String webAddress, UnitClassification classification,
	    Department department, Degree degree, AdministrativeOffice administrativeOffice,
	    Boolean canBeResponsibleOfSpaces, Campus campus) {

	init(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification,
		canBeResponsibleOfSpaces, campus);
    }

    @Override
    public void setCanBeResponsibleOfSpaces(Boolean canBeResponsibleOfSpaces) {
	super.setCanBeResponsibleOfSpaces(canBeResponsibleOfSpaces != null ? canBeResponsibleOfSpaces
		: Boolean.FALSE);
    }

    @Override
    public void setCostCenterCode(Integer costCenterCode) {
	Unit unit = readByCostCenterCode(costCenterCode);
	if (unit != null && !unit.equals(this)) {
	    throw new DomainException("error.costCenter.alreadyExists");
	}
	super.setCostCenterCode(costCenterCode);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
	final YearMonthDay start = getBeginDateYearMonthDay();
	final YearMonthDay end = getEndDateYearMonthDay();	
	return start != null && (end == null || !start.isAfter(end));
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

	removeCampus();
	getUnitName().delete();
	super.delete();
    }

    private boolean canBeDeleted() {
	return (!hasAnyParents() || (getParentsCount() == 1 && getParentUnits().size() == 1))
	&& !hasAnyChilds() && !hasAnyFunctions() && !hasAnyVigilantGroups()		
	&& !hasAnyAssociatedNonAffiliatedTeachers() && !hasAnyPayedGuides()
	&& !hasAnyPayedReceipts() && !hasAnyExtraPayingUnitAuthorizations()
	&& !hasAnyExtraWorkingUnitAuthorizations() && !hasAnyExternalCurricularCourses()
	&& !hasAnyResultUnitAssociations() && !hasUnitServiceAgreementTemplate()
	&& !hasAnyResearchInterests() && !hasAnyProjectParticipations()
	&& !hasAnyParticipations() && !hasAnyBoards()
	&& (!hasSite() || getSite().canBeDeleted()) && !hasAnyOwnedReceipts()
	&& !hasAnyCreatedReceipts() && !hasAnyProtocols() && !hasAnyPartnerProtocols()
	&& !hasAnyPrecedentDegreeInformations();
    }

    @Override
    public Campus getCampus() {

	Campus campus = super.getCampus();        

	if(campus != null) {
	    return campus;
	}

	Collection<Unit> parentUnits = getParentUnits();
	if(parentUnits.size() == 1) {
	    campus = parentUnits.iterator().next().getCampus();
	}

	return campus;
    }

    public boolean isInternal() {
	if (this.equals(UnitUtils.readInstitutionUnit())) {
	    return true;
	}

	for (final Unit parentUnit : getParentUnits()) {
	    if (parentUnit.isInternal()) {
		return true;
	    }
	}

	return false;
    }

    public boolean isNoOfficialExternal() {
	if (this.equals(UnitUtils.readExternalInstitutionUnit())) {
	    return true;
	}
	for (final Unit parentUnit : getParentUnits()) {
	    if (parentUnit.isNoOfficialExternal()) {
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
    public boolean isUnit() {
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

    final public Collection<Function> getFunctions(final FunctionType functionType) {
	final Collection<Function> result = new HashSet<Function>();

	for (final Function function : getFunctionsSet()) {
	    if (function.getFunctionType() == functionType) {
		result.add(function);
	    }
	}

	return result;
    }

    final public Function getUnitCoordinatorFunction() {
	final Collection<Function> possibleCoordinators = getFunctions(FunctionType.ASSIDUOUSNESS_RESPONSIBLE);
	if (possibleCoordinators.isEmpty()) {
	    throw new DomainException("Unit.no.one.entitled.to.be.unit.coordinator");
	} else if (possibleCoordinators.size() > 1) {
	    throw new DomainException("Unit.more.than.one.person.entitled.to.be.unit.coordinator");
	}

	return possibleCoordinators.iterator().next();
    }

    final public Person getActiveUnitCoordinator() {
	return getActiveUnitCoordinator(new YearMonthDay());
    }

    final public Person getActiveUnitCoordinator(final YearMonthDay yearMonthDay) {
	for (final Accountability accountability : getUnitCoordinatorFunction().getAccountabilitiesSet()) {
	    if (accountability.isPersonFunction() && accountability.isActive(yearMonthDay)) {
		return ((PersonFunction)accountability).getPerson();
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

    public List<Unit> getInactiveSubUnits(YearMonthDay currentDate,
	    AccountabilityTypeEnum accountabilityTypeEnum) {
	return getSubUnitsByState(currentDate, accountabilityTypeEnum, false);
    }

    public List<Unit> getActiveSubUnits(YearMonthDay currentDate,
	    AccountabilityTypeEnum accountabilityTypeEnum) {
	return getSubUnitsByState(currentDate, accountabilityTypeEnum, true);
    }

    private List<Unit> getSubUnitsByState(YearMonthDay currentDate,
	    AccountabilityTypeEnum accountabilityTypeEnum, boolean state) {
	List<Unit> allSubUnits = new ArrayList<Unit>();
	for (Unit subUnit : getSubUnits(accountabilityTypeEnum)) {
	    if (subUnit.isActive(currentDate) == state) {
		allSubUnits.add(subUnit);
	    }
	}
	return allSubUnits;
    }

    public List<Unit> getActiveSubUnits(YearMonthDay currentDate,
	    List<AccountabilityTypeEnum> accountabilityTypeEnums) {
	return getSubUnitsByState(currentDate, accountabilityTypeEnums, true);
    }

    public List<Unit> getInactiveSubUnits(YearMonthDay currentDate,
	    List<AccountabilityTypeEnum> accountabilityTypeEnums) {
	return getSubUnitsByState(currentDate, accountabilityTypeEnums, false);
    }

    private List<Unit> getSubUnitsByState(YearMonthDay currentDate,
	    List<AccountabilityTypeEnum> accountabilityTypeEnums, boolean state) {
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

    public List<Unit> getAllActiveSubUnits(YearMonthDay currentDate,
	    AccountabilityTypeEnum accountabilityTypeEnum) {
	Set<Unit> allActiveSubUnits = new HashSet<Unit>();
	allActiveSubUnits.addAll(getActiveSubUnits(currentDate, accountabilityTypeEnum));
	for (Unit subUnit : getSubUnits(accountabilityTypeEnum)) {
	    allActiveSubUnits.addAll(subUnit.getAllActiveSubUnits(currentDate));
	}
	return new ArrayList<Unit>(allActiveSubUnits);
    }

    public List<Unit> getAllInactiveSubUnits(YearMonthDay currentDate,
	    AccountabilityTypeEnum accountabilityTypeEnum) {
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
	Collection<Unit> parentUnits = getParentUnits();
	allParentUnits.addAll(parentUnits);
	for (Unit subUnit : parentUnits) {
	    allParentUnits.addAll(subUnit.getAllParentUnits());
	}
	return allParentUnits;
    }

    public Collection<ExternalContract> getExternalPersons() {
	return (Collection<ExternalContract>) getChildAccountabilities(
		ExternalContract.class, AccountabilityTypeEnum.WORKING_CONTRACT);
    }

    public List<Contract> getWorkingContracts() {
	List<Contract> contracts = new ArrayList<Contract>();
	contracts.addAll(getEmployeeContractsByType(AccountabilityTypeEnum.WORKING_CONTRACT));
	return contracts;
    }

    private Collection<Contract> getEmployeeContractsByType(AccountabilityTypeEnum contractType) {
	return (Collection<Contract>) getChildAccountabilities(EmployeeContract.class, contractType);
    }

    public List<Contract> getWorkingContracts(YearMonthDay begin, YearMonthDay end) {
	List<Contract> contracts = new ArrayList<Contract>();
	for (Contract contract : getWorkingContracts()) {
	    if (contract.belongsToPeriod(begin, end)) {
		contracts.add(contract);
	    }
	}
	return contracts;
    }

    public List<Contract> getContracts(YearMonthDay begin, YearMonthDay end, AccountabilityTypeEnum ... types) {
	List<Contract> contracts = new ArrayList<Contract>();
	for (Contract contract : getContracts(types)) {
	    if (contract.belongsToPeriod(begin, end)) {
		contracts.add(contract);
	    }
	}
	return contracts;
    }

    public Collection<Contract> getContracts(AccountabilityTypeEnum ... types) {
	return (Collection<Contract>) getChildAccountabilities(Contract.class, types);
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
	    if (teacher != null
		    && !teacher.getAllLegalRegimensWithoutSpecialSituations(begin, end).isEmpty()) {
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
	return (Collection<Unit>) getParentParties(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE,
		Unit.class);
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
	List<Unit> subUnits = (parentUnit.isActive(currentDate)) ? getAllActiveSubUnits(currentDate)
		: getAllInactiveSubUnits(currentDate);
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
	    if (party.isUnit()) {
		allUnits.add((Unit) party);
	    }
	}
	return allUnits;
    }

    /**
     * This method should be used only for Unit types where acronyms are unique.
     */
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
			|| partyTypeEnum.equals(PartyTypeEnum.SCHOOL)
			|| partyTypeEnum.equals(PartyTypeEnum.RESEARCH_UNIT)	
		)) {

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
	return (Collection<Unit>) getParentParties(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE,
		Unit.class);
    }

    public static Unit createNewUnit(MultiLanguageString unitName, Integer costCenterCode, String acronym,
	    YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
	    AccountabilityType accountabilityType, String webAddress, UnitClassification classification,
	    Boolean canBeResponsibleOfSpaces, Campus campus) {

	Unit unit = new Unit();
	unit.init(unitName, costCenterCode, acronym, beginDate, endDate, webAddress, classification, canBeResponsibleOfSpaces, campus);
	if (parentUnit != null && accountabilityType != null) {
	    unit.addParentUnit(parentUnit, accountabilityType);
	}
	return unit;
    }

    public static Unit createNewNoOfficialExternalInstitution(String unitName) {
	return createNewNoOfficialExternalInstitution(unitName, null);
    }

    public static Unit createNewNoOfficialExternalInstitution(String unitName, Country country) {
	Unit externalInstitutionUnit = UnitUtils.readExternalInstitutionUnit();
	Unit noOfficialExternalInstitutionUnit = new Unit();
	noOfficialExternalInstitutionUnit.init(new MultiLanguageString(LanguageUtils.getSystemLanguage(), unitName), null, null, new YearMonthDay(), null, null, null, null, null);
	noOfficialExternalInstitutionUnit.addParentUnit(externalInstitutionUnit, AccountabilityType.readAccountabilityTypeByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
	noOfficialExternalInstitutionUnit.setCountry(country);
	return noOfficialExternalInstitutionUnit;
    }

    public static Party createContributor(final String contributorName, final String contributorNumber,
	    final PhysicalAddressData data) {

	if (Party.readByContributorNumber(contributorNumber) != null) {
	    throw new DomainException(
	    "EXTERNAL_INSTITUTION_UNIT.createContributor.existing.contributor.number");
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
    public PartyClassification getPartyClassification() {
	return PartyClassification.UNIT;
    }

    public static Unit findFirstExternalUnitByName(final String unitName) {
	if (unitName == null || unitName.length() == 0) {
	    return null;
	}
	for (final Party party : RootDomainObject.getInstance().getExternalInstitutionUnit()
		.getSubUnits()) {
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
	String name = getName().trim();
	return (getAcronym() == null || StringUtils.isEmpty(getAcronym().trim())) ? name :
	    name + " ("	+ getAcronym().trim() + ")";
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
	return (!StringUtils.isEmpty(parentUnits.trim())) ? parentUnits + " - " + getPresentationName()
		: getPresentationName();
    }

    public String getPresentationNameWithParentsAndBreakLine() {
	String parentUnits = getParentUnitsPresentationNameWithBreakLine();
	return (!StringUtils.isEmpty(parentUnits.trim())) ? parentUnits
		+ applicationResourcesBundle.getString("label.html.breakLine") + getPresentationName()
		: getPresentationName();
    }

    public String getParentUnitsPresentationNameWithBreakLine() {
	return getParentUnitsPresentationName(applicationResourcesBundle.getString("label.html.breakLine"));
    }

    public String getParentUnitsPresentationName() {
	return getParentUnitsPresentationName(" - ");
    }

    private String getParentUnitsPresentationName(String separator) {
	StringBuilder builder = new StringBuilder();
	List<Unit> parentUnits = getParentUnitsPath();
	int index = 1;

	for (Unit unit : parentUnits) {
	    if (!unit.isAggregateUnit()) {
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

    public List<Unit> getParentUnitsPath() {

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
	    if (searchedUnit.isInternal()) {
		parentUnits.add(0, institutionUnit);
	    } else if (searchedUnit.isNoOfficialExternal()) {
		parentUnits.add(0, externalInstitutionUnit);
	    } else {
		parentUnits.add(0, earthUnit);
	    }
	}

	return parentUnits;
    }

    public String getDirectParentUnitsPresentationName() {
	StringBuilder builder = new StringBuilder();
	for (Unit unit : getParentUnits()) {
	    if (!unit.isAggregateUnit()) {
		builder.append(unit.getNameWithAcronym());
	    }
	}
	return builder.toString();
    }

    public String getShortPresentationName() {
	final StringBuilder stringBuilder = new StringBuilder();
	for (final Unit unit : getParentUnits()) {
	    if (!unit.isAggregateUnit() && unit != RootDomainObject.getInstance().getInstitutionUnit()) {
		stringBuilder.append(unit.getName());
		stringBuilder.append(" - ");
	    }
	}
	stringBuilder.append(getName());
	return stringBuilder.toString();
    }

    public SortedSet<Unit> getSortedExternalChilds() {
	final SortedSet<Unit> result = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
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

	if (fromUnit == null || destinationUnit == null || fromUnit.equals(destinationUnit)) {
	    throw new DomainException("error.merge.external.units.equals.units");
	}

	if (!fromUnit.isNoOfficialExternal() || destinationUnit.isInternal()) {
	    throw new DomainException("error.merge.external.units.invalid.units");
	}

	Collection<? extends Accountability> externalContracts = fromUnit.getChildAccountabilitiesByAccountabilityClass(ExternalContract.class);	
	destinationUnit.getChilds().addAll(externalContracts);	
	destinationUnit.getPayedReceipts().addAll(fromUnit.getPayedReceipts());
	destinationUnit.getPayedGuides().addAll(fromUnit.getPayedGuides());
	destinationUnit.getResultUnitAssociations().addAll(fromUnit.getResultUnitAssociations());
	destinationUnit.getAssociatedNonAffiliatedTeachers().addAll(fromUnit.getAssociatedNonAffiliatedTeachers());
	destinationUnit.getPrecedentDegreeInformations().addAll(fromUnit.getPrecedentDegreeInformations());
	
	fromUnit.delete();
    }

    public String[] getPrinterNamesByFunctionalityName(final String name) {
	for (FunctionalityPrinters functionalityPrinters : getFunctionalityPrintersSet()) {
	    if (functionalityPrinters.getFunctionality().equals(name)) {
		return functionalityPrinters.getPrinterNames();
	    }
	}
	return new String[0];
    }

    public boolean isSiteAvailable() {
	return hasSite();
    }

    public List<ResearchResultPublication> getResearchResultPublications() {
	Set<ResearchResultPublication> publications = new HashSet<ResearchResultPublication>();

	for (ResultUnitAssociation association : getResultUnitAssociations()) {
	    if (association.getResult() instanceof ResearchResultPublication) {
		publications.add((ResearchResultPublication) association.getResult());
	    }
	}
	return new ArrayList<ResearchResultPublication>(publications);
    }

    public List<ResearchResultPatent> getAssociatedPatents() {
	Set<ResearchResultPatent> patents = new HashSet<ResearchResultPatent>();

	for (ResultUnitAssociation association : getResultUnitAssociations()) {
	    if (association.getResult() instanceof ResearchResultPatent) {
		patents.add((ResearchResultPatent) association.getResult());
	    }
	}
	return new ArrayList<ResearchResultPatent>(patents);
    }

    public List<UnitFile> getAccessibileFiles(Person person) {
	List<UnitFile> files = new ArrayList<UnitFile>();
	for (UnitFile file : getFiles()) {
	    if (file.isPersonAllowedToAccess(person)) {
		files.add(file);
	    }
	}
	return files;
    }

    public List<UnitFile> getAccessibileFiles(Person person, Collection<UnitFileTag> tag) {
	List<UnitFile> files = new ArrayList<UnitFile>();
	for (UnitFile file : getAccessibileFiles(person)) {
	    if (file.hasUnitFileTags(tag)) {
		files.add(file);
	    }
	}
	return files;
    }

    public List<UnitFile> getAccessibileFiles(Person person, UnitFileTag tag) {
	List<UnitFile> files = new ArrayList<UnitFile>();
	if (tag != null) {
	    for (UnitFile file : tag.getTaggedFiles()) {
		if (file.isPersonAllowedToAccess(person)) {
		    files.add(file);
		}
	    }
	}
	return files;

    }

    public List<UnitFile> getAccessibileFiles(Person person, String tagName) {
	return getAccessibileFiles(person, getUnitFileTag(tagName));
    }

    public UnitFileTag getUnitFileTag(String name) {
	for (UnitFileTag tag : getUnitFileTags()) {
	    if (tag.getName().equalsIgnoreCase(name)) {
		return tag;
	    }
	}
	return null;
    }

    public void removeGroupFromUnitFiles(PersistentGroupMembers members) {
	PersistentGroup group = new PersistentGroup(members);
	for (UnitFile file : getFiles()) {
	    file.updatePermissions(group);
	}
    }

    public boolean isUserAllowedToUploadFiles(Person person) {
	return getAllowedPeopleToUploadFiles().contains(person);
    }

    public boolean isCurrentUserAllowedToUploadFiles() {
	return isUserAllowedToUploadFiles(AccessControl.getPerson());
    }

    public void setAllowedPeopleToUploadFiles(List<Person> allowedPeople) {
	getAllowedPeopleToUploadFiles().clear();
	getAllowedPeopleToUploadFiles().addAll(allowedPeople);
    }

    public MultiLanguageString getNameI18n() {
	return getPartyName(); 
    }

    public List<ExtraWorkRequest> getExtraWorkRequests(int year, Month month, int hoursDoneInYear,
	    Month hoursDoneInMonth) {
	Partial partialDate = new Partial().with(DateTimeFieldType.year(), year).with(
		DateTimeFieldType.monthOfYear(), month.ordinal() + 1);
	Partial hoursDonePartialDate = new Partial().with(DateTimeFieldType.year(), hoursDoneInYear)
	.with(DateTimeFieldType.monthOfYear(), hoursDoneInMonth.ordinal() + 1);
	List<ExtraWorkRequest> extraWorkRequestList = new ArrayList<ExtraWorkRequest>();
	for (ExtraWorkRequest extraWorkRequest : getExtraWorkRequests()) {
	    if (extraWorkRequest.getPartialPayingDate().equals(partialDate)
		    && extraWorkRequest.getHoursDoneInPartialDate().equals(hoursDonePartialDate)) {
		extraWorkRequestList.add(extraWorkRequest);
	    }
	}
	return extraWorkRequestList;
    }

    public UnitExtraWorkAmount getUnitExtraWorkAmountByYear(Integer year) {
	for (UnitExtraWorkAmount unitExtraWorkAmount : getUnitExtraWorkAmounts()) {
	    if (unitExtraWorkAmount.getYear().equals(year)) {
		return unitExtraWorkAmount;
	    }
	}
	return null;
    }

    public List<IGroup> getUserDefinedGroups() {
	final List<IGroup> groups = new ArrayList<IGroup>();
	for (final PersistentGroupMembers persistentMembers : this.getPersistentGroups()) {
	    groups.add(new PersistentGroup(persistentMembers));
	}
	return groups;
    }

    public boolean isEarth() {
	return this.equals(RootDomainObject.getInstance().getEarthUnit());
    }

    public List<ExtraWorkRequest> getExtraWorkRequestsDoneIn(Integer year, Month month) {
	Partial partialDate = new Partial().with(DateTimeFieldType.year(), year).with(
		DateTimeFieldType.monthOfYear(), month.ordinal() + 1);
	List<ExtraWorkRequest> extraWorkRequestList = new ArrayList<ExtraWorkRequest>();
	for (ExtraWorkRequest extraWorkRequest : getExtraWorkRequests()) {
	    if (extraWorkRequest.getHoursDoneInPartialDate().equals(partialDate)) {
		extraWorkRequestList.add(extraWorkRequest);
	    }
	}
	return extraWorkRequestList;
    }

    @Override
    public String getPartyPresentationName() {
	return getPresentationNameWithParents();
    }


    public List<IGroup> getGroups() {
	List<IGroup> groups = new ArrayList<IGroup>();
	groups.addAll(getDefaultGroups());
	groups.addAll(getUserDefinedGroups());
	return groups;
    }

    protected List<IGroup> getDefaultGroups() {
	return new ArrayList<IGroup>();
    }

    public boolean isUserAbleToDefineGroups(Person person) {
	UnitSite site = getSite();
	return (site == null) ? false : site.getManagers().contains(person); 
    }

    public boolean isCurrentUserAbleToDefineGroups() {
	return isUserAbleToDefineGroups(AccessControl.getPerson());
    }

    public Collection<Person> getPossibleGroupMembers() {
	List<Person> people = new ArrayList<Person>();

	for (Employee employee : getAllWorkingEmployees(new YearMonthDay(), null)) {
	    people.add(employee.getPerson());
	}

	return people;
    }

    public Collection<Function> getVirtualFunctions() {
	return getFunctions(FunctionType.VIRTUAL);
    }

    public void setFunctionsOrder(Collection<Function> functions) {
	Unit.FUNCTION_ORDERED_ADAPTER.updateOrder(this, functions);
    }

    public Collection<Function> getFunctions(boolean active) {
	List<Function> result = new ArrayList<Function>();
	YearMonthDay today = new YearMonthDay();
	for (Function function : getFunctions()) {
	    if (function.isActive(today) != active) {
		continue;
	    }
	    result.add(function);
	}	
	return result;
    }

    public Collection<Function> getActiveFunctions() {
	return getFunctions(true);
    }

    public Collection<Function> getInactiveFunctions() {
	return getFunctions(false);
    }

    public SortedSet<Function> getOrderedFunctions() {
	SortedSet<Function> functions = new TreeSet<Function>(Function.COMPARATOR_BY_ORDER);
	functions.addAll(getFunctions());

	return functions;
    }

    public SortedSet<Function> getOrderedActiveFunctions() {
	SortedSet<Function> functions = new TreeSet<Function>(Function.COMPARATOR_BY_ORDER);
	functions.addAll(getActiveFunctions());

	return functions;
    }

    @Override
    protected UnitSite createSite() {
	if (this == RootDomainObject.getInstance().getInstitutionUnit()) {
	    // TODO: to be removed if institution unit becomes a specific class
	    return InstitutionSite.initialize();
	}
	else {
	    return new UnitSite(this);
	}
    }

    @Override
    public UnitSite initializeSite() {
	return (UnitSite) super.initializeSite();
    }
}
