package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Date;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditInsuranceValueByExecutionYearID {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Atomic
    public static void run(String executionYearID, Double annualValue, Date endDate) {

        final ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);

        InsuranceValue insuranceValue = executionYear.getInsuranceValue();

        if (insuranceValue != null) {
            insuranceValue.setEndDate(endDate);
            insuranceValue.setAnnualValue(annualValue);
        } else {
            insuranceValue = new InsuranceValue(executionYear, annualValue, endDate);
        }
    }

}