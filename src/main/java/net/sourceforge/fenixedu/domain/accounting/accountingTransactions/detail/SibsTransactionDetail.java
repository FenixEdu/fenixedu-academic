package net.sourceforge.fenixedu.domain.accounting.accountingTransactions.detail;

import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class SibsTransactionDetail extends SibsTransactionDetail_Base {

    protected SibsTransactionDetail() {
        super();
    }

    public SibsTransactionDetail(DateTime whenRegistered, String sibsTransactionId, String sibsCode, String comments) {
        this();
        init(whenRegistered, sibsTransactionId, sibsCode, comments);
    }

    protected void init(DateTime whenRegistered, String sibsTransactionId, String sibsCode, String comments) {

        super.init(whenRegistered, PaymentMode.ATM, comments);

        checkParameters(sibsTransactionId, sibsCode);

        super.setSibsTransactionId(sibsTransactionId);
        super.setSibsCode(sibsCode);
    }

    private void checkParameters(String sibsTransactionId, String sibsCode) {
        if (sibsTransactionId == null) {
            throw new DomainException(
                    "error.accounting.accountingTransactions.detail.SibsTransactionDetail.sibsTransactionId.cannot.be.null");
        }

        if (sibsCode == null) {
            throw new DomainException(
                    "error.accounting.accountingTransactions.detail.SibsTransactionDetail.sibsCode.cannot.be.null");
        }

    }

    @Override
    public void setSibsTransactionId(String sibsTransactionId) {
        throw new DomainException(
                "error.accounting.accountingTransactions.detail.SibsTransactionDetail.cannot.modify.sibsTransactionId");

    }

    @Override
    public void setSibsCode(String sibsCode) {
        throw new DomainException("error.accounting.accountingTransactions.detail.SibsTransactionDetail.cannot.modify.sibsCode");
    }

    @Deprecated
    public boolean hasSibsCode() {
        return getSibsCode() != null;
    }

    @Deprecated
    public boolean hasSibsTransactionId() {
        return getSibsTransactionId() != null;
    }

}
