/*
 * Created on Jun 26, 2004
 */
package ServidorPersistente.OJB.grant.contract;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantInsurance;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.grant.IPersistentGrantInsurance;

/**
 * @author Pica
 * @author Barbosa
 */
public class GrantInsuranceOJB extends ObjectFenixOJB implements
        IPersistentGrantInsurance {

    public List readGrantInsuranceByGrantContract(Integer key_contract)
            throws ExcepcaoPersistencia {

        List grantInsurances = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_contract", key_contract);
        grantInsurances = queryList(GrantInsurance.class, criteria);
        return grantInsurances;
    }

    public List readGrantInsuranceByGrantContractAndState(Integer key_contract, Integer state)
            throws ExcepcaoPersistencia {

        List grantInsurances = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_contract", key_contract);
        criteria.addEqualTo("state",state);
        grantInsurances = queryList(GrantInsurance.class, criteria);
        return grantInsurances;
    }
}
