package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractMovement;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractMovementWithContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadAllGrantMovementsByContract extends Service {

    public List run(Integer grantContractId) throws FenixServiceException {
	List<InfoGrantContractMovement> result = new ArrayList<InfoGrantContractMovement>();

	GrantContract grantContract = rootDomainObject.readGrantContractByOID(grantContractId);
	List<GrantContractMovement> grantMovements = grantContract.getAssociatedGrantContractMovements();

	for (GrantContractMovement grantContractMovement : grantMovements) {
	    result.add(InfoGrantContractMovementWithContract.newInfoFromDomain(grantContractMovement));
	}

	return result;
    }

}
