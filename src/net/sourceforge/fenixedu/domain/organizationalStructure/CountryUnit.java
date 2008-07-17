package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.StringNormalizer;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CountryUnit extends CountryUnit_Base {

    private CountryUnit() {
	super();
	super.setType(PartyTypeEnum.COUNTRY);
    }

    public static CountryUnit createNewCountryUnit(MultiLanguageString countryName, Integer costCenterCode,
	    String countryAcronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, String webAddress,
	    UnitClassification classification, Boolean canBeResponsibleOfSpaces, Campus campus) {

	CountryUnit countryUnit = new CountryUnit();
	countryUnit.init(countryName, costCenterCode, countryAcronym, beginDate, endDate, webAddress, classification,
		canBeResponsibleOfSpaces, campus);
	countryUnit.addParentUnit(parentUnit, AccountabilityType.readAccountabilityTypeByType(AccountabilityTypeEnum.GEOGRAPHIC));

	checkIfAlreadyExistsOneCountryWithSameAcronymAndName(countryUnit);

	return countryUnit;
    }

    @Override
    public void edit(MultiLanguageString name, String acronym) {
	super.edit(name, acronym);
	checkIfAlreadyExistsOneCountryWithSameAcronymAndName(this);
    }

    @Override
    public void edit(MultiLanguageString unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
	    YearMonthDay endDate, String webAddress, UnitClassification classification, Department department, Degree degree,
	    AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces, Campus campus) {

	super.edit(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department, degree,
		administrativeOffice, canBeResponsibleOfSpaces, campus);

	checkIfAlreadyExistsOneCountryWithSameAcronymAndName(this);
    }

    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
	if (parentUnit != null && (!parentUnit.isOfficialExternal() || !parentUnit.isPlanetUnit())) {
	    throw new DomainException("error.exception.commons.institution.invalidParentUnit");
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
    public void setType(PartyTypeEnum partyTypeEnum) {
	throw new DomainException("unit.impossible.set.type");
    }

    @Override
    public List<ExternalCurricularCourse> getAllExternalCurricularCourses() {
	final List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>(getExternalCurricularCourses());
	for (Unit subUnit : getSubUnits()) {
	    if (subUnit.isUniversityUnit() || subUnit.isSchoolUnit()) {
		result.addAll(subUnit.getExternalCurricularCourses());
	    }
	}
	return result;
    }

    @Override
    public boolean isCountryUnit() {
	return true;
    }

    private static void checkIfAlreadyExistsOneCountryWithSameAcronymAndName(CountryUnit countryUnit) {
	for (Unit parentUnit : countryUnit.getParentUnits()) {
	    for (Unit unit : parentUnit.getAllSubUnits()) {
		if ((!unit.equals(countryUnit)) && unit.isCountryUnit()) {
		    if (countryUnit.getAcronym().equalsIgnoreCase(unit.getAcronym())) {
			throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym", countryUnit
				.getAcronym());
		    }
		    if (countryUnit.getName().equalsIgnoreCase(unit.getName())) {
			throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym", countryUnit
				.getName());
		    }
		}
	    }
	}
    }

    transient static private CountryUnit defaultCountry;

    public static CountryUnit getDefault() {
	if (defaultCountry != null && defaultCountry.getRootDomainObject() == RootDomainObject.getInstance()) {
	    return defaultCountry;
	}
	defaultCountry = (CountryUnit) Unit.readUnitByAcronymAndType(ResourceBundle.getBundle("resources.GlobalResources",
		Language.getLocale()).getString("default.country.code"), PartyTypeEnum.COUNTRY);
	return defaultCountry;
    }
}
