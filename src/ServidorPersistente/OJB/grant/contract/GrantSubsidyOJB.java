/*
 * Created on Jan 21, 2004
 */
package ServidorPersistente.OJB.grant.contract;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantSubsidy;
import Dominio.grant.contract.IGrantSubsidy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.grant.IPersistentGrantSubsidy;

/**
 * @author pica
 * @author barbosa
 */
public class GrantSubsidyOJB extends ObjectFenixOJB implements IPersistentGrantSubsidy
{
    public GrantSubsidyOJB()
    {
    }

    public IGrantSubsidy readActualGrantSubsidyByContract(Integer grantContractId) throws ExcepcaoPersistencia
    {
        IGrantSubsidy grantSubsidy = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyGrantContract", grantContractId);
        grantSubsidy = (IGrantSubsidy) queryObject(GrantSubsidy.class, criteria);
        return grantSubsidy;
    }

}
