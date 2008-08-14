package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;

public abstract class AcademicalInstitutionUnit extends AcademicalInstitutionUnit_Base {

    @Override
    public boolean isAcademicalUnit() {
	return true;
    }

    @Override
    public void setAcronym(String acronym) {
	if (StringUtils.isEmpty(acronym)) {
	    throw new DomainException("error.unit.empty.acronym");
	}
	super.setAcronym(acronym);
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
    public Boolean isOfficial() {
	return (getOfficial() != null && getOfficial().equals(Boolean.TRUE));
    }

    public Boolean isOfficialAndIsType(AcademicalInstitutionType type) {
	return (isOfficial() && getInstitutionType().equals(type));
    }

    protected static List<AcademicalInstitutionUnit> readOfficialUnits() {
	final List<AcademicalInstitutionUnit> officialUnits = new ArrayList<AcademicalInstitutionUnit>();

	for (final UnitName unitName : RootDomainObject.getInstance().getUnitName()) {
	    if (unitName.getUnit().isOfficial()) {
		officialUnits.add((AcademicalInstitutionUnit) unitName.getUnit());
	    }
	}
	return officialUnits;
    }

    public static List<AcademicalInstitutionUnit> readOfficialParentUnitsByType(AcademicalInstitutionType type) {
	final List<AcademicalInstitutionUnit> parentUnits = new ArrayList<AcademicalInstitutionUnit>();

	Unit countryUnit = CountryUnit.getDefault();
	for (final AcademicalInstitutionUnit unit : readOfficialUnits()) {
	    if (unit.hasParentUnit(countryUnit) && unit.isOfficialAndIsType(type)) {
		parentUnits.add(unit);
	    }
	}
	return parentUnits;
    }

    public static List<Unit> readOfficialChildUnits(AcademicalInstitutionUnit parentUnit) {
	final List<Unit> childUnits = new ArrayList<Unit>();
	for (final AcademicalInstitutionUnit unit : readOfficialUnits()) {
	    if (unit.hasParentUnit(parentUnit)) {
		childUnits.add(unit);
	    }
	}
	return childUnits;
    }

    public static List<AcademicalInstitutionUnit> readOtherAcademicUnits() {
	final List<AcademicalInstitutionUnit> otherUnits = new ArrayList<AcademicalInstitutionUnit>();
	for (final UnitName unitName : RootDomainObject.getInstance().getUnitName()) {
	    if (unitName.getUnit().isAcademicalUnit() && !unitName.getUnit().isOfficial()) {
		otherUnits.add((AcademicalInstitutionUnit) unitName.getUnit());
	    }
	}
	return otherUnits;
    }

    public Boolean hasAnyOfficialChilds() {
	for (final AcademicalInstitutionUnit unit : readOfficialUnits()) {
	    if (unit.hasParentUnit(this)) {
		return Boolean.TRUE;
	    }
	}
	return Boolean.FALSE;
    }
}
