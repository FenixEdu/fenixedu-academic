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
package org.fenixedu.academic.ui.renderers.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.fenixedu.academic.domain.CourseLoad;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.dto.teacher.ImportLessonPlanningsBean;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ListShiftsToImportLessonPlanningsProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) source;
        Set<Shift> shifts = new TreeSet<Shift>(Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);

        ExecutionCourse executionCourseFrom = bean.getExecutionCourse();
        ExecutionCourse executionCourseTo = bean.getExecutionCourseTo();

        if (executionCourseFrom != null && executionCourseTo != null) {

            List<ShiftType> shiftTypesTo = new ArrayList<ShiftType>();
            for (ShiftType shiftType : executionCourseTo.getShiftTypes()) {
                if (hasCourseLoadForType(executionCourseTo, shiftType)) {
                    shiftTypesTo.add(shiftType);
                }
            }

            for (Shift shift : executionCourseFrom.getAssociatedShifts()) {
                if (CollectionUtils.containsAny(shiftTypesTo, shift.getTypes())) {
                    shifts.add(shift);
                }
            }

            if (shifts.isEmpty()) {
                return executionCourseFrom.getAssociatedShifts();
            }
        }

        return shifts;
    }

    private boolean hasCourseLoadForType(ExecutionCourse executionCourse, ShiftType type) {
        CourseLoad courseLoad = executionCourse.getCourseLoadByShiftType(type);
        return courseLoad != null && !courseLoad.isEmpty();
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
