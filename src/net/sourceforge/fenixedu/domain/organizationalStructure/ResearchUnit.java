package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;



import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class ResearchUnit extends ResearchUnit_Base {

    private ResearchUnit() {
	super();
	super.setType(PartyTypeEnum.RESEARCH_UNIT);
    }

    public static ResearchUnit createNewResearchUnit(String name, Integer costCenterCode,
	    String acronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
	    AccountabilityType accountabilityType, String webAddress, UnitClassification classification, 
	    Boolean canBeResponsibleOfSpaces) {

	ResearchUnit researchUnit = new ResearchUnit();
	researchUnit.init(name, costCenterCode, acronym, beginDate, endDate, webAddress, classification, canBeResponsibleOfSpaces);
	researchUnit.addParentUnit(parentUnit, accountabilityType);
	
	checkIfAlreadyExistsOneResearchUnitWithSameNameOrAcronym(researchUnit);

	return researchUnit;
    }  
       
    @Override
    public void setAcronym(String acronym) {
	if(StringUtils.isEmpty(acronym)) {
	    throw new DomainException("acronym.cannot.be.null");
	}
        super.setAcronym(acronym);
    }
    
    @Override
    public void edit(String unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
	    YearMonthDay endDate, String webAddress, UnitClassification classification,
	    Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces) {
	
	super.edit(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification,department, degree, administrativeOffice, canBeResponsibleOfSpaces);
	
	checkIfAlreadyExistsOneResearchUnitWithSameNameOrAcronym(this);
    }

    @Override
    public boolean isResearchUnit() {
	return true;
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
	throw new DomainException("unit.impossible.set.type");
    }

    private static void checkIfAlreadyExistsOneResearchUnitWithSameNameOrAcronym(ResearchUnit researchUnit) {
	PartyType type = PartyType.readPartyTypeByType(PartyTypeEnum.RESEARCH_UNIT);	
	for (Party party : type.getParties()) {
	    ResearchUnit unit = (ResearchUnit) party;
	    if (!unit.equals(researchUnit) && unit.isResearchUnit() && researchUnit.getName().equalsIgnoreCase(unit.getName())
		    && researchUnit.getAcronym().equalsIgnoreCase(unit.getAcronym())) {
		throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
	    }
	}

    }
    
    
}
