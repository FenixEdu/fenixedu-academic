package net.sourceforge.fenixedu.applicationTier.Servico.accounting.postingRules.serviceRequests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRules.serviceRequests.CertificateRequestPRDTO;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.CertificateRequestPR;

import org.joda.time.DateTime;

public class EditCertificateRequestPR extends Service {

    public void run(final CertificateRequestPRDTO certificateRequestPRDTO) {

        final CertificateRequestPR certificateRequestPR = certificateRequestPRDTO.getPostingRule();
        certificateRequestPR.setEndDate(new DateTime().minus(10000));

        new CertificateRequestPR(certificateRequestPR.getEntryType(), certificateRequestPR
                .getEventType(), new DateTime().minus(1000), null, certificateRequestPR
                .getServiceAgreementTemplate(), certificateRequestPRDTO.getBaseAmount(),
                certificateRequestPRDTO.getAmountPerUnit(), certificateRequestPRDTO.getAmountPerPage());

    }

}
