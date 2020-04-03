/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.service.services.manager.organizationalStructureManagement;

public class CreateUnit {

//    @Atomic
//    public static Unit run(Unit parentUnit, LocalizedString unitName, String unitNameCard, String unitCostCenter, String acronym,
//            YearMonthDay begin, YearMonthDay end, PartyTypeEnum type, String departmentID, String degreeID,
//            String administrativeOfficeID, AccountabilityType accountabilityType, String webAddress,
//            UnitClassification classification, Boolean canBeResponsibleOfSpaces, String campusID) throws FenixServiceException {
//
//        ServiceMonitoring.logService(CreateUnit.class, parentUnit, unitName, unitNameCard, unitCostCenter, acronym, begin, end,
//                type, departmentID, degreeID, administrativeOfficeID, accountabilityType, webAddress, classification,
//                canBeResponsibleOfSpaces, campusID);
//
//        Integer costCenterCode = getCostCenterCode(unitCostCenter);
//        Space campus = (Space) FenixFramework.getDomainObject(campusID);
//
//        if (type != null) {
//
//            switch (type) {
//
//            case DEPARTMENT:
//                Department department = FenixFramework.getDomainObject(departmentID);
//                DepartmentUnit departmentUnit =
//                        DepartmentUnit.createNewDepartmentUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end,
//                                parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);
//                departmentUnit.setDepartment(department);
//                return departmentUnit;
//
//            case DEGREE_UNIT:
//                Degree degree = FenixFramework.getDomainObject(degreeID);
//                return DegreeUnit.createNewInternalDegreeUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end,
//                        parentUnit, accountabilityType, webAddress, degree, classification, canBeResponsibleOfSpaces, campus);
//
//            case PLANET:
//                return PlanetUnit.createNewPlanetUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end, parentUnit,
//                        webAddress, classification, canBeResponsibleOfSpaces, campus);
//
//            case COUNTRY:
//                return CountryUnit.createNewCountryUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end, parentUnit,
//                        webAddress, classification, canBeResponsibleOfSpaces, campus);
//
//            case SCHOOL:
//                return SchoolUnit.createNewSchoolUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end, parentUnit,
//                        webAddress, classification, canBeResponsibleOfSpaces, campus);
//
//            case UNIVERSITY:
//                return UniversityUnit.createNewUniversityUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end,
//                        parentUnit, webAddress, classification, canBeResponsibleOfSpaces, campus);
//
//            case ADMINISTRATIVE_OFFICE_UNIT:
//                AdministrativeOffice office = FenixFramework.getDomainObject(administrativeOfficeID);
//                Unit unit = Unit.createNewUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end, parentUnit,
//                        accountabilityType, webAddress, classification, office, canBeResponsibleOfSpaces, campus);
//                unit.setType(type);
//                return unit;
//            case AGGREGATE_UNIT:
//                return AggregateUnit.createNewAggregateUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end,
//                        parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);
//
//            case COMPETENCE_COURSE_GROUP:
//                return CompetenceCourseGroupUnit.createNewInternalCompetenceCourseGroupUnit(unitName, unitNameCard,
//                        costCenterCode, acronym, begin, end, parentUnit, accountabilityType, webAddress, classification,
//                        canBeResponsibleOfSpaces, campus);
//
//            case SCIENTIFIC_AREA:
//                return ScientificAreaUnit.createNewInternalScientificArea(unitName, unitNameCard, costCenterCode, acronym, begin,
//                        end, parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);
//
////            case SECTION:
////                return SectionUnit.createNewSectionUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end, parentUnit,
////                        accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);
////
////            case RESEARCH_UNIT:
////                return ResearchUnit.createNewResearchUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end,
////                        parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);
//
////            case MANAGEMENT_COUNCIL:
////                return ManagementCouncilUnit.createManagementCouncilUnit(unitName, unitNameCard, costCenterCode, acronym, begin,
////                        end, parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);
//
////            case SCIENTIFIC_COUNCIL:
////                return ScientificCouncilUnit.createScientificCouncilUnit(unitName, unitNameCard, costCenterCode, acronym, begin,
////                        end, parentUnit, accountabilityType, webAddress, classification, canBeResponsibleOfSpaces, campus);
//            default:
//                return Unit.createNewUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end, parentUnit,
//                        accountabilityType, webAddress, classification, null, canBeResponsibleOfSpaces, campus);
//            }
//
//        } else {
//            return Unit.createNewUnit(unitName, unitNameCard, costCenterCode, acronym, begin, end, parentUnit, accountabilityType,
//                    webAddress, classification, null, canBeResponsibleOfSpaces, campus);
//        }
//
//    }
//
//    private static Integer getCostCenterCode(String unitCostCenter) {
//        Integer costCenterCode = null;
//        if (unitCostCenter != null && !unitCostCenter.equals("")) {
//            costCenterCode = (Integer.valueOf(unitCostCenter));
//        }
//        return costCenterCode;
//    }
}