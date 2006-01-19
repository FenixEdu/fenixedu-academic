package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoInsuranceValue;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadInsuranceValueByExecutionYearID implements IService {

	public InfoInsuranceValue run(Integer executionYearID) throws FenixServiceException, ExcepcaoPersistencia {

		InfoInsuranceValue infoInsuranceValue = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		ExecutionYear executionYear = (ExecutionYear) sp.getIPersistentExecutionYear().readByOID(
				ExecutionYear.class, executionYearID);

		InsuranceValue insuranceValue = sp.getIPersistentInsuranceValue().readByExecutionYear(
				executionYear.getIdInternal());

		if (insuranceValue != null) {
			infoInsuranceValue = InfoInsuranceValue.newInfoFromDomain(insuranceValue);
		}

		return infoInsuranceValue;
	}
}