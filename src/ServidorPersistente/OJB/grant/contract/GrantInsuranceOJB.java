/*
 * Created on Jun 26, 2004
 */
package ServidorPersistente.OJB.grant.contract;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantInsurance;
import Dominio.grant.contract.IGrantInsurance;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.grant.IPersistentGrantInsurance;

/**
 * @author Pica
 * @author Barbosa
 */
public class GrantInsuranceOJB extends ObjectFenixOJB implements
        IPersistentGrantInsurance {

    public IGrantInsurance readGrantInsuranceByGrantContract(Integer key_contract)
            throws ExcepcaoPersistencia {

        IGrantInsurance grantInsurance = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_contract", key_contract);
        grantInsurance = (IGrantInsurance)queryObject(GrantInsurance.class, criteria);
        return grantInsurance;
    }

}
