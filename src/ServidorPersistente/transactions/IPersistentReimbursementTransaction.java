package ServidorPersistente.transactions;

import Dominio.reimbursementGuide.IReimbursementGuideEntry;
import Dominio.transactions.IReimbursementTransaction;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IPersistentReimbursementTransaction extends IPersistentObject {

    public IReimbursementTransaction readByReimbursementGuideEntry(
            IReimbursementGuideEntry reimbursementGuideEntry) throws ExcepcaoPersistencia;
}