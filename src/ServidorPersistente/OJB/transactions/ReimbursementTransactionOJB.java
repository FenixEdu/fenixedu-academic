package ServidorPersistente.OJB.transactions;

import org.apache.ojb.broker.query.Criteria;

import Dominio.reimbursementGuide.IReimbursementGuideEntry;
import Dominio.transactions.IReimbursementTransaction;
import Dominio.transactions.ReimbursementTransaction;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.transactions.IPersistentReimbursementTransaction;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReimbursementTransactionOJB extends ObjectFenixOJB implements
        IPersistentReimbursementTransaction {

    public ReimbursementTransactionOJB() {
    }

    public IReimbursementTransaction readByReimbursementGuideEntry(
            IReimbursementGuideEntry reimbursementGuideEntry) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("reimbursementGuideEntry.idInternal", reimbursementGuideEntry
                .getIdInternal());

        return (IReimbursementTransaction) queryObject(ReimbursementTransaction.class, criteria);

    }

}