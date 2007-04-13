package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class PlanetUnit extends PlanetUnit_Base {
    
    private PlanetUnit() {
        super();
        super.setType(PartyTypeEnum.PLANET);
    }
    
    public static Unit createNewPlanetUnit(String planetName, Integer costCenterCode, String planetAcronym,
	    YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, String webAddress,
	    UnitClassification classification, Boolean canBeResponsibleOfSpaces) {	
				
	PlanetUnit planetUnit = new PlanetUnit();	
	planetUnit.init(planetName, costCenterCode, planetAcronym, beginDate, endDate, webAddress, classification, canBeResponsibleOfSpaces);
	
	checkIfAlreadyExistsOnePlanetWithSameAcronymAndName(planetUnit);
	
	return planetUnit;
    }    
    
    @Override
    public void edit(String unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
            YearMonthDay endDate, String webAddress, UnitClassification classification, Department department, 
            Degree degree, Boolean canBeResponsibleOfSpaces) {
        
	super.edit(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department, degree, canBeResponsibleOfSpaces);
	
	checkIfAlreadyExistsOnePlanetWithSameAcronymAndName(this);
    }
    
    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
        throw new DomainException("error.unit.cannot.have.parentUnit");
    }
    
    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }
    
    @Override
    public void setAcronym(String acronym) {
        if(StringUtils.isEmpty(acronym)) {
            throw new DomainException("error.unit.empty.acronym");
        }
	super.setAcronym(acronym);
    }
    
    @Override
    public boolean isPlanetUnit() {
        return true;
    }

    private static void checkIfAlreadyExistsOnePlanetWithSameAcronymAndName(PlanetUnit planetUnit) {
	for (Unit unit : UnitUtils.readAllUnitsWithoutParents()) {
	    if(!unit.equals(planetUnit) && unit.isPlanetUnit() && 
		    (planetUnit.getAcronym().equalsIgnoreCase(unit.getAcronym()) || planetUnit.getName().equalsIgnoreCase(unit.getName()))) {		
		throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
	    }
	}		
    }
}
