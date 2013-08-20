package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Date;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class EditInsuranceValueByExecutionYearID {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(String executionYearID, Double annualValue, Date endDate) {

        final ExecutionYear executionYear = AbstractDomainObject.fromExternalId(executionYearID);

        InsuranceValue insuranceValue = executionYear.getInsuranceValue();

        if (insuranceValue != null) {
            insuranceValue.setEndDate(endDate);
            insuranceValue.setAnnualValue(annualValue);
        } else {
            insuranceValue = new InsuranceValue(executionYear, annualValue, endDate);
        }
    }

}