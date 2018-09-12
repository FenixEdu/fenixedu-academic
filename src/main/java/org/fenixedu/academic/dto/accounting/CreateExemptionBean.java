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

import org.fenixedu.academic.domain.accounting.events.EventExemptionJustificationType;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

public class CreateExemptionBean implements Serializable {

    private static final long serialVersionUID = 7480560410293395468L;

    public enum ExemptionType {
        DEBT, INTEREST, FINE
    }

    private ExemptionType exemptionType;

    private EventExemptionJustificationType justificationType;

    private String reason;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private DateTime dispatchDate;

    private Money value;

    public CreateExemptionBean(){
        setDispatchDate(new DateTime());
    }

    public ExemptionType getExemptionType() {
        return exemptionType;
    }

    public void setExemptionType(ExemptionType exemptionType) {
        this.exemptionType = exemptionType;
    }

    public EventExemptionJustificationType getJustificationType() {
        return justificationType;
    }

    public void setJustificationType(EventExemptionJustificationType justificationType) {
        this.justificationType = justificationType;
    }

    public DateTime getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(DateTime dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }
}
