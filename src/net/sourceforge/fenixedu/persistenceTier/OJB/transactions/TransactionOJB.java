package net.sourceforge.fenixedu.persistenceTier.OJB.transactions;

import java.util.List;

import net.sourceforge.fenixedu.domain.IPersonAccount;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.ObjectFenixOJB;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentTransaction;

import org.apache.ojb.broker.query.Criteria;

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