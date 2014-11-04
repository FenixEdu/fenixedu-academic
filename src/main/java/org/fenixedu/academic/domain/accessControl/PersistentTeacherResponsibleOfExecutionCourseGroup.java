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
package org.fenixedu.academic.domain.accessControl;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.bennu.core.groups.Group;

public class PersistentTeacherResponsibleOfExecutionCourseGroup extends PersistentTeacherResponsibleOfExecutionCourseGroup_Base {
    protected PersistentTeacherResponsibleOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        super();
        init(executionCourse);
    }

    @Override
    public Group toGroup() {
        return TeacherResponsibleOfExecutionCourseGroup.get(getExecutionCourse());
    }

    public static PersistentTeacherResponsibleOfExecutionCourseGroup getInstance(ExecutionCourse executionCourse) {
        return singleton(PersistentTeacherResponsibleOfExecutionCourseGroup.class, executionCourse,
                () -> new PersistentTeacherResponsibleOfExecutionCourseGroup(executionCourse));
    }
}
