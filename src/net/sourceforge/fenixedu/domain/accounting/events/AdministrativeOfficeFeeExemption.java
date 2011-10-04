package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.AdministrativeOfficeFeeAndInsuranceExemptionJustificationFactory;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.administrativeOfficeFee.IAdministrativeOfficeFeeEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import dml.runtime.RelationAdapter;

public class AdministrativeOfficeFeeExemption extends AdministrativeOfficeFeeExemption_Base implements
	IAdministrativeOfficeFeeEvent {
    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {
	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {

		if (exemption instanceof AdministrativeOfficeFeeAndInsuranceExemption && event != null) {
		    final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent = (AdministrativeOfficeFeeAndInsuranceEvent) event;
		    if (administrativeOfficeFeeAndInsuranceEvent.hasAdministrativeOfficeFeeAndInsuranceExemption()) {
			throw new DomainException(
				"error.net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemption.event.already.has.exemption");

		    }
		}
	    }
	});
    }

    protected AdministrativeOfficeFeeExemption() {
	super();
    }

    public AdministrativeOfficeFeeExemption(Employee employee,
	    AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent,
	    AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, String reason,
	    YearMonthDay dispatchDate) {
	this();

	super.init(employee, administrativeOfficeFeeAndInsuranceEvent,
		AdministrativeOfficeFeeAndInsuranceExemptionJustificationFactory.create(this, justificationType, reason,
			dispatchDate));

	administrativeOfficeFeeAndInsuranceEvent.recalculateState(new DateTime());
    }

    @Override
    public boolean isAdministrativeOfficeFeeExemption() {
	return true;
    }

    @Override
    public boolean isForAdministrativeOfficeFee() {
	return true;
    }

    public String getKindDescription() {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources");
	return bundle.getString(this.getClass().getSimpleName() + ".kindDescription");
    }
}
