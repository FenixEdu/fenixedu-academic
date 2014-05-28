/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.events.InsuranceExemptionJustificationType;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;

import org.joda.time.YearMonthDay;

public class InsuranceExemptionBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private InsuranceEvent insuranceEvent;

    private InsuranceExemptionJustificationType justificationType;

    private String reason;

    private YearMonthDay dispatchDate;

    public InsuranceExemptionBean(final InsuranceEvent event) {
        setInsuranceEvent(event);
    }

    public InsuranceEvent getInsuranceEvent() {
        return insuranceEvent;
    }

    public void setInsuranceEvent(InsuranceEvent insuranceEvent) {
        this.insuranceEvent = insuranceEvent;
    }

    public InsuranceExemptionJustificationType getJustificationType() {
        return justificationType;
    }

    public void setJustificationType(InsuranceExemptionJustificationType justificationType) {
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

}
