package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoInsuranceValue;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.InsuranceValue;

public class ReadInsuranceValueByExecutionYearID extends FenixService {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static InfoInsuranceValue run(Integer executionYearID) throws FenixServiceException {
	ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);
	InsuranceValue insuranceValue = executionYear.getInsuranceValue();
	if (insuranceValue != null) {
	    return InfoInsuranceValue.newInfoFromDomain(insuranceValue);
	}

	return null;
    }

}