package net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPart;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPart;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Pica
 * @author Barbosa
 */
public class GrantPartOJB extends PersistentObjectOJB implements IPersistentGrantPart {

    public List readAllGrantPartsByGrantSubsidy(Integer grantSubsidyId) throws ExcepcaoPersistencia {
        List grantPartsList = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_subsidy", grantSubsidyId);
        grantPartsList = queryList(GrantPart.class, criteria);
        return grantPartsList;
    }

    public IGrantPart readGrantPartByUnique(Integer grantSubsidyId, Integer paymentEntityId)
            throws ExcepcaoPersistencia {
        IGrantPart grantPart = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_subsidy", grantSubsidyId);
        criteria.addEqualTo("key_grant_payment_entity", paymentEntityId);
        grantPart = (IGrantPart) queryObject(GrantPart.class, criteria);
        return grantPart;
    }

}