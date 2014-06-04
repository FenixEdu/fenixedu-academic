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
package net.sourceforge.fenixedu.presentationTier.Action.operator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PartyContactValidation;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressValidation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.predicates.AndPredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;

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
                new AndPredicate<PartyContactValidation>(PartyContactValidation.PREDICATE_INVALID,
                        PhysicalAddressValidation.PREDICATE_FILE);

        final Collection<PartyContactValidation> partyContactValidation =
                Bennu.getInstance().getInvalidPartyContactValidationsSet();
        final List<PartyContactValidation> invalidPartyContactValidations = filter(partyContactValidation, predicate);
        request.setAttribute("partyContacts", invalidPartyContactValidations);
        return mapping.findForward("showPendingPartyContactsValidation");
    }

    private static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        final List<T> result = new ArrayList<T>();
        for (final T each : collection) {
            if (predicate.eval(each)) {
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
            new Message(Bennu.getInstance().getSystemSender(), Collections.EMPTY_LIST, Collections.EMPTY_LIST, subject, body,
                    sendingEmail);
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
