package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Date;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class EditInsuranceValueByExecutionYearID {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(Integer executionYearID, Double annualValue, Date endDate) {

        final ExecutionYear executionYear = RootDomainObject.getInstance().readExecutionYearByOID(executionYearID);

        InsuranceValue insuranceValue = executionYear.getInsuranceValue();

        if (insuranceValue != null) {
            insuranceValue.setEndDate(endDate);
            insuranceValue.setAnnualValue(annualValue);
        } else {
            insuranceValue = new InsuranceValue(executionYear, annualValue, endDate);
        }
    }

}