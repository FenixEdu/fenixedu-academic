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

public class SchoolUnit extends SchoolUnit_Base {
    
    private SchoolUnit() {
        super();
        super.setType(PartyTypeEnum.SCHOOL);
    }
    
    public static Unit createNewSchoolUnit(String schoolName, Integer costCenterCode, String schoolAcronym,
	    YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, String webAddress, 
	    UnitClassification classification, Boolean canBeResponsibleOfSpaces) {	
	
	SchoolUnit schoolUnit = new SchoolUnit();
	schoolUnit.init(schoolName, costCenterCode, schoolAcronym, beginDate, endDate, webAddress, classification, canBeResponsibleOfSpaces);
			
	if(parentUnit.isCountryUnit()) {
	    schoolUnit.addParentUnit(parentUnit, AccountabilityType.readAccountabilityTypeByType(AccountabilityTypeEnum.GEOGRAPHIC));
	} else if(parentUnit.isUniversityUnit()){
	    schoolUnit.addParentUnit(parentUnit, AccountabilityType.readAccountabilityTypeByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
	}
	
	checkIfAlreadyExistsOneSchoolWithSameAcronymAndName(schoolUnit);
	
	return schoolUnit;
    }
    
    @Override
    public void edit(String unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
            YearMonthDay endDate, String webAddress, UnitClassification classification,
            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces) {
	
        super.edit(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department, degree, administrativeOffice, canBeResponsibleOfSpaces);
        
        checkIfAlreadyExistsOneSchoolWithSameAcronymAndName(this);
    }
    
    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
	if (parentUnit != null && (!parentUnit.isOfficialExternal() ||
		((!parentUnit.isUniversityUnit() && !parentUnit.isCountryUnit())))) {
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
    public boolean isSchoolUnit() {
        return true;
    }
    
    @Override
    public List<ExternalCurricularCourse> getAllExternalCurricularCourses() {
	final List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>(getExternalCurricularCourses());	
	for (Unit subUnit : getSubUnits()) {
	    if(subUnit.isDepartmentUnit()) {
		result.addAll(subUnit.getExternalCurricularCourses());
	    }
	}
	return result;
    }
    
    private static void checkIfAlreadyExistsOneSchoolWithSameAcronymAndName(SchoolUnit schoolUnit) {
	for (Unit parentUnit : schoolUnit.getParentUnits()) {	    
	    for (Unit unit : parentUnit.getAllSubUnits()) {
		if (!unit.equals(schoolUnit) && unit.isSchoolUnit() && (schoolUnit.getAcronym().equalsIgnoreCase(unit.getAcronym()) || 
			schoolUnit.getName().equalsIgnoreCase(unit.getName()))) {		
		    throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
		}
	    }
	}		
    }
}
