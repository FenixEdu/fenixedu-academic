package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoTransaction;
import net.sourceforge.fenixedu.domain.transactions.Transaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadTransactionByID implements IService {

	public InfoTransaction run(Integer transactionId) throws FenixServiceException, ExcepcaoPersistencia {
		InfoTransaction infoTransaction = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		Transaction transaction = (Transaction) sp.getIPersistentTransaction().readByOID(
				Transaction.class, transactionId);

		if (transaction == null) {
			throw new ExcepcaoInexistente();
		}
		infoTransaction = InfoTransaction.newInfoFromDomain(transaction);

		return infoTransaction;
	}

}