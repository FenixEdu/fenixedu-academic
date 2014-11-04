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
package org.fenixedu.academic.dto.accounting;

import java.io.Serializable;

import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustificationType;
import org.fenixedu.academic.dto.accounting.penaltyExemption.AdministrativeOfficeExemptionAppliance;
import org.joda.time.YearMonthDay;

public class AdministrativeOfficeFeeAndInsuranceExemptionBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent;

    private AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType;

    private String reason;

    private YearMonthDay dispatchDate;

    private AdministrativeOfficeExemptionAppliance applyExemptionOn;

    public AdministrativeOfficeFeeAndInsuranceExemptionBean(final AdministrativeOfficeFeeAndInsuranceEvent event) {
        setAdministrativeOfficeFeeAndInsuranceEvent(event);
    }

    public AdministrativeOfficeFeeAndInsuranceEvent getAdministrativeOfficeFeeAndInsuranceEvent() {
        return this.administrativeOfficeFeeAndInsuranceEvent;
    }

    public void setAdministrativeOfficeFeeAndInsuranceEvent(
            AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent) {
        this.administrativeOfficeFeeAndInsuranceEvent = administrativeOfficeFeeAndInsuranceEvent;
    }

    public AdministrativeOfficeFeeAndInsuranceExemptionJustificationType getJustificationType() {
        return justificationType;
    }

    public void setJustificationType(AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType) {
        this.justificationType = justificationType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public YearMonthDay getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(YearMonthDay dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public AdministrativeOfficeExemptionAppliance getApplyExemptionOn() {
        return applyExemptionOn;
    }

    public void setApplyExemptionOn(AdministrativeOfficeExemptionAppliance applyExemptionOn) {
        this.applyExemptionOn = applyExemptionOn;
    }
}
