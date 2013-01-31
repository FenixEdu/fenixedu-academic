package net.sourceforge.fenixedu.presentationTier.Action.microPayments;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.InstitutionAffiliationEvent;
import net.sourceforge.fenixedu.domain.accounting.events.MicroPaymentEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA;
import net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA.SearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA.SearchTransactions;
import net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.SearchBeanType;
import net.sourceforge.fenixedu.util.Money;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/operator", module = "microPayments")
@Forwards({ @Forward(name = "start", path = "/microPayments/operator.jsp"),
		@Forward(name = "viewProfile", path = "/microPayments/viewProfile.jsp"),
		@Forward(name = "showTransactions", path = "/microPayments/showTransactions.jsp") })
public class MicroPaymentsOperator extends FenixDispatchAction {

	public static class MicroPaymentCreationBean implements Serializable {

		private Person person;

		private Money amount = new Money(0.0);

		private Unit unit;

		private String paymentTicket;

		public MicroPaymentCreationBean(final Person person) {
			this.person = person;
			final Set<Unit> units = getUnitsForCurrentUser();
			if (units.size() == 1) {
				unit = units.iterator().next();
			}
		}

		public Person getPerson() {
			return person;
		}

		public Money getAmount() {
			return amount;
		}

		public void setAmount(Money amount) {
			this.amount = amount;
		}

		public Unit getUnit() {
			return unit;
		}

		public void setUnit(Unit unit) {
			this.unit = unit;
		}

		public String getPaymentTicket() {
			return paymentTicket;
		}

		public void setPaymentTicket(String paymentTicket) {
			this.paymentTicket = paymentTicket;
		}
	}

	private static Set<Unit> getUnitsForCurrentUser() {
		return PaymentManagementDA.getUnitsForCurrentUser();
	}

	public static class MicroPaymentUnitsProvider extends
			net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA.MicroPaymentUnitsProvider {
	}

	public ActionForward startPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		searchBean(request, "searchBean");
		final SortedSet<Person> people = (SortedSet<Person>) request.getAttribute("people");
		if (people != null && people.size() == 1) {
			return viewProfile(people.iterator().next(), mapping, request);
		}

		searchTransactionBean(request, "searchBeanTransactions");

		return mapping.findForward("start");
	}

	private void searchBean(final HttpServletRequest request, final String attribute) {
		SearchBean searchBean = (SearchBean) getRenderedObject(attribute);
		RenderUtils.invalidateViewState(attribute);
		if (searchBean == null) {
			searchBean = new SearchBean();
		} else {
			final SortedSet<Person> people = searchBean.getSearchResult();
			request.setAttribute("people", people);
		}
		request.setAttribute(attribute, searchBean);
	}

	private void searchTransactionBean(final HttpServletRequest request, final String attribute) {
		SearchTransactions searchBean = (SearchTransactions) getRenderedObject(attribute);
		RenderUtils.invalidateViewState(attribute);
		if (searchBean == null) {
			searchBean = new SearchTransactions();
		} else if (searchBean.getSearchBeanType() == SearchBeanType.PERSON_SEARCH_BEAN) {
			final SortedSet<Person> people = searchBean.getSearchBean().getSearchResult();
			request.setAttribute("people", people);
		}
		request.setAttribute(attribute, searchBean);
	}

	public ActionForward showPerson(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse response) {
		final Person person = getDomainObject(request, "personOid");
		return viewProfile(person, mapping, request);
	}

	private ActionForward viewProfile(final Person person, final ActionMapping mapping, final HttpServletRequest request) {
		request.setAttribute("person", person);
		final SearchBean searchBean = new SearchBean();
		request.setAttribute("searchBean", searchBean);
		request.setAttribute("microPayment", new MicroPaymentCreationBean(person));
		return mapping.findForward("viewProfile");
	}

	public ActionForward createMicroPayment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		MicroPaymentCreationBean microPayment = getRenderedObject("microPayment");
		final Person person = microPayment.getPerson();
		try {
			MicroPaymentEvent.create(getLoggedPerson(request).getUser(), person, microPayment.getUnit(),
					microPayment.getAmount(), microPayment.getPaymentTicket());
			RenderUtils.invalidateViewState();
		} catch (final DomainException e) {
			addActionMessage(request, e.getKey());
		}
		return viewProfile(person, mapping, request);
	}

	public ActionForward showTransactions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		final SearchTransactions searchTransactions = getRenderedObject();
		if (searchTransactions.hasValidArgs()) {
			final Set<MicroPaymentEvent> microPaymentEvents = searchTransactions.getResult();
			request.setAttribute("microPaymentEvents", microPaymentEvents);
			startPage(mapping, actionForm, request, response);
			return mapping.findForward("showTransactions");
		}
		return startPage(mapping, actionForm, request, response);
	}

	public ActionForward showTransactionsForPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		final Person person = getDomainObject(request, "personOid");
		if (person != null) {
			final InstitutionAffiliationEvent openAffiliationEvent = person.getOpenAffiliationEvent();
			if (openAffiliationEvent != null) {
				request.setAttribute("microPaymentEvents", openAffiliationEvent.getSortedMicroPaymentEvents());
				startPage(mapping, actionForm, request, response);
				return mapping.findForward("showTransactions");
			}
		}
		return startPage(mapping, actionForm, request, response);
	}
}
