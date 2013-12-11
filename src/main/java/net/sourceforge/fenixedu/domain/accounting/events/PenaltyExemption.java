package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

abstract public class PenaltyExemption extends PenaltyExemption_Base {

    protected PenaltyExemption() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
    }

    protected PenaltyExemption(final PenaltyExemptionJustificationType justificationType, final GratuityEvent gratuityEvent,
            final Person responsible, final String comments, final YearMonthDay dispatchDate) {
        this();
        init(justificationType, gratuityEvent, responsible, comments, dispatchDate);
    }

    protected void init(PenaltyExemptionJustificationType justificationType, Event event, Person responsible, String reason,
            YearMonthDay dispatchDate) {
        checkParameters(justificationType);
        super.init(responsible, event, PenaltyExemptionJustificationFactory.create(this, justificationType, reason, dispatchDate));
        event.recalculateState(new DateTime());

    }

    private void checkParameters(PenaltyExemptionJustificationType penaltyExemptionType) {
        if (penaltyExemptionType == null) {
            throw new DomainException(
                    "error.accounting.events.gratuity.exemption.penalty.PenaltyExemption.penaltyExemptionType.cannot.be.null");
        }
    }

//    @Override
//    public void setResponsible(Person responsible) {
//        throw new DomainException(
//                "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.PenaltyExemption.cannot.modify.responsible");
//    }

    public PenaltyExemptionJustificationType getJustificationType() {
        return getExemptionJustification().getPenaltyExemptionJustificationType();
    }

    @Override
    public PenaltyExemptionJustification getExemptionJustification() {
        return (PenaltyExemptionJustification) super.getExemptionJustification();
    }

    @Override
    public boolean isPenaltyExemption() {
        return true;
    }

}
