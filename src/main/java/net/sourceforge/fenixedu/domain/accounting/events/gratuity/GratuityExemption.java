package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public abstract class GratuityExemption extends GratuityExemption_Base {

    static {
        getRelationExemptionEvent().addListener(new RelationAdapter<Exemption, Event>() {

            @Override
            public void beforeAdd(Exemption exemption, Event event) {
                if (event instanceof GratuityEvent) {
                    if (exemption instanceof GratuityExemption) {
                        final GratuityEvent gratuityEvent = (GratuityEvent) event;
                        if (gratuityEvent.hasGratuityExemption()) {
                            throw new DomainException(
                                    "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemption.event.already.has.gratuity.exemption");
                        }

                        final GratuityExemption gratuityExemption = (GratuityExemption) exemption;
                        if (!gratuityEvent.canApplyExemption(gratuityExemption.getJustificationType())) {
                            throw new DomainException(
                                    "error.accounting.events.gratuity.GratuityExemption.gratuity.exemption.type.cannot.applied.to.gratuity.event");
                        }
                    }
                }
            }
        });
    }

    protected GratuityExemption() {
        super();
    }

    protected void init(final Person responsible, final GratuityEvent gratuityEvent,
            final GratuityExemptionJustificationType exemptionType, final String reason, final YearMonthDay dispatchDate) {
        super.init(responsible, gratuityEvent,
                GratuityExemptionJustificationFactory.create(this, exemptionType, reason, dispatchDate));

        gratuityEvent.recalculateState(new DateTime());
    }

    public GratuityEvent getGratuityEvent() {
        return (GratuityEvent) getEvent();
    }

    public GratuityExemptionJustificationType getJustificationType() {
        return getExemptionJustification().getGratuityExemptionJustificationType();
    }

    @Override
    public GratuityExemptionJustification getExemptionJustification() {
        return (GratuityExemptionJustification) super.getExemptionJustification();
    }

    public boolean isValueExemption() {
        return false;
    }

    public boolean isPercentageExemption() {
        return false;
    }

    abstract public BigDecimal calculateDiscountPercentage(Money amount);

    @Override
    public boolean isGratuityExemption() {
        return true;
    }
}
