package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import org.joda.time.YearMonthDay;

public class AdministrativeOfficeUnit extends AdministrativeOfficeUnit_Base {

    private AdministrativeOfficeUnit() {
	super();
	super.setType(PartyTypeEnum.ADMINISTRATIVE_OFFICE_UNIT);
    }

    public static AdministrativeOfficeUnit createNewAdministrativeOfficeUnit(MultiLanguageString unitName,
	    Integer costCenterCode, String acronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
	    AccountabilityType accountabilityType, String webAddress, UnitClassification classification,
	    AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces, Campus campus) {

	AdministrativeOfficeUnit administrativeOfficeUnit = new AdministrativeOfficeUnit();
	administrativeOfficeUnit.init(unitName, costCenterCode, acronym, beginDate, endDate, webAddress, classification,
		canBeResponsibleOfSpaces, campus);
	administrativeOfficeUnit.setAdministrativeOffice(administrativeOffice);
	administrativeOfficeUnit.addParentUnit(parentUnit, accountabilityType);

	return administrativeOfficeUnit;
    }

    @Override
    public void edit(MultiLanguageString unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
	    YearMonthDay endDate, String webAddress, UnitClassification classification, Department department, Degree degree,
	    AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces, Campus campus) {

	super.edit(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department, degree,
		administrativeOffice, canBeResponsibleOfSpaces, campus);
	setAdministrativeOffice(administrativeOffice);
    }

    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
	if (parentUnit != null && !parentUnit.isInternal()) {
	    throw new DomainException("error.unit.invalid.parentUnit");
	}
	return super.addParentUnit(parentUnit, accountabilityType);
    }

    @Override
    public void setAdministrativeOffice(AdministrativeOffice administrativeOffice) {
	if (administrativeOffice == null) {
	    throw new DomainException("error.AdministrativeOfficeUnit.empty.administrativeOffice");
	}
	super.setAdministrativeOffice(administrativeOffice);
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
	throw new DomainException("unit.impossible.set.type");
    }

    @Override
    public boolean isAdministrativeOfficeUnit() {
	return true;
    }

    @Override
    public void delete() {
	if (hasAdministrativeOffice()) {
	    throw new DomainException("error.unit.cannot.be.deleted");
	}
	super.delete();
    }
}
