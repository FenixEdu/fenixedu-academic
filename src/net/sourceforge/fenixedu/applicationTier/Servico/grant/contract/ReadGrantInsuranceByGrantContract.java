package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsurance;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsuranceWithContractAndPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadGrantInsuranceByGrantContract extends FenixService {

	@Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
	@Service
	public static InfoGrantInsurance run(Integer idContract) throws FenixServiceException {
		GrantContract grantContract = rootDomainObject.readGrantContractByOID(idContract);
		if (grantContract.getGrantInsurance() != null) {
			return InfoGrantInsuranceWithContractAndPaymentEntity.newInfoFromDomain(grantContract.getGrantInsurance());
		}
		return null;
	}

}