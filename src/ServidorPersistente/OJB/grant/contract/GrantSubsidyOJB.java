/*
 * Created on Jan 21, 2004
 */
package ServidorPersistente.OJB.grant.contract;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantSubsidy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.grant.IPersistentGrantSubsidy;

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