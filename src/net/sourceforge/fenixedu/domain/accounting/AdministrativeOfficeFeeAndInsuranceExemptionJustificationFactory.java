package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustification;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustificationType;

import org.joda.time.YearMonthDay;

public class AdministrativeOfficeFeeAndInsuranceExemptionJustificationFactory {

	public static AdministrativeOfficeFeeAndInsuranceExemptionJustification create(
			final Exemption administrativeOfficeFeeAndInsuranceExemption,
			final AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, final String reason,
			final YearMonthDay dispatchDate) {

		switch (justificationType) {
		case MIT_AGREEMENT:
			return new AdministrativeOfficeFeeAndInsuranceExemptionJustification(administrativeOfficeFeeAndInsuranceExemption,
					justificationType, reason);
		case DIRECTIVE_COUNCIL_AUTHORIZATION:
			return new AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch(
					administrativeOfficeFeeAndInsuranceExemption, justificationType, reason, dispatchDate);
		default:
			throw new RuntimeException("Unknown justification type");

		}

	}
}
