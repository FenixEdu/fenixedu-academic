/*
 * Created on Jun 26, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsurance;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsuranceWithContractAndPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Barbosa
 * @author Pica
 */
public class ReadGrantInsuranceByGrantContract extends Service {

	public InfoGrantInsurance run(Integer idContract) throws FenixServiceException, ExcepcaoPersistencia {
		GrantInsurance grantInsurance = null;
		IPersistentGrantInsurance persistentGrantInsurance = null;
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		persistentGrantInsurance = sp.getIPersistentGrantInsurance();
		grantInsurance = persistentGrantInsurance.readGrantInsuranceByGrantContract(idContract);

		InfoGrantInsurance infoGrantInsurance = null;
		if (grantInsurance != null) {
			infoGrantInsurance = InfoGrantInsuranceWithContractAndPaymentEntity
					.newInfoFromDomain(grantInsurance);
		}
		return infoGrantInsurance;
	}

}