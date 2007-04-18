package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AdministrativeOfficeUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.AggregateUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.DegreeUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.PlanetUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.SchoolUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.SectionUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitClassification;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.YearMonthDay;

public class CreateUnit extends Service {
    
    public Unit run(Unit parentUnit, String unitName, String unitCostCenter,
            String acronym, YearMonthDay begin, YearMonthDay end, PartyTypeEnum type, Integer departmentID,
            Integer degreeID, Integer administrativeOfficeID, AccountabilityType accountabilityType, String webAddress, 
            UnitClassification classification, Boolean canBeResponsibleOfSpaces)
            throws ExcepcaoPersistencia, FenixServiceException, DomainException, FenixFilterException {
                
        Integer costCenterCode = getCostCenterCode(unitCostCenter);
 
        if(type != null) {
            
	    switch (type) {
	    
	    case DEPARTMENT:
		Department department = rootDomainObject.readDepartmentByOID(departmentID);
		return DepartmentUnit.createNewInternalDepartmentUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, accountabilityType, webAddress, department, classification, canBeResponsibleOfSpaces);	
			    
	    case DEGREE_UNIT:
		Degree degree = rootDomainObject.readDegreeByOID(degreeID);
		return DegreeUnit.createNewInternalDegreeUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, accountabilityType, webAddress, degree, classification, canBeResponsibleOfSpaces);
			
	    case PLANET:
		return PlanetUnit.createNewPlanetUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, webAddress, classification, canBeResponsibleOfSpaces);
				
	    case COUNTRY:	
		return CountryUnit.createNewCountryUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, webAddress, classification, canBeResponsibleOfSpaces);
			
	    case SCHOOL:
		return SchoolUnit.createNewSchoolUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, webAddress, classification, canBeResponsibleOfSpaces);		
		
	    case UNIVERSITY:
		return UniversityUnit.createNewUniversityUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, webAddress, classification, canBeResponsibleOfSpaces);
			
	    case ADMINISTRATIVE_OFFICE_UNIT:
		AdministrativeOffice administrativeOffice = rootDomainObject.readAdministrativeOfficeByOID(administrativeOfficeID);
		return AdministrativeOfficeUnit.createNewAdministrativeOfficeUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, accountabilityType, webAddress, classification, administrativeOffice, canBeResponsibleOfSpaces);	
		
	    case AGGREGATE_UNIT:
		return AggregateUnit.createNewAggregateUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces);	
	
	    case COMPETENCE_COURSE_GROUP:
		return CompetenceCourseGroupUnit.createNewInternalCompetenceCourseGroupUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces);
				
	    case SCIENTIFIC_AREA:
		return ScientificAreaUnit.createNewInternalScientificArea(unitName, costCenterCode, acronym, begin, end, parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces);
				
	    case SECTION:
		return SectionUnit.createNewSectionUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces);			    
	    
	    case RESEARCH_UNIT:
		return ResearchUnit.createNewResearchUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces);
	    }
	    
	} else {
	    return Unit.createNewUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces);
	}
        
        throw new FenixServiceException("createUnit.service.empty.unit.type");
    }
          
    private Integer getCostCenterCode(String unitCostCenter) {
        Integer costCenterCode = null;
        if (unitCostCenter != null && !unitCostCenter.equals("")) {
            costCenterCode = (Integer.valueOf(unitCostCenter));
        }
        return costCenterCode;
    }    
}
