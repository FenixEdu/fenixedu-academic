package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
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
import net.sourceforge.fenixedu.domain.space.Campus;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateUnit extends Service {

    public Unit run(Unit parentUnit, MultiLanguageString unitName, String unitCostCenter, String acronym, YearMonthDay begin,
	    YearMonthDay end, PartyTypeEnum type, Integer departmentID, Integer degreeID, Integer administrativeOfficeID,
	    AccountabilityType accountabilityType, String webAddress, UnitClassification classification,
	    Boolean canBeResponsibleOfSpaces, Integer campusID) throws FenixServiceException {

	Integer costCenterCode = getCostCenterCode(unitCostCenter);
	Campus campus = (Campus) rootDomainObject.readResourceByOID(campusID);

	if (type != null) {

	    switch (type) {

	    case DEPARTMENT:
		Department department = rootDomainObject.readDepartmentByOID(departmentID);
		return DepartmentUnit.createNewInternalDepartmentUnit(unitName, costCenterCode, acronym, begin, end, parentUnit,
			accountabilityType, webAddress, department, classification, canBeResponsibleOfSpaces, campus);

	    case DEGREE_UNIT:
		Degree degree = rootDomainObject.readDegreeByOID(degreeID);
		return DegreeUnit.createNewInternalDegreeUnit(unitName, costCenterCode, acronym, begin, end, parentUnit,
			accountabilityType, webAddress, degree, classification, canBeResponsibleOfSpaces, campus);

	    case PLANET:
		return PlanetUnit.createNewPlanetUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, webAddress,
			classification, canBeResponsibleOfSpaces, campus);

	    case COUNTRY:
		return CountryUnit.createNewCountryUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, webAddress,
			classification, canBeResponsibleOfSpaces, campus);

	    case SCHOOL:
		return SchoolUnit.createNewSchoolUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, webAddress,
			classification, canBeResponsibleOfSpaces, campus);

	    case UNIVERSITY:
		return UniversityUnit.createNewUniversityUnit(unitName, costCenterCode, acronym, begin, end, parentUnit,
			webAddress, classification, canBeResponsibleOfSpaces, campus);

	    case ADMINISTRATIVE_OFFICE_UNIT:
		AdministrativeOffice administrativeOffice = rootDomainObject
			.readAdministrativeOfficeByOID(administrativeOfficeID);
		return AdministrativeOfficeUnit.createNewAdministrativeOfficeUnit(unitName, costCenterCode, acronym, begin, end,
			parentUnit, accountabilityType, webAddress, classification, administrativeOffice,
			canBeResponsibleOfSpaces, campus);

	    case AGGREGATE_UNIT:
		return AggregateUnit.createNewAggregateUnit(unitName, costCenterCode, acronym, begin, end, parentUnit,
			accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);

	    case COMPETENCE_COURSE_GROUP:
		return CompetenceCourseGroupUnit.createNewInternalCompetenceCourseGroupUnit(unitName, costCenterCode, acronym,
			begin, end, parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);

	    case SCIENTIFIC_AREA:
		return ScientificAreaUnit.createNewInternalScientificArea(unitName, costCenterCode, acronym, begin, end,
			parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);

	    case SECTION:
		return SectionUnit.createNewSectionUnit(unitName, costCenterCode, acronym, begin, end, parentUnit,
			accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);

	    case RESEARCH_UNIT:
		return ResearchUnit.createNewResearchUnit(unitName, costCenterCode, acronym, begin, end, parentUnit,
			accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);
	    }

	} else {
	    return Unit.createNewUnit(unitName, costCenterCode, acronym, begin, end, parentUnit, accountabilityType, webAddress,
		    classification, canBeResponsibleOfSpaces, campus);
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
