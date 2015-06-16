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
import java.util.ArrayList;
import java.util.Locale;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.domain.serviceRequests.IRegistryDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.google.common.base.Joiner;

public class RegistryDiploma extends AdministrativeOfficeDocument {
    private static final long serialVersionUID = 7788392282506503345L;

    protected RegistryDiploma(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected IRegistryDiplomaRequest getDocumentRequest() {
        return (IRegistryDiplomaRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
        super.fillReport();
        IRegistryDiplomaRequest request = getDocumentRequest();
        Person person = request.getPerson();

        setHeader();

        addParameter("institution", getInstitutionName());
        addParameter("university", getUniversity(getDocumentRequest().getConclusionDate().toDateTimeAtCurrentTime())
                .getPartyName().getContent(Locale.getDefault()));

        setFirstParagraph(request);
        setSecondParagraph(person, request);
        String thirdParagraph = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.thirdParagraph");

        String dateWord[] = getDateByWords(request.getConclusionDate());

        addParameter("thirdParagraph", MessageFormat.format(thirdParagraph, dateWord[0], dateWord[1], dateWord[2]));
        addParameter("by", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.by.university"));
        if (getDocumentRequest().isRequestForRegistration()) {
            setFifthParagraph();
        }

        addParameter("studentName", person.getValidatedName());

        String graduateTitle = request.getGraduateTitle(getLocale());

        addParameter("graduateTitle", graduateTitle);
        setFooter();
    }

    private void setFifthParagraph() {
        String fifthParagraph;
        if (getUniversity(new DateTime()) != getUniversity(getDocumentRequest().getConclusionDate().toDateTimeAtCurrentTime())) {
            fifthParagraph =
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.fifthParagraph.UTL.UL");
        } else {
            fifthParagraph = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.fifthParagraph");
        }
        addParameter("fifthParagraph", MessageFormat.format(fifthParagraph,
                BundleUtil.getString(Bundle.ENUMERATION, getLocale(), getDocumentRequest().getFinalAverage(getLocale())),
                getDocumentRequest().getFinalAverage(getLocale()),
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), getDocumentRequest().getQualifiedAverageGrade(getLocale()))));
    }

    protected void setHeader() {

        String degreeRegistrationDiploma =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.degreeRegistrationDiploma");
        final LocalizedString graduationLevel = getDocumentRequest().getProgramConclusion().getGraduationLevel();

        if (!graduationLevel.isEmpty()) {
            degreeRegistrationDiploma =
                    Joiner.on(" ").join(degreeRegistrationDiploma,
                            BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.of.both"),
                            graduationLevel.getContent(getLocale()));
        }

        addParameter("degreeRegistrationDiploma", degreeRegistrationDiploma);
        addParameter("portugueseRepublic_1",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.portugueseRepublic.part1"));
        addParameter("portugueseRepublic_2",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.portugueseRepublic.part2"));
    }

    protected void setFirstParagraph(IRegistryDiplomaRequest request) {

        final UniversityUnit university = getUniversity(new DateTime());
        String universityName = university.getPartyName().getPreferedContent();

        final Person rector = university.getCurrentPrincipal();

        String rectorGender, rectorGrant;

        if (rector.isMale()) {
            rectorGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.rectorMale");
            rectorGrant = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentGrantMale");
        } else {
            rectorGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.rectorFemale");
            rectorGrant = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentGrantFemale");
        }

        String firstParagraph = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.firstParagraph");
        addParameter("firstParagraph", MessageFormat.format(firstParagraph, rectorGender, universityName, rectorGrant,
                rector.getValidatedName(), request.getRegistryCode().getCode()));
    }

    void setSecondParagraph(Person person, IRegistryDiplomaRequest request) {

        String studentGender;
        if (person.isMale()) {
            studentGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.studentHolderMale");
        } else {
            studentGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.studentHolderFemale");
        }

        String secondParagraph = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.secondParagraph");

        String country;
        if (person.getCountry() != null) {
            country = person.getCountry().getCountryNationality().getContent(getLanguage()).toLowerCase();
        } else {
            throw new DomainException("error.personWithoutParishOfBirth");
        }

        ExecutionYear year = getDocumentRequest().getConclusionYear();

        addParameter(
                "secondParagraph",
                MessageFormat.format(secondParagraph, studentGender,
                        BundleUtil.getString(Bundle.ENUMERATION, getLocale(), person.getIdDocumentType().getName()),
                        person.getDocumentIdNumber(), country, getProgramConclusionDescription(year)));
    }

    protected void setFooter() {
        final UniversityUnit university = getUniversity(new DateTime());
        final String institutionUnitName = getInstitutionName();
        final Person president = university.getCurrentPresident();

        final Person rector = university.getCurrentPrincipal();

        String presidentGender;

        if (president.isMale()) {
            presidentGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentMale");
            addParameter("presidentName",
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentGrantMale") + " "
                            + president.getName());

        } else {
            presidentGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentFemale");
            addParameter("presidentName",
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentGrantFemale") + " "
                            + president.getName());
        }

        String rectorGender;
        if (rector.isMale()) {
            rectorGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.rectorMale");
            addParameter("rectorName",
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentGrantMale") + " "
                            + rector.getName());
        } else {
            rectorGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.rectorFemale");
            addParameter("rectorName",
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentGrantFemale") + " "
                            + rector.getName());
        }

        String universityName = university.getPartyName().getPreferedContent();

        addParameter("dateParagraph", getFormatedCurrentDate(universityName));
        addParameter("rector", rectorGender);
        addParameter("president", MessageFormat.format(presidentGender, institutionUnitName));

    }

    private String getFormatedCurrentDate(String universityName) {
        final StringBuilder result = new StringBuilder();
        LocalDate date = new LocalDate();
        String day = Integer.toString(date.getDayOfMonth());
        String month = date.toString("MMMM", getLocale());
        if (getDocumentRequest().getLanguage().getLanguage().equals(Locale.getDefault().getLanguage())) {
            month = month.toLowerCase();
        }
        result.append(universityName).append(", ");
        result.append(day + " ");
        result.append(BundleUtil.getString(Bundle.APPLICATION, getLocale(), "label.of"));
        result.append(" " + month + " ");
        result.append(BundleUtil.getString(Bundle.APPLICATION, getLocale(), "label.of"));
        result.append(" ").append(date.getYear()).append(".");
        return result.toString();
    }

    protected String[] getDateByWords(LocalDate date) {

        String day = Integer.toString(date.getDayOfMonth());
        String month = date.toString("MMMM", getLocale());
        if (getDocumentRequest().getLanguage().getLanguage().equals(Locale.getDefault().getLanguage())) {
            month = month.toLowerCase();
        }
        String year = Integer.toString(date.getYear());
        String finalDate[] = new String[] { day, month, year };
        return finalDate;

    }

    protected String getProgramConclusionDescription(ExecutionYear year) {
        final ProgramConclusion programConclusion = getDocumentRequest().getProgramConclusion();
        final ArrayList<String> result = new ArrayList<>();

        result.add(programConclusion.getName().getContent(getLocale()));

        if (!programConclusion.getDescription().isEmpty()) {
            result.add(BundleUtil.getString(Bundle.ACADEMIC, "label.of.both"));
            result.add(programConclusion.getDescription().getContent(getLocale()));
        }

        result.add(BundleUtil.getString(Bundle.ACADEMIC, "label.in"));
        result.add(getDocumentRequest().getDegreeName(year));

        return Joiner.on(" ").join(result);
    }
}
