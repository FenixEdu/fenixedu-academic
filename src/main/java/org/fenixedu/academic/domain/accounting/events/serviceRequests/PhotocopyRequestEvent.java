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
package org.fenixedu.academic.domain.accounting.events.serviceRequests;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.PhotocopyRequest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

public class PhotocopyRequestEvent extends PhotocopyRequestEvent_Base {

    protected PhotocopyRequestEvent() {
        super();
    }

    public PhotocopyRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
            final PhotocopyRequest request) {
        this();
        super.init(administrativeOffice, request.getEventType(), person, request);
    }

    @Override
    public PhotocopyRequest getAcademicServiceRequest() {
        return (PhotocopyRequest) super.getAcademicServiceRequest();
    }

    public Integer getNumberOfPages() {
        return getAcademicServiceRequest().getNumberOfPages();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();

        labelFormatter.appendLabel(entryType.name(), Bundle.ENUMERATION);
        if (!StringUtils.isEmpty(getAcademicServiceRequest().getPurpose())) {
            labelFormatter.appendLabel(" (").appendLabel(getAcademicServiceRequest().getPurpose()).appendLabel(")");
        }
        if (getAcademicServiceRequest().getExecutionYear() != null) {
            labelFormatter.appendLabel(" - " + getExecutionYear().getYear());
        }
        return labelFormatter;
    }
}
