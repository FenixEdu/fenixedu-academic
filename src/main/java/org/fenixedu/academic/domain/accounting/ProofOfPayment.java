package org.fenixedu.academic.domain.accounting;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

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

}
