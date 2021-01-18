package org.fenixedu.academic.domain.accounting;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import pt.ist.fenixframework.Atomic;

public class ProofOfPayment extends ProofOfPayment_Base {
    
    public ProofOfPayment(final Event event, final byte[] proofOfPaymentContent, final String contentType) {
        setBennu(Bennu.getInstance());
        setEvent(event);
        setUploadDate(new DateTime());
        new ProofOfPaymentFile(this, proofOfPaymentContent, contentType);
    }

    protected void delete() {
        getProofOfPaymentFile().delete();
        setEvent(null);
        setBennu(null);
        super.deleteDomainObject();
    }

    @Atomic
    public void markAsProcessed(final AccountingTransaction accountingTransaction) {
        if (accountingTransaction.getEvent() != getEvent()) {
            throw new Error("Event does not match: " + accountingTransaction.getEvent().getExternalId()
                + " != " + getEvent().getExternalId());
        }
        setBennu(null);
        setVerificationDate(new DateTime());
        setVerificationUsername(Authenticate.getUser().getUsername());
        setAccountingTransactionDetail(accountingTransaction.getTransactionDetail());
    }

    @Atomic
    public void reject() {
        setBennu(null);
        setVerificationDate(new DateTime());
        final User user = Authenticate.getUser();
        if (user != null) {
            setVerificationUsername(user.getUsername());
        }
        setAccountingTransactionDetail(null);
    }

}
