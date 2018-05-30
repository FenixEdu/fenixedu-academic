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
package org.fenixedu.academic.ui.struts.action.operator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.contacts.ContactRoot;
import org.fenixedu.academic.domain.contacts.PartyContactValidation;
import org.fenixedu.academic.domain.contacts.PhysicalAddressValidation;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.predicates.AndPredicate;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import org.fenixedu.messaging.core.domain.Message;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = OperatorApplication.class, path = "address-validation", titleKey = "label.contacts.validate.address",
        bundle = "AcademicAdminOffice")
@Mapping(module = "operator", path = "/validate")
@Forwards({
        @Forward(name = "showPendingPartyContactsValidation", path = "/operator/contacts/showPendingPartyContactsValidation.jsp"),
        @Forward(name = "viewPartyContactValidation", path = "/operator/contacts/viewPartyContactValidation.jsp") })
public class OperatorValidatePartyContactsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        Predicate<PartyContactValidation> predicate =
                new AndPredicate<>(PartyContactValidation.PREDICATE_INVALID, PhysicalAddressValidation.PREDICATE_FILE);

        final Collection<PartyContactValidation> partyContactValidation =
                ContactRoot.getInstance().getInvalidPartyContactValidationsSet();
        final List<PartyContactValidation> invalidPartyContactValidations = filter(partyContactValidation, predicate);
        request.setAttribute("partyContacts", invalidPartyContactValidations);
        return mapping.findForward("showPendingPartyContactsValidation");
    }

    private static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        final List<T> result = new ArrayList<>();
        for (final T each : collection) {
            if (predicate.test(each)) {
                result.add(each);
            }
        }
        return result;
    }

    public ActionForward validate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final PartyContactValidation partyContactValidation = getRenderedObject("physicalAddressValidation");
        if (partyContactValidation != null) {
            sendPhysicalAddressValidationEmail((PhysicalAddressValidation) partyContactValidation);
        }
        return prepare(mapping, actionForm, request, response);
    }

    @Atomic
    private void sendPhysicalAddressValidationEmail(PhysicalAddressValidation physicalAddressValidation) {
        final Person person = (Person) physicalAddressValidation.getPartyContact().getParty();
        final String subject =
                BundleUtil.getString(Bundle.MANAGER, "label.contacts.validation.operator.email.subject",
                        Unit.getInstitutionAcronym());
        final String state = StringUtils.uncapitalize(physicalAddressValidation.getState().getPresentationName());
        String body =
                BundleUtil.getString(Bundle.MANAGER, "label.contacts.validation.operator.email.body", physicalAddressValidation
                        .getPartyContact().getPresentationValue(), state);
        final String description = physicalAddressValidation.getDescription();
        if (!StringUtils.isEmpty(description)) {
            body += "\n" + description;
        }
        final String sendingEmail = person.getEmailForSendingEmails();
        if (!StringUtils.isEmpty(sendingEmail)) {
            Message.fromSystem()
                    .singleBcc(sendingEmail)
                    .subject(subject)
                    .textBody(body)
                    .send();
        }
    }

    public ActionForward viewPartyContactValidation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String extId = request.getParameter("partyContactValidation");
        final PhysicalAddressValidation physicalAddressValidation = FenixFramework.getDomainObject(extId);
        request.setAttribute("physicalAddressValidation", physicalAddressValidation);
        request.setAttribute("person", physicalAddressValidation.getPartyContact().getParty());
        return mapping.findForward("viewPartyContactValidation");
    }
}
