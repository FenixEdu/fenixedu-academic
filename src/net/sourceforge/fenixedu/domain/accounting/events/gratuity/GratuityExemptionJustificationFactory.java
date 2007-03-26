package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import org.joda.time.YearMonthDay;

public class GratuityExemptionJustificationFactory {

    public static GratuityExemptionJustification create(final GratuityExemption gratuityExemption,
	    final GratuityExemptionJustificationType justificationType, final String reason,
	    final YearMonthDay dispatchDate) {

	switch (justificationType) {
	case INSTITUTION:
	case INSTITUTION_GRANT_OWNER:
	case OTHER_INSTITUTION:
	case PALOP_TEACHER:
	case SON_OF_DECORATED_MILITARY:
	case SOCIAL_SHARE_GRANT_OWNER:
	case STUDENT_TEACH:
	    return new GratuityExemptionJustification(gratuityExemption, justificationType, reason);
	case DIRECTIVE_COUNCIL_AUTHORIZATION:
	    return new GratuityExemptionJustificationByDispatch(gratuityExemption, justificationType,
		    reason, dispatchDate);
	default:
	    throw new RuntimeException("Unknown justification type");

	}

    }
}
