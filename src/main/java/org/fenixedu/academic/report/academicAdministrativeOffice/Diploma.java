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
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.StringFormatter;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

public class Diploma extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 1L;

    protected Diploma(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected DiplomaRequest getDocumentRequest() {
        return (DiplomaRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
        super.fillReport();
        final DiplomaRequest diplomaRequest = getDocumentRequest();

        String universityName = getUniversity(diplomaRequest.getRequestDate()).getPartyName().getPreferedContent();

        addParameter("registryCode", diplomaRequest.hasRegistryCode() ? diplomaRequest.getRegistryCode().getCode() : null);
        addParameter("documentNumber", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.documentNumber"));
        addParameter("conclusionDate", diplomaRequest.getConclusionDate().toString(getDatePattern(), getLocale()));
        addParameter("day", MessageFormat.format(
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.university.actualDate"), universityName,
                getFormatedCurrentDate()));

        if (diplomaRequest.hasFinalAverageDescription()) {
            addParameter("finalAverageDescription", StringUtils.capitalize(BundleUtil.getString(Bundle.ENUMERATION, getLocale(),
                    diplomaRequest.getFinalAverage().toString())));
            addParameter("finalAverageQualified", diplomaRequest.getFinalAverageQualified());
        } else if (diplomaRequest.hasDissertationTitle()) {
            addParameter("dissertationTitle", diplomaRequest.getDissertationThesisTitle());
        }

        String finalAverage = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "diploma.finalAverage");
        addParameter("finalAverageDescription", MessageFormat.format(finalAverage,
                BundleUtil.getString(Bundle.ENUMERATION, getLocale(), diplomaRequest.getFinalAverage().toString()),
                diplomaRequest.getFinalAverage().toString(),
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), getQualifiedAverageGrade(getLocale()))));

        addParameter("conclusionStatus", MessageFormat.format(
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.conclusionStatus"),
                getConclusionStatusAndDegreeType(diplomaRequest, getRegistration())));
        addParameter("degreeFilteredName", diplomaRequest.getDegreeFilteredName());

        String graduateTitle = diplomaRequest.getGraduateTitle(getLocale());

        addParameter("graduateTitle", graduateTitle);
        addParameter("message1", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.message1"));
        addParameter("message2", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.message2"));
        addParameter("message3", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.message3"));

    }

    @Override
    protected void addIntroParameters() {
        super.addIntroParameters();

        final String institutionUnitName = getInstitutionName();

        Person principal = getUniversity(getDocumentRequest().getRequestDate()).getCurrentPrincipal();
        Person president = getUniversity(getDocumentRequest().getRequestDate()).getCurrentPresident();

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

        addParameter("universityPrincipal", principal);
        addParameter("universityPrincipalName", principal.getValidatedName());

        if (getUniversity(getDocumentRequest().getRequestDate()) != getUniversity(getDocumentRequest().getConclusionDate()
                .toDateTimeAtCurrentTime())) {
            addParameter("UTLDescription", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.UTLDescription"));
            addParameter("certification", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.certification.UTL"));
        } else {
            addParameter("UTLDescription", StringUtils.EMPTY);
            addParameter("certification", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.diploma.certification.UL"));
        }
    }

    @Override
    protected void setPersonFields() {
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

    private String getFormatedCurrentDate() {
        return new LocalDate().toString(getDatePattern(), getLocale());
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

    final private String getConclusionStatusAndDegreeType(final DiplomaRequest diplomaRequest, final Registration registration) {
        final StringBuilder result = new StringBuilder();

        if (registration.getDegreeType().isAdvancedFormationDiploma()
                || registration.getDegreeType().isAdvancedSpecializationDiploma()) {
            forDFA(result, diplomaRequest, registration);
        } else {
            forOthers(result, diplomaRequest, registration);
        }

        return result.toString();
    }

    private void forOthers(StringBuilder result, final DiplomaRequest diplomaRequest, final Registration registration) {
        final DegreeType degreeType = registration.getDegreeType();

        if (degreeType.hasAnyCycleTypes() && diplomaRequest.getRequestedCycle() != null) {
            result.append(BundleUtil.getString(Bundle.ENUMERATION, getLocale(), diplomaRequest.getRequestedCycle()
                    .getQualifiedName()));
            result.append(SINGLE_SPACE).append(BundleUtil.getString(Bundle.APPLICATION, getLocale(), "of.masculine"))
                    .append(SINGLE_SPACE);
        }

        result.append(degreeType.getPrefix(getLocale())).append(
                getDocumentRequest().getProgramConclusion().getDescription().getContent(getLocale()));
    }

    private void forDFA(StringBuilder result, final DiplomaRequest diplomaRequest, final Registration registration) {
        final DegreeType degreeType = registration.getDegreeType();

        result.append(degreeType.getPrefix()).append(degreeType.getName().getContent());
        if (degreeType.hasExactlyOneCycleType()) {
            result.append(" (")
                    .append(BundleUtil.getString(Bundle.ENUMERATION, getLocale(), degreeType.getCycleType().getQualifiedName()))
                    .append(")");
        }
    }

    public String getQualifiedAverageGrade(final Locale locale) {
        Integer finalGrade = getDocumentRequest().getFinalAverage();

        String qualifiedAverageGrade;

        if (finalGrade <= 13) {
            qualifiedAverageGrade = "sufficient";
        } else if (finalGrade <= 15) {
            qualifiedAverageGrade = "good";
        } else if (finalGrade <= 17) {
            qualifiedAverageGrade = "verygood";
        } else {
            qualifiedAverageGrade = "excelent";
        }

        return "diploma.supplement.qualifiedgrade." + qualifiedAverageGrade;
    }

    @Override
    protected String getDegreeDescription() {
        if (getRegistration() == null) {
            return super.getDegreeDescription();
        }
        return getRegistration().getDegreeDescription(getRegistration().getStartExecutionYear(),
                getDocumentRequest().getProgramConclusion(), getLocale());
    }

}
