package net.sourceforge.fenixedu.presentationTier.Action.treasury.payments;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(module = "treasury", path = "/paymentManagement", parameter = "method", scope = "request" )
@Forwards(value = {
	@Forward(name = "paymentManagement", path = "treasury.paymentManagement")
})
public class PaymentManagementDA extends FenixDispatchAction {

    public static class SearchBean implements Serializable {

	private String searchString;

	private SearchPerson searchPerson = new SearchPerson();

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
	    }
	    return result;
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

    }

    public static class PaymentBean implements Serializable {

	private final EntryDTO entryDTO;

	private final Event event;

	private DateTime paymentDateTime = new DateTime();

	private String contributorNumber;

	private String contributorName;

	private Money value;

	public PaymentBean(final EntryDTO entryDTO) {
	    this.entryDTO = entryDTO;
	    event = entryDTO.getEvent();
	    value = entryDTO.getAmountToPay();
	    final Person person = event.getPerson();
	    contributorName = person.getName();
	    contributorNumber = person.getSocialSecurityNumber();
	}

//	public PaymentBean(final Event event) {
//	    this.event = event;
//	    value = event.getAmountToPay();
//	    final Person person = event.getPerson();
//	    contributorName = person.getName();
//	    contributorNumber = person.getSocialSecurityNumber();
//	}

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

	public List<EntryDTO> getEntryDTOs() {
	    return Collections.singletonList(entryDTO);
	}

	public Party getContributorParty() {
	    return contributorNumber == null || StringUtils.isEmpty(contributorNumber) ?
		    null : Party.readByContributorNumber(contributorNumber);
	}

    }

    public ActionForward searchPeople(final SearchBean searchBean, final ActionMapping mapping, final HttpServletRequest request) {
	request.setAttribute("searchBean", searchBean);
	return mapping.findForward("paymentManagement");
    }

    public ActionForward searchPeople(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	SearchBean searchBean = (SearchBean) getRenderedObject("searchBean");
	RenderUtils.invalidateViewState();
	if (searchBean == null) {
	    searchBean = new SearchBean();
	}
	return searchPeople(searchBean, mapping, request);
    }

    public ActionForward showPerson(final Person person, final ActionMapping mapping, final HttpServletRequest request) {
	final SearchBean searchBean = new SearchBean();
	searchBean.setSearchString(person.getUsername());
	return searchPeople(searchBean, mapping, request);
    }

    public ActionForward showPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final Person person = getDomainObject(request, "personOid");
	return showPerson(person, mapping, request);
    }

    public ActionForward viewEvent(final Event event, final ActionMapping mapping, final HttpServletRequest request) {
	request.setAttribute("event", event);

	if (!event.getAmountToPay().isZero()) {
	    for (final EntryDTO entryDTO : event.calculateEntries()) {
		final PaymentBean paymentBean = new PaymentBean(entryDTO);
		request.setAttribute("paymentBean", paymentBean);
	    }
	}

	final Person person = event.getPerson();
	return showPerson(person, mapping, request);
    }

    public ActionForward viewEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final Event event = getDomainObject(request, "eventId");
	return viewEvent(event, mapping, request);
    }

    public ActionForward payEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final PaymentBean paymentBean = getRenderedObject("paymentBean");
	final Event event = paymentBean.getEvent();
	final User currentUser = AccessControl.getPerson().getUser();

	final CreatePaymentsForEvents service = new CreatePaymentsForEvents();
	final Receipt receipt = service.run(currentUser, paymentBean.getEntryDTOs(), PaymentMode.CASH, false,
		paymentBean.getPaymentDateTime(), event.getPerson(),
		paymentBean.getContributorParty(), paymentBean.getContributorName(),
		getReceiptCreatorUnit(request), paymentBean.getEvent().getOwnerUnit());
	request.setAttribute("receipt", receipt);

	return viewEvent(event, mapping, request);
    }

    protected Unit getReceiptCreatorUnit(HttpServletRequest request) {
	return getUserView(request).getPerson().getEmployee().getCurrentWorkingPlace();
    }

}
