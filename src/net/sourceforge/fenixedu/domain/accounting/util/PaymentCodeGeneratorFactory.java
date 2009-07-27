package net.sourceforge.fenixedu.domain.accounting.util;

import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.GratuitySituationPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.IndividualCandidacyPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.MasterDegreeInsurancePaymentCode;

public class PaymentCodeGeneratorFactory {
    private static final StudentPaymentCodeGenerator studentPaymentCodeGenerator = new StudentPaymentCodeGenerator();
    private static final IndividualCandidacyPaymentCodeGenerator individualCandidacyPaymentCodeGenerator = new IndividualCandidacyPaymentCodeGenerator();

    public static PaymentCodeGenerator getGenerator(Class<? extends PaymentCode> paymentCodeType) {
	if (IndividualCandidacyPaymentCode.class.isAssignableFrom(paymentCodeType))
	    return individualCandidacyPaymentCodeGenerator;
	else if (AccountingEventPaymentCode.class.isAssignableFrom(paymentCodeType))
	    return studentPaymentCodeGenerator;
	else if (GratuitySituationPaymentCode.class.isAssignableFrom(paymentCodeType))
	    return studentPaymentCodeGenerator;
	else if (MasterDegreeInsurancePaymentCode.class.isAssignableFrom(paymentCodeType))
	    return studentPaymentCodeGenerator;

	return null;
    }
}
