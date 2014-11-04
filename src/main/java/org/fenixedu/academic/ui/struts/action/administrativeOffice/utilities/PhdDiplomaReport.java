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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.utilities;

import org.fenixedu.academic.util.StringFormatter;

public class PhdDiplomaReport extends DiplomaReport {

    private static final long serialVersionUID = 1L;

    public PhdDiplomaReport(StudentDiplomaInformation studentDiplomaInformation) {
        super(studentDiplomaInformation);
    }

    @Override
    protected void fillReportSpecificParameters() {
        addParameter("graduateTitle",
                "Doutor" + " em " + StringFormatter.prettyPrint(this.studentDiplomaInformation.getDegreeName()));
        addParameter("classificationResult", this.studentDiplomaInformation.getClassificationResult());
    }

    @Override
    public String getReportTemplateKey() {
        return "org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaRequest.BOLONHA_PHD_PROGRAM";
    }

}
