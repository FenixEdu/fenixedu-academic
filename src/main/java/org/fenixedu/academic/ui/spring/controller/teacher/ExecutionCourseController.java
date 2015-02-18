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
package org.fenixedu.academic.ui.spring.controller.teacher;

import java.util.Optional;

import javax.ws.rs.core.Response.Status;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.spring.StrutsFunctionalityController;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

public abstract class ExecutionCourseController extends StrutsFunctionalityController {

    ExecutionCourse executionCourse;

    public ExecutionCourseController() {
        super();
    }

    private Professorship findProfessorship(final ExecutionCourse executionCourse) {
        final Person person = AccessControl.getPerson();
        if (person != null) {
            Optional<Professorship> professorshipOpt =
                    person.getProfessorshipsSet().stream()
                            .filter(professorship -> professorship.getExecutionCourse().equals(executionCourse)).findFirst();
            if (professorshipOpt.isPresent()) {
                Professorship prof = professorshipOpt.get();
                if (!this.getPermission(prof)) {
                    throw new DomainException(Status.FORBIDDEN, "message.error.notAuthorized");
                } else {
                    return prof;
                }
            }
        }
        throw new DomainException(Status.FORBIDDEN, "message.error.notAuthorized");
    }

    @ModelAttribute("projectGroup")
    public ProjectGroupBean setProjectGroup() {
        return new ProjectGroupBean();
    }

    @ModelAttribute("professorship")
    public Professorship setProfessorship(@PathVariable ExecutionCourse executionCourse) {
        return findProfessorship(executionCourse);
    }

    abstract Boolean getPermission(Professorship prof);

    @ModelAttribute("executionCourse")
    public ExecutionCourse getExecutionCourse(@PathVariable ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
        return executionCourse;
    }
}