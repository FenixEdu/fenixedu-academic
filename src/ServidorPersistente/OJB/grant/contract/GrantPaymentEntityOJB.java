/*
 * Created on Jan 21, 2004
 */
package ServidorPersistente.OJB.grant.contract;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantPaymentEntity;
import Dominio.grant.contract.IGrantPaymentEntity;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.grant.IPersistentGrantPaymentEntity;

/**
 * @author pica
 * @author barbosa
 */
public class GrantPaymentEntityOJB extends ObjectFenixOJB implements IPersistentGrantPaymentEntity
{
	public IGrantPaymentEntity readByNumber(Integer entityNumber) throws ExcepcaoPersistencia
    {
        IGrantPaymentEntity paymentEntity = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", entityNumber);
        paymentEntity = (IGrantPaymentEntity) queryObject(GrantPaymentEntity.class, criteria);
        return paymentEntity;
    }
}
