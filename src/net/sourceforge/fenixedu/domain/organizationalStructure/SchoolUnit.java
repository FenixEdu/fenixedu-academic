package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class SchoolUnit extends SchoolUnit_Base {

    private SchoolUnit() {
        super();
        super.setType(PartyTypeEnum.SCHOOL);
    }

    @Override
    public boolean isSchoolUnit() {
        return true;
    }

    public static SchoolUnit createNewSchoolUnit(MultiLanguageString schoolName, String schoolNameCard, Unit parentUnit,
            Boolean official, String code, AcademicalInstitutionType institutionType) {

        SchoolUnit schoolUnit = new SchoolUnit();
        schoolUnit.setPartyName(schoolName);
        schoolUnit.setIdentificationCardLabel(schoolNameCard);
        schoolUnit.setOfficial(official);
        schoolUnit.setCode(code);
        schoolUnit.setInstitutionType(institutionType);
        schoolUnit.setBeginDateYearMonthDay(YearMonthDay.fromDateFields(new GregorianCalendar().getTime()));
        schoolUnit.setCanBeResponsibleOfSpaces(Boolean.FALSE);
        return createNewUnit(parentUnit, schoolUnit, Boolean.FALSE);
    }

    public static SchoolUnit createNewSchoolUnit(MultiLanguageString schoolName, String schoolNameCard, Integer costCenterCode,
            String schoolAcronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, String webAddress,
            UnitClassification classification, Boolean canBeResponsibleOfSpaces, Campus campus) {

        SchoolUnit schoolUnit = new SchoolUnit();
        schoolUnit.init(schoolName, schoolNameCard, costCenterCode, schoolAcronym, beginDate, endDate, webAddress,
                classification, null, canBeResponsibleOfSpaces, campus);
        return createNewUnit(parentUnit, schoolUnit, Boolean.TRUE);
    }

    private static SchoolUnit createNewUnit(Unit parentUnit, SchoolUnit schoolUnit, Boolean checkExistingUnit) {
        if (parentUnit.isCountryUnit()) {
            schoolUnit.addParentUnit(parentUnit, AccountabilityType.readByType(AccountabilityTypeEnum.GEOGRAPHIC));
        } else if (parentUnit.isUniversityUnit()) {
            schoolUnit.addParentUnit(parentUnit, AccountabilityType.readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
        }

        if (checkExistingUnit) {
            checkIfAlreadyExistsOneSchoolWithSameAcronymAndName(schoolUnit);
        }

        return schoolUnit;
    }

    @Override
    public void edit(MultiLanguageString name, String acronym) {
        super.edit(name, acronym);
        checkIfAlreadyExistsOneSchoolWithSameAcronymAndName(this);
    }

    @Override
    public void edit(MultiLanguageString unitName, String unitNameCard, Integer unitCostCenter, String acronym,
            YearMonthDay beginDate, YearMonthDay endDate, String webAddress, UnitClassification classification,
            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces,
            Campus campus) {

        super.edit(unitName, unitNameCard, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department,
                degree, administrativeOffice, canBeResponsibleOfSpaces, campus);
        checkIfAlreadyExistsOneSchoolWithSameAcronymAndName(this);
    }

    protected static void checkIfAlreadyExistsOneSchoolWithSameAcronymAndName(SchoolUnit schoolUnit) {
        for (Unit parentUnit : schoolUnit.getParentUnits()) {
            for (Unit unit : parentUnit.getAllSubUnits()) {
                if (!unit.equals(schoolUnit)
                        && unit.isSchoolUnit()
                        && ((schoolUnit.getAcronym() != null && schoolUnit.getAcronym().equalsIgnoreCase(unit.getAcronym())) || schoolUnit
                                .getName().equalsIgnoreCase(unit.getName()))) {
                    throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
                }
            }
        }
    }

    @Override
    public List<ExternalCurricularCourse> getAllExternalCurricularCourses() {
        final List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>(getExternalCurricularCourses());
        for (Unit subUnit : getSubUnits()) {
            if (subUnit.isDepartmentUnit()) {
                result.addAll(subUnit.getExternalCurricularCourses());
            }
        }
        return result;
    }

    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
        if (parentUnit != null
                && (!parentUnit.isOfficialExternal() || (!parentUnit.isPlanetUnit() && !parentUnit.isCountryUnit() && !parentUnit
                        .isUniversityUnit()))) {
            throw new DomainException("error.unit.invalid.parentUnit");
        }
        return super.addParentUnit(parentUnit, accountabilityType);
    }

    @Override
    public String getFullPresentationName() {
        StringBuilder output = new StringBuilder();
        output.append(getName().trim());
        output.append(" da ");
        List<Unit> parents = getParentUnitsPath();
        output.append(parents.get(parents.size() - 1).getName());
        return output.toString();
    }
}
