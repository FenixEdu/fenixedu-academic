package ServidorPersistente.OJB.transactions;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IGratuitySituation;
import Dominio.transactions.GratuityTransaction;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.transactions.IPersistentGratuityTransaction;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class GratuityTransactionOJB extends ObjectFenixOJB implements IPersistentGratuityTransaction {

    public GratuityTransactionOJB() {
    }

    public List readAllByGratuitySituation(IGratuitySituation gratuitySituation)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("gratuitySituation.idInternal", gratuitySituation.getIdInternal());
        return queryList(GratuityTransaction.class, criteria);

    }

}