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
package org.fenixedu.academic.service.services.resourceAllocationManager;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.CourseLoad;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.dto.resourceAllocationManager.CourseLoadBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;

public class EditExecutionCourse {

    @Atomic
    public static void run(CourseLoadBean bean) throws FenixServiceException {
        if (bean != null) {
            ExecutionCourse executionCourse = bean.getExecutionCourse();
            editCourseLoad(executionCourse, bean.getType(), bean.getUnitQuantity(), bean.getTotalQuantity());
        }
    }

    private static void editCourseLoad(ExecutionCourse executionCourse, ShiftType type, BigDecimal unitQuantity,
            BigDecimal totalQuantity) {
        CourseLoad courseLoad = executionCourse.getCourseLoadByShiftType(type);
        if (courseLoad == null) {
            new CourseLoad(executionCourse, type, unitQuantity, totalQuantity);
        } else {
            courseLoad.edit(unitQuantity, totalQuantity);
        }
    }
}