package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoInsuranceValue;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.InsuranceValue;

public class ReadInsuranceValueByExecutionYearID extends FenixService {

    public InfoInsuranceValue run(Integer executionYearID) throws FenixServiceException {
	ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);
	InsuranceValue insuranceValue = executionYear.getInsuranceValue();
	if (insuranceValue != null) {
	    return InfoInsuranceValue.newInfoFromDomain(insuranceValue);
	}

	return null;
    }

}
