/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author pica
 * @author barbosa
 */
public class GrantSubsidyOJB extends PersistentObjectOJB implements IPersistentGrantSubsidy {

    public GrantSubsidyOJB() {
    }

    public List readAllSubsidiesByGrantContract(Integer idContract) throws ExcepcaoPersistencia {
        List subsidyList = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_contract", idContract);
        subsidyList = queryList(GrantSubsidy.class, criteria, "id_internal", false);
        return subsidyList;
    }

    public List readAllSubsidiesByGrantContractAndState(Integer idContract, Integer state)
            throws ExcepcaoPersistencia {
        List subsidyList = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_contract", idContract);
        criteria.addEqualTo("state", state);
        subsidyList = queryList(GrantSubsidy.class, criteria, "dateBeginSubsidy", false);
        return subsidyList;
    }
}