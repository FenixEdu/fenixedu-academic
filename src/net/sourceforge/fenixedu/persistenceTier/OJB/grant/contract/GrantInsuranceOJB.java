/*
 * Created on Jun 26, 2004
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract;

import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Pica
 * @author Barbosa
 */
public class GrantInsuranceOJB extends PersistentObjectOJB implements IPersistentGrantInsurance {

    public IGrantInsurance readGrantInsuranceByGrantContract(Integer key_contract)
            throws ExcepcaoPersistencia {

        IGrantInsurance grantInsurance = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_contract", key_contract);
        grantInsurance = (IGrantInsurance) queryObject(GrantInsurance.class, criteria);
        return grantInsurance;
    }

}