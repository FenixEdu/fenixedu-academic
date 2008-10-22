package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoTransaction;
import net.sourceforge.fenixedu.domain.transactions.Transaction;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadTransactionByID extends FenixService {

    public InfoTransaction run(Integer transactionId) throws FenixServiceException {
	Transaction transaction = rootDomainObject.readTransactionByOID(transactionId);
	if (transaction == null) {
	    throw new ExcepcaoInexistente();
	}
	return InfoTransaction.newInfoFromDomain(transaction);
    }

}
