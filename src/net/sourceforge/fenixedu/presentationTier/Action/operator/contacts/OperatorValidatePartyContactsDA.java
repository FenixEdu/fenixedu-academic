package net.sourceforge.fenixedu.presentationTier.Action.operator.contacts;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contacts.PartyContactValidation;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressValidation;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.predicates.AndPredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;

@Mapping(module = "operator", path = "/validate", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "showPendingPartyContactsValidation", path = "/operator/contacts/showPendingPartyContactsValidation.jsp",
                tileProperties = @Tile(title = "private.operator.contacts.addressvalidation")),
        @Forward(name = "viewPartyContactValidation", path = "/operator/contacts/viewPartyContactValidation.jsp") })
public class OperatorValidatePartyContactsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        Predicate<PartyContactValidation> predicate =
                new AndPredicate<PartyContactValidation>(PartyContactValidation.PREDICATE_INVALID,
                        PhysicalAddressValidation.PREDICATE_FILE);

        final List<PartyContactValidation> partyContactValidation =
                RootDomainObject.getInstance().getInvalidPartyContactValidations();
        final List<PartyContactValidation> invalidPartyContactValidations =
                CollectionUtils.filter(partyContactValidation, predicate);
        request.setAttribute("partyContacts", invalidPartyContactValidations);
        return mapping.findForward("showPendingPartyContactsValidation");
    }

    public ActionForward validate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final PartyContactValidation partyContactValidation = getRenderedObject("physicalAddressValidation");
        if (partyContactValidation != null) {
            sendPhysicalAddressValidationEmail((PhysicalAddressValidation) partyContactValidation);
        }
        return prepare(mapping, actionForm, request, response);
    }

    @Service
    private void sendPhysicalAddressValidationEmail(PhysicalAddressValidation physicalAddressValidation) {
        final Person person = (Person) physicalAddressValidation.getPartyContact().getParty();
        final String subject =
                BundleUtil.getMessageFromModuleOrApplication("manager", "label.contacts.validation.operator.email.subject");
        final String state = StringUtils.uncapitalize(physicalAddressValidation.getState().getPresentationName());
        String body =
                BundleUtil.getMessageFromModuleOrApplication("manager", "label.contacts.validation.operator.email.body",
                        physicalAddressValidation.getPartyContact().getPresentationValue(), state);
        final String description = physicalAddressValidation.getDescription();
        if (!StringUtils.isEmpty(description)) {
            body += "\n" + description;
        }
        final String sendingEmail = person.getEmailForSendingEmails();
        if (!StringUtils.isEmpty(sendingEmail)) {
            new Message(RootDomainObject.getInstance().getSystemSender(), Collections.EMPTY_LIST, Collections.EMPTY_LIST,
                    subject, body, sendingEmail);
        }
    }

    public ActionForward viewPartyContactValidation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String extId = request.getParameter("partyContactValidation");
        final PhysicalAddressValidation physicalAddressValidation = PartyContactValidation.fromExternalId(extId);
        request.setAttribute("physicalAddressValidation", physicalAddressValidation);
        request.setAttribute("person", physicalAddressValidation.getPartyContact().getParty());
        return mapping.findForward("viewPartyContactValidation");
    }
}
