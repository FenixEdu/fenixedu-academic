package ServidorPersistente.OJB.grant.contract;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantContractRegime;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.grant.IPersistentGrantContractRegime;

/**
 * @author Barbosa
 * @author Pica
 */
public class GrantContractRegimeOJB extends
        ServidorPersistente.OJB.ObjectFenixOJB implements
        IPersistentGrantContractRegime {

    public GrantContractRegimeOJB() {
    }

    public List readGrantContractRegimeByGrantContract(Integer grantContractId)
            throws ExcepcaoPersistencia {
        List grantContractRegimes = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_contract", grantContractId);
        criteria.addOrderBy("dateBeginContract", false);
        grantContractRegimes = queryList(GrantContractRegime.class, criteria);
        return grantContractRegimes;
    }

    public List readGrantContractRegimeByGrantContractAndState(
            Integer grantContractId, Integer state) throws ExcepcaoPersistencia {
        List grantContractRegime = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_contract", grantContractId);
        criteria.addEqualTo("state", state);
        criteria.addOrderBy("dateBeginContract", false);
        grantContractRegime = queryList(GrantContractRegime.class, criteria);
        return grantContractRegime;
    }
}
