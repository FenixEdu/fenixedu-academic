package net.sourceforge.fenixedu.presentationTier.Action.treasury.payments;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.accounting.CreatePaymentsForEvents;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.accounting.events.InstitutionAffiliationEvent;
import net.sourceforge.fenixedu.domain.accounting.events.MicroPaymentEvent;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.microPayments.MicroPaymentsOperator.MicroPaymentCreationBean;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(module = "treasury", path = "/paymentManagement", parameter = "method", scope = "request")
@Forwards(
		value = {
				@Forward(name = "paymentManagement", path = "treasury.paymentManagement", tileProperties = @Tile(
						title = "private.treasury.payments")),
				@Forward(name = "viewProfile", path = "treasury.viewProfile", tileProperties = @Tile(
						title = "private.treasury.payments")) })
public class PaymentManagementDA extends FenixDispatchAction {

	public static Set<Unit> getUnitsForCurrentUser() {
		final Set<Unit> units = new HashSet<Unit>();
		final Person person = AccessControl.getPerson();
		if (person != null) {
			for (final PersonFunction function : person.getActivePersonFunctions()) {
				if (function.getFunction().getFunctionType().equals(FunctionType.MICRO_PAYMENT_MANAGER)
						&& function.isActive(new YearMonthDay())) {
					units.add((Unit) function.getParentParty());
				}
			}
		}
		return units;
	}

	public static Set<Person> getOperatorsForCurrentUser() {
		final Set<Person> result = new HashSet<Person>();
		final Set<Unit> units = getUnitsForCurrentUser();
		final YearMonthDay today = new YearMonthDay();
		for (final Unit unit : units) {
			for (final Accountability accountability : unit.getChildsSet()) {
				if (accountability instanceof PersonFunction) {
					final PersonFunction personFunction = (PersonFunction) accountability;
					if (personFunction.isActive(today)) {
						result.add(personFunction.getPerson());
					}
				}
			}
		}
		return result;
	}

	public static class MicroPaymentUnitsProvider extends AbstractDomainObjectProvider {
		@Override
		public Object provide(Object source, Object currentValue) {
			return getUnitsForCurrentUser();
		}
	}

	public static class MicroPaymentOperatorProvider extends AbstractDomainObjectProvider {
		@Override
		public Object provide(Object source, Object currentValue) {
			return getOperatorsForCurrentUser();
		}
	}

	public static class SearchBean implements Serializable {

		private String searchString;

		private final SearchPerson searchPerson = new SearchPerson();

		public String getSearchString() {
			return searchString;
		}

		public void setSearchString(String searchString) {
			this.searchString = searchString;
		}

		public SortedSet<Person> getSearchResult() {
			final SortedSet<Person> result = new TreeSet<Person>(Person.COMPARATOR_BY_NAME_AND_ID);
			if (searchString != null && !searchString.isEmpty()) {
				result.addAll(searchName(searchString));
				result.addAll(searchUsername(searchString));
				result.addAll(searchNumber(searchString));
				result.addAll(searchStudentNumber(searchString));
				result.addAll(searchPaymentCode(searchString));
			}
			return result;
		}

		private Collection<Person> searchPaymentCode(final String paymentCode) {
			final SearchParameters searchParameters = new SearchParameters();
			searchParameters.setPaymentCode(paymentCode);
			return search(searchParameters);
		}

		private Collection<Person> searchName(final String name) {
			final SearchParameters searchParameters = new SearchParameters();
			searchParameters.setName(name);
			return search(searchParameters);
		}

		private Collection<Person> searchUsername(final String username) {
			final SearchParameters searchParameters = new SearchParameters();
			searchParameters.setUsername(username);
			return search(searchParameters);
		}

		private Collection<Person> searchStudentNumber(final String number) {
			if (StringUtils.isNumeric(number)) {
				final SearchParameters searchParameters = new SearchParameters();
				searchParameters.setStudentNumber(new Integer(number));
				return search(searchParameters);
			}
			return Collections.emptySet();
		}

		private Collection<Person> searchNumber(final String number) {
			if (StringUtils.isNumeric(number)) {
				final SearchParameters searchParameters = new SearchParameters();
				searchParameters.setMechanoGraphicalNumber(new Integer(number));
				return search(searchParameters);
			}
			return Collections.emptySet();
		}

