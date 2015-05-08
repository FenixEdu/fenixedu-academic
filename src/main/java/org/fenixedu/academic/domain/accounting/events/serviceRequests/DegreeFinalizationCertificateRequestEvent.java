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
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

public class DegreeFinalizationCertificateRequestEvent extends DegreeFinalizationCertificateRequestEvent_Base {

    private DegreeFinalizationCertificateRequestEvent() {
        super();
    }

    public DegreeFinalizationCertificateRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
            final DegreeFinalizationCertificateRequest certificateRequest) {
        this();
        super.init(administrativeOffice, EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST, person, certificateRequest);
    }

    @Override
    public DegreeFinalizationCertificateRequest getAcademicServiceRequest() {
        return (DegreeFinalizationCertificateRequest) super.getAcademicServiceRequest();
    }

    @Override
    protected void fillDescription(final LabelFormatter labelFormatter) {
        labelFormatter.appendLabel(" (");
        addCycleDescriptionIfRequired(labelFormatter);
        labelFormatter.appendLabel(getDegree().getDegreeType().getName().getContent());
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel("label.in", Bundle.APPLICATION);
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent());
        labelFormatter.appendLabel(")");
        labelFormatter.appendLabel(" - ").appendLabel(getAcademicServiceRequest().getServiceRequestNumberYear());
    }

    private void addCycleDescriptionIfRequired(LabelFormatter labelFormatter) {
        final DegreeFinalizationCertificateRequest request = getAcademicServiceRequest();
        if (request.getProgramConclusion() != null) {
            labelFormatter.appendLabel(request.getProgramConclusion().getName().getContent()).appendLabel(" ")
                    .appendLabel("label.of", Bundle.APPLICATION).appendLabel(" ");
        }
    }

}
