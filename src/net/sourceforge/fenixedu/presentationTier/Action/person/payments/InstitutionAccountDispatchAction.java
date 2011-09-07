package net.sourceforge.fenixedu.presentationTier.Action.person.payments;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Entry;
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
@Forwards({ @Forward(name = "viewAccount", path = "/person/account/payments.jsp") })
public class InstitutionAccountDispatchAction extends FenixDispatchAction {
    public ActionForward viewAccount(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = getLoggedPerson(request);

	final Set<Entry> payments = new HashSet<Entry>();
	for (AccountingTransaction transaction : person.getPaymentTransactions(EventType.INSTITUTION_AFFILIATION)) {
	    payments.add(transaction.getToAccountEntry());
	}
	for (AccountingTransaction transaction : person.getPaymentTransactions(EventType.MICRO_PAYMENT)) {
	    payments.add(transaction.getFromAccountEntry());
	}
	request.setAttribute("payments", payments);

	Money balance = Money.ZERO;
	for (Entry entry : payments) {
	    balance = balance.add(entry.getOriginalAmount());
	}
	request.setAttribute("balance", balance);

	InstitutionAffiliationEvent affiliation = person.getOpenAffiliationEvent();
	if (affiliation != null) {
	    List<AccountingEventPaymentCode> codes = affiliation.getNonProcessedPaymentCodes();
	    if (!codes.isEmpty()) {
		request.setAttribute("paymentCode", codes.iterator().next());
	    }
	}

	return mapping.findForward("viewAccount");
    }
}
