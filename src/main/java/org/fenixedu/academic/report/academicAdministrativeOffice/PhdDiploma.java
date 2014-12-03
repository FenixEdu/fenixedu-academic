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

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.StringFormatter;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

public class PhdDiploma extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 1L;

    protected PhdDiploma(IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected PhdDiplomaRequest getDocumentRequest() {
        return (PhdDiplomaRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
        addInstitutionParameters();
        addPersonParameters();
        final UniversityUnit university = getUniversity(getDocumentRequest().getRequestDate());
        String universityName = university.getPartyName().getPreferedContent();

        PhdDiplomaRequest diplomaRequest = getDocumentRequest();
        String phdProgramConclusion =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.diploma.pdhProgramConclusion");
        ExecutionYear conclusionYear = ExecutionYear.readByDateTime(diplomaRequest.getConclusionDate());
        String phdProgramDescription =
                getDocumentRequest().getPhdIndividualProgramProcess().getPhdProgram().getDegree().getNameI18N(conclusionYear)
                        .getContent(getLanguage());
        String phdConclusionDate = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.diploma.pdhConclusionDate");

        addParameter("conclusionMessage", phdProgramConclusion);
        addParameter("phdProgram", phdProgramDescription);
        addParameter(
                "conclusionDate",
                MessageFormat.format(phdConclusionDate, diplomaRequest.getConclusionDate()
                        .toString(getDatePattern(), getLocale()).toLowerCase()));

        addParameter("documentNumber", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.documentNumber"));
        addParameter("registryCode", diplomaRequest.hasRegistryCode() ? diplomaRequest.getRegistryCode().getCode() : null);
        addParameter("institutionName", Bennu.getInstance().getInstitutionUnit().getName());
        addParameter("day", MessageFormat.format(
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.university.actualDate"), universityName,
                getFormatedCurrentDate()));

        addParameter("classificationResult", MessageFormat.format(BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                "label.phd.Diploma.classificationResult"), diplomaRequest.getThesisFinalGrade().getLocalizedName(getLocale())));
        addParameter("dissertationTitle", diplomaRequest.getDissertationThesisTitle());
        addParameter("graduateTitle", diplomaRequest.getGraduateTitle(getLocale()));

        if (getUniversity(getDocumentRequest().getRequestDate()) != getUniversity(getDocumentRequest().getConclusionDate()
                .toDateTimeAtCurrentTime())) {
            addParameter("UTLDescription", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.UTLDescription"));
            addParameter("certification",
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.phd.certification.UTL"));

        } else {
            addParameter("UTLDescription", StringUtils.EMPTY);
            addParameter("certification",
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.phd.certification.UL"));
        }

        addParameter("message1", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.message1"));
        addParameter("message3", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.message3"));
        addParameter("phdmessage1", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.diploma.message1"));
    }

    private String getFormatedCurrentDate() {
        return new LocalDate().toString(getDatePattern(), getLocale()).toLowerCase();
    }

    private String getDatePattern() {
        final StringBuilder result = new StringBuilder();
        result.append("dd '");
        result.append(BundleUtil.getString(Bundle.APPLICATION, getLocale(), "label.of"));
        result.append("' MMMM '");
        result.append(BundleUtil.getString(Bundle.APPLICATION, getLocale(), "label.of"));
        result.append("' yyyy");
        return result.toString();
    }

    private void addPersonParameters() {
        final Person person = getDocumentRequest().getPerson();
        addParameter("name", StringFormatter.prettyPrint(person.getName()));
        addParameter("nameOfFather", StringFormatter.prettyPrint(person.getNameOfFather()));
        addParameter("nameOfMother", StringFormatter.prettyPrint(person.getNameOfMother()));

        String country;
        String countryUpperCase;
        if (person.getCountry() != null) {
            countryUpperCase = person.getCountry().getCountryNationality().getContent(getLanguage()).toLowerCase();
            country = WordUtils.capitalize(countryUpperCase);
        } else {
            throw new DomainException("error.personWithoutParishOfBirth");
        }

        String nationality = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "diploma.nationality");
        addParameter("birthLocale", MessageFormat.format(nationality, country));

    }

    private void addInstitutionParameters() {

        addParameter("universityName", getUniversity(getDocumentRequest().getRequestDate()).getName());
        addParameter("universityPrincipal", getUniversity(getDocumentRequest().getRequestDate()).getCurrentPrincipal());

        final String institutionUnitName = getInstitutionName();

        Person principal = getUniversity(getDocumentRequest().getRequestDate()).getCurrentPrincipal();
        final Person president = getUniversity(getDocumentRequest().getRequestDate()).getCurrentPresident();

        final UniversityUnit university = getUniversity(getDocumentRequest().getRequestDate());
        String universityName = university.getPartyName().getPreferedContent();

        String rectorGender, rectorGrant, presidentGender;

        if (president.isMale()) {
            presidentGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentMale");
        } else {
            presidentGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentFemale");
        }

        if (principal.isMale()) {
            rectorGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.rectorMale");
            rectorGrant = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentGrantMale");
        } else {
            rectorGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.rectorFemale");
            rectorGrant = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentGrantFemale");
        }
        addParameter("theRector", rectorGender);
        addParameter("president", MessageFormat.format(presidentGender, institutionUnitName));
        String firstParagraph = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.universityPrincipal");
        addParameter("firstParagraph",
                MessageFormat.format(firstParagraph, rectorGender, universityName, rectorGrant, principal.getValidatedName()));
    }

}
