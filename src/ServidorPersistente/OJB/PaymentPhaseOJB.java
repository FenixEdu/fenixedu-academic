/*
 * Created on 6/Jan/2004
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IGratuityValues;
import Dominio.PaymentPhase;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPaymentPhase;

/**
 * @author Tânia Pousão
 *  
 */
public class PaymentPhaseOJB extends PersistentObjectOJB implements IPersistentPaymentPhase {
    public void deletePaymentPhasesOfThisGratuity(Integer gratuityValuesID) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("gratuityValues.idInternal", gratuityValuesID);

        List result = queryList(PaymentPhase.class, crit);
        if (result != null) {

            ListIterator iterator = result.listIterator();
            while (iterator.hasNext()) {

                delete(iterator.next());
            }
        }

    }

    public List readByGratuityValues(IGratuityValues gratuityValues) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("gratuityValues.idInternal", gratuityValues.getIdInternal());

        return queryList(PaymentPhase.class, criteria);
    }

}