package ServidorPersistente.OJB.grant.contract;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantContract;
import Dominio.grant.contract.IGrantContract;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.grant.IPersistentGrantContract;

/**
 *
 * @author  Barbosa
 * @author  Pica
 */

public class GrantContractOJB
    extends ServidorPersistente.OJB.ObjectFenixOJB
    implements IPersistentGrantContract
{
    public GrantContractOJB()
    {
    }

    public IGrantContract readGrantContractByNumberAndGrantOwner(
        Integer grantContractNumber,
        Integer grantOwnerId)
        throws ExcepcaoPersistencia
    {
        IGrantContract grantContract = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", grantContractNumber);
        criteria.addEqualTo("key_grant_owner", grantOwnerId);
        grantContract = (IGrantContract) queryObject(GrantContract.class, criteria);
        return grantContract;
    }

    public Integer readMaxGrantContractNumberByGrantOwner(Integer grantOwnerId)
        throws ExcepcaoPersistencia
    {
        IGrantContract grantContract = new GrantContract();
        Integer maxGrantContractNumber = new Integer(0);

        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_owner", grantOwnerId);
        criteria.addOrderBy("number", false);
        grantContract = (IGrantContract) queryObject(GrantContract.class, criteria);
        if (grantContract != null)
            maxGrantContractNumber = grantContract.getContractNumber();
        return maxGrantContractNumber;
    }
}
