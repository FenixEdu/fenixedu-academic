package net.sourceforge.fenixedu.domain.accounting.events;

import org.joda.time.YearMonthDay;

public class InsuranceExemptionJustificationFactory {

	public static InsuranceExemptionJustification create(final InsuranceExemption administrativeOfficeFeeAndInsuranceExemption,
			final InsuranceExemptionJustificationType justificationType, final String reason, final YearMonthDay dispatchDate) {

		switch (justificationType) {
		case DIRECTIVE_COUNCIL_AUTHORIZATION:
		case MIT_AGREEMENT:
			return new InsuranceExemptionJustificationByDispatch(administrativeOfficeFeeAndInsuranceExemption, justificationType,
					reason, dispatchDate);
		default:
			throw new RuntimeException("Unknown justification type");

		}

	}

}
