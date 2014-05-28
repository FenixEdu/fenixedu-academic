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

import java.util.HashSet;

import net.sourceforge.fenixedu.dataTransferObject.gesdis.CreateLessonPlanningBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ShiftType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class ExecutionCourseShiftTypesToCreateLessonPlanningProvider implements DataProvider {

    public ExecutionCourse getExecutionCourse(Object source) {
        return ((CreateLessonPlanningBean) source).getExecutionCourse();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        ExecutionCourse executionCourse = getExecutionCourse(source);
        return (getExecutionCourse(source) != null) ? executionCourse.getShiftTypes() : new HashSet<ShiftType>();
    }

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }
}
