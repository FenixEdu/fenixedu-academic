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
import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.report.FenixReport;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
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

        addParameter("administrativeOfficeName", administrativeOffice.getName().getContent());
        addParameter("administrativeOfficeCoordinator", administrativeOffice.getCoordinator().getProfile().getDisplayName());

        addParameter("institutionName", Bennu.getInstance().getInstitutionUnit().getPartyName().getContent());
        addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getPartyName().getContent());

        addParameter("studentNumber", getStudentNumber());
        addParameter("studentName", getPerson().getName());
        addParameter("documentId", getPerson().getDocumentIdNumber());
        addParameter("parishOfBirth", getPerson().getParishOfBirth());
        addParameter("nationality", getPerson().getCountry().getCountryNationality().getContent());

        addParameter("registrationState", getRegistrationStateLabel());
        addParameter("executionYear", process.getExecutionYear().getName());
        final ExecutionYear executionYear = process.getExecutionYear();
        addParameter("phdProgramName", process.getPhdProgram().getName(executionYear).getContent());

        addParameter("documentDate", new LocalDate().toString(DD_MMMM_YYYY, I18N.getLocale()));
    }

    private String getStudentNumber() {
        return hasRegistration() ? getRegistration().getNumber().toString() : getStudent().getNumber().toString();
    }

    private String getRegistrationStateLabel() {
        final Gender gender = getPerson().getGender();
        return gender == Gender.MALE ? BundleUtil
                .getString(Bundle.PHD, "label.phd.schoolRegistrationDeclaration.registered.male") : BundleUtil.getString(
                Bundle.PHD, "label.phd.schoolRegistrationDeclaration.registered.female");
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
