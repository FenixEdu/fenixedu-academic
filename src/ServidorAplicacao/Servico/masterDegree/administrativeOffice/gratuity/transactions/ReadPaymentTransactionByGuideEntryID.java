package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.transactions.InfoPaymentTransaction;
import Dominio.transactions.IPaymentTransaction;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */

public class ReadPaymentTransactionByGuideEntryID implements IService {

    public InfoPaymentTransaction run(Integer guideEntryId) throws FenixServiceException,
            ExcepcaoPersistencia {

        InfoPaymentTransaction infoPaymentTransaction = null;

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        IPaymentTransaction paymentTransaction = sp.getIPersistentPaymentTransaction()
                .readByGuideEntryID(guideEntryId);

        if (paymentTransaction == null) {
            throw new ExcepcaoInexistente();
        }

        infoPaymentTransaction = InfoPaymentTransaction.newInfoFromDomain(paymentTransaction);

        return infoPaymentTransaction;
    }

}