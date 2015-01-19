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

import static org.fenixedu.academic.predicate.AccessControl.check;
import static org.fenixedu.academic.predicate.AccessControl.getPerson;
import static pt.ist.fenixframework.FenixFramework.getDomainObject;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.ui.spring.StrutsFunctionalityController;
import org.fenixedu.academic.ui.struts.action.teacher.ManageExecutionCourseDA;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import pt.ist.fenixframework.Atomic;

@Controller
@RequestMapping("/teacher/{executionCourseId}/communication")
public class ExecutionCourseCommunicationController extends StrutsFunctionalityController {

    @RequestMapping(method = RequestMethod.GET)
    public TeacherView communication(Model model, @PathVariable String executionCourseId) {
        final ExecutionCourse executionCourse = getDomainObject(executionCourseId);
        check(getPerson(), person -> hasPermissions(person, executionCourse));
        model.addAttribute("professorship", executionCourse.getProfessorship(getPerson()));
        model.addAttribute("executionCourse", executionCourse);
        return new TeacherView("communication");
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public RedirectView edit(@PathVariable String executionCourseId, @RequestParam String email, @RequestParam String siteUrl) {
        update(getDomainObject(executionCourseId), email, siteUrl);
        return new RedirectView("/teacher/" + executionCourseId + "/communication", true);
    }

    @Atomic
    private static void update(ExecutionCourse executionCourse, String email, String siteUrl) {
        executionCourse.setEmail(email);
        executionCourse.setSiteUrl(siteUrl);
    }

    @Override
    protected Class<?> getFunctionalityType() {
        return ManageExecutionCourseDA.class;
    }

    private boolean hasPermissions(Person person, ExecutionCourse executionCourse) {
        return person != null && executionCourse.getProfessorship(person) != null
                && executionCourse.getProfessorship(person).getPermissions().getPersonalization();
    }
}
