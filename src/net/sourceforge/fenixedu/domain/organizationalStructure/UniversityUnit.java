package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class UniversityUnit extends UniversityUnit_Base {
    
    private UniversityUnit() {
        super();
        super.setType(PartyTypeEnum.UNIVERSITY);
    }
    
    public static Unit createNewUniversityUnit(String universityName, Integer costCenterCode, String universityAcronym,
	    YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, String webAddress, UnitClassification classification, 
	    Boolean canBeResponsibleOfSpaces) {			
	
	UniversityUnit universityUnit = new UniversityUnit();
	universityUnit.init(universityName, costCenterCode, universityAcronym, beginDate, endDate, webAddress, classification, canBeResponsibleOfSpaces);	
	universityUnit.addParentUnit(parentUnit, AccountabilityType.readAccountabilityTypeByType(AccountabilityTypeEnum.GEOGRAPHIC));
		
	checkIfAlreadyExistsOneUniversityWithSameAcronymAndName(universityUnit);
	
	return universityUnit;
    }
  
    @Override
    public void edit(String unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
            YearMonthDay endDate, String webAddress, UnitClassification classification,
            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces) {
     	
	super.edit(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department, degree, administrativeOffice, canBeResponsibleOfSpaces);
	
	checkIfAlreadyExistsOneUniversityWithSameAcronymAndName(this);	
    }
    
    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
	if (parentUnit != null && (!parentUnit.isOfficialExternal() ||
		(!parentUnit.isPlanetUnit() && !parentUnit.isCountryUnit()))) {
	    throw new DomainException("error.unit.invalid.parentUnit");
	}
	return super.addParentUnit(parentUnit, accountabilityType);
    }
    
    
    @Override
    public void setAcronym(String acronym) {
        if(StringUtils.isEmpty(acronym)) {
            throw new DomainException("error.unit.ampty.acronym");
        }
	super.setAcronym(acronym);
    }
    
    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }
    
    @Override
    public boolean isUniversityUnit() {
        return true;
    }
    
    @Override
    public List<ExternalCurricularCourse> getAllExternalCurricularCourses() {
	final List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>(getExternalCurricularCourses());	
	for (Unit subUnit : getSubUnits()) {
	    if(subUnit.isSchoolUnit() || subUnit.isDepartmentUnit()) {
		result.addAll(subUnit.getExternalCurricularCourses());
	    }
	}
	return result;
    }      
    
    private static void checkIfAlreadyExistsOneUniversityWithSameAcronymAndName(UniversityUnit universityUnit) {
	for (Unit parentUnit : universityUnit.getParentUnits()) {
	    for (Unit unit : parentUnit.getAllSubUnits()) {
		if (!unit.equals(universityUnit) && unit.isUniversityUnit() && (universityUnit.getAcronym().equalsIgnoreCase(unit.getAcronym()) || 
			universityUnit.getName().equalsIgnoreCase(unit.getName()))) {		
		    throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
		}
	    }
	}		
    }
}
