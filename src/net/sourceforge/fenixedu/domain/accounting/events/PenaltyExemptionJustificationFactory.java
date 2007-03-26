package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.accounting.events.penaltyExemptionJustifications.PenaltyExemptionJustificationByDispatch;

import org.joda.time.YearMonthDay;

public class PenaltyExemptionJustificationFactory {

    public static PenaltyExemptionJustification create(final PenaltyExemption penaltyExemption,
	    final PenaltyExemptionJustificationType justificationType, final String reason,
	    final YearMonthDay dispatchDate) {

	switch (justificationType) {

	case SOCIAL_SHARE_GRANT_OWNER:
	case ENROLMENT_AFTER_EQUIVALENCE:
	    return new PenaltyExemptionJustification(penaltyExemption, justificationType, reason);
	case DIRECTIVE_COUNCIL_AUTHORIZATION:
	case NUCLEUS_COORDINATOR_AUTHORIZATION:
	    return new PenaltyExemptionJustificationByDispatch(penaltyExemption,
		    justificationType, reason, dispatchDate);

	default:
	    throw new RuntimeException("Unknown justification type");

	}

    }
}
