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
package org.fenixedu.academic.report.academicAdministrativeOffice;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;

import java.util.Locale;

public class PhdDiploma extends Diploma {

    private static final long serialVersionUID = 1L;

    PhdDiploma(IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    public PhdDiplomaRequest getDocumentRequest() {
        return (PhdDiplomaRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
        super.fillReport();

        final PhdDiplomaRequest request = getDocumentRequest();

        getPayload().addProperty("isPHD", true);
        getPayload().addProperty("thesisTitle", request.getThesisTitle(getLocale()));
        getPayload().addProperty("graduateTitle", request.getGraduateTitle(Locale.getDefault()));
    }

    @Override
    protected String getQualification() {
        return getDocumentRequest().getThesisFinalGrade().getLocalizedName(getLocale());
    }

    @Override
    protected String getConclusion() {
        final ExecutionYear ingressionYear = getDocumentRequest().getPhdIndividualProgramProcess().getExecutionYear();

        return getDocumentRequest().getPhdIndividualProgramProcess().getPhdProgram().getName(ingressionYear)
                .getContent(getLanguage());
    }

}
