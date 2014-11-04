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
package org.fenixedu.academic.dto.serviceRequests;

import java.util.Locale;

import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.student.RegistrationSelectExecutionYearBean;
import org.joda.time.DateTime;

public class AcademicServiceRequestCreateBean extends RegistrationSelectExecutionYearBean {

    private DateTime requestDate = new DateTime();

    private Boolean urgentRequest = Boolean.FALSE;

    private Boolean freeProcessed = Boolean.FALSE;

    private Locale language = Locale.getDefault();

    public AcademicServiceRequestCreateBean(Registration registration) {
        super(registration);
    }

    final public DateTime getRequestDate() {
        return requestDate;
    }

    final public void setRequestDate(DateTime requestDate) {
        this.requestDate = requestDate;
    }

    final public Boolean getUrgentRequest() {
        return urgentRequest;
    }

    final public void setUrgentRequest(Boolean urgentRequest) {
        this.urgentRequest = urgentRequest;
    }

    final public Boolean getFreeProcessed() {
        return freeProcessed;
    }

    final public void setFreeProcessed(Boolean freeProcessed) {
        this.freeProcessed = freeProcessed;
    }

    final public Locale getLanguage() {
        return language;
    }

    final public void setLanguage(final Locale language) {
        this.language = language;
    }

}
