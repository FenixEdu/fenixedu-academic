package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DepartmentSite;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.accessControl.DegreeStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentEmployeesByExecutionYearGroup;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentTeachersByExecutionYearGroup;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.util.email.UnitBasedSender;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DepartmentUnit extends DepartmentUnit_Base {

    private DepartmentUnit() {
        super();
        super.setType(PartyTypeEnum.DEPARTMENT);
    }

    public static DepartmentUnit createNewInternalDepartmentUnit(MultiLanguageString departmentName, String departmentNameCard,
            Integer costCenterCode, String departmentAcronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
            AccountabilityType accountabilityType, String webAddress, Department department, UnitClassification classification,
            Boolean canBeResponsibleOfSpaces, Campus campus) {

        DepartmentUnit departmentUnit = new DepartmentUnit();
        departmentUnit.init(departmentName, departmentNameCard, costCenterCode, departmentAcronym, beginDate, endDate,
                webAddress, classification, null, canBeResponsibleOfSpaces, campus);
        departmentUnit.setDepartment(department);
        departmentUnit.addParentUnit(parentUnit, accountabilityType);

        checkIfAlreadyExistsOneDepartmentUnitWithSameAcronymAndName(departmentUnit);

        return departmentUnit;
    }

    public static DepartmentUnit createNewOfficialExternalDepartmentUnit(final String departmentName,
            final String departmentAcronym, final Unit parentUnit) {

        final DepartmentUnit departmentUnit = new DepartmentUnit();
        departmentUnit.init(new MultiLanguageString(Language.getDefaultLanguage(), departmentName), null, null,
                departmentAcronym, new YearMonthDay(), null, null, null, null, null, null);
        if (parentUnit.isCountryUnit()) {
            departmentUnit.addParentUnit(parentUnit, AccountabilityType.readByType(AccountabilityTypeEnum.GEOGRAPHIC));
        } else {
            departmentUnit.addParentUnit(parentUnit,
                    AccountabilityType.readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
        }

        checkIfAlreadyExistsOneDepartmentUnitWithSameAcronymAndName(departmentUnit);
        return departmentUnit;
    }

    @Override
    public void edit(MultiLanguageString name, String acronym) {
        super.edit(name, acronym);
        checkIfAlreadyExistsOneDepartmentUnitWithSameAcronymAndName(this);
    }

    @Override
    public void edit(MultiLanguageString unitName, String departmentNameCard, Integer unitCostCenter, String acronym,
            YearMonthDay beginDate, YearMonthDay endDate, String webAddress, UnitClassification classification,
            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces,
            Campus campus) {

        super.edit(unitName, departmentNameCard, unitCostCenter, acronym, beginDate, endDate, webAddress, classification,
                department, degree, administrativeOffice, canBeResponsibleOfSpaces, campus);
        if (isInternal()) {
            setDepartment(department);
        }

        checkIfAlreadyExistsOneDepartmentUnitWithSameAcronymAndName(this);
    }

    public List<CompetenceCourse> getCompetenceCourses() {
        return getCompetenceCourses(null);
    }

    public List<CompetenceCourse> getCompetenceCourses(CurricularStage curricularStage) {
        List<CompetenceCourse> result = new ArrayList<CompetenceCourse>();
        for (ScientificAreaUnit scientificAreaUnit : getScientificAreaUnits()) {
            for (CompetenceCourseGroupUnit competenceCourseGroupUnit : scientificAreaUnit.getCompetenceCourseGroupUnits()) {
                for (CompetenceCourse competenceCourse : competenceCourseGroupUnit.getCompetenceCourses()) {
                    if (curricularStage == null || competenceCourse.getCurricularStage().equals(curricularStage)) {
                        result.add(competenceCourse);
                    }
                }
            }
        }
        return result;
    }

    public List<ScientificAreaUnit> getScientificAreaUnits() {
        final SortedSet<ScientificAreaUnit> result =
                new TreeSet<ScientificAreaUnit>(ScientificAreaUnit.COMPARATOR_BY_NAME_AND_ID);
        for (Unit unit : getSubUnits()) {
            if (unit.isScientificAreaUnit()) {
                result.add((ScientificAreaUnit) unit);
            }
        }
        return new ArrayList<ScientificAreaUnit>(result);
    }

    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
        if (getDepartment() == null) {
            if (parentUnit != null
                    && (!parentUnit.isOfficialExternal() || (!parentUnit.isCountryUnit() && !parentUnit.isSchoolUnit() && !parentUnit
                            .isUniversityUnit()))) {
                throw new DomainException("error.unit.invalid.parentUnit");
            }
        } else {
            if (parentUnit != null && !parentUnit.isInternal()) {
                throw new DomainException("error.unit.invalid.parentUnit");
            }
        }
        return super.addParentUnit(parentUnit, accountabilityType);
    }

    @Override
    public void setAcronym(String acronym) {
        if (StringUtils.isEmpty(acronym)) {
            throw new DomainException("error.unit.empty.acronym");
        }
        super.setAcronym(acronym);
    }

    @Override
    public void setDepartment(Department department) {
        if (department == null) {
            throw new DomainException("error.departmentUnit.empty.department");
        }
        super.setDepartment(department);
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }

    @Override
    public boolean isDepartmentUnit() {
        return true;
    }

    @Override
    public boolean hasCompetenceCourses(final CompetenceCourse competenceCourse) {
        for (Unit subUnit : getSubUnits()) {
            if (subUnit.hasCompetenceCourses(competenceCourse)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void delete() {
        for (; !getParticipatingAnyCurricularCourseCurricularRules().isEmpty(); getParticipatingAnyCurricularCourseCurricularRules()
                .iterator().next().delete()) {
            ;
        }
        super.setDepartment(null);
        super.delete();
    }

    private static void checkIfAlreadyExistsOneDepartmentUnitWithSameAcronymAndName(DepartmentUnit departmentUnit) {
        if (departmentUnit.getDepartment() == null) {
            for (Unit parentUnit : departmentUnit.getParentUnits()) {
                for (Unit subUnit : parentUnit.getAllSubUnits()) {
                    if (!subUnit.equals(departmentUnit)
                            && subUnit.isDepartmentUnit()
                            && (departmentUnit.getName().equalsIgnoreCase(subUnit.getName()) || departmentUnit.getAcronym()
                                    .equalsIgnoreCase(subUnit.getAcronym()))) {
                        throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
                    }
                }
            }
        } else {
            for (Unit unit : UnitUtils.readInstitutionUnit().getAllSubUnits()) {
                if (!unit.equals(departmentUnit)
                        && unit.isDepartmentUnit()
                        && (departmentUnit.getAcronym().equalsIgnoreCase(unit.getAcronym()) || departmentUnit.getName()
                                .equalsIgnoreCase(unit.getName()))) {
                    throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
                }
            }
        }
    }

    @Override
    public List<IGroup> getDefaultGroups() {
        List<IGroup> groups = super.getDefaultGroups();

        ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
        Department department = this.getDepartment();
        if (department != null) {

            groups.add(new DepartmentTeachersByExecutionYearGroup(currentYear, department));
            // groups.add(new
            // DepartmentStudentsByExecutionYearGroup(currentYear,
            // department));
            groups.add(new DepartmentEmployeesByExecutionYearGroup(currentYear, department));

            SortedSet<Degree> degrees = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
            degrees.addAll(department.getDegrees());

            for (Degree degree : degrees) {
                groups.add(new DegreeStudentsGroup(degree));
            }
        }

        return groups;
    }

    @Override
    protected UnitSite createSite() {
        return new DepartmentSite(this);
    }

    public static List<DepartmentUnit> readAllDepartmentUnits() {
        List<Unit> units = readAllUnits();
        List<DepartmentUnit> departments = new ArrayList<DepartmentUnit>();
        for (Unit unit : units) {
            if (unit instanceof DepartmentUnit && unit.getType().equals(PartyTypeEnum.DEPARTMENT)) {
                departments.add((DepartmentUnit) unit);
            }
        }
        return departments;
    }

    @Override
    public UnitBasedSender getOneUnitBasedSender() {
        if (hasAnyUnitBasedSender()) {
            return getUnitBasedSender().iterator().next();
        } else {
            return UnitBasedSender.newInstance(this);
        }
    }

    public Person getCurrentDepartmentPresident() {
        final YearMonthDay today = new YearMonthDay();
        for (final Accountability accountability : getChildsSet()) {
            if (accountability.isPersonFunction() && accountability.isActive(today)) {
                final PersonFunction personFunction = (PersonFunction) accountability;
                final Function function = personFunction.getFunction();
                if (function != null && function.getFunctionType() == FunctionType.PRESIDENT) {
                    final Party childParty = accountability.getChildParty();
                    if (childParty != null && childParty.isPerson()) {
                        return (Person) childParty;
                    }
                }
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.curricularRules.AnyCurricularCourse> getParticipatingAnyCurricularCourseCurricularRules() {
        return getParticipatingAnyCurricularCourseCurricularRulesSet();
    }

    @Deprecated
    public boolean hasAnyParticipatingAnyCurricularCourseCurricularRules() {
        return !getParticipatingAnyCurricularCourseCurricularRulesSet().isEmpty();
    }

    @Deprecated
    public boolean hasDepartment() {
        return getDepartment() != null;
    }

}
