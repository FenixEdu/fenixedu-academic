/*
 * Created on Jan 23, 2004
 */
package ServidorPersistente.OJB.grant.contract;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantPart;
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

}
