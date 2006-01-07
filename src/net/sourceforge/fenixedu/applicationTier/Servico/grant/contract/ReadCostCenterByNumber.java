package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author jpvl
 */

public class ReadCostCenterByNumber implements IService {

	public InfoGrantCostCenter run(String costContractNumber) throws FenixServiceException,
			ExcepcaoPersistencia {
		// When creating a New Contract its needed to verify if the costContract
		// exists
		// chosen for orientator really exists
		InfoGrantCostCenter infoGrantCostCenter = new InfoGrantCostCenter();

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentGrantCostCenter pCostContract = sp.getIPersistentGrantCostCenter();
		GrantCostCenter costCenter = pCostContract.readGrantCostCenterByNumber(costContractNumber);
		if (costCenter == null)
			throw new GrantOrientationTeacherNotFoundException();
		infoGrantCostCenter = InfoGrantCostCenter.newInfoFromDomain(costCenter);

		return infoGrantCostCenter;
	}
}
