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

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

public class CourseLoad extends CourseLoad_Base {

    public CourseLoad(ExecutionCourse executionCourse, ShiftType type, BigDecimal unitQuantity, BigDecimal totalQuantity) {

        super();

        if (executionCourse != null && type != null && executionCourse.getCourseLoadByShiftType(type) != null) {
            throw new DomainException("error.CourseLoad.executionCourse.already.contains.type");
        }

        setRootDomainObject(Bennu.getInstance());
        setUnitQuantity(unitQuantity);
        setTotalQuantity(totalQuantity);
        setExecutionCourse(executionCourse);
        setType(type);

        checkQuantities();
    }

    public void edit(BigDecimal unitQuantity, BigDecimal totalQuantity) {
        setUnitQuantity(unitQuantity);
        setTotalQuantity(totalQuantity);

        checkQuantities();
    }

    public void delete() {
        if (!canBeDeleted()) {
            throw new DomainException("error.CourseLoad.cannot.be.deleted");
        }
        super.setExecutionCourse(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Override
    public void setExecutionCourse(ExecutionCourse executionCourse) {
        if (executionCourse == null) {
            throw new DomainException("error.CourseLoad.empty.executionCourse");
        }
        super.setExecutionCourse(executionCourse);
    }

    @Override
    public void setType(ShiftType type) {
        if (type == null) {
            throw new DomainException("error.CourseLoad.empty.type");
        }
        super.setType(type);
    }

    public boolean isEmpty() {
        return getTotalQuantity().signum() == 0;
    }

    private boolean canBeDeleted() {
        return !hasAnyLessonInstances() && !hasAnyShifts();
    }

    public BigDecimal getWeeklyHours() {
        return getTotalQuantity().divide(BigDecimal.valueOf(CompetenceCourseLoad.NUMBER_OF_WEEKS), 2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public void setTotalQuantity(BigDecimal totalQuantity) {
        if (totalQuantity == null || totalQuantity.signum() == -1) {
            throw new DomainException("error.CourseLoad.empty.totalQuantity");
        }
        super.setTotalQuantity(totalQuantity);
    }

    @Override
    public void setUnitQuantity(BigDecimal unitQuantity) {
        if (unitQuantity != null && unitQuantity.signum() == -1) {
            throw new DomainException("error.CourseLoad.empty.unitQuantity");
        }
        super.setUnitQuantity(unitQuantity);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
        return !isEmpty() && isTotalQuantityValid() && getType() != null;
    }

    private void checkQuantities() {
        if (!isTotalQuantityValid()) {
            throw new DomainException("error.CourseLoad.totalQuantity.less.than.unitQuantity");
        }
    }

    private boolean isTotalQuantityValid() {
        return getUnitQuantity() == null || getTotalQuantity().compareTo(getUnitQuantity()) != -1;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.LessonInstance> getLessonInstances() {
        return getLessonInstancesSet();
    }

    @Deprecated
    public boolean hasAnyLessonInstances() {
        return !getLessonInstancesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Shift> getShifts() {
        return getShiftsSet();
    }

    @Deprecated
    public boolean hasAnyShifts() {
        return !getShiftsSet().isEmpty();
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasTotalQuantity() {
        return getTotalQuantity() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasUnitQuantity() {
        return getUnitQuantity() != null;
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

}
