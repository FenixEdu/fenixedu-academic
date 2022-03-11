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
package org.fenixedu.academic.report.phd.registration;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.report.FenixReport;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PhdSchoolRegistrationDeclarationDocument extends FenixReport {

    static private final long serialVersionUID = 1L;

    private final PhdIndividualProgramProcess process;

    public PhdSchoolRegistrationDeclarationDocument(final PhdIndividualProgramProcess process) {
        this.process = process;
        fillReport();
    }

    @Override
    protected void fillReport() {
        final AdministrativeOffice administrativeOffice = process.getPhdProgram().getAdministrativeOffice();
        final ExecutionYear executionYear = process.getExecutionYear();

        getPayload().addProperty("administrativeOfficeName", administrativeOffice.getName().getContent());
        getPayload().addProperty("administrativeOfficeCoordinator", administrativeOffice.getCoordinator()
                .getProfile().getDisplayName());

        getPayload().addProperty("institutionName", Bennu.getInstance().getInstitutionUnit().getPartyName().getContent());
        getPayload().addProperty("universityName", UniversityUnit.getInstitutionsUniversityUnit().getPartyName().getContent());

        getPayload().addProperty("studentNumber", getStudentNumber());
        getPayload().addProperty("studentName", getPerson().getName());
        getPayload().addProperty("documentId", getPerson().getDocumentIdNumber());
        getPayload().addProperty("parishOfBirth", getPerson().getParishOfBirth());
        getPayload().addProperty("nationality", getPerson().getCountry().getCountryNationality().getContent());
        getPayload().addProperty("studentGender", getPerson().isMale() ? "male" : "female");
        getPayload().addProperty("executionYear", executionYear.getName());
        getPayload().addProperty("phdProgramName", process.getPhdProgram().getName(executionYear).getContent());
        getPayload().addProperty("documentDate", new LocalDate().toString(DD_MMMM_YYYY, I18N.getLocale()));
    }

    private String getStudentNumber() {
        return hasRegistration() ? getRegistration().getNumber().toString() : getStudent().getNumber().toString();
    }

    private Person getPerson() {
        return process.getPerson();
    }

    private Student getStudent() {
        return getPerson().getStudent();
    }

    private Registration getRegistration() {
        return process.getRegistration();
    }

    private boolean hasRegistration() {
        return process.getRegistration() != null;
    }

    @Override
    public String getReportFileName() {
        return "SchoolRegistrationDeclaration-" + new DateTime().toString(YYYYMMDDHHMMSS);
    }

    @Override
    public String getReportTemplateKey() {
        return super.getReportTemplateKey() + ".pt";
    }
}
