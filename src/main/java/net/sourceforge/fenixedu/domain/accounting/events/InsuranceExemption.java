package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.IInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class InsuranceExemption extends InsuranceExemption_Base {

    static {
        getRelationExemptionEvent().addListener(new RelationAdapter<Exemption, Event>() {
            @Override
            public void beforeAdd(Exemption exemption, Event event) {

                if (exemption instanceof InsuranceExemption && event != null) {
                    AnnualEvent annualEvent = (AnnualEvent) event;

                    if (annualEvent.isInsuranceEvent()) {
                        if (((InsuranceEvent) annualEvent).hasInsuranceExemption()) {
                            throw new DomainException(
                                    "error.net.sourceforge.fenixedu.domain.accounting.events.InsuranceExemption.event.already.has.exemption");
                        }
                    } else if (annualEvent.isAdministrativeOfficeAndInsuranceEvent()) {
                        if (((AdministrativeOfficeFeeAndInsuranceEvent) annualEvent).hasInsuranceExemption()) {
                            throw new DomainException(
                                    "error.net.sourceforge.fenixedu.domain.accounting.events.InsuranceExemption.event.already.has.exemption");
                        }
                    }
                }
            }
        });
    }

    public InsuranceExemption() {
        super();
    }

    public InsuranceExemption(Person responsible, IInsuranceEvent administrativeOfficeFeeAndInsuranceEvent,
            InsuranceExemptionJustificationType justificationType, String reason, YearMonthDay dispatchDate) {
        this();
        super.init(responsible, (Event) administrativeOfficeFeeAndInsuranceEvent,
                InsuranceExemptionJustificationFactory.create(this, justificationType, reason, dispatchDate));

        ((Event) administrativeOfficeFeeAndInsuranceEvent).recalculateState(new DateTime());
    }

    @Override
    public boolean isInsuranceExemption() {
        return true;
    }

    @Override
    public boolean isForInsurance() {
        return true;
    }

    public String getKindDescription() {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources");
        return bundle.getString(this.getClass().getSimpleName() + ".kindDescription");
    }
}
