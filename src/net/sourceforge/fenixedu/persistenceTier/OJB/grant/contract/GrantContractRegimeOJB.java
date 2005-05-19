package net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Barbosa
 * @author Pica
 */
public class GrantContractRegimeOJB extends PersistentObjectOJB implements
        IPersistentGrantContractRegime {

    public List readGrantContractRegimeByGrantContract(Integer grantContractId)
            throws ExcepcaoPersistencia {
        List grantContractRegimes = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_contract", grantContractId);
        grantContractRegimes = queryList(GrantContractRegime.class, criteria, "dateBeginContract", false);
        return grantContractRegimes;
    }

    public List readGrantContractRegimeByGrantContractAndState(Integer grantContractId, Integer state)
            throws ExcepcaoPersistencia {
        List grantContractRegime = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_contract", grantContractId);
        criteria.addEqualTo("state", state);
        grantContractRegime = queryList(GrantContractRegime.class, criteria, "dateBeginContract", false);
        return grantContractRegime;
    }

}