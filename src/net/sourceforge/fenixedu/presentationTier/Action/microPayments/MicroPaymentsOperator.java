package net.sourceforge.fenixedu.presentationTier.Action.microPayments;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;
import net.sourceforge.fenixedu.util.Money;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/operator", module = "microPayments")
@Forwards({ @Forward(name = "start", path = "/microPayments/operator.jsp") })
public class MicroPaymentsOperator extends FenixDispatchAction {
    public static class MicroPaymentCreationBean implements Serializable {
	private String person;

	private Money amount;

	private Unit unit;

	public String getPerson() {
	    return person;
	}

	public void setPerson(String person) {
	    this.person = person;
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

    public static class MicroPaymentUnitsProvider extends AbstractDomainObjectProvider {
	@Override
	public Object provide(Object source, Object currentValue) {
	    Set<Unit> units = new HashSet<Unit>();
	    for (PersonFunction function : AccessControl.getPerson().getActivePersonFunctions()) {
		if (function.getFunction().getFunctionType().equals(FunctionType.MICRO_PAYMENT_MANAGER)) {
		    units.add((Unit) function.getParentParty());
		}
	    }
	    return units;
	}
    }

    public ActionForward startPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("microPayment", new MicroPaymentCreationBean());
	return mapping.findForward("start");
    }

    public ActionForward createMicroPayment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	MicroPaymentCreationBean microPayment = getRenderedObject("microPayment");
	try {
	    MicroPaymentEvent event = MicroPaymentEvent.create(getLoggedPerson(request).getUser(),
		    Person.findByUsername(microPayment.getPerson()), microPayment.getUnit(), microPayment.getAmount());
	    // addActionMessage(request, "info.account",
	    // event.getAmountToPay());
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey());
	}
	return mapping.findForward("start");
    }
}
