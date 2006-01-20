package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoPaymentTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */

public class ReadPaymentTransactionByGuideEntryID extends Service {

    public InfoPaymentTransaction run(Integer guideEntryId) throws FenixServiceException,
            ExcepcaoPersistencia {

        InfoPaymentTransaction infoPaymentTransaction = null;

        PaymentTransaction paymentTransaction = persistentSupport.getIPersistentPaymentTransaction()
                .readByGuideEntryID(guideEntryId);

        if (paymentTransaction == null) {
            throw new ExcepcaoInexistente();
        }

        infoPaymentTransaction = InfoPaymentTransaction.newInfoFromDomain(paymentTransaction);

        return infoPaymentTransaction;
    }

}