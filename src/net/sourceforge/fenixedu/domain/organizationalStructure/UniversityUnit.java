package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import org.joda.time.YearMonthDay;

public class UniversityUnit extends UniversityUnit_Base {

    private UniversityUnit() {
	super();
	super.setType(PartyTypeEnum.UNIVERSITY);
    }

    @Override
    public boolean isUniversityUnit() {
	return true;
    }

    public static UniversityUnit createNewUniversityUnit(MultiLanguageString name, Unit parentUnit, Boolean official,
	    String code, AcademicalInstitutionType institutionType) {

	UniversityUnit universityUnit = new UniversityUnit();
	universityUnit.setPartyName(name);
	universityUnit.setOfficial(official);
	universityUnit.setCode(code);
	universityUnit.setInstitutionType(institutionType);
	universityUnit.setBeginDateYearMonthDay(YearMonthDay.fromDateFields(new GregorianCalendar().getTime()));
	universityUnit.setCanBeResponsibleOfSpaces(Boolean.FALSE);
	return createNewUnit(parentUnit, universityUnit);
    }

    public static UniversityUnit createNewUniversityUnit(MultiLanguageString universityName, Integer costCenterCode,
	    String universityAcronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, String webAddress,
	    UnitClassification classification, Boolean canBeResponsibleOfSpaces, Campus campus) {

	UniversityUnit universityUnit = new UniversityUnit();
	universityUnit.init(universityName, costCenterCode, universityAcronym, beginDate, endDate, webAddress, classification,
		canBeResponsibleOfSpaces, campus);
	return createNewUnit(parentUnit, universityUnit);
    }

    private static UniversityUnit createNewUnit(Unit parentUnit, UniversityUnit universityUnit) {
	universityUnit.addParentUnit(parentUnit, AccountabilityType
		.readAccountabilityTypeByType(AccountabilityTypeEnum.GEOGRAPHIC));

	checkIfAlreadyExistsOneUniversityWithSameAcronymAndName(universityUnit);
	return universityUnit;
    }

    @Override
    public void edit(MultiLanguageString name, String acronym) {
	super.edit(name, acronym);
	checkIfAlreadyExistsOneUniversityWithSameAcronymAndName(this);
    }

    @Override
    public void edit(MultiLanguageString unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
	    YearMonthDay endDate, String webAddress, UnitClassification classification, Department department, Degree degree,
	    AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces, Campus campus) {

	super.edit(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department, degree,
		administrativeOffice, canBeResponsibleOfSpaces, campus);
	checkIfAlreadyExistsOneUniversityWithSameAcronymAndName(this);
    }

    protected static void checkIfAlreadyExistsOneUniversityWithSameAcronymAndName(AcademicalInstitutionUnit universityUnit) {
	for (Unit parentUnit : universityUnit.getParentUnits()) {
	    for (Unit unit : parentUnit.getAllSubUnits()) {
		if (!unit.equals(universityUnit) && unit.isUniversityUnit()
			&& ((universityUnit.getAcronym() != null && universityUnit.getAcronym().equalsIgnoreCase(
				unit.getAcronym())) || universityUnit.getName().equalsIgnoreCase(unit.getName()))) {
		    throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
		}
	    }
	}
    }

    @SuppressWarnings("unchecked")
    final static public UniversityUnit getInstitutionsUniversityUnit() {
	final Unit institutionUnit = RootDomainObject.getInstance().getInstitutionUnit();
	final Collection<UniversityUnit> parentUniversityUnits = (Collection<UniversityUnit>) institutionUnit.getParentParties(
		AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE, UniversityUnit.class);

	if (parentUniversityUnits.size() != 1) {
	    throw new DomainException("UniversityUnit.unable.to.determine.single.university.unit.for.institution.unit");
	}

	return parentUniversityUnits.iterator().next();
    }

    final public Person getInstitutionsUniversityPrincipal() {
	final Unit institutionUnit = RootDomainObject.getInstance().getInstitutionUnit();
	if (!getChildParties(Unit.class).contains(institutionUnit)) {
	    throw new DomainException("UniversityUnit.not.parent.of.institution.unit");
	}

	final Collection<? extends Accountability> childAccountabilities = institutionUnit.getChildAccountabilities(
		PersonFunction.class, AccountabilityTypeEnum.MANAGEMENT_FUNCTION);
	for (final Accountability accountability : childAccountabilities) {
	    if (((Function) accountability.getAccountabilityType()).getFunctionType() == FunctionType.PRINCIPAL) {
		return ((PersonFunction) accountability).getPerson();
	    }
	}

	return null;
    }

    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
	if (parentUnit != null
		&& (!parentUnit.isOfficialExternal() || (!parentUnit.isPlanetUnit() && !parentUnit.isCountryUnit()))) {
	    throw new DomainException("error.unit.invalid.parentUnit");
	}
	return super.addParentUnit(parentUnit, accountabilityType);
    }

}
