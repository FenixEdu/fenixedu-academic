package org.fenixedu.academic.domain.accounting;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;

public class EnrolmentBlocker {

    public static EnrolmentBlocker enrolmentBlocker = new EnrolmentBlocker();

    public boolean isAnyGratuityOrAdministrativeOfficeFeeAndInsuranceInDebt(final StudentCurricularPlan scp, final ExecutionYear executionYear) {
        return scp.getRegistration().getStudent().isAnyGratuityOrAdministrativeOfficeFeeAndInsuranceInDebt(executionYear);
    }

}
