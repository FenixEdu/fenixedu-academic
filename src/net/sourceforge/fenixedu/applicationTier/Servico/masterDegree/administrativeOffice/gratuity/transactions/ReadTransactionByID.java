package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoTransaction;
import net.sourceforge.fenixedu.domain.transactions.ITransaction;
import net.sourceforge.fenixedu.domain.transactions.Transaction;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

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