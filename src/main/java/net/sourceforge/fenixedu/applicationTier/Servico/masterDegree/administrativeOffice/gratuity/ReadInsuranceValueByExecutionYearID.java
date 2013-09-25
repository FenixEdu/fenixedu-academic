package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoInsuranceValue;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadInsuranceValueByExecutionYearID {

    @Atomic
    public static InfoInsuranceValue run(String executionYearID) throws FenixServiceException {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);
        ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);
        InsuranceValue insuranceValue = executionYear.getInsuranceValue();
        if (insuranceValue != null) {
            return InfoInsuranceValue.newInfoFromDomain(insuranceValue);
        }

        return null;
    }

}