package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import dml.runtime.RelationAdapter;

public class InsuranceExemption extends InsuranceExemption_Base {

    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {
	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {

		if (exemption instanceof InsuranceExemption && event != null) {
		    final InsuranceEvent insuranceEvent = (InsuranceEvent) event;
		    if (insuranceEvent.hasInsuranceExemption()) {
			throw new DomainException(
				"error.net.sourceforge.fenixedu.domain.accounting.events.InsuranceExemption.event.already.has.exemption");

		    }
		}
	    }
	});
    }

    public InsuranceExemption() {
	super();
    }

    public InsuranceExemption(Employee employee, InsuranceEvent administrativeOfficeFeeAndInsuranceEvent,
	    InsuranceExemptionJustificationType justificationType, String reason, YearMonthDay dispatchDate) {
	this();

	super.init(employee, administrativeOfficeFeeAndInsuranceEvent, InsuranceExemptionJustificationFactory.create(this,
		justificationType, reason, dispatchDate));

	administrativeOfficeFeeAndInsuranceEvent.recalculateState(new DateTime());
    }

    public boolean isInsuranceExemption() {
	return true;
    }
}
