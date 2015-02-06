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
package org.fenixedu.academic.ui.struts.action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PublicCandidacyHashCode;
import org.fenixedu.academic.service.services.exceptions.PasswordInitializationException;
import org.fenixedu.academic.service.services.person.InitializePassword;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.publico.candidacies.erasmus.ErasmusIndividualCandidacyProcessPublicDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(module = "external", path = "/internationalRegistration", scope = "request", parameter = "method", validate = true,
        formBean = "internationalRegistrationForm", formBeanClass = InternationalRegistrationForm.class,
        functionality = ErasmusIndividualCandidacyProcessPublicDA.class)
@Forwards({ @Forward(name = "international-registration", path = "/publico/candidacy/internationalRegistration_bd.jsp"),
        @Forward(name = "success", path = "/publico/candidacy/internationalRegistrationSuccess_bd.jsp") })
public class InternationalRegistrationDA extends FenixDispatchAction {

    public ActionForward showInternationalRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person person = readPersonByCandidacyHashCode(request.getParameter("hash"));

        if (person != null) {
            request.setAttribute("person", person);
            return mapping.findForward("international-registration");
        } else {
            return setError(request, mapping, "internationalRegistration.error.invalidLink", "international-registration", null);
        }

    }

    public ActionForward updateUserPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if ((form == null) || !(form instanceof InternationalRegistrationForm)) {
            return setError(request, mapping, "internationalRegistration.error.invalidLink", "international-registration", null);
        }

        InternationalRegistrationForm registrationForm = (InternationalRegistrationForm) form;
        Person person = readPersonByCandidacyHashCode(registrationForm.getHashCode());

        request.setAttribute("person", person);

        if (person == null) {
            return setError(request, mapping, "internationalRegistration.error.invalidLink", "international-registration", null);
        }

        if (!StringUtils.equals(registrationForm.getPassword(), registrationForm.getRetypedPassword())) {
            return setError(request, mapping, "internationalRegistration.error.passwordsDontMatch", "international-registration",
                    null);
        }

        if (StringUtils.isEmpty(registrationForm.getPassword())) {
            return setError(request, mapping, "internationalRegistration.error.passwordsDontMatch", "international-registration",
                    null);
        }

        try {
            InitializePassword.run(person.getUser(), registrationForm.getPassword());
        } catch (PasswordInitializationException e) {
            return setError(request, mapping, e.getMessage(), "international-registration", e);
        } catch (Exception e) {
            return setError(request, mapping, "internationalRegistration.error.registering", "international-registration", e);
        }
        return mapping.findForward("success");
    }

    private Person readPersonByCandidacyHashCode(String hashCode) {
        if (!StringUtils.isEmpty(hashCode)) {
            PublicCandidacyHashCode publicCandidacyHashCode = PublicCandidacyHashCode.getPublicCandidacyCodeByHash(hashCode);
            return publicCandidacyHashCode != null ? Person.readPersonByEmailAddress(publicCandidacyHashCode.getEmail()) : null;
        }
        return null;
    }

}
