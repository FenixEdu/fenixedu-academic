package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.transactions.InfoTransaction;
import Dominio.transactions.ITransaction;
import Dominio.transactions.Transaction;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReadTransactionByID implements IService {

    public ReadTransactionByID() {

    }

    public InfoTransaction run(Integer transactionId) throws FenixServiceException {

        InfoTransaction infoTransaction = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ITransaction transaction = (ITransaction) sp.getIPersistentTransaction().readByOID(
                    Transaction.class, transactionId);

            if (transaction == null) {
                throw new ExcepcaoInexistente();
            }
            infoTransaction = InfoTransaction.newInfoFromDomain(transaction);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoTransaction;
    }

}