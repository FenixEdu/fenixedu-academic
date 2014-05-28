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
package net.sourceforge.fenixedu.presentationTier.docs.phd.registration;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Locale;

public class PhdSchoolRegistrationDeclarationDocument extends FenixReport {

    static private final long serialVersionUID = 1L;

    private final PhdIndividualProgramProcess process;

    public PhdSchoolRegistrationDeclarationDocument(final PhdIndividualProgramProcess process) {
        this.process = process;

        setResourceBundle(ResourceBundle.getBundle("resources.PhdResources", I18N.getLocale()));
        fillReport();
    }

    @Override
    protected void fillReport() {
        final Unit unit = process.getPhdProgram().getAdministrativeOffice().getUnit();

        addParameter("administrativeOfficeName", unit.getName());
        addParameter("administrativeOfficeCoordinator", unit.getActiveUnitCoordinator().getName());

        addParameter("institutionName", Bennu.getInstance().getInstitutionUnit().getPartyName().getContent());
        addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getPartyName().getContent());

        addParameter("studentNumber", getStudentNumber());
        addParameter("studentName", getPerson().getName());
        addParameter("documentId", getPerson().getDocumentIdNumber());
        addParameter("parishOfBirth", getPerson().getParishOfBirth());
        addParameter("nationality", getPerson().getCountry().getCountryNationality().getContent());

        addParameter("registrationState", getRegistrationStateLabel());
        addParameter("executionYear", process.getExecutionYear().getName());
        addParameter("phdProgramName", process.getPhdProgram().getName().getContent());

        addParameter("documentDate", new LocalDate().toString(DD_MMMM_YYYY, I18N.getLocale()));
    }

    private String getStudentNumber() {
        return hasRegistration() ? getRegistration().getNumber().toString() : getStudent().getNumber().toString();
    }

    private String getRegistrationStateLabel() {
        final Gender gender = getPerson().getGender();
        return gender == Gender.MALE ? getMessage("label.phd.schoolRegistrationDeclaration.registered.male") : getMessage("label.phd.schoolRegistrationDeclaration.registered.female");
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
        return process.hasRegistration();
    }

    private String getMessage(final String key) {
        return getResourceBundle().getString(key);
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
