package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class ResearchUnit extends ResearchUnit_Base {

    private ResearchUnit() {
	super();
	this.setType(PartyTypeEnum.RESEARCH_UNIT);
    }

    public static ResearchUnit createNewInternalResearchUnit(String name, Integer costCenterCode,
	    String acronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
	    AccountabilityType accountabilityType, String webAddress, Department department,
	    UnitClassification classification, Boolean canBeResponsibleOfSpaces) {

	ResearchUnit researchUnit = new ResearchUnit();
	researchUnit.init(name, costCenterCode, acronym, beginDate, endDate, webAddress, classification,
		canBeResponsibleOfSpaces);
	researchUnit.addParentUnit(parentUnit, accountabilityType);
	checkIfAlreadyExistsOneResearchUnitWithSameAcronymAndName(researchUnit);

	return researchUnit;
    }

    public static ResearchUnit createNewOfficialExternalResearchUnit(String departmentName,
	    Integer costCenterCode, String departmentAcronym, YearMonthDay beginDate, YearMonthDay endDate,
	    Unit parentUnit, AccountabilityType accountabilityType, String webAddress, Department department,
	    UnitClassification classification, Boolean canBeResponsibleOfSpaces) {

	ResearchUnit researchUnit = new ResearchUnit();
	researchUnit.init(departmentName, costCenterCode, departmentAcronym, beginDate, endDate, webAddress,
		classification, canBeResponsibleOfSpaces);
	researchUnit.addParentUnit(parentUnit, AccountabilityType
		.readAccountabilityTypeByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));

	checkIfAlreadyExistsOneResearchUnitWithSameAcronymAndName(researchUnit);

	return researchUnit;
    }

    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
	if (parentUnit != null && !parentUnit.isInternal()) {
	    throw new DomainException("error.unit.invalid.parentUnit");
	}	
	return super.addParentUnit(parentUnit, accountabilityType);
    }
     
    @Override
    public void edit(String unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
	    YearMonthDay endDate, String webAddress, UnitClassification classification,
	    Department department, Degree degree, Boolean canBeResponsibleOfSpaces) {
	super.edit(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification,
		department, degree, canBeResponsibleOfSpaces);
	checkIfAlreadyExistsOneResearchUnitWithSameAcronymAndName(this);
    }

    @Override
    public boolean isResearchUnit() {
	return true;
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
	throw new DomainException("unit.impossible.set.type");
    }

    private static void checkIfAlreadyExistsOneResearchUnitWithSameAcronymAndName(ResearchUnit researchUnit) {
	for (Unit unit : UnitUtils.readInstitutionUnit().getAllSubUnits()) {
	    if (!unit.equals(researchUnit)
		    && unit.isResearchUnit()
		    && (researchUnit.getAcronym().equalsIgnoreCase(unit.getAcronym()) || researchUnit
			    .getName().equalsIgnoreCase(unit.getName()))) {
		throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
	    }
	}

    }
}
