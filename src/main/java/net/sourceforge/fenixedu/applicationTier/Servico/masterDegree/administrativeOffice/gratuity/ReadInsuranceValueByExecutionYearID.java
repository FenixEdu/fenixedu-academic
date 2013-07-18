package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoInsuranceValue;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadInsuranceValueByExecutionYearID {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static InfoInsuranceValue run(Integer executionYearID) throws FenixServiceException {
        ExecutionYear executionYear = RootDomainObject.getInstance().readExecutionYearByOID(executionYearID);
        InsuranceValue insuranceValue = executionYear.getInsuranceValue();
        if (insuranceValue != null) {
            return InfoInsuranceValue.newInfoFromDomain(insuranceValue);
        }

        return null;
    }

}