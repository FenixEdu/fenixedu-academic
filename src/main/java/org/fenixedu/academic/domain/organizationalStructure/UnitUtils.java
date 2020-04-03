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
/*
 * Created on Feb 6, 2006
 *	by mrsp
 */
package org.fenixedu.academic.domain.organizationalStructure;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.YearMonthDay;

public class UnitUtils {

//    public static List<Unit> readAllExternalInstitutionUnits() {
//        List<Unit> allExternalUnits = new ArrayList<Unit>();
//        allExternalUnits.addAll(readExternalInstitutionUnit().getAllSubUnits());
//        return allExternalUnits;
//    }

    public static Unit readExternalInstitutionUnitByName(final String name) {
        return readExternalInstitutionUnitByName(readExternalInstitutionUnit(), name);
    }

    private static Unit readExternalInstitutionUnitByName(final Unit unit, final String name) {
        if (unit.getName().equals(name)) {
            return unit;
        }
        for (final Accountability acc : unit.getChildsSet()) {
            final Party child = acc.getChildParty();
            if (child instanceof Unit) {
                final Unit childUnit = (Unit) child;
                final Unit result = readExternalInstitutionUnitByName(childUnit, name);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

//    public static List<Unit> readAllUnitsWithoutParents() {
//        List<Unit> allUnitsWithoutParent = new ArrayList<Unit>();
//        for (Party party : Bennu.getInstance().getPartysSet()) {
//            if (party.isUnit()) {
//                Unit unit = (Unit) party;
//                if (unit.getParentUnits().isEmpty()) {
//                    allUnitsWithoutParent.add(unit);
//                }
//            }
//        }
//        return allUnitsWithoutParent;
//    }

//    public static List<Unit> readAllInternalActiveUnitsThatCanBeResponsibleOfSpaces() {
//        List<Unit> result = new ArrayList<Unit>();
//        final YearMonthDay now = new YearMonthDay();
//        Unit institutionUnit = readInstitutionUnit();
//        if (institutionUnit != null) {
//            if (institutionUnit.getCanBeResponsibleOfSpaces() && institutionUnit.isActive(now)) {
//                result.add(institutionUnit);
//            }
//            for (Unit subUnit : institutionUnit.getSubUnits()) {
//                if (subUnit.getCanBeResponsibleOfSpaces() && subUnit.isActive(now)) {
//                    result.add(subUnit);
//                }
//                readAllInternalActiveSubUnitsThatCanBeResponsibleOfSpaces(result, subUnit, now);
//            }
//        }
//        return result;
//    }
//
//    private static void readAllInternalActiveSubUnitsThatCanBeResponsibleOfSpaces(List<Unit> result, Unit subUnit,
//            YearMonthDay now) {
//        for (Unit unit : subUnit.getSubUnits()) {
//            if (unit.getCanBeResponsibleOfSpaces() && unit.isActive(now)) {
//                result.add(unit);
//            }
//            readAllInternalActiveSubUnitsThatCanBeResponsibleOfSpaces(result, unit, now);
//        }
//    }

    public static List<Unit> readAllActiveUnitsByType(PartyTypeEnum type) {
        final List<Unit> result = new ArrayList<Unit>();
        final YearMonthDay now = new YearMonthDay();
        PartyType partyType = PartyType.readPartyTypeByType(type);
        if (partyType != null) {
            Collection<Party> parties = partyType.getPartiesSet();
            for (Party party : parties) {
                if (party.isUnit()) {
                    Unit unit = (Unit) party;
                    if (unit.isActive(now)) {
                        result.add(unit);
                    }
                }
            }
        }
        return result;
    }

//    public static List<Unit> readAllActiveUnitsByClassification(UnitClassification unitClassification) {
//        final List<Unit> result = new ArrayList<Unit>();
//        final YearMonthDay now = new YearMonthDay();
//        if (unitClassification != null) {
//            for (Party party : Bennu.getInstance().getPartysSet()) {
//                if (party.isUnit()) {
//                    Unit unit = (Unit) party;
//                    if (unit.getClassification() != null && unit.getClassification().equals(unitClassification)
//                            && unit.isActive(now)) {
//                        result.add(unit);
//                    }
//                }
//            }
//        }
//        return result;
//    }

//    public static List<Unit> readAllDepartmentUnits() {
//        List<Unit> result = new ArrayList<Unit>();
//        List<Unit> readAllActiveUnitsByType = readAllActiveUnitsByType(PartyTypeEnum.DEPARTMENT);
//        for (Unit unit : readAllActiveUnitsByType) {
//            result.add((Unit) unit);
//        }
//        return result;
//    }

//    public static Unit readUnitWithoutParentstByAcronym(String acronym) {
//        for (Unit topUnit : readAllUnitsWithoutParents()) {
//            if (topUnit.getAcronym() != null && topUnit.getAcronym().equals(acronym)) {
//                return topUnit;
//            }
//        }
//        return null;
//    }

    public static Unit readExternalInstitutionUnit() {
        return Bennu.getInstance().getExternalInstitutionUnit();
    }

    public static Unit readInstitutionUnit() {
        return Bennu.getInstance().getInstitutionUnit();
    }

    public static Unit readEarthUnit() {
        return Bennu.getInstance().getEarthUnit();
    }

//    public static Set<Unit> readExternalUnitsByNameAndTypes(final String unitName, List<PartyTypeEnum> types) {
//        if (unitName == null) {
//            return Collections.emptySet();
//        }
//        final Collection<UnitName> units = UnitName.findExternalUnit(unitName.replace('%', ' '), Integer.MAX_VALUE);
//        final Set<Unit> result = new HashSet<Unit>();
//        for (final UnitName un : units) {
//            final Unit unit = un.getUnit();
//            if (types.contains(unit.getType())) {
//                result.add(unit);
//            }
//        }
//        return result;
//    }

    public static List<Unit> getUnitFullPath(final Unit unit, final List<AccountabilityTypeEnum> validAccountabilityTypes) {
        final Collection<Unit> parentUnits = unit.getParentUnits(validAccountabilityTypes);
        if (parentUnits.isEmpty()) {
            return Collections.emptyList();
        }
        if (parentUnits.size() == 1) {
            final List<Unit> result = new ArrayList<Unit>();
            result.add(unit);
            result.addAll(0, getUnitFullPath(parentUnits.iterator().next(), validAccountabilityTypes));
            return result;
        }
        throw new DomainException("error.unitUtils.unit.full.path.has.more.than.one.parent");
    }

    public static StringBuilder getUnitFullPathName(final Unit unit,
            final List<AccountabilityTypeEnum> validAccountabilityTypes) {
        if (unit == readEarthUnit()) {
            return new StringBuilder(0);
        }
        final Collection<Unit> parentUnits = unit.getParentUnits(validAccountabilityTypes);
        if (parentUnits.isEmpty()) {
            return new StringBuilder(unit.getName());
        }
        if (parentUnits.size() == 1) {
            final StringBuilder builder = new StringBuilder();
            builder.append(parentUnits.iterator().next() == readEarthUnit() ? "" : " > ").append(unit.getName());
            builder.insert(0, getUnitFullPathName(parentUnits.iterator().next(), validAccountabilityTypes));
            return builder;
        }
        throw new DomainException("error.unitUtils.unit.full.path.has.more.than.one.parent");
    }

//    public static List<Unit> readExternalUnitsByNameAndTypesStartingAtEarth(final String unitName,
//            final List<PartyTypeEnum> types) {
//        if (unitName == null) {
//            return Collections.emptyList();
//        }
//
//        final String nameToSearch = Normalizer.normalize(unitName.replaceAll("%", ".*").toLowerCase(), Normalizer.Form.NFD)
//                .replaceAll("\\s", " ").replaceAll("[^\\p{ASCII}]", "");
//
//        final List<Unit> result = new ArrayList<Unit>();
//        for (final UnitName name : Bennu.getInstance().getUnitNameSet()) {
//
//            final String current = Normalizer.normalize(name.getName().toLowerCase(), Normalizer.Form.NFD).replaceAll("\\s", " ")
//                    .replaceAll("[^\\p{ASCII}]", "");
//
//            if (current.matches(nameToSearch) && name.getIsExternalUnit() && types.contains(name.getUnit().getType())) {
//                result.add(name.getUnit());
//            }
//        }
//
//        return result;
//    }

//    public static Collection<Unit> readAllUnitsWithClassification(UnitClassification classification) {
//        List<Unit> result = new ArrayList<Unit>();
//
//        for (Party party : Bennu.getInstance().getPartysSet()) {
//            if (party.isUnit()) {
//                Unit unit = (Unit) party;
//
//                UnitClassification unitClassification = unit.getClassification();
//                if (unitClassification != null && unitClassification.equals(classification)) {
//                    result.add(unit);
//                }
//            }
//        }
//
//        return result;
//    }
}
