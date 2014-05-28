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
package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExtraCurricularCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.util.FenixStringTools;

public class ExtraCurricularCertificateRequestDocument extends AdministrativeOfficeDocument {

    protected ExtraCurricularCertificateRequestDocument(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected ExtraCurricularCertificateRequest getDocumentRequest() {
        return (ExtraCurricularCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
        super.fillReport();
        addParameter("enrolmentsInfo", getEnrolmentsInfo());
    }

    @Override
    protected String getDegreeDescription() {
        return getDocumentRequest().getRegistration().getDegreeDescription(null, getLocale());
    }

    final private String getEnrolmentsInfo() {
        final StringBuilder result = new StringBuilder();
        ExtraCurricularCertificateRequest request = getDocumentRequest();

        final Collection<Enrolment> enrolments = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID);
        enrolments.addAll(request.getEnrolments());

        for (final Enrolment enrolment : enrolments) {
            result.append(
                    FenixStringTools.multipleLineRightPadWithSuffix(getPresentationNameFor(enrolment).toUpperCase(), LINE_LENGTH,
                            END_CHAR, getCreditsAndGradeInfo(enrolment, enrolment.getExecutionYear()))).append(LINE_BREAK);
        }

        result.append(generateEndLine());

        return result.toString();
    }

}
