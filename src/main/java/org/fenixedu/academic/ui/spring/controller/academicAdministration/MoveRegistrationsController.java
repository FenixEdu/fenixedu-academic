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
package org.fenixedu.academic.ui.spring.controller.academicAdministration;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.log.StudentRegistrationTransferLog;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.ui.spring.StrutsFunctionalityController;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.exceptions.AuthorizationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@Controller
@RequestMapping("/academic/move-registrations")
public class MoveRegistrationsController extends StrutsFunctionalityController {
    @RequestMapping(method = GET)
    public String home(Model model, @RequestParam User target, @RequestParam(defaultValue = "false") Boolean success) {
        MoveRegistrationParameters bean = new MoveRegistrationParameters(target);
        validateUsers(bean);
        model.addAttribute("bean", bean);
        model.addAttribute("success", success);
        return "fenixedu-academic/registrationManagement/move-registrations";
    }

    @RequestMapping(method = POST, value = "find")
    public String find(Model model, @ModelAttribute MoveRegistrationParameters bean) {
        validateUsers(bean);
        model.addAttribute("bean", bean);
        return "fenixedu-academic/registrationManagement/move-registrations";
    }

    @RequestMapping(method = POST, value = "merge")
    public String merge(Model model, @ModelAttribute MoveRegistrationParameters bean) {
        validateUsers(bean);
        merge(bean.getSource().getPerson(), bean.getTarget().getPerson());
        return "redirect:/academic/move-registrations?success=1&target=" + bean.getTarget().getUsername();
    }

    @Atomic(mode = TxMode.WRITE)
    private void merge(Person source, Person target) {
        // Accounting
        if (target.getPartySocialSecurityNumber() == null) {
            if (source.getPartySocialSecurityNumber() != null) {
                source.getPartySocialSecurityNumber().setParty(target);
            }
        }

        // Curriculum
        source.getCandidaciesSet().forEach(c -> c.setPerson(target));
        mergeStudent(source.getStudent(), target.getStudent());

        new StudentRegistrationTransferLog(source.getStudent(), target.getStudent());
    }

    private void mergeStudent(Student source, Student target) {
        source.getRegistrationsSet().forEach(r -> r.setStudent(target));
        source.getStudentStatutesSet().forEach(s -> s.setStudent(target));
        source.getExtraCurricularActivitySet().forEach(s -> s.setStudent(target));
    }

    private void validateUsers(MoveRegistrationParameters bean) {
        if (bean.getTarget() != null) {
            if (bean.getTarget().getPerson() == null || bean.getTarget().getPerson().getStudent() == null) {
                throw AuthorizationException.unauthorized();
            }
        }
        if (bean.getSource() != null) {
            if (bean.getSource().getPerson() == null || bean.getSource().getPerson().getStudent() == null) {
                throw AuthorizationException.unauthorized();
            }
        }
    }

    @Override
    protected Class<?> getFunctionalityType() {
        return null; // StudentOperationsDispatchAction.class;
    }
}
