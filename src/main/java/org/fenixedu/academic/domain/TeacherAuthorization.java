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

import java.util.Objects;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

public class TeacherAuthorization extends TeacherAuthorization_Base implements Comparable<TeacherAuthorization> {
    protected TeacherAuthorization() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCreationDate(DateTime.now());
    }

    protected TeacherAuthorization(Teacher teacher, Department department, ExecutionSemester executionSemester,
            TeacherCategory teacherCategory, Boolean contracted, Double lessonHours, Double workPercentageInInstitution) {
        this();
        setTeacher(teacher);
        setDepartment(department);
        setExecutionSemester(executionSemester);
        setTeacherCategory(teacherCategory);
        setContracted(contracted);
        setLessonHours(lessonHours);
        setWorkPercentageInInstitution(workPercentageInInstitution);
        setAuthorizer(Authenticate.getUser());
    }

    public static TeacherAuthorization createOrUpdate(Teacher teacher, Department department, ExecutionSemester executionSemester,
            TeacherCategory teacherCategory, Boolean contracted, Double lessonHours, Double workPercentageInInstitution) {
        Objects.requireNonNull(teacher);
        Objects.requireNonNull(department);
        Objects.requireNonNull(executionSemester);
        Objects.requireNonNull(teacherCategory);
        Objects.requireNonNull(contracted);
        Objects.requireNonNull(lessonHours);
        TeacherAuthorization existing = teacher.getTeacherAuthorization(executionSemester.getAcademicInterval()).orElse(null);
        if (existing != null) {
            if (existing.getDepartment().equals(department) && existing.getContracted().equals(contracted)
                    && existing.getLessonHours().equals(lessonHours)) {
                return existing;
            } else {
                existing.revoke();
            }
        }
        return new TeacherAuthorization(teacher, department, executionSemester, teacherCategory, contracted, lessonHours,
                workPercentageInInstitution);
    }

    @Override
    public DateTime getCreationDate() {
        //FIXME: remove when the framework enables read-only slots
        return super.getCreationDate();
    }

    public void revoke() {
        setRevokedTeacher(getTeacher());
        setTeacher(null);
        setRevokedDepartment(getDepartment());
        setDepartment(null);
        setRevokedExecutionSemester(getExecutionSemester());
        setExecutionSemester(null);
        setRevoker(Authenticate.getUser());
        setRevokeTime(new DateTime());
        setRevokedRootDomainObject(getRootDomainObject());
        setRootDomainObject(null);
    }

    @Override
    public Department getDepartment() {
        if (getRevokedRootDomainObject() != null) {
            return getRevokedDepartment();
        }
        // FIXME: Removed when framework support read-only slots
        return super.getDepartment();
    }

    @Override
    public ExecutionSemester getExecutionSemester() {
        if (getRevokedRootDomainObject() != null) {
            return getRevokedExecutionSemester();
        }
        // FIXME: Removed when framework support read-only slots
        return super.getExecutionSemester();
    }

    @Override
    public Teacher getTeacher() {
        // FIXME: Removed when framework support read-only slots
        if (getRevokedRootDomainObject() != null) {
            return getRevokedTeacher();
        }

        return super.getTeacher();
    }

    @Override
    public TeacherCategory getTeacherCategory() {
        // FIXME: Removed when framework support read-only slots
        return super.getTeacherCategory();
    }

    public boolean isContracted() {
        // FIXME: Removed when framework support read-only slots
        return super.getContracted();
    }

    @Override
    public Double getLessonHours() {
        // FIXME: Removed when framework support read-only slots
        return super.getLessonHours();
    }
    
    @Override
    public Double getWorkPercentageInInstitution() {
        // FIXME: Removed when framework support read-only slots
        return super.getWorkPercentageInInstitution();
    }

    @Override
    public User getAuthorizer() {
        // FIXME: Removed when framework support read-only slots
        return super.getAuthorizer();
    }

    @Override
    public User getRevoker() {
        // FIXME: Removed when framework support read-only slots
        return super.getRevoker();
    }

    @Override
    public DateTime getRevokeTime() {
        // FIXME: Removed when framework support read-only slots
        return super.getRevokeTime();
    }

    @Override
    public int compareTo(TeacherAuthorization o) {
        int semester = getExecutionSemester().compareTo(o.getExecutionSemester());
        if (semester != 0) {
            return semester;
        }
        int category = getTeacherCategory().compareTo(o.getTeacherCategory());
        if (category != 0) {
            return category;
        }
        return getExternalId().compareTo(o.getExternalId());
    }
}
