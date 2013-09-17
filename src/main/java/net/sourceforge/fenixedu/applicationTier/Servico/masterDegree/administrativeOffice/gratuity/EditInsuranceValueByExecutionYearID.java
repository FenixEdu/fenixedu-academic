package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Date;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditInsuranceValueByExecutionYearID {

    @Atomic
    public static void run(String executionYearID, Double annualValue, Date endDate) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

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