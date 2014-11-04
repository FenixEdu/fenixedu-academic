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

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.spaces.domain.Space;

public class PersistentTeacherGroup extends PersistentTeacherGroup_Base {
    protected PersistentTeacherGroup(Degree degree, ExecutionCourse executionCourse, Space campus, Department department,
            ExecutionYear executionYear) {
        super();
        setDegree(degree);
        setExecutionCourse(executionCourse);
        setCampus(campus);
        setDepartment(department);
        setExecutionYear(executionYear);
    }

    @Override
    public Group toGroup() {
        return TeacherGroup.get(getDegree(), getExecutionCourse(), getCampus(), getDepartment(), getExecutionYear());
    }

    @Override
    protected void gc() {
        setDegree(null);
        setExecutionCourse(null);
        setCampus(null);
        setDepartment(null);
        setExecutionYear(null);
        super.gc();
    }

    public static PersistentTeacherGroup getInstance(Degree degree) {
        return getInstance(() -> degree.getTeacherGroupSet().stream(), degree, null, null, null, null);
    }

    public static PersistentTeacherGroup getInstance(Space campus) {
        return getInstance(() -> campus.getTeacherGroupSet().stream(), null, null, campus, null, null);
    }

    public static PersistentTeacherGroup getInstance(Department department, ExecutionYear executionYear) {
        return getInstance(() -> department.getTeacherGroupSet().stream(), null, null, null, department, executionYear);
    }

    public static PersistentTeacherGroup getInstance(ExecutionCourse executionCourse) {
        return getInstance(() -> executionCourse.getTeacherGroupSet().stream(), null, executionCourse, null, null, null);
    }

    public static PersistentTeacherGroup getInstance(Degree degree, ExecutionCourse executionCourse, Space campus,
            Department department, ExecutionYear executionYear) {
        if (degree != null) {
            return getInstance(degree);
        }
        if (campus != null) {
            return getInstance(campus);
        }
        if (executionCourse != null) {
            return getInstance(executionCourse);
        }
        if (department != null) {
            return getInstance(department, executionYear);
        }
        return null;
    }

    private static PersistentTeacherGroup getInstance(Supplier<Stream<PersistentTeacherGroup>> options, Degree degree,
            ExecutionCourse executionCourse, Space campus, Department department, ExecutionYear executionYear) {
        return singleton(
                () -> options
                        .get()
                        .filter(group -> Objects.equals(group.getDegree(), degree)
                                && Objects.equals(group.getExecutionCourse(), executionCourse)
                                && Objects.equals(group.getCampus(), campus) && Objects.equals(group.getDepartment(), department)
                                && Objects.equals(group.getExecutionYear(), executionYear)).findAny(),
                () -> new PersistentTeacherGroup(degree, executionCourse, campus, department, executionYear));
    }
}
