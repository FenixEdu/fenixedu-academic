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
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.UnitFileTag;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.Unstructured;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.util.FunctionalityPrinters;
import net.sourceforge.fenixedu.domain.util.email.UnitBasedSender;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.domain.OrderedRelationAdapter;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.StringNormalizer;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class Unit extends Unit_Base {

    private static final ResourceBundle applicationResourcesBundle = ResourceBundle.getBundle("resources.ApplicationResources",
            new Locale("pt"));
    public static OrderedRelationAdapter<Unit, Function> FUNCTION_ORDERED_ADAPTER;
    static {
        FUNCTION_ORDERED_ADAPTER = new OrderedRelationAdapter<Unit, Function>("activeFunctions", "functionOrder");
        getRelationUnitFunction().addListener(FUNCTION_ORDERED_ADAPTER);
    }

    protected Unit() {
        super();
    }

    protected void init(MultiLanguageString name, String unitNameCard, Integer costCenterCode, String acronym,
            YearMonthDay beginDate, YearMonthDay endDate, String webAddress, UnitClassification classification,
            AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces, Campus campus) {

        setPartyName(name);
        if (acronym != null) {
            setAcronym(acronym);
        }
        if (getCostCenterCode() == null || !getCostCenterCode().equals(costCenterCode)) {
            setCostCenterCode(costCenterCode);
        }
        setIdentificationCardLabel(unitNameCard);
        setBeginDateYearMonthDay(beginDate);
        setEndDateYearMonthDay(endDate);
        setClassification(classification);
        setAdministrativeOffice(administrativeOffice);
        setCanBeResponsibleOfSpaces(canBeResponsibleOfSpaces);
        setCampus(campus);
        setDefaultWebAddressUrl(webAddress);
    }

    @Override
    public void setPartyName(MultiLanguageString partyName) {
        super.setPartyName(partyName);
        setName(partyName.getPreferedContent());
    }

    @Override
    public String getName() {
        return getPartyName().getPreferedContent();
    }

    @Override
    public void setName(String name) {

        if (name == null || StringUtils.isEmpty(name.trim())) {
            throw new DomainException("error.person.empty.name");
        }

        MultiLanguageString partyName = getPartyName();

        partyName =
                partyName == null ? new MultiLanguageString(Language.getDefaultLanguage(), name) : partyName.with(
                        Language.getDefaultLanguage(), name);

        super.setPartyName(partyName);

        UnitName unitName = getUnitName();
        unitName = unitName == null ? new UnitName(this) : unitName;
        unitName.setName(name);
    }

    public void edit(MultiLanguageString name, String acronym) {
        setPartyName(name);
        setAcronym(acronym);
    }

    public void edit(MultiLanguageString unitName, String unitNameCard, Integer unitCostCenter, String acronym,
            YearMonthDay beginDate, YearMonthDay endDate, String webAddress, UnitClassification classification,
            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces,
            Campus campus) {

        init(unitName, unitNameCard, unitCostCenter, acronym, beginDate, endDate, webAddress, classification,
                administrativeOffice, canBeResponsibleOfSpaces, campus);
    }

    @Override
    public void setCanBeResponsibleOfSpaces(Boolean canBeResponsibleOfSpaces) {
        super.setCanBeResponsibleOfSpaces(canBeResponsibleOfSpaces != null ? canBeResponsibleOfSpaces : Boolean.FALSE);
    }

    public void setCostCenterCode(Integer costCenterCode) {
        final UnitCostCenterCode otherUnitCostCenterCode = UnitCostCenterCode.find(costCenterCode);
        if (otherUnitCostCenterCode != null && otherUnitCostCenterCode.getUnit() != this) {
            throw new DomainException("error.costCenter.alreadyExists");
        }
        final UnitCostCenterCode unitCostCenterCode = getUnitCostCenterCode();
        if (unitCostCenterCode == null && costCenterCode != null) {
            new UnitCostCenterCode(this, costCenterCode);
        } else if (unitCostCenterCode != null && costCenterCode != null) {
            unitCostCenterCode.setCostCenterCode(costCenterCode);
        } else if (unitCostCenterCode != null && costCenterCode == null) {
            unitCostCenterCode.delete();
        }
    }

    public Integer getCostCenterCode() {
        final UnitCostCenterCode unitCostCenterCode = getUnitCostCenterCode();
        return unitCostCenterCode == null ? null : unitCostCenterCode.getCostCenterCode();
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
        final YearMonthDay start = getBeginDateYearMonthDay();
        final YearMonthDay end = getEndDateYearMonthDay();
        return start != null && (end == null || !start.isAfter(end));
    }

    @Override
    public void delete() {

        if (!canBeDeleted()) {
            throw new DomainException("error.unit.cannot.be.deleted");
        }

        if (hasAnyParentUnits()) {
            getParents().iterator().next().delete();
        }

        if (hasSite()) {
            getSite().delete();
        }

        for (; !getUnitFileTagsSet().isEmpty(); getUnitFileTags().iterator().next().delete()) {
            ;
        }

        getUnitName().delete();
        getFunctionalityPrinters().clear();
        getAllowedPeopleToUploadFiles().clear();

        setRootDomainObjectForEarthUnit(null);
        setRootDomainObjectForExternalInstitutionUnit(null);
        setRootDomainObjectForInstitutionUnit(null);
        setCampus(null);
        setUnitAcronym(null);
        setAdministrativeOffice(null);
        super.delete();
    }

    private boolean canBeDeleted() {
        return (!hasAnyParents() || (getParentsSet().size() == 1 && getParentUnits().size() == 1)) && !hasAnyChilds()
                && !hasAnyFunctions() && !hasAnyVigilantGroups() && !hasAnyAssociatedNonAffiliatedTeachers()
                && !hasAnyPayedGuides() && !hasAnyPayedReceipts() && !hasAnyExternalCurricularCourses()
                && !hasAnyResultUnitAssociations() && !hasUnitServiceAgreementTemplate() && !hasAnyResearchInterests()
                && !hasAnyProjectParticipations() && !hasAnyParticipations() && !hasAnyBoards()
                && (!hasSite() || getSite().isDeletable()) && !hasAnyOwnedReceipts() && !hasAnyPrecedentDegreeInformations()
                && !hasAnyCandidacyPrecedentDegreeInformations() && !hasAnyUnitSpaceOccupations() && !hasAnyExamCoordinators()
                && !hasAnyExternalRegistrationDatas() && !hasAnyCooperation() && !hasAnyFiles() && !hasAnyPersistentGroups()
                && !hasAnyExternalCourseLoadRequests() && !hasAnyExternalProgramCertificateRequests()
                && !getUnitGroupSet().isEmpty();
    }

    @Override
    public Campus getCampus() {

        Campus campus = super.getCampus();

        if (campus != null) {
            return campus;
        }

        Collection<Unit> parentUnits = getParentUnits();
        if (parentUnits.size() == 1) {
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

        for (Function function : super.getFunctionsSet()) {
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
                return ((PersonFunction) accountability).getPerson();
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

    private List<Unit> getSubUnitsByState(YearMonthDay currentDate, List<AccountabilityTypeEnum> accountabilityTypeEnums,
            boolean state) {
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
        Collection<Unit> parentUnits = getParentUnits();
        allParentUnits.addAll(parentUnits);
        for (Unit subUnit : parentUnits) {
            allParentUnits.addAll(subUnit.getAllParentUnits());
        }
        return allParentUnits;
    }

    public Collection<ExternalContract> getExternalPersons() {
        return (Collection<ExternalContract>) getChildAccountabilities(ExternalContract.class,
                AccountabilityTypeEnum.WORKING_CONTRACT);
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

    public List<Contract> getContracts(YearMonthDay begin, YearMonthDay end, AccountabilityTypeEnum... types) {
        List<Contract> contracts = new ArrayList<Contract>();
        for (Contract contract : getContracts(types)) {
            if (contract.belongsToPeriod(begin, end)) {
                contracts.add(contract);
            }
        }
        return contracts;
    }

    public Collection<Contract> getContracts(AccountabilityTypeEnum... types) {
        return (Collection<Contract>) getChildAccountabilities(Contract.class, types);
    }

    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<Teacher>();
        List<Employee> employees = getAllWorkingEmployees();
        for (Employee employee : employees) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null && teacher.hasAnyTeacherContractSituation()) {
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
            if (teacher != null && teacher.hasAnyTeacherContractSituation(begin.toLocalDate(), end.toLocalDate())) {
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    public List<Teacher> getAllTeachers(AcademicInterval academicInterval) {
        return getAllTeachers(academicInterval.getStart().toYearMonthDay(), academicInterval.getEnd().toYearMonthDay());
    }

    public List<Teacher> getAllCurrentTeachers() {
        List<Teacher> teachers = new ArrayList<Teacher>();
        List<Employee> employees = getAllCurrentActiveWorkingEmployees();
        for (Employee employee : employees) {
            Teacher teacher = employee.getPerson().getTeacher();
            if (teacher != null && teacher.getCurrentTeacherContractSituation() != null) {
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
            if (teacher != null && teacher.getCurrentTeacherContractSituation() != null) {
                iter.remove();
            }
        }
        return employees;
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
            if (contract.isActive(currentDate)) {
                employees.add(employee);
            }
        }
        for (Unit subUnit : getSubUnits()) {
            employees.addAll(subUnit.getAllCurrentActiveWorkingEmployees());
        }
        return new ArrayList<Employee>(employees);
    }

    @Override
    public Collection<Unit> getParentUnits() {
        return (Collection<Unit>) getParentParties(Unit.class);
    }

    @Override
    public Collection<Unit> getParentUnits(String accountabilityTypeEnum) {
        return (Collection<Unit>) getParentParties(AccountabilityTypeEnum.valueOf(accountabilityTypeEnum), Unit.class);
    }

    @Override
    public Collection<Unit> getParentUnits(AccountabilityTypeEnum accountabilityTypeEnum) {
        return (Collection<Unit>) getParentParties(accountabilityTypeEnum, Unit.class);
    }

    @Override
    public Collection<Unit> getParentUnits(List<AccountabilityTypeEnum> accountabilityTypeEnums) {
        return (Collection<Unit>) getParentParties(accountabilityTypeEnums, Unit.class);
    }

    @Override
    public Collection<Unit> getSubUnits() {
        return (Collection<Unit>) getChildParties(Unit.class);
    }

    public boolean hasSubUnit(final Unit unit) {
        if (unit != null) {
            for (final Unit child : getSubUnits()) {
                if (child.equals(unit)) {
                    return true;
                }
            }
        }
        return false;
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

    public Collection<Unit> getCurrentParentByOrganizationalStructureAccountabilityType() {
        return (Collection<Unit>) getCurrentParentParties(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE, Unit.class);
    }

    public Collection<Unit> getParentUnitsByOrganizationalStructureAccountabilityType() {
        return (Collection<Unit>) getParentParties(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE, Unit.class);
    }

    @Atomic
    /*
     * @See UnitMailSenderAction
     */
    public UnitBasedSender getOneUnitBasedSender() {
        if (hasAnyUnitBasedSender()) {
            return getUnitBasedSender().iterator().next();
        } else {
            return UnitBasedSender.newInstance(this);
        }
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
        List<Unit> subUnits =
                (parentUnit.isActive(currentDate)) ? getAllActiveSubUnits(currentDate) : getAllInactiveSubUnits(currentDate);
        if (subUnits.contains(parentUnit)) {
            throw new DomainException("error.unit.parentUnit.is.already.subUnit");
        }

        return new Accountability(parentUnit, this, accountabilityType);
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
        for (final Party party : Bennu.getInstance().getPartysSet()) {
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
                && (partyTypeEnum.equals(PartyTypeEnum.DEGREE_UNIT) || partyTypeEnum.equals(PartyTypeEnum.DEPARTMENT)
                        || partyTypeEnum.equals(PartyTypeEnum.PLANET) || partyTypeEnum.equals(PartyTypeEnum.COUNTRY)
                        || partyTypeEnum.equals(PartyTypeEnum.DEPARTMENT) || partyTypeEnum.equals(PartyTypeEnum.UNIVERSITY)
                        || partyTypeEnum.equals(PartyTypeEnum.SCHOOL) || partyTypeEnum.equals(PartyTypeEnum.RESEARCH_UNIT))) {

            UnitAcronym unitAcronymByAcronym = UnitAcronym.readUnitAcronymByAcronym(acronym);
            if (unitAcronymByAcronym != null) {
                for (Unit unit : unitAcronymByAcronym.getUnitsSet()) {
                    if (unit.getType() != null && unit.getType().equals(partyTypeEnum)) {
                        return unit;
                    }
                }
            }
        }
        return null;
    }

    public static List<Unit> readUnitsByAcronym(String acronym, boolean shouldNormalize) {
        List<Unit> result = new ArrayList<Unit>();
        if (!StringUtils.isEmpty(acronym.trim())) {
            UnitAcronym unitAcronymByAcronym = UnitAcronym.readUnitAcronymByAcronym(acronym, shouldNormalize);
            if (unitAcronymByAcronym != null) {
                result.addAll(unitAcronymByAcronym.getUnitsSet());
            }
        }
        return result;
    }

    public static List<Unit> readUnitsByAcronym(String acronym) {
        return readUnitsByAcronym(acronym, false);
    }

    public static Unit readByCostCenterCode(Integer costCenterCode) {
        final UnitCostCenterCode unitCostCenterCode = UnitCostCenterCode.find(costCenterCode);
        return unitCostCenterCode == null ? null : unitCostCenterCode.getUnit();
    }

    public static Unit createNewUnit(MultiLanguageString unitName, String unitNameCard, Integer costCenterCode, String acronym,
            YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, AccountabilityType accountabilityType,
            String webAddress, UnitClassification classification, AdministrativeOffice administrativeOffice,
            Boolean canBeResponsibleOfSpaces, Campus campus) {

        Unit unit = new Unit();
        unit.init(unitName, unitNameCard, costCenterCode, acronym, beginDate, endDate, webAddress, classification,
                administrativeOffice, canBeResponsibleOfSpaces, campus);
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
        noOfficialExternalInstitutionUnit.init(new MultiLanguageString(Language.getDefaultLanguage(), unitName), null, null,
                null, new YearMonthDay(), null, null, null, null, null, null);
        noOfficialExternalInstitutionUnit.addParentUnit(externalInstitutionUnit,
                AccountabilityType.readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
        noOfficialExternalInstitutionUnit.setCountry(country);
        return noOfficialExternalInstitutionUnit;
    }

    public static Party createContributor(final String contributorName, final String contributorNumber,
            final PhysicalAddressData data) {

        final Unit contributor = Unit.createNewNoOfficialExternalInstitution(contributorName);
        contributor.setSocialSecurityNumber(contributorNumber);
        final PhysicalAddress address = PhysicalAddress.createPhysicalAddress(contributor, data, PartyContactType.PERSONAL, true);
        address.setValid();
        return contributor;
    }

    public List<VigilantGroup> getVigilantGroupsForGivenExecutionYear(ExecutionYear executionYear) {

        List<VigilantGroup> groups = new ArrayList<VigilantGroup>();
        for (Unit unit : getSubUnits()) {
            groups.addAll(unit.getVigilantGroupsForGivenExecutionYear(executionYear));
        }
        for (VigilantGroup group : getVigilantGroups()) {
            if (group.getExecutionYear().equals(executionYear)) {
                groups.add(group);
            }
        }
        return groups;
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

    @Deprecated
    @Override
    public PartyClassification getPartyClassification() {
        return PartyClassification.UNIT;
    }

    public static Unit findFirstExternalUnitByName(final String unitName) {
        if (unitName == null || unitName.length() == 0) {
            return null;
        }
        for (final Party party : Bennu.getInstance().getExternalInstitutionUnit().getSubUnits()) {
            if (!party.isPerson() && unitName.equalsIgnoreCase(party.getName())) {
                final Unit unit = (Unit) party;
                return unit;
            }
        }
        return null;
    }

    public static Unit findFirstUnitByName(final String unitNameString) {
        if (StringUtils.isEmpty(unitNameString)) {
            return null;
        }
        final Collection<UnitName> unitNames = UnitName.find(unitNameString, Integer.MAX_VALUE);
        for (final UnitName unitName : unitNames) {
            final Unit unit = unitName.getUnit();
            if (StringNormalizer.normalize(unitNameString).equalsIgnoreCase(StringNormalizer.normalize(unit.getName()))) {
                return unit;
            }
        }
        return null;
    }

    public String getNameWithAcronym() {
        String name = getName().trim();
        return (getAcronym() == null || StringUtils.isEmpty(getAcronym().trim())) ? name : name + " (" + getAcronym().trim()
                + ")";
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
        return (!StringUtils.isEmpty(parentUnits.trim())) ? parentUnits
                + applicationResourcesBundle.getString("label.html.breakLine") + getPresentationName() : getPresentationName();
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

    public String getUnitPath(String separator) {
        return getUnitPath(separator, true);
    }

    public String getUnitPath(String separator, boolean addInstitutionalUnit) {
        StringBuilder builder = new StringBuilder();
        List<Unit> parentUnits = getParentUnitsPath(addInstitutionalUnit);
        int index = 1;

        for (Unit unit : parentUnits) {
            if (!unit.isAggregateUnit()) {
                if (index == 1) {
                    builder.append(unit.getAcronym());
                } else {
                    builder.append(separator + unit.getAcronym());
                }
            }
            index++;
        }

        builder.append("/");
        builder.append(this.getAcronym());
        return builder.toString();
    }

    public List<Unit> getParentUnitsPath() {
        return getParentUnitsPath(true);
    }

    public List<Unit> getParentUnitsPath(boolean addInstitutionalUnit) {

        List<Unit> parentUnits = new ArrayList<Unit>();
        Unit searchedUnit = this;
        Unit externalInstitutionUnit = UnitUtils.readExternalInstitutionUnit();
        Unit institutionUnit = UnitUtils.readInstitutionUnit();
        Unit earthUnit = UnitUtils.readEarthUnit();

        while (searchedUnit.getParentUnits().size() == 1) {
            Unit parentUnit = searchedUnit.getParentUnits().iterator().next();
            if (addInstitutionalUnit || parentUnit != institutionUnit) {
                parentUnits.add(0, parentUnit);
            }
            if (parentUnit != institutionUnit && parentUnit != externalInstitutionUnit && parentUnit != earthUnit) {
                searchedUnit = parentUnit;
                continue;
            }
            break;
        }

        if (searchedUnit.getParentUnits().size() > 1) {
            if (searchedUnit.isInternal() && addInstitutionalUnit) {
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
            if (!unit.isAggregateUnit() && unit != Bennu.getInstance().getInstitutionUnit()) {
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

        Collection<? extends Accountability> externalContracts =
                fromUnit.getChildAccountabilitiesByAccountabilityClass(ExternalContract.class);
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

    public List<IGroup> getUserDefinedGroups() {
        final List<IGroup> groups = new ArrayList<IGroup>();
        for (final PersistentGroupMembers persistentMembers : this.getPersistentGroups()) {
            groups.add(new PersistentGroup(persistentMembers));
        }
        return groups;
    }

    public boolean isEarth() {
        return this.equals(Bennu.getInstance().getEarthUnit());
    }

    @Override
    public String getPartyPresentationName() {
        return getPresentationNameWithParents();
    }

    /**
     * Used by messaging system
     * 
     * @return Groups to used as recipients
     */
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

    /**
     * Used by UnitBasedSender as sender group members
     * 
     * @return members allowed to use the UnitBasedSenders
     */
    public Collection<Person> getPossibleGroupMembers() {
        HashSet<Person> people = new HashSet<Person>();

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
        if (this == Bennu.getInstance().getInstitutionUnit()) {
            // TODO: to be removed if institution unit becomes a specific
            // class
            return InstitutionSite.initialize();
        } else {
            return new UnitSite(this);
        }
    }

    static public MultiLanguageString getInstitutionName() {
        final Bennu root = Bennu.getInstance();
        MultiLanguageString result = new MultiLanguageString();
        if (root != null) {
            final Unit institutionUnit = root.getInstitutionUnit();
            if (root != null) {
                result = institutionUnit.getNameI18n();
            }
        }
        if (result.isEmpty()) {
            result =
                    result.with(Language.getDefaultLanguage(),
                            BundleUtil.getStringFromResourceBundle("resources/GlobalResources", "institution.name"));
        }

        return result;
    }

    static public String getInstitutionAcronym() {
        final Bennu root = Bennu.getInstance();
        String result = StringUtils.EMPTY;
        if (root != null) {
            final Unit institutionUnit = root.getInstitutionUnit();
            if (root != null) {
                result = institutionUnit.getAcronym();
            }
        }
        if (result.isEmpty()) {
            result = BundleUtil.getStringFromResourceBundle("resources/GlobalResources", "institution.name.abbreviation");
        }

        return result;
    }

    @Override
    public UnitSite initializeSite() {
        return (UnitSite) super.initializeSite();
    }

    @Override
    public Set<Function> getFunctionsSet() {
        Set<Function> result = new HashSet<Function>();
        for (Function function : super.getFunctionsSet()) {
            if (function.getType().equals(AccountabilityTypeEnum.MANAGEMENT_FUNCTION)) {
                result.add(function);
            }
        }
        return result;
    }

    /*
     * ResearchResultPublication getters
     */

    public List<ResearchResultPublication> getBooks(Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Book.class, checkSubunits);
    }

    public List<ResearchResultPublication> getBooks(ExecutionYear executionYear, Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Book.class, executionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getBooks(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear,
            Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Book.class, firstExecutionYear, lastExecutionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getInbooks(Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(BookPart.class, checkSubunits);
    }

    public List<ResearchResultPublication> getInbooks(ExecutionYear executionYear, Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(BookPart.class, executionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getInbooks(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear,
            Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(BookPart.class, firstExecutionYear, lastExecutionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getArticles(ScopeType locationType, Boolean checkSubunits) {
        return filterArticlesWithType(this.getResearchResultPublicationsByType(Article.class, checkSubunits), locationType);
    }

    public List<ResearchResultPublication> getArticles(ScopeType locationType, ExecutionYear executionYear, Boolean checkSubunits) {
        return filterArticlesWithType(this.getResearchResultPublicationsByType(Article.class, executionYear, checkSubunits),
                locationType);
    }

    public List<ResearchResultPublication> getArticles(ScopeType locationType, ExecutionYear firstExecutionYear,
            ExecutionYear lastExecutionYear, Boolean checkSubunits) {
        return filterArticlesWithType(
                this.getResearchResultPublicationsByType(Article.class, firstExecutionYear, lastExecutionYear, checkSubunits),
                locationType);
    }

    public List<ResearchResultPublication> getArticles(Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Article.class, checkSubunits);
    }

    public List<ResearchResultPublication> getArticles(ExecutionYear executionYear, Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Article.class, executionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getArticles(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear,
            Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Article.class, firstExecutionYear, lastExecutionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getInproceedings(ScopeType locationType, Boolean checkSubunits) {
        return filterInproceedingsWithType(this.getResearchResultPublicationsByType(Inproceedings.class, checkSubunits),
                locationType);
    }

    public List<ResearchResultPublication> getInproceedings(ScopeType locationType, ExecutionYear executionYear,
            Boolean checkSubunits) {
        return filterInproceedingsWithType(
                this.getResearchResultPublicationsByType(Inproceedings.class, executionYear, checkSubunits), locationType);
    }

    public List<ResearchResultPublication> getInproceedings(ScopeType locationType, ExecutionYear firstExecutionYear,
            ExecutionYear lastExecutionYear, Boolean checkSubunits) {
        return filterInproceedingsWithType(this.getResearchResultPublicationsByType(Inproceedings.class, firstExecutionYear,
                lastExecutionYear, checkSubunits), locationType);
    }

    public List<ResearchResultPublication> getInproceedings(Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Inproceedings.class, checkSubunits);
    }

    public List<ResearchResultPublication> getInproceedings(ExecutionYear executionYear, Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Inproceedings.class, executionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getInproceedings(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear,
            Boolean checkSubunits) {
        return this
                .getResearchResultPublicationsByType(Inproceedings.class, firstExecutionYear, lastExecutionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getProceedings(Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Proceedings.class, checkSubunits);
    }

    public List<ResearchResultPublication> getProceedings(ExecutionYear executionYear, Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Proceedings.class, executionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getProceedings(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear,
            Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Proceedings.class, firstExecutionYear, lastExecutionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getTheses(Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Thesis.class, checkSubunits);
    }

    public List<ResearchResultPublication> getTheses(ExecutionYear executionYear, Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Thesis.class, executionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getTheses(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear,
            Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Thesis.class, firstExecutionYear, lastExecutionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getManuals(Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Manual.class, checkSubunits);
    }

    public List<ResearchResultPublication> getManuals(ExecutionYear executionYear, Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Manual.class, executionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getManuals(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear,
            Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Manual.class, firstExecutionYear, lastExecutionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getTechnicalReports(Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(TechnicalReport.class, checkSubunits);
    }

    public List<ResearchResultPublication> getTechnicalReports(ExecutionYear executionYear, Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(TechnicalReport.class, executionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getTechnicalReports(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear,
            Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(TechnicalReport.class, firstExecutionYear, lastExecutionYear,
                checkSubunits);
    }

    public List<ResearchResultPublication> getOtherPublications(Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(OtherPublication.class, checkSubunits);
    }

    public List<ResearchResultPublication> getOtherPublications(ExecutionYear executionYear, Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(OtherPublication.class, executionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getOtherPublications(ExecutionYear firstExecutionYear,
            ExecutionYear lastExecutionYear, Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(OtherPublication.class, firstExecutionYear, lastExecutionYear,
                checkSubunits);
    }

    public List<ResearchResultPublication> getUnstructureds(Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Unstructured.class, checkSubunits);
    }

    public List<ResearchResultPublication> getUnstructureds(ExecutionYear executionYear, Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Unstructured.class, executionYear, checkSubunits);
    }

    public List<ResearchResultPublication> getUnstructureds(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear,
            Boolean checkSubunits) {
        return this.getResearchResultPublicationsByType(Unstructured.class, firstExecutionYear, lastExecutionYear, checkSubunits);
    }

    @Override
    public List<ResearchResultPublication> getResearchResultPublications() {
        Set<ResearchResultPublication> publications = new HashSet<ResearchResultPublication>();

        for (ResultUnitAssociation association : getResultUnitAssociations()) {
            if (association.getResult() instanceof ResearchResultPublication) {
                publications.add((ResearchResultPublication) association.getResult());
            }
        }
        return new ArrayList<ResearchResultPublication>(publications);
    }

    public List<ResearchResultPublication> getResearchResultPublications(Boolean checkSubunits) {

        List<ResearchResultPublication> publications = new ArrayList<ResearchResultPublication>(getResearchResultPublications());

        if (checkSubunits.equals(Boolean.TRUE)) {

            Set<ResearchResultPublication> uniquePublications = new HashSet<ResearchResultPublication>(publications);
            for (Unit unit : getAllSubUnits()) {
                uniquePublications.addAll(unit.getResearchResultPublications());
            }

            return new ArrayList<ResearchResultPublication>(uniquePublications);
        }

        return publications;

    }

    protected List<ResearchResultPublication> getResearchResultPublicationsByType(
            final Class<? extends ResearchResultPublication> clazz, Boolean checkSubunits) {
        return filterResultPublicationsByType(clazz, getResearchResultPublications(checkSubunits));
    }

    protected List<ResearchResultPublication> getResearchResultPublicationsByType(
            final Class<? extends ResearchResultPublication> clazz, ExecutionYear executionYear, Boolean checkSubunits) {
        return filterResultPublicationsByType(clazz, getResearchResultPublicationsByExecutionYear(executionYear, checkSubunits));
    }

    protected List<ResearchResultPublication> getResearchResultPublicationsByType(
            final Class<? extends ResearchResultPublication> clazz, ExecutionYear firstExecutionYear,
            ExecutionYear lastExecutionYear, Boolean checkSubunits) {
        return filterResultPublicationsByType(clazz,
                getResearchResultPublicationsByExecutionYear(firstExecutionYear, lastExecutionYear, checkSubunits));
    }

    protected List<ResearchResultPublication> getResearchResultPublicationsByExecutionYear(ExecutionYear executionYear,
            Boolean checkSubunits) {

        List<ResearchResultPublication> publications = getResearchResultPublicationsByExecutionYear(executionYear);

        if (checkSubunits.equals(Boolean.TRUE)) {

            Set<ResearchResultPublication> uniquePublications = new HashSet<ResearchResultPublication>(publications);
            for (Unit unit : getAllSubUnits()) {
                uniquePublications.addAll(unit.getResearchResultPublicationsByExecutionYear(executionYear));
            }

            return new ArrayList<ResearchResultPublication>(uniquePublications);
        }

        return publications;
    }

    protected List<ResearchResultPublication> getResearchResultPublicationsByExecutionYear(ExecutionYear firstExecutionYear,
            ExecutionYear lastExecutionYear, Boolean checkSubunits) {

        List<ResearchResultPublication> publications =
                getResearchResultPublicationsByExecutionYear(firstExecutionYear, lastExecutionYear);

        if (checkSubunits.equals(Boolean.TRUE)) {
            Set<ResearchResultPublication> uniquePublications = new HashSet<ResearchResultPublication>(publications);
            for (Unit unit : getAllSubUnits()) {
                uniquePublications.addAll(unit
                        .getResearchResultPublicationsByExecutionYear(firstExecutionYear, lastExecutionYear));
            }

            return new ArrayList<ResearchResultPublication>(uniquePublications);
        }

        return publications;
    }

    @Override
    public Country getCountry() {
        if (super.getCountry() != null) {
            return super.getCountry();
        }
        for (final Unit unit : getParentUnits()) {
            final Country country = unit.getCountry();
            if (country != null) {
                return country;
            }
        }
        return null;
    }

    @Override
    public void setAcronym(String acronym) {
        super.setAcronym(acronym);
        UnitAcronym unitAcronym = UnitAcronym.readUnitAcronymByAcronym(acronym);
        if (unitAcronym == null) {
            unitAcronym = new UnitAcronym(acronym);
        }
        setUnitAcronym(unitAcronym);
    }

    public boolean hasCurrentActiveWorkingEmployee(final Employee employee) {
        final YearMonthDay currentDate = new YearMonthDay();
        for (final Contract contract : getWorkingContracts()) {
            final Employee employeeFromContract = contract.getEmployee();
            if (employee == employeeFromContract && contract.isActive(currentDate)) {
                return true;
            }
        }
        for (final Unit subUnit : getSubUnits()) {
            if (subUnit.hasCurrentActiveWorkingEmployee(employee)) {
                return true;
            }
        }
        return false;
    }

    public Boolean hasParentUnit(Unit parentUnit) {
        for (Unit parent : getParentUnits()) {
            if (parent.equals(parentUnit)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static Unit getParentUnit(String unitNormalizedName, Class<? extends Unit> clazz) {
        if (StringUtils.isEmpty(unitNormalizedName)) {
            return null;
        }

        for (final UnitName unitName : UnitName.find(unitNormalizedName, Integer.MAX_VALUE)) {
            final Unit unit = unitName.getUnit();
            if (unit.getClass().equals(clazz)) {
                return unit;
            }
        }
        return null;
    }

    public static Unit getParentUnitByNormalizedName(Unit childUnit, String parentNormalizedName) {
        for (Unit possibleParent : childUnit.getParentUnits()) {
            if (parentNormalizedName.equalsIgnoreCase(StringNormalizer.normalize(possibleParent.getName()))) {
                return possibleParent;
            }
        }
        return null;
    }

    public void deleteParentUnitRelation(Unit parentUnit) {
        for (Accountability relation : this.getParents()) {
            if (relation.getParentParty().equals(parentUnit)) {
                relation.delete();
                return;
            }
        }
    }

    public Boolean isOfficial() {
        return Boolean.FALSE;
    }

    @Deprecated
    public java.util.Date getBeginDate() {
        org.joda.time.YearMonthDay ymd = getBeginDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setBeginDate(java.util.Date date) {
        if (date == null) {
            setBeginDateYearMonthDay(null);
        } else {
            setBeginDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEndDate() {
        org.joda.time.YearMonthDay ymd = getEndDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEndDate(java.util.Date date) {
        if (date == null) {
            setEndDateYearMonthDay(null);
        } else {
            setEndDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Override
    public boolean isAdministrativeOfficeUnit() {
        return getAdministrativeOffice() != null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.UnitBasedSender> getUnitBasedSender() {
        return getUnitBasedSenderSet();
    }

    @Deprecated
    public boolean hasAnyUnitBasedSender() {
        return !getUnitBasedSenderSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator> getExamCoordinators() {
        return getExamCoordinatorsSet();
    }

    @Deprecated
    public boolean hasAnyExamCoordinators() {
        return !getExamCoordinatorsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.Cooperation> getCooperation() {
        return getCooperationSet();
    }

    @Deprecated
    public boolean hasAnyCooperation() {
        return !getCooperationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Formation> getAssociatedBaseFormations() {
        return getAssociatedBaseFormationsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedBaseFormations() {
        return !getAssociatedBaseFormationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.EctsCycleGraduationGradeConversionTable> getEctsGraduationGradeConversionTables() {
        return getEctsGraduationGradeConversionTablesSet();
    }

    @Deprecated
    public boolean hasAnyEctsGraduationGradeConversionTables() {
        return !getEctsGraduationGradeConversionTablesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.UnitFileTag> getUnitFileTags() {
        return getUnitFileTagsSet();
    }

    @Deprecated
    public boolean hasAnyUnitFileTags() {
        return !getUnitFileTagsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation> getDestinationPrecedentDegreeInformations() {
        return getDestinationPrecedentDegreeInformationsSet();
    }

    @Deprecated
    public boolean hasAnyDestinationPrecedentDegreeInformations() {
        return !getDestinationPrecedentDegreeInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Receipt> getOwnedReceipts() {
        return getOwnedReceiptsSet();
    }

    @Deprecated
    public boolean hasAnyOwnedReceipts() {
        return !getOwnedReceiptsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup> getVigilantGroups() {
        return getVigilantGroupsSet();
    }

    @Deprecated
    public boolean hasAnyVigilantGroups() {
        return !getVigilantGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.ExternalPrecedentDegreeInformation> getDestinationExternalPrecedentDegreeInformations() {
        return getDestinationExternalPrecedentDegreeInformationsSet();
    }

    @Deprecated
    public boolean hasAnyDestinationExternalPrecedentDegreeInformations() {
        return !getDestinationExternalPrecedentDegreeInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.EctsInstitutionConversionTable> getEctsInstitutionConversionTables() {
        return getEctsInstitutionConversionTablesSet();
    }

    @Deprecated
    public boolean hasAnyEctsInstitutionConversionTables() {
        return !getEctsInstitutionConversionTablesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers> getPersistentGroups() {
        return getPersistentGroupsSet();
    }

    @Deprecated
    public boolean hasAnyPersistentGroups() {
        return !getPersistentGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation> getTransitionPrecedentDegreeInformations() {
        return getTransitionPrecedentDegreeInformationsSet();
    }

    @Deprecated
    public boolean hasAnyTransitionPrecedentDegreeInformations() {
        return !getTransitionPrecedentDegreeInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.UnitFile> getFiles() {
        return getFilesSet();
    }

    @Deprecated
    public boolean hasAnyFiles() {
        return !getFilesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.space.UnitSpaceOccupation> getUnitSpaceOccupations() {
        return getUnitSpaceOccupationsSet();
    }

    @Deprecated
    public boolean hasAnyUnitSpaceOccupations() {
        return !getUnitSpaceOccupationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.EctsInstitutionByCurricularYearConversionTable> getEctsCourseConversionTables() {
        return getEctsCourseConversionTablesSet();
    }

    @Deprecated
    public boolean hasAnyEctsCourseConversionTables() {
        return !getEctsCourseConversionTablesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.NonAffiliatedTeacher> getAssociatedNonAffiliatedTeachers() {
        return getAssociatedNonAffiliatedTeachersSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedNonAffiliatedTeachers() {
        return !getAssociatedNonAffiliatedTeachersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.FunctionalityPrinters> getFunctionalityPrinters() {
        return getFunctionalityPrintersSet();
    }

    @Deprecated
    public boolean hasAnyFunctionalityPrinters() {
        return !getFunctionalityPrintersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.raides.DegreeDesignation> getDegreeDesignation() {
        return getDegreeDesignationSet();
    }

    @Deprecated
    public boolean hasAnyDegreeDesignation() {
        return !getDegreeDesignationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation> getPrecedentDegreeInformations() {
        return getPrecedentDegreeInformationsSet();
    }

    @Deprecated
    public boolean hasAnyPrecedentDegreeInformations() {
        return !getPrecedentDegreeInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Formation> getAssociatedFormations() {
        return getAssociatedFormationsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedFormations() {
        return !getAssociatedFormationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExternalProgramCertificateRequest> getExternalProgramCertificateRequests() {
        return getExternalProgramCertificateRequestsSet();
    }

    @Deprecated
    public boolean hasAnyExternalProgramCertificateRequests() {
        return !getExternalProgramCertificateRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExternalCurricularCourse> getExternalCurricularCourses() {
        return getExternalCurricularCoursesSet();
    }

    @Deprecated
    public boolean hasAnyExternalCurricularCourses() {
        return !getExternalCurricularCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.ExternalRegistrationData> getExternalRegistrationDatas() {
        return getExternalRegistrationDatasSet();
    }

    @Deprecated
    public boolean hasAnyExternalRegistrationDatas() {
        return !getExternalRegistrationDatasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy> getCandidacies() {
        return getCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyCandidacies() {
        return !getCandidaciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.ExternalPrecedentDegreeInformation> getCandidacyPrecedentDegreeInformations() {
        return getCandidacyPrecedentDegreeInformationsSet();
    }

    @Deprecated
    public boolean hasAnyCandidacyPrecedentDegreeInformations() {
        return !getCandidacyPrecedentDegreeInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.events.InstitutionAffiliationEvent> getAffiliationEvent() {
        return getAffiliationEventSet();
    }

    @Deprecated
    public boolean hasAnyAffiliationEvent() {
        return !getAffiliationEventSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExternalCourseLoadRequest> getExternalCourseLoadRequests() {
        return getExternalCourseLoadRequestsSet();
    }

    @Deprecated
    public boolean hasAnyExternalCourseLoadRequests() {
        return !getExternalCourseLoadRequestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.events.MicroPaymentEvent> getMicroPaymentEvent() {
        return getMicroPaymentEventSet();
    }

    @Deprecated
    public boolean hasAnyMicroPaymentEvent() {
        return !getMicroPaymentEventSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Person> getAllowedPeopleToUploadFiles() {
        return getAllowedPeopleToUploadFilesSet();
    }

    @Deprecated
    public boolean hasAnyAllowedPeopleToUploadFiles() {
        return !getAllowedPeopleToUploadFilesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PersonalIngressionData> getPersonalIngressionsData() {
        return getPersonalIngressionsDataSet();
    }

    @Deprecated
    public boolean hasAnyPersonalIngressionsData() {
        return !getPersonalIngressionsDataSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy> getStudentCandidacies() {
        return getStudentCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyStudentCandidacies() {
        return !getStudentCandidaciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.Function> getFunctions() {
        return getFunctionsSet();
    }

    @Deprecated
    public boolean hasAnyFunctions() {
        return !getFunctionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.events.InstitutionAffiliationEvent> getOpenAffiliationEvent() {
        return getOpenAffiliationEventSet();
    }

    @Deprecated
    public boolean hasAnyOpenAffiliationEvent() {
        return !getOpenAffiliationEventSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation> getResultUnitAssociations() {
        return getResultUnitAssociationsSet();
    }

    @Deprecated
    public boolean hasAnyResultUnitAssociations() {
        return !getResultUnitAssociationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBeginDateYearMonthDay() {
        return getBeginDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasUnitName() {
        return getUnitName() != null;
    }

    @Deprecated
    public boolean hasBennuForExternalInstitutionUnit() {
        return getRootDomainObjectForExternalInstitutionUnit() != null;
    }

    @Deprecated
    public boolean hasSite() {
        return getSite() != null;
    }

    @Deprecated
    public boolean hasRegistryCodeGenerator() {
        return getRegistryCodeGenerator() != null;
    }

    @Override
    @Deprecated
    public boolean hasAdministrativeOffice() {
        return getAdministrativeOffice() != null;
    }

    @Deprecated
    public boolean hasUnitServiceAgreementTemplate() {
        return getUnitServiceAgreementTemplate() != null;
    }

    @Deprecated
    public boolean hasUnitAcronym() {
        return getUnitAcronym() != null;
    }

    @Deprecated
    public boolean hasCampus() {
        return getCampus() != null;
    }

    @Deprecated
    public boolean hasCode() {
        return getCode() != null;
    }

    @Deprecated
    public boolean hasUnitCostCenterCode() {
        return getUnitCostCenterCode() != null;
    }

    @Deprecated
    public boolean hasBennuForEarthUnit() {
        return getRootDomainObjectForEarthUnit() != null;
    }

    @Deprecated
    public boolean hasBennuForInstitutionUnit() {
        return getRootDomainObjectForInstitutionUnit() != null;
    }

    @Deprecated
    public boolean hasClassification() {
        return getClassification() != null;
    }

    @Deprecated
    public boolean hasEndDateYearMonthDay() {
        return getEndDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasAcronym() {
        return getAcronym() != null;
    }

    @Deprecated
    public boolean hasCanBeResponsibleOfSpaces() {
        return getCanBeResponsibleOfSpaces() != null;
    }

    @Deprecated
    public boolean hasIdentificationCardLabel() {
        return getIdentificationCardLabel() != null;
    }

}
