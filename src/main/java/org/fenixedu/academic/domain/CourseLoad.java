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
package org.fenixedu.academic.domain;

import java.math.BigDecimal;
import java.util.Collection;

import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLoad;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

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

//    public void edit(BigDecimal unitQuantity, BigDecimal totalQuantity) {
//        setUnitQuantity(unitQuantity);
//        setTotalQuantity(totalQuantity);
//
//        checkQuantities();
//    }

    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
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

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getShiftsSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION, "error.CourseLoad.cannot.be.deleted"));
        }
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

}
