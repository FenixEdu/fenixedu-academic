package org.fenixedu.academic.domain.accounting.postingRules;

import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public interface IAdministrativeOfficeFeeAndInsurancePR {
    YearMonthDay getAdministrativeOfficeFeePaymentLimitDate(DateTime startDate, DateTime endDate);
    Money getInsuranceAmount(DateTime startDate, DateTime endDate);
    Money getAdministrativeOfficeFeeAmount(Event event, DateTime startDate, DateTime endDate);
    Money getAdministrativeOfficeFeePenaltyAmount(Event event, DateTime startDate, DateTime endDate);
}