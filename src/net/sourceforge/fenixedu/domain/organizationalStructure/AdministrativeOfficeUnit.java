package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class AdministrativeOfficeUnit extends AdministrativeOfficeUnit_Base {
    
    private AdministrativeOfficeUnit() {
        super();
        super.setType(PartyTypeEnum.ADMINISTRATIVE_OFFICE_UNIT);        
    }
    
    public static AdministrativeOfficeUnit createNewAdministrativeOfficeUnit(String unitName, Integer costCenterCode, String acronym,
	    YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, AccountabilityType accountabilityType, 
	    String webAddress, UnitClassification classification, AdministrativeOffice administrativeOffice) {
	
	AdministrativeOfficeUnit administrativeOfficeUnit = new AdministrativeOfficeUnit();	

	administrativeOfficeUnit.setName(unitName);
	administrativeOfficeUnit.setAdministrativeOffice(administrativeOffice);
	administrativeOfficeUnit.setAcronym(acronym);
	administrativeOfficeUnit.setCostCenterCode(costCenterCode);	
	administrativeOfficeUnit.setBeginDateYearMonthDay(beginDate);
	administrativeOfficeUnit.setEndDateYearMonthDay(endDate);
	administrativeOfficeUnit.setWebAddress(webAddress);
	administrativeOfficeUnit.setClassification(classification);
	administrativeOfficeUnit.addParentUnit(parentUnit, accountabilityType);	
		
	return administrativeOfficeUnit;
    }
    
    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
        if(parentUnit != null && !parentUnit.isInternal()) {
            throw new DomainException("error.unit.invalid.parentUnit");
        }
	return super.addParentUnit(parentUnit, accountabilityType);
    }
    
    @Override
    public void setAdministrativeOffice(AdministrativeOffice administrativeOffice) {
	if(administrativeOffice == null) {
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
        if(hasAdministrativeOffice()) {
            throw new DomainException("error.unit.cannot.be.deleted");
        }
	super.delete();
    }
}
