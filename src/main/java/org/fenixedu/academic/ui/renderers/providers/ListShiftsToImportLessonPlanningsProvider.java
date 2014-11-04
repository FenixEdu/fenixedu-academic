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
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.teacher.ImportLessonPlanningsBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;

import org.apache.commons.collections.CollectionUtils;

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
                if (executionCourseTo.hasCourseLoadForType(shiftType)) {
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

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
