package net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractMovement;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Barbosa
 * @author Pica
 */

public class GrantContractMovementOJB extends PersistentObjectOJB implements
        IPersistentGrantContractMovement {

    public GrantContractMovementOJB() {

    }

    public List readAllMovementsByContract(Integer contractId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyGrantContract", contractId);
        return queryList(GrantContractMovement.class, criteria);
    }
}