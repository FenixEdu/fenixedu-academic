package org.fenixedu.academic.domain.accounting.postingRules;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public interface IAdministrativeOfficeFeeAndInsurancePR {
    public YearMonthDay getAdministrativeOfficeFeePaymentLimitDate(DateTime startDate, DateTime endDate);
}
