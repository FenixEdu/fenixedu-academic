package ServidorPersistente.OJB.transactions;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IPersonAccount;
import Dominio.transactions.IPaymentTransaction;
import Dominio.transactions.InsuranceTransaction;
import Dominio.transactions.PaymentTransaction;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.transactions.IPersistentPaymentTransaction;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class PaymentTransactionOJB extends ObjectFenixOJB implements IPersistentPaymentTransaction {

    public PaymentTransactionOJB() {
    }

    public IPaymentTransaction readByGuideEntryID(Integer guideEntryID) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("guideEntry.idInternal", guideEntryID);

        return (IPaymentTransaction) queryObject(PaymentTransaction.class, crit);

    }

}