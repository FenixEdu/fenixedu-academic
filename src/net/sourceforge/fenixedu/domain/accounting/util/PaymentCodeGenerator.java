package net.sourceforge.fenixedu.domain.accounting.util;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;

public abstract class PaymentCodeGenerator {
	public abstract boolean canGenerateNewCode(final PaymentCodeType paymentCodeType, final Person person);

	public abstract String generateNewCodeFor(final PaymentCodeType paymentCodeType, final Person person);

	public abstract boolean isCodeMadeByThisFactory(final PaymentCode paymentCode);
}
