package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class DegreeUnit extends DegreeUnit_Base {
    
    private DegreeUnit() {
        super();
        super.setType(PartyTypeEnum.DEGREE_UNIT);
    }
        
    public static DegreeUnit createNewInternalDegreeUnit(String unitName, Integer costCenterCode, String acronym,
	    YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, AccountabilityType accountabilityType, 
	    String webAddress, Degree degree, UnitClassification classification, Boolean canBeResponsibleOfSpaces) {
	
	DegreeUnit degreeUnit = new DegreeUnit();	
	degreeUnit.init(unitName, costCenterCode, acronym, beginDate, endDate, webAddress, classification, canBeResponsibleOfSpaces);
	degreeUnit.setDegree(degree);		
	degreeUnit.addParentUnit(parentUnit, accountabilityType);
	
	checkIfAlreadyExistsOneDegreeWithSameAcronym(degreeUnit);
	
	return degreeUnit;
    }
    
    @Override
    public void edit(String unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
            YearMonthDay endDate, String webAddress, UnitClassification classification, 
            Department department, Degree degree, Boolean canBeResponsibleOfSpaces) {
        
	super.edit(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department, degree, canBeResponsibleOfSpaces);
        setDegree(degree);
        
        checkIfAlreadyExistsOneDegreeWithSameAcronym(this);        
    }
    
    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
	if (parentUnit != null && !parentUnit.isInternal()) {
	    throw new DomainException("error.unit.invalid.parentUnit");
	}	
	return super.addParentUnit(parentUnit, accountabilityType);
    }
     
    @Override
    public void setAcronym(String acronym) {
        if(StringUtils.isEmpty(acronym)) {
            throw new DomainException("error.unit.empty.acronym");
        }
	super.setAcronym(acronym);
    }
     
    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }
    
    @Override
    public boolean isDegreeUnit() {
        return true;
    }
    
    @Override
    public void setDegree(Degree degree) {
        if(degree == null) {
            throw new DomainException("error.DegreeUnit.empty.degree");
        }
	super.setDegree(degree);
    }
    
    @Override
    public void delete() {
	super.setDegree(null);
        super.delete();        
    }
    
    private static void checkIfAlreadyExistsOneDegreeWithSameAcronym(DegreeUnit degreeUnit) {
	for (Unit unit: UnitUtils.readInstitutionUnit().getAllSubUnits()) {
	    if(!unit.equals(degreeUnit) && unit.isDegreeUnit() && degreeUnit.getAcronym().equalsIgnoreCase(unit.getAcronym())) {		
		throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");		
	    }
	}		
    }
}
