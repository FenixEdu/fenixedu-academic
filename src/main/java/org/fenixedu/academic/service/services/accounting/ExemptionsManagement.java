/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.service.services.accounting;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemption;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustificationType;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeExemption;
import org.fenixedu.academic.domain.accounting.events.InsuranceExemption;
import org.fenixedu.academic.domain.accounting.events.InsuranceExemptionJustificationType;
import org.fenixedu.academic.dto.accounting.AdministrativeOfficeFeeAndInsuranceExemptionBean;
import org.fenixedu.academic.dto.accounting.InsuranceExemptionBean;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class ExemptionsManagement {

    @Atomic
    public static void createAdministrativeOfficeFeeAndInsuranceExemption(final Person responsible,
            final AdministrativeOfficeFeeAndInsuranceExemptionBean exemptionBean) {
        AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent =
                exemptionBean.getAdministrativeOfficeFeeAndInsuranceEvent();
        AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType = exemptionBean.getJustificationType();
        String reason = exemptionBean.getReason();
        YearMonthDay dispatchDate = exemptionBean.getDispatchDate();

        switch (exemptionBean.getApplyExemptionOn()) {
        case ADMINISTRATIVE_OFFICE_FEE:
            new AdministrativeOfficeFeeExemption(responsible, administrativeOfficeFeeAndInsuranceEvent, justificationType,
                    reason, dispatchDate);
            return;
        case ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE:
            new AdministrativeOfficeFeeAndInsuranceExemption(responsible, administrativeOfficeFeeAndInsuranceEvent,
                    justificationType, reason, dispatchDate);
            return;
        case INSURANCE_FEE:
            InsuranceExemptionJustificationType insuranceJustificationType = null;
            switch (justificationType) {
            case DIRECTIVE_COUNCIL_AUTHORIZATION:
                insuranceJustificationType = InsuranceExemptionJustificationType.DIRECTIVE_COUNCIL_AUTHORIZATION;
                break;
            case MIT_AGREEMENT:
                insuranceJustificationType = InsuranceExemptionJustificationType.MIT_AGREEMENT;
            }

            new InsuranceExemption(responsible, administrativeOfficeFeeAndInsuranceEvent, insuranceJustificationType, reason,
                    dispatchDate);
        }

    }

    @Atomic
    public static void createInsuranceExemption(final Person responsible, final InsuranceExemptionBean exemptionBean) {
        new InsuranceExemption(responsible, exemptionBean.getInsuranceEvent(), exemptionBean.getJustificationType(),
                exemptionBean.getReason(), exemptionBean.getDispatchDate());
    }

}
