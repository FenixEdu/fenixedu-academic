package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class ImprovementOfApprovedEnrolmentPenaltyExemption extends ImprovementOfApprovedEnrolmentPenaltyExemption_Base {

    static {
        getRelationExemptionEvent().addListener(new RelationAdapter<Exemption, Event>() {
            @Override
            public void beforeAdd(Exemption exemption, Event event) {
                if (exemption != null && event != null) {
                    if (exemption instanceof ImprovementOfApprovedEnrolmentPenaltyExemption) {
                        final ImprovementOfApprovedEnrolmentEvent improvementOfApprovedEnrolmentEvent =
                                ((ImprovementOfApprovedEnrolmentEvent) event);
                        if (improvementOfApprovedEnrolmentEvent.hasImprovementOfApprovedEnrolmentPenaltyExemption()) {
                            throw new DomainException(
                                    "error.accounting.events.ImprovementOfApprovedEnrolmentPenaltyExemption.event.already.has.exemption");
                        }

                    }
                }
            }
        });
    }

    protected ImprovementOfApprovedEnrolmentPenaltyExemption() {
        super();
    }

    public ImprovementOfApprovedEnrolmentPenaltyExemption(final PenaltyExemptionJustificationType penaltyExemptionType,
            final ImprovementOfApprovedEnrolmentEvent improvementOfApprovedEnrolmentEvent, final Person responsible,
            final String comments, final YearMonthDay directiveCouncilDispatchDate) {
        this();
        super.init(penaltyExemptionType, improvementOfApprovedEnrolmentEvent, responsible, comments, directiveCouncilDispatchDate);
    }

}
