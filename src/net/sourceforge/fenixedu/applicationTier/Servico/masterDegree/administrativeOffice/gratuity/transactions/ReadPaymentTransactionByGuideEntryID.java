package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoPaymentTransaction;
import net.sourceforge.fenixedu.domain.transactions.IPaymentTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */

public class ReadPaymentTransactionByGuideEntryID implements IService {

    public InfoPaymentTransaction run(Integer guideEntryId) throws FenixServiceException,
            ExcepcaoPersistencia {

        InfoPaymentTransaction infoPaymentTransaction = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPaymentTransaction paymentTransaction = sp.getIPersistentPaymentTransaction()
                .readByGuideEntryID(guideEntryId);

        if (paymentTransaction == null) {
            throw new ExcepcaoInexistente();
        }

        infoPaymentTransaction = InfoPaymentTransaction.newInfoFromDomain(paymentTransaction);

        return infoPaymentTransaction;
    }

}