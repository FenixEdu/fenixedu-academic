package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import DataBeans.transactions.InfoPaymentTransaction;
import DataBeans.transactions.InfoTransaction;
import Dominio.transactions.IPaymentTransaction;
import Dominio.transactions.ITransaction;
import Dominio.transactions.Transaction;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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