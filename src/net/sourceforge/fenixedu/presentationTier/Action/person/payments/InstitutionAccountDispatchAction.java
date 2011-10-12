package net.sourceforge.fenixedu.presentationTier.Action.person.payments;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.InstitutionAffiliationEvent;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.Money;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/payments", module = "person")
@Forwards({ 
    @Forward(name = "viewAccount", path = "/person/account/payments.jsp"),
    @Forward(name = "viewEvent", path = "/person/account/viewEvent.jsp")
})
public class InstitutionAccountDispatchAction extends FenixDispatchAction {

    public ActionForward viewAccount(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Person person = getLoggedPerson(request);

	final Set<Entry> payments = new TreeSet<Entry>(Entry.COMPARATOR_BY_MOST_RECENT_WHEN_REGISTERED);
	for (final AccountingTransaction transaction : person.getPaymentTransactions(EventType.INSTITUTION_AFFILIATION)) {
	    payments.add(transaction.getToAccountEntry());
	}
	for (final AccountingTransaction transaction : person.getPaymentTransactions(EventType.MICRO_PAYMENT)) {
	    payments.add(transaction.getFromAccountEntry());
	}
	request.setAttribute("payments", payments);

	Money balance = Money.ZERO;
	for (final Entry entry : payments) {
	    balance = balance.add(entry.getOriginalAmount());
	}
	request.setAttribute("balance", balance);

	final InstitutionAffiliationEvent affiliation = person.getOpenAffiliationEvent();
	if (affiliation != null) {
	    request.setAttribute("affiliation", affiliation);
	    List<AccountingEventPaymentCode> codes = affiliation.getNonProcessedPaymentCodes();
	    if (!codes.isEmpty()) {
		request.setAttribute("paymentCode", codes.iterator().next());
	    }
	}

	return mapping.findForward("viewAccount");
    }

    public ActionForward acceptTermsAndConditions(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Person person = getLoggedPerson(request);
	final InstitutionAffiliationEvent affiliation = getDomainObject(request, "affiliationOid");

	if (person.getOpenAffiliationEvent() == affiliation) {
	    final String readTermsAndConditions = request.getParameter("readTermsAndConditions");
	    if (readTermsAndConditions != null && readTermsAndConditions.equals("on")) {
		affiliation.acceptTermsAndConditions();
	    }
	}

	return viewAccount(mapping, actionForm, request, response);
    }

    public ActionForward viewEvent(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Person person = getLoggedPerson(request);
	request.setAttribute("person", person);
	final Event event = getDomainObject(request, "eventId");
	request.setAttribute("event", event);
	return mapping.findForward("viewEvent");
    }

}
