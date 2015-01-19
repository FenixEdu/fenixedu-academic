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
package org.fenixedu.academic.ui.spring.controller.teacher.professorship;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.springframework.beans.factory.annotation.Value;

public class CreateProfessorshipBean {

    private ExecutionSemester period;
    private Person teacher;
    private ExecutionDegree degree;
    private ExecutionCourse course;

    @Value("false")
    private Boolean responsibleFor;

    public ExecutionSemester getPeriod() {
        return period;
    }

    public void setPeriod(ExecutionSemester period) {
        this.period = period;
    }

    public ExecutionDegree getDegree() {
        return degree;
    }

    public void setDegree(ExecutionDegree degree) {
        this.degree = degree;
    }

    public ExecutionCourse getCourse() {
        return course;
    }

    public void setCourse(ExecutionCourse course) {
        this.course = course;
    }

    public Boolean getResponsibleFor() {
        return responsibleFor;
    }

    public void setResponsibleFor(Boolean responsibleFor) {
        this.responsibleFor = responsibleFor;
    }

    public Person getTeacher() {
        return teacher;
    }

    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

}
