package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class GrantContractMovementVO extends VersionedObjectsBase implements
        IPersistentGrantContractMovement {

    public List readAllMovementsByContract(Integer contractId) throws ExcepcaoPersistencia {
        List<IGrantContractMovement> grantContractMovements = (List<IGrantContractMovement>) readAll(GrantContractMovement.class);
        List<IGrantContractMovement> result = null;

        for (IGrantContractMovement movement : grantContractMovements) {
            if (movement.getKeyGrantContract().equals(contractId)) {
                result.add(movement);
            }
        }

        return result;
    }

}