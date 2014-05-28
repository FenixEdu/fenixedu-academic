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
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.SchoolUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class ExternalCurricularCourse extends ExternalCurricularCourse_Base {

    static {
        getRelationExternalCurricularCourseUnit().addListener(new RelationAdapter<Unit, ExternalCurricularCourse>() {
            @Override
            public void beforeAdd(Unit unit, ExternalCurricularCourse externalCurricularCourse) {
                if (unit != null) {
                    if (!unit.isUniversityUnit() && !unit.isSchoolUnit() && !unit.isDepartmentUnit()) {
                        throw new DomainException("error.extraCurricularCourse.invalid.unit.type");
                    }
                }
            }
        });
    }

    public ExternalCurricularCourse(final Unit unit, final String name, final String code) {
        super();
        if (unit == null) {
            throw new DomainException("error.externalCurricularCourse.unit.cannot.be.null");
        }
        if (StringUtils.isEmpty(name)) {
            throw new DomainException("error.externalCurricularCourse.name.cannot.be.empty");
        }

        checkForExternalCurricularCourseWithSameNameAndCode(unit, name, code);

        setRootDomainObject(Bennu.getInstance());
        setUnit(unit);
        setName(name);
        setCode(code);
    }

    public void edit(final String name, final String code) {
        if (StringUtils.isEmpty(name)) {
            throw new DomainException("error.externalCurricularCourse.name.cannot.be.empty");
        }
        checkForExternalCurricularCourseWithSameNameAndCode(getUnit(), name, code);
        setName(name);
        setCode(code);
    }

    private void checkForExternalCurricularCourseWithSameNameAndCode(final Unit unit, final String name, final String code) {
        final String nameToSearch = name.toLowerCase();
        for (final ExternalCurricularCourse externalCurricularCourse : unit.getExternalCurricularCourses()) {
            if (externalCurricularCourse.getName().toLowerCase().equals(nameToSearch)) {
                if ((externalCurricularCourse.getCode() != null && externalCurricularCourse.getCode().equals(code))
                        || externalCurricularCourse.getCode() == null && code == null) {
                    throw new DomainException(
                            "error.externalCurricularCourse.parent.unit.already.has.externalCurricularCourse.with.same.type");
                }
            }
        }
    }

    public void delete() {
        if (canBeDeleted()) {
            setRootDomainObject(null);
            setUnit(null);
            super.deleteDomainObject();
        } else {
            throw new DomainException("error.external.enrolment.cannot.be.deleted");
        }
    }

    private boolean canBeDeleted() {
        return !hasAnyExternalEnrolments();
    }

    public String getFullPathName() {
        final List<AccountabilityTypeEnum> validAccountabilityTypes =
                Arrays.asList(new AccountabilityTypeEnum[] { AccountabilityTypeEnum.GEOGRAPHIC,
                        AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE, AccountabilityTypeEnum.ACADEMIC_STRUCTURE });
        return UnitUtils.getUnitFullPathName(getUnit(), validAccountabilityTypes).toString() + " > " + getName();
    }

    final public Unit getAcademicUnit() {
        final List<AccountabilityTypeEnum> validAccountabilityTypes =
                Arrays.asList(new AccountabilityTypeEnum[] { AccountabilityTypeEnum.GEOGRAPHIC,
                        AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE, AccountabilityTypeEnum.ACADEMIC_STRUCTURE });

        UniversityUnit universityUnit = null;
        SchoolUnit schoolUnit = null;
        for (final Unit unit : UnitUtils.getUnitFullPath(getUnit(), validAccountabilityTypes)) {
            if (unit.isUniversityUnit()) {
                universityUnit = (UniversityUnit) unit;
            } else if (unit.isSchoolUnit()) {
                schoolUnit = (SchoolUnit) unit;
            }
        }

        return (schoolUnit != null) ? schoolUnit : universityUnit;
    }

    static public ExternalCurricularCourse readExternalCurricularCourse(Unit unit, String name, String code) {
        for (final ExternalCurricularCourse externalCurricularCourse : unit.getExternalCurricularCourses()) {
            if (externalCurricularCourse.getCode().equals(code) && externalCurricularCourse.getName().equals(name)) {
                return externalCurricularCourse;
            }
        }
        return null;
    }

    static public ExternalCurricularCourse readExternalCurricularCourse(Unit unit, String code) {
        List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>();
        for (final ExternalCurricularCourse externalCurricularCourse : unit.getExternalCurricularCourses()) {
            if (StringUtils.isEmpty(externalCurricularCourse.getCode()) && StringUtils.isEmpty(code)
                    || externalCurricularCourse.getCode().equals(code)) {
                result.add(externalCurricularCourse);
            }
        }
        if (result.size() == 1) {
            return result.iterator().next();
        } else if (result.size() == 0) {
            return null;
        }
        throw new DomainException("error.externalCurricularCourse.manyFoundWithSameCode");
    }

    static public List<ExternalCurricularCourse> readExternalCurricularCoursesByCode(String code) {
        List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>();
        for (final ExternalCurricularCourse externalCurricularCourse : Bennu.getInstance().getExternalCurricularCoursesSet()) {
            if (StringUtils.isEmpty(externalCurricularCourse.getCode()) && StringUtils.isEmpty(code)
                    || externalCurricularCourse.getCode().equals(code)) {
                result.add(externalCurricularCourse);
            }
        }
        return result;
    }

    static public List<ExternalCurricularCourse> readByName(final String name) {
        if (name == null) {
            return Collections.emptyList();
        }
        final String nameToMatch = name.replaceAll("%", ".*").toLowerCase();
        final List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>();
        for (final ExternalCurricularCourse externalCurricularCourse : Bennu.getInstance().getExternalCurricularCoursesSet()) {
            if (externalCurricularCourse.getName().toLowerCase().matches(nameToMatch)) {
                result.add(externalCurricularCourse);
            }
        }
        return result;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment> getExternalEnrolments() {
        return getExternalEnrolmentsSet();
    }

    @Deprecated
    public boolean hasAnyExternalEnrolments() {
        return !getExternalEnrolmentsSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCode() {
        return getCode() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

}
