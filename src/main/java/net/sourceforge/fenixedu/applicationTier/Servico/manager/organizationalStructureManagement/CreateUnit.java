package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AggregateUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.DegreeUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ManagementCouncilUnit;
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

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateUnit {

    @Service
    public static Unit run(Unit parentUnit, MultiLanguageString unitName, String unitNameCard, String unitCostCenter,
            String acronym, YearMonthDay begin, YearMonthDay end, PartyTypeEnum type, String departmentID, String degreeID,
            String administrativeOfficeID, AccountabilityType accountabilityType, String webAddress,
            UnitClassification classification, Boolean canBeResponsibleOfSpaces, String campusID) throws FenixServiceException {

        ServiceMonitoring.logService(CreateUnit.class, parentUnit, unitName, unitNameCard, unitCostCenter, acronym, begin, end,
                type, departmentID, degreeID, administrativeOfficeID, accountabilityType, webAddress, classification,
                canBeResponsibleOfSpaces, campusID);

        Integer costCenterCode = getCostCenterCode(unitCostCenter);
        Campus campus = (Campus) AbstractDomainObject.fromExternalId(campusID);

        if (type != null) {

            switch (type) {

            case DEPARTMENT:
                Department department = AbstractDomainObject.fromExternalId(departmentID);
                return DepartmentUnit.createNewInternalDepartmentUnit(unitName, unitNameCard, costCenterCode, acronym, begin,
                        end, parentUnit, accountabilityType, webAddress, department, classification, canBeResponsibleOfSpaces,
                        campus);

            case DEGREE_UNIT:
                Degree degree = AbstractDomainObject.fromExternalId(degreeID);
                return DegreeUnit.createNewInternalDegreeUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end,
                        parentUnit, accountabilityType, webAddress, degree, classification, canBeResponsibleOfSpaces, campus);

            case PLANET:
                return PlanetUnit.createNewPlanetUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end, parentUnit,
                        webAddress, classification, canBeResponsibleOfSpaces, campus);

            case COUNTRY:
                return CountryUnit.createNewCountryUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end, parentUnit,
                        webAddress, classification, canBeResponsibleOfSpaces, campus);

            case SCHOOL:
                return SchoolUnit.createNewSchoolUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end, parentUnit,
                        webAddress, classification, canBeResponsibleOfSpaces, campus);

            case UNIVERSITY:
                return UniversityUnit.createNewUniversityUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end,
                        parentUnit, webAddress, classification, canBeResponsibleOfSpaces, campus);

            case ADMINISTRATIVE_OFFICE_UNIT:
                AdministrativeOffice office = null;
                if (administrativeOfficeID == null) {
                    office = new AdministrativeOffice();
                } else {
                    office = AbstractDomainObject.fromExternalId(administrativeOfficeID);
                }
                Unit unit =
                        Unit.createNewUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end, parentUnit,
                                accountabilityType, webAddress, classification, office, canBeResponsibleOfSpaces, campus);
                unit.setType(type);
                return unit;
            case AGGREGATE_UNIT:
                return AggregateUnit.createNewAggregateUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end,
                        parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);

            case COMPETENCE_COURSE_GROUP:
                return CompetenceCourseGroupUnit.createNewInternalCompetenceCourseGroupUnit(unitName, unitNameCard,
                        costCenterCode, acronym, begin, end, parentUnit, accountabilityType, webAddress, classification,
                        canBeResponsibleOfSpaces, campus);

            case SCIENTIFIC_AREA:
                return ScientificAreaUnit.createNewInternalScientificArea(unitName, unitNameCard, costCenterCode, acronym, begin,
                        end, parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);

            case SECTION:
                return SectionUnit.createNewSectionUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end, parentUnit,
                        accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);

            case RESEARCH_UNIT:
                return ResearchUnit.createNewResearchUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end,
                        parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);

            case MANAGEMENT_COUNCIL:
                return ManagementCouncilUnit.createManagementCouncilUnit(unitName, unitNameCard, costCenterCode, acronym, begin,
                        end, parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);
            }

        } else {
            return Unit.createNewUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end, parentUnit,
                    accountabilityType, webAddress, classification, null, canBeResponsibleOfSpaces, campus);
        }

        throw new FenixServiceException("createUnit.service.empty.unit.type");
    }

    private static Integer getCostCenterCode(String unitCostCenter) {
        Integer costCenterCode = null;
        if (unitCostCenter != null && !unitCostCenter.equals("")) {
            costCenterCode = (Integer.valueOf(unitCostCenter));
        }
        return costCenterCode;
    }
}