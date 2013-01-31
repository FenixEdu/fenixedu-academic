package net.sourceforge.fenixedu.domain.accounting.util;

import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;

public class PaymentCodeGeneratorFactory {
	private static final PersonRotationPaymentCodeGenerator personRotationPaymentCodeGenerator =
			new PersonRotationPaymentCodeGenerator();
	private static final PersonPaymentCodeGenerator personPaymentCodeGenerator = new PersonPaymentCodeGenerator();
	private static final IndividualCandidacyPaymentCodeGenerator individualCandidacyPaymentCodeGenerator =
			new IndividualCandidacyPaymentCodeGenerator();
	private static final RectoratePaymentCodeGenerator rectoratePaymentCodeGenerator = new RectoratePaymentCodeGenerator();

	public static PaymentCodeGenerator getGenerator(PaymentCodeType type) {
		switch (type) {
		case TOTAL_GRATUITY:
		case GRATUITY_FIRST_INSTALLMENT:
		case GRATUITY_SECOND_INSTALLMENT:
		case ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE:
		case INSURANCE:
		case PRE_BOLONHA_MASTER_DEGREE_TOTAL_GRATUITY:
		case PRE_BOLONHA_MASTER_DEGREE_INSURANCE:
		case RESIDENCE_FEE:
			return personRotationPaymentCodeGenerator;
		case INSTITUTION_ACCOUNT_CREDIT:
			return personPaymentCodeGenerator;
		case INTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS:
		case EXTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS:
		case INTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS:
		case EXTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS:
		case SECOND_CYCLE_INDIVIDUAL_CANDIDACY_PROCESS:
		case INTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS:
		case EXTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS:
		case OVER_23_INDIVIDUAL_CANDIDACY_PROCESS:
		case PHD_PROGRAM_CANDIDACY_PROCESS:
			return individualCandidacyPaymentCodeGenerator;
		case RECTORATE:
			return rectoratePaymentCodeGenerator;
		default:
			return null;
		}
	}
}
