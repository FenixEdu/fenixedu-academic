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

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.CertificateRequest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

public class CertificateRequestEvent extends CertificateRequestEvent_Base {

    protected CertificateRequestEvent() {
        super();
    }

    public CertificateRequestEvent(final AdministrativeOffice administrativeOffice, final EventType eventType,
            final Person person, final CertificateRequest certificateRequest) {
        this();
        super.init(administrativeOffice, eventType, person, certificateRequest);
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter result = super.getDescription();
        fillDescription(result);
        return result;
    }

    @Override
    final public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();

        labelFormatter.appendLabel(entryType.name(), Bundle.ENUMERATION);

        fillDescription(labelFormatter);

        if (getAcademicServiceRequest().getExecutionYear() != null) {
            labelFormatter.appendLabel(" - " + getExecutionYear().getYear());
        }

        return labelFormatter;
    }

    protected void fillDescription(final LabelFormatter labelFormatter) {
        labelFormatter.appendLabel(" (");
        labelFormatter.appendLabel(getDegree().getDegreeType().getName().getContent());
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel("label.in", Bundle.APPLICATION);
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent());
        labelFormatter.appendLabel(")");
    }

    final public Integer getNumberOfUnits() {
        return ((CertificateRequest) getAcademicServiceRequest()).getNumberOfUnits();
    }

    final public Integer getNumberOfPages() {
        return ((CertificateRequest) getAcademicServiceRequest()).getNumberOfPages();
    }

}
