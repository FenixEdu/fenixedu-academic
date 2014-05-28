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
package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.PhotocopyRequest;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

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

        labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
        if (!StringUtils.isEmpty(getAcademicServiceRequest().getPurpose())) {
            labelFormatter.appendLabel(" (").appendLabel(getAcademicServiceRequest().getPurpose()).appendLabel(")");
        }
        if (getAcademicServiceRequest().hasExecutionYear()) {
            labelFormatter.appendLabel(" - " + getExecutionYear().getYear());
        }
        return labelFormatter;
    }
}
