package org.fenixedu.academic.domain.accounting;

import org.fenixedu.academic.ui.spring.service.AccountingManagementAccessControlService;
import org.fenixedu.bennu.core.domain.User;

public class ProofOfPaymentFile extends ProofOfPaymentFile_Base {
    
    public ProofOfPaymentFile(final ProofOfPayment proofOfPayment, final byte[] proofOfPaymentContent, final String contentType) {
        setProofOfPayment(proofOfPayment);
        init("ProofOfPayment", "ProofOfPayment" + suffix(contentType), proofOfPaymentContent);
    }

    private String suffix(final String contentType) {
        final int i = contentType == null ? -1 : contentType.indexOf('/');
        return i >= 0 ? contentType.substring(i + 1) : "";
    }

    @Override
    public boolean isAccessible(final User user) {
        return new AccountingManagementAccessControlService().isPaymentManager(user)
                || (user != null && getProofOfPayment().getEvent().getPerson() == user.getPerson());
    }

    @Override
    public void delete() {
        setProofOfPayment(null);
        super.delete();
    }

}
