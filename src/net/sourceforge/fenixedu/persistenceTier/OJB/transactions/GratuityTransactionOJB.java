package net.sourceforge.fenixedu.persistenceTier.OJB.transactions;

import java.util.List;

import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.ObjectFenixOJB;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentGratuityTransaction;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class GratuityTransactionOJB extends ObjectFenixOJB implements IPersistentGratuityTransaction {

    public List readAllByGratuitySituation(Integer gratuitySituationID)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("gratuitySituation.idInternal", gratuitySituationID);
        return queryList(GratuityTransaction.class, criteria);

    }

}