/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.LinkObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class ExternalUnitResultBean extends AbstractExternalUnitResultBean {

    private Unit unit;

    public ExternalUnitResultBean(final Unit unit, final PartyTypeEnum parentUnitType) {
        super();
        setUnit(unit);
        setParentUnitType(parentUnitType);
    }

    public ExternalUnitResultBean(final Unit unit) {
        this(unit, null);
    }

    @Override
    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public PartyTypeEnum getType() {
        return getUnit().getType();
    }

    @Override
    public List<LinkObject> getFullPath() {
        final List<LinkObject> result = new ArrayList<LinkObject>();
        for (final Unit unit : searchFullPath()) {
            final LinkObject linkObject = new LinkObject();
            linkObject.setId(unit.getExternalId());
            linkObject.setLabel(unit.getName());
            linkObject.setMethod("viewUnit");
            result.add(linkObject);
        }
        return result;
    }

    @Override
    public String getName() {
        return getUnit().getName();
    }

    @Override
    public String getNumberOfUniversities() {
        return getUnit().isCountryUnit() ? String.valueOf(getUnit().getSubUnits(PartyTypeEnum.UNIVERSITY).size()) : super
                .getNumberOfUniversities();
    }

    @Override
    public String getNumberOfSchools() {
        return (getUnit().isCountryUnit() || getUnit().isUniversityUnit()) ? String.valueOf(countNumberOfUnitsWithType(getUnit(),
                PartyTypeEnum.SCHOOL)) : super.getNumberOfSchools();
    }

    @Override
    public String getNumberOfDepartments() {
        return (getUnit().isUniversityUnit() || getUnit().isSchoolUnit()) ? String.valueOf(countNumberOfUnitsWithType(getUnit(),
                PartyTypeEnum.DEPARTMENT)) : super.getNumberOfDepartments();
    }

    private int countNumberOfUnitsWithType(final Unit unit, final PartyTypeEnum unitTypeToSearch) {

        int result = unit.getSubUnits(unitTypeToSearch).size();

        switch (unit.getType()) {
        case COUNTRY:
            result += countNumberOfUnitsWithType(unit, PartyTypeEnum.UNIVERSITY, unitTypeToSearch);
            result += countNumberOfUnitsWithType(unit, PartyTypeEnum.SCHOOL, unitTypeToSearch);
            break;
        case UNIVERSITY:
            result += countNumberOfUnitsWithType(unit, PartyTypeEnum.SCHOOL, unitTypeToSearch);
            result += countNumberOfUnitsWithType(unit, PartyTypeEnum.DEPARTMENT, unitTypeToSearch);
            break;
        case SCHOOL:
            result += countNumberOfUnitsWithType(unit, PartyTypeEnum.DEPARTMENT, unitTypeToSearch);
            break;
        case DEPARTMENT:
        default:
            break;
        }
        return result;
    }

    private int countNumberOfUnitsWithType(final Unit unit, final PartyTypeEnum parentUnitType,
            final PartyTypeEnum unitTypeToSearch) {
        int result = 0;
        if (unitTypeToSearch != parentUnitType) {
            for (final Unit each : unit.getSubUnits(parentUnitType)) {
                result += countNumberOfUnitsWithType(each, unitTypeToSearch);
            }
        }
        return result;
    }

    @Override
    public String getNumberOfExternalCurricularCourses() {
        return !getUnit().isCountryUnit() ? String.valueOf(countNumberOfExternalCurricularCourses(getUnit())) : super
                .getNumberOfExternalCurricularCourses();
    }

    private int countNumberOfExternalCurricularCourses(final Unit unit) {

        int result = unit.getExternalCurricularCourses().size();

        switch (unit.getType()) {
        case COUNTRY:
            result += countNumberOfExternalCurricularCourses(unit, PartyTypeEnum.UNIVERSITY);
            result += countNumberOfExternalCurricularCourses(unit, PartyTypeEnum.SCHOOL);
            break;
        case UNIVERSITY:
            result += countNumberOfExternalCurricularCourses(unit, PartyTypeEnum.SCHOOL);
            result += countNumberOfExternalCurricularCourses(unit, PartyTypeEnum.DEPARTMENT);
            break;
        case SCHOOL:
            result += countNumberOfExternalCurricularCourses(unit, PartyTypeEnum.DEPARTMENT);
            break;
        case DEPARTMENT:
        default:
            break;
        }
        return result;
    }

    private int countNumberOfExternalCurricularCourses(final Unit unit, final PartyTypeEnum parentUnitType) {
        int result = 0;
        for (final Unit each : unit.getSubUnits(parentUnitType)) {
            result += countNumberOfExternalCurricularCourses(each);
        }
        return result;
    }

    static private List<Unit> getChildUnitsFor(final Unit unit, final PartyTypeEnum type) {
        final List<Unit> result = new ArrayList<Unit>();
        getChildsWithType(result, unit, type);
        return result;
    }

    static private void getChildsWithType(final List<Unit> result, final Unit unit, final PartyTypeEnum subUnitTypeToSearch) {

        result.addAll(unit.getSubUnits(subUnitTypeToSearch));

        switch (unit.getType()) {
        case COUNTRY:
            addSubUnits(result, unit, PartyTypeEnum.UNIVERSITY, subUnitTypeToSearch);
            addSubUnits(result, unit, PartyTypeEnum.SCHOOL, subUnitTypeToSearch);
            break;
        case UNIVERSITY:
            addSubUnits(result, unit, PartyTypeEnum.SCHOOL, subUnitTypeToSearch);
            addSubUnits(result, unit, PartyTypeEnum.DEPARTMENT, subUnitTypeToSearch);
            break;
        case SCHOOL:
            addSubUnits(result, unit, PartyTypeEnum.DEPARTMENT, subUnitTypeToSearch);
            break;
        case DEPARTMENT:
        default:
            break;
        }
    }

    static private void addSubUnits(final List<Unit> result, final Unit unit, final PartyTypeEnum parentUnitType,
            final PartyTypeEnum subUnitType) {
        if (subUnitType != parentUnitType) {
            for (final Unit each : unit.getSubUnits(parentUnitType)) {
                getChildsWithType(result, each, subUnitType);
            }
        }
    }

    static public List<ExternalUnitResultBean> buildFrom(final Unit unit, final PartyTypeEnum type) {
        final List<ExternalUnitResultBean> result = new ArrayList<ExternalUnitResultBean>();
        for (final Unit each : getChildUnitsFor(unit, type)) {
            result.add(new ExternalUnitResultBean(each, unit.getType()));
        }
        return result;
    }
}
