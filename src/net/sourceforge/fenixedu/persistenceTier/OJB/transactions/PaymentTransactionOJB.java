package net.sourceforge.fenixedu.persistenceTier.OJB.transactions;

import net.sourceforge.fenixedu.domain.transactions.IPaymentTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.ObjectFenixOJB;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentPaymentTransaction;

import org.apache.ojb.broker.query.Criteria;

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