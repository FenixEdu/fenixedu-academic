/*
 * Created on Jan 23, 2004
 */
package ServidorPersistente.OJB.grant.contract;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantPart;
import Dominio.grant.contract.IGrantPart;
import Dominio.grant.contract.IGrantPaymentEntity;
import Dominio.grant.contract.IGrantSubsidy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.grant.IPersistentGrantPart;

/**
 * @author Pica
 * @author Barbosa
 */
public class GrantPartOJB extends ObjectFenixOJB implements IPersistentGrantPart
{
    public GrantPartOJB()
    {
    }
    
    public List readAllGrantPartsByGrantSubsidy(Integer grantSubsidyId) throws ExcepcaoPersistencia
    {
        List grantPartsList = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_subsidy", grantSubsidyId);
        grantPartsList = queryList(GrantPart.class,criteria);
        return grantPartsList;
    }
    
    public IGrantPart readGrantPartByUnique(IGrantSubsidy grantSubsidy, IGrantPaymentEntity paymentEntity) throws ExcepcaoPersistencia
    {
        IGrantPart grantPart = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_subsidy", grantSubsidy.getIdInternal());
        criteria.addEqualTo("key_grant_payment_entity", paymentEntity.getIdInternal());
        grantPart = (IGrantPart) queryObject(GrantPart.class,criteria);
        return grantPart;
    }

}
