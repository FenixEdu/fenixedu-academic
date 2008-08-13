package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoPaymentTransaction;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadPaymentTransactionByGuideEntryID extends Service {

    public InfoPaymentTransaction run(Integer guideEntryId) throws FenixServiceException{
        GuideEntry guideEntry = rootDomainObject.readGuideEntryByOID(guideEntryId);
        PaymentTransaction paymentTransaction = guideEntry.getPaymentTransaction();
        if (paymentTransaction == null) {
            throw new ExcepcaoInexistente();
        }

        return InfoPaymentTransaction.newInfoFromDomain(paymentTransaction);
    }

}