		private Collection<Person> search(final SearchParameters searchParameters) {
			final SearchPersonPredicate searchPersonPredicate = new SearchPerson.SearchPersonPredicate(searchParameters);
			final CollectionPager<Person> people = searchPerson.run(searchParameters, searchPersonPredicate);
			return people.getCollection();
		}

		public Set<MicroPaymentEvent> getResult() {
			final SortedSet<Person> set = getSearchResult();
			final Person person = set.size() == 1 ? set.iterator().next() : null;
			if (person != null) {
				final InstitutionAffiliationEvent openAffiliationEvent = person.getOpenAffiliationEvent();
				if (openAffiliationEvent != null) {
					return openAffiliationEvent.getSortedMicroPaymentEvents();
				}
			}
			return Collections.emptySet();
		}

		public boolean hasValidArgs() {
			final SortedSet<Person> set = getSearchResult();
			return set.size() == 1;
		}

	}

	public static class OperatorSearchBean implements Serializable {

		private Person operator;

		public Set<MicroPaymentEvent> getResult() {
			final Set<MicroPaymentEvent> result = new TreeSet<MicroPaymentEvent>(MicroPaymentEvent.COMPARATOR_BY_DATE);
			if (operator != null) {
				final Set<Unit> units = getUnitsForCurrentUser();
				for (final Unit unit : units) {
					for (final MicroPaymentEvent microPaymentEvent : unit.getMicroPaymentEventSet()) {
						if (operator.getUsername().equals(microPaymentEvent.getCreatedBy())) {
							result.add(microPaymentEvent);
						}
					}
				}
			}
			return result;
		}

		public boolean hasValidArgs() {
			return operator != null;
		}

		public Person getOperator() {
			return operator;
		}

		public void setOperator(Person operator) {
			this.operator = operator;
		}

	}

	public static class UnitSearchBean implements Serializable {

		private Unit unit;

		public Set<MicroPaymentEvent> getResult() {
			final Set<MicroPaymentEvent> result = new TreeSet<MicroPaymentEvent>(MicroPaymentEvent.COMPARATOR_BY_DATE);
			if (unit != null) {
				result.addAll(unit.getMicroPaymentEventSet());
			}
			return result;
		}

		public Unit getUnit() {
			return unit;
		}

		public void setUnit(Unit unit) {
			this.unit = unit;
		}

		public boolean hasValidArgs() {
			return unit != null;
		}

	}

	public static class SearchTransactions implements Serializable {

		private SearchBeanType searchBeanType = SearchBeanType.OPERATOR_SEARCH_BEAN;

		private final SearchBean searchBean = new SearchBean();

		private final OperatorSearchBean operatorSearchBean = new OperatorSearchBean();

		private final UnitSearchBean unitSearchBean = new UnitSearchBean();

		public Set<MicroPaymentEvent> getResult() {
			if (searchBeanType == SearchBeanType.PERSON_SEARCH_BEAN) {
				return searchBean.getResult();
			}
			if (searchBeanType == SearchBeanType.OPERATOR_SEARCH_BEAN) {
				return operatorSearchBean.getResult();
			}
			if (searchBeanType == SearchBeanType.UNIT_SEARCH_BEAN) {
				return unitSearchBean.getResult();
			}
			return null;
		}

		public SearchBeanType getSearchBeanType() {
			return searchBeanType;
		}

		public void setSearchBeanType(SearchBeanType searchBeanType) {
			if (this.searchBeanType != null && this.searchBeanType != searchBeanType) {
				searchBean.setSearchString(null);
				operatorSearchBean.setOperator(null);
				unitSearchBean.setUnit(null);
			}
			this.searchBeanType = searchBeanType;
		}

		public SearchBean getSearchBean() {
			return searchBean;
		}

		public OperatorSearchBean getOperatorSearchBean() {
			return operatorSearchBean;
		}

		public UnitSearchBean getUnitSearchBean() {
			return unitSearchBean;
		}

		public boolean hasValidArgs() {
			if (searchBeanType == SearchBeanType.PERSON_SEARCH_BEAN) {
				return searchBean.hasValidArgs();
			}
			if (searchBeanType == SearchBeanType.OPERATOR_SEARCH_BEAN) {
				return operatorSearchBean.hasValidArgs();
			}
			if (searchBeanType == SearchBeanType.UNIT_SEARCH_BEAN) {
				return unitSearchBean.hasValidArgs();
			}
			return false;
		}

	}

