package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsurance;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsuranceWithContractAndPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadGrantInsuranceByGrantContract extends Service {

	public InfoGrantInsurance run(Integer idContract) throws FenixServiceException{
	    GrantContract grantContract = rootDomainObject.readGrantContractByOID(idContract);
		if (grantContract.getGrantInsurance() != null) {
			return InfoGrantInsuranceWithContractAndPaymentEntity.newInfoFromDomain(grantContract.getGrantInsurance());
		}
		return null;
	}

}
