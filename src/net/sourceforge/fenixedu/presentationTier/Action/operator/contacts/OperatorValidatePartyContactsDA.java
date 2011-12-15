package net.sourceforge.fenixedu.presentationTier.Action.operator.contacts;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contacts.PartyContactValidation;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressValidation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.predicates.AndPredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;

@Mapping(module = "operator", path = "/validate", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "showPendingPartyContactsValidation", path = "/operator/contacts/showPendingPartyContactsValidation.jsp"),
	@Forward(name = "viewPartyContactValidation", path = "/operator/contacts/viewPartyContactValidation.jsp") })
public class OperatorValidatePartyContactsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	Predicate<PartyContactValidation> predicate = new AndPredicate<PartyContactValidation>(
		PartyContactValidation.PREDICATE_INVALID, PhysicalAddressValidation.PREDICATE_FILE);

	final List<PartyContactValidation> partyContactValidation = RootDomainObject.getInstance()
		.getInvalidPartyContactValidations();
	final List<PartyContactValidation> invalidPartyContactValidations = CollectionUtils.filter(partyContactValidation,
		predicate);
	request.setAttribute("partyContacts", invalidPartyContactValidations);
	return mapping.findForward("showPendingPartyContactsValidation");
    }

    public ActionForward viewPartyContactValidation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final String extId = request.getParameter("partyContactValidation");
	final PhysicalAddressValidation physicalAddressValidation = PartyContactValidation.fromExternalId(extId);
	request.setAttribute("physicalAddressValidation", physicalAddressValidation);
	return mapping.findForward("viewPartyContactValidation");
    }
}
