package ServidorPersistente.OJB.transactions;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IPersonAccount;
import Dominio.transactions.InsuranceTransaction;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.transactions.IPersistentTransaction;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class TransactionOJB extends ObjectFenixOJB implements IPersistentTransaction {

    public TransactionOJB() {
    }

    public List readAllByPersonAccount(IPersonAccount personAccount) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("personAccount.idInternal", personAccount.getIdInternal());

        return queryList(InsuranceTransaction.class, crit);
    }

}