package ServidorPersistente.OJB.grant.contract;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantContractMovement;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.grant.IPersistentGrantContractMovement;

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