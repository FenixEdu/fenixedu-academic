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

import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.report.FenixReport;
import org.fenixedu.academic.util.StringFormatter;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.YearMonthDay;

abstract public class DiplomaReport extends FenixReport {

    private static final long serialVersionUID = 1L;

    protected StudentDiplomaInformation studentDiplomaInformation;

    protected DiplomaReport(final StudentDiplomaInformation studentDiplomaInformation) {
        super();
        this.studentDiplomaInformation = studentDiplomaInformation;

        fillReport();
    }

    @Override
    protected void fillReport() {
        final UniversityUnit institutionsUniversityUnit = UniversityUnit.getInstitutionsUniversityUnit();
        addParameter("universityName", institutionsUniversityUnit.getName());
        addParameter("universityPrincipalName", institutionsUniversityUnit.getCurrentPrincipal().getValidatedName());

        addParameter("name", StringFormatter.prettyPrint(this.studentDiplomaInformation.getName()));
        addParameter("nameOfFather", StringFormatter.prettyPrint(this.studentDiplomaInformation.getNameOfFather()));
        addParameter("nameOfMother", StringFormatter.prettyPrint(this.studentDiplomaInformation.getNameOfMother()));
        addParameter("birthLocale", StringFormatter.prettyPrint(this.studentDiplomaInformation.getBirthLocale()));

        addParameter("conclusionDate", this.studentDiplomaInformation.getConclusionDate().toString(DD_MMMM_YYYY, getLocale()));
        addParameter("institutionName", Bennu.getInstance().getInstitutionUnit().getName());
        addParameter("day", new YearMonthDay().toString(DD_MMMM_YYYY, getLocale()));
        addParameter("dissertationTitle", this.studentDiplomaInformation.getDissertationTitle());

        fillReportSpecificParameters();
    }

    @Override
    public String getReportFileName() {
        return this.studentDiplomaInformation.getFilename();
    }

    abstract protected void fillReportSpecificParameters();

}
