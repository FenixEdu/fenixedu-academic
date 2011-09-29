package net.sourceforge.fenixedu.presentationTier.Action.microPayments;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.MicroPaymentEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA.SearchBean;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;
import net.sourceforge.fenixedu.util.Money;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/operator", module = "microPayments")
@Forwards({
    @Forward(name = "start", path = "/microPayments/operator.jsp"),
    @Forward(name = "viewProfile", path = "/microPayments/viewProfile.jsp")
})
public class MicroPaymentsOperator extends FenixDispatchAction {

    public static class MicroPaymentCreationBean implements Serializable {

	private Person person;

	private Money amount = new Money(0.0);

	private Unit unit;

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
    }

    private static Set<Unit> getUnitsForCurrentUser() {
	final Set<Unit> units = new HashSet<Unit>();
	for (final PersonFunction function : AccessControl.getPerson().getActivePersonFunctions()) {
	    if (function.getFunction().getFunctionType().equals(FunctionType.MICRO_PAYMENT_MANAGER)
		    && function.isActive(new YearMonthDay())) {
		units.add((Unit) function.getParentParty());
	    }
	}
	return units;
    }

    public static class MicroPaymentUnitsProvider extends AbstractDomainObjectProvider {
	@Override
	public Object provide(Object source, Object currentValue) {
	    return getUnitsForCurrentUser();
	}
    }

    public ActionForward startPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
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

	return mapping.findForward("start");
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
	    MicroPaymentEvent.create(getLoggedPerson(request).getUser(), person, microPayment.getUnit(), microPayment.getAmount());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getKey());
	}
	return viewProfile(person, mapping, request);
    }
}
