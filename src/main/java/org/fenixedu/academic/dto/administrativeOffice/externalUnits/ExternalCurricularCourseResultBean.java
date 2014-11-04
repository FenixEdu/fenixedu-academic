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
package org.fenixedu.academic.dto.administrativeOffice.externalUnits;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExternalCurricularCourse;
import org.fenixedu.academic.domain.organizationalStructure.PartyTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.dto.LinkObject;

public class ExternalCurricularCourseResultBean extends AbstractExternalUnitResultBean {

    private ExternalCurricularCourse externalCurricularCourse;

    public ExternalCurricularCourseResultBean(final ExternalCurricularCourse externalCurricularCourse,
            PartyTypeEnum parentUnitType) {
        super();
        setExternalCurricularCourse(externalCurricularCourse);
        setParentUnitType(parentUnitType);
    }

    public ExternalCurricularCourseResultBean(final ExternalCurricularCourse externalCurricularCourse) {
        this(externalCurricularCourse, null);
    }

    public ExternalCurricularCourse getExternalCurricularCourse() {
        return this.externalCurricularCourse;
    }

    public void setExternalCurricularCourse(ExternalCurricularCourse externalCurricularCourse) {
        this.externalCurricularCourse = externalCurricularCourse;
    }

    @Override
    public Unit getUnit() {
        return getExternalCurricularCourse().getUnit();
    }

    @Override
    public ExternalDegreeModuleType getType() {
        return ExternalDegreeModuleType.CURRICULAR_COURSE;
    }

    public int getNumberOfExternalEnrolments() {
        return getExternalCurricularCourse().getExternalEnrolmentsSet().size();
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

        final LinkObject linkObject = new LinkObject();
        linkObject.setId(getExternalCurricularCourse().getExternalId());
        linkObject.setLabel(getExternalCurricularCourse().getName());
        linkObject.setMethod("viewExternalCurricularCourse");
        result.add(linkObject);

        return result;
    }

    @Override
    public String getName() {
        return getExternalCurricularCourse().getName();
    }

    private static enum ExternalDegreeModuleType {
        CURRICULAR_COURSE;
        public String getName() {
            return this.name();
        }
    }

    static public List<ExternalCurricularCourseResultBean> buildFrom(final Unit unit) {
        final List<ExternalCurricularCourseResultBean> result = new ArrayList<ExternalCurricularCourseResultBean>();
        for (final ExternalCurricularCourse each : getChildExternalCurricularCoursesFor(unit)) {
            result.add(new ExternalCurricularCourseResultBean(each, unit.getType()));
        }
        return result;
    }

    static private List<ExternalCurricularCourse> getChildExternalCurricularCoursesFor(final Unit unit) {
        final List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>();
        getChildsWithType(result, unit);
        return result;
    }

    static private void getChildsWithType(final List<ExternalCurricularCourse> result, final Unit unit) {

        result.addAll(unit.getExternalCurricularCoursesSet());

        switch (unit.getType()) {
        case COUNTRY:
            addChildExternalCurricularCourses(result, unit, PartyTypeEnum.UNIVERSITY);
            addChildExternalCurricularCourses(result, unit, PartyTypeEnum.SCHOOL);
            break;
        case UNIVERSITY:
            addChildExternalCurricularCourses(result, unit, PartyTypeEnum.SCHOOL);
            addChildExternalCurricularCourses(result, unit, PartyTypeEnum.DEPARTMENT);
            break;
        case SCHOOL:
            addChildExternalCurricularCourses(result, unit, PartyTypeEnum.DEPARTMENT);
            break;
        case DEPARTMENT:
        default:
            break;
        }
    }

    static private void addChildExternalCurricularCourses(final List<ExternalCurricularCourse> result, final Unit unit,
            final PartyTypeEnum parentUnitType) {

        for (final Unit each : unit.getSubUnits(parentUnitType)) {
            getChildsWithType(result, each);
        }
    }

}
