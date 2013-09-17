package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.AdministrativeOfficeFeeAndInsuranceExemptionJustificationFactory;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class AdministrativeOfficeFeeAndInsuranceExemption extends AdministrativeOfficeFeeAndInsuranceExemption_Base {

    static {
        getRelationExemptionEvent().addListener(new RelationAdapter<Exemption, Event>() {
            @Override
            public void beforeAdd(Exemption exemption, Event event) {

                if (exemption instanceof AdministrativeOfficeFeeAndInsuranceExemption && event != null) {
                    final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent =
                            (AdministrativeOfficeFeeAndInsuranceEvent) event;
                    if (administrativeOfficeFeeAndInsuranceEvent.hasAdministrativeOfficeFeeAndInsuranceExemption()) {
                        throw new DomainException(
                                "error.net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemption.event.already.has.exemption");

                    }
                }
            }
        });
    }

    protected AdministrativeOfficeFeeAndInsuranceExemption() {
        super();
    }

    public AdministrativeOfficeFeeAndInsuranceExemption(Person responsible,
            AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent,
            AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, String reason,
            YearMonthDay dispatchDate) {
        this();

        super.init(responsible, administrativeOfficeFeeAndInsuranceEvent,
                AdministrativeOfficeFeeAndInsuranceExemptionJustificationFactory.create(this, justificationType, reason,
                        dispatchDate));

        administrativeOfficeFeeAndInsuranceEvent.recalculateState(new DateTime());
    }

    @Override
    public boolean isAdministrativeOfficeFeeAndInsuranceExemption() {
        return true;
    }

    @Override
    public boolean isForAdministrativeOfficeFee() {
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
