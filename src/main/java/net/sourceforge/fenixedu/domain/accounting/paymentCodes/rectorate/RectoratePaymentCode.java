package net.sourceforge.fenixedu.domain.accounting.paymentCodes.rectorate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class RectoratePaymentCode extends RectoratePaymentCode_Base {

    protected RectoratePaymentCode(final YearMonthDay startDate, final YearMonthDay endDate, final Money minAmount,
            final Money maxAmount) {
        super();
        init(PaymentCodeType.RECTORATE, startDate, endDate, minAmount, maxAmount, null);
    }

    @Override
    protected void internalProcess(Person person, Money amount, DateTime whenRegistered, String sibsTransactionId, String comments) {

    }

    @Override
    public boolean isForRectorate() {
        return true;
    }

    public static RectoratePaymentCode create(final LocalDate startDate, final LocalDate endDate, final Money minAmount,
            final Money maxAmount) {
        return new RectoratePaymentCode(startDate.toDateTimeAtStartOfDay().toYearMonthDay(), endDate.toDateTimeAtStartOfDay()
                .toYearMonthDay(), minAmount, maxAmount);
    }

    public static List<RectoratePaymentCode> getAllRectoratePaymentCodes() {
        List<RectoratePaymentCode> result = new ArrayList<RectoratePaymentCode>();

        Collection<PaymentCode> paymentCodes = RootDomainObject.getInstance().getPaymentCodes();

        for (PaymentCode paymentCode : paymentCodes) {
            if (paymentCode.isForRectorate() && !StringUtils.isEmpty(paymentCode.getCode())) {
                result.add((RectoratePaymentCode) paymentCode);
            }
        }

        return result;
    }

}