	public static class PaymentBean implements Serializable {

		private final Collection<EntryDTO> entryDTOs;

		private final Event event;

		private DateTime paymentDateTime = new DateTime();

		private String contributorNumber;

		private String contributorName;

		private Money value = Money.ZERO;

		public PaymentBean(final Event event) {
			this.event = event;
			entryDTOs = event.calculateEntries();
			for (final EntryDTO entryDTO : entryDTOs) {
				value = value.add(entryDTO.getAmountToPay());
			}
			final Person person = event.getPerson();
			contributorName = person.getName();
			contributorNumber = person.getSocialSecurityNumber();
		}

		public Event getEvent() {
			return event;
		}

		public DateTime getPaymentDateTime() {
			return paymentDateTime;
		}

		public void setPaymentDateTime(DateTime paymentDateTime) {
			this.paymentDateTime = paymentDateTime;
		}

		public String getContributorNumber() {
			return contributorNumber;
		}

		public void setContributorNumber(String contributorNumber) {
			this.contributorNumber = contributorNumber;
		}

		public String getContributorName() {
			return contributorName;
		}

		public void setContributorName(String contributorName) {
			this.contributorName = contributorName;
		}

		public Money getValue() {
			return value;
		}

		public void setValue(Money value) {
			this.value = value;
		}

		public Collection<EntryDTO> getEntryDTOs() {
			return entryDTOs;
		}

		public Party getContributorParty() {
			return contributorNumber == null || StringUtils.isEmpty(contributorNumber) ? null : Party
					.readByContributorNumber(contributorNumber);
		}

		public Collection<EntryDTO> getEntryDTOsForPayment() {
			if (event instanceof InstitutionAffiliationEvent) {
				final EntryDTO entryDTO = entryDTOs.iterator().next();
				entryDTO.setAmountToPay(entryDTO.getPayedAmount().add(value));
			}
			return entryDTOs;
		}

	}

	public ActionForward searchPeople(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		SearchBean searchBean = (SearchBean) getRenderedObject("searchBean");
		RenderUtils.invalidateViewState();
		if (searchBean == null) {
			searchBean = new SearchBean();
		} else {
			final SortedSet<Person> people = searchBean.getSearchResult();
			if (people.size() == 1) {
				return viewProfile(people.iterator().next(), mapping, request);
			}
			request.setAttribute("people", people);
		}
		request.setAttribute("searchBean", searchBean);

		return mapping.findForward("paymentManagement");
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

	public ActionForward viewEvent(final Event event, final ActionMapping mapping, final HttpServletRequest request) {
		request.setAttribute("event", event);

		final Person person = event.getPerson();

		if (!event.getAmountToPay().isZero() || person.getOpenAffiliationEvent() == event) {
			final PaymentBean paymentBean = new PaymentBean(event);
			request.setAttribute("paymentBean", paymentBean);
		}

		return viewProfile(person, mapping, request);
	}

	public ActionForward viewEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		final Event event = getDomainObject(request, "eventId");
		return viewEvent(event, mapping, request);
	}

	public ActionForward payEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		final PaymentBean paymentBean = getRenderedObject("paymentBean");
		final Event event = paymentBean.getEvent();
		final User currentUser = AccessControl.getPerson().getUser();

		final Receipt receipt =
				CreatePaymentsForEvents.run(currentUser, paymentBean.getEntryDTOsForPayment(), PaymentMode.CASH, false,
						paymentBean.getPaymentDateTime(), event.getPerson(), paymentBean.getContributorParty(),
						paymentBean.getContributorParty() == null ? paymentBean.getContributorName() : null);
		request.setAttribute("receipt", receipt);

		return viewEvent(event, mapping, request);
	}

	public ActionForward searchPeople(final SearchBean searchBean, final ActionMapping mapping, final HttpServletRequest request) {
		request.setAttribute("searchBean", searchBean);
		return mapping.findForward("paymentManagement");
	}

	public ActionForward showPerson(final Person person, final ActionMapping mapping, final HttpServletRequest request) {
		final SearchBean searchBean = new SearchBean();
		searchBean.setSearchString(person.getUsername());
		return searchPeople(searchBean, mapping, request);
	}

}
