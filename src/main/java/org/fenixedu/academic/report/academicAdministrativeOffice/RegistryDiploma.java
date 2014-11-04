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
package org.fenixedu.academic.report.academicAdministrativeOffice;

import java.text.MessageFormat;
import java.util.Locale;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.IRegistryDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

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

        if (graduateTitle.contains("Graduated")) {
            graduateTitle = graduateTitle.replace("Graduated", "Licenciado");
        }

        if (graduateTitle.contains("Master")) {
            graduateTitle = graduateTitle.replace("Master", "Mestre");
        }
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

        final CycleType cycleType = getDocumentRequest().getRequestedCycle();
        final String diplomaTypeQualifier;
        if (cycleType == CycleType.FIRST_CYCLE) {
            diplomaTypeQualifier = "label.diploma.type.qualifier.first.cycle";
        } else if (cycleType == CycleType.SECOND_CYCLE) {
            diplomaTypeQualifier = "label.diploma.type.qualifier.second.cycle";
        } else if (cycleType == CycleType.THIRD_CYCLE) {
            diplomaTypeQualifier = "label.diploma.type.qualifier.third.cycle";
        } else {
            diplomaTypeQualifier = "label.diploma.type.qualifier.unknown";
        }

        addParameter("degreeRegistrationDiploma", MessageFormat.format(
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.degreeRegistrationDiploma"),
                new Object[] { BundleUtil.getString(Bundle.ACADEMIC, getLocale(), diplomaTypeQualifier) }));
        addParameter("portugueseRepublic_1",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.portugueseRepublic.part1"));
        addParameter("portugueseRepublic_2",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.portugueseRepublic.part2"));
    }

    protected void setFirstParagraph(IRegistryDiplomaRequest request) {

        final UniversityUnit university = getUniversity(new DateTime());
        String universityName = university.getPartyName().getPreferedContent();

        final Person rectorIst = university.getInstitutionsUniversityResponsible(FunctionType.PRINCIPAL);

        String rectorGender, rectorGrant;

        if (rectorIst.isMale()) {
            rectorGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.rectorMale");
            rectorGrant = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentGrantMale");
        } else {
            rectorGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.rectorFemale");
            rectorGrant = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentGrantFemale");
        }

        String firstParagraph = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.firstParagraph");
        addParameter("firstParagraph", MessageFormat.format(firstParagraph, rectorGender, universityName, rectorGrant,
                rectorIst.getValidatedName(), request.getRegistryCode().getCode()));
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

        Degree degree = ((RegistryDiplomaRequest) getDocumentRequest()).getDegree();
        ExecutionYear year = ((RegistryDiplomaRequest) getDocumentRequest()).getConclusionYear();

        addParameter(
                "secondParagraph",
                MessageFormat.format(secondParagraph, studentGender,
                        BundleUtil.getString(Bundle.ENUMERATION, getLocale(), person.getIdDocumentType().getName()),
                        person.getDocumentIdNumber(), country, getCycleDescription(), degree.getFilteredName(year, getLocale())));
    }

    protected void setFooter() {
        final UniversityUnit university = getUniversity(new DateTime());
        final String institutionUnitName = getInstitutionName();
        final Person presidentIst = university.getInstitutionsUniversityResponsible(FunctionType.PRESIDENT);

        final Person rectorIst = university.getInstitutionsUniversityResponsible(FunctionType.PRINCIPAL);

        String presidentGender;

        if (presidentIst.isMale()) {
            presidentGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentMale");
            addParameter("presidentName",
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentGrantMale") + " "
                            + presidentIst.getName());

        } else {
            presidentGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentFemale");
            addParameter("presidentName",
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentGrantFemale") + " "
                            + presidentIst.getName());
        }

        String rectorGender;
        if (rectorIst.isMale()) {
            rectorGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.rectorMale");
            addParameter("rectorName",
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentGrantMale") + " "
                            + rectorIst.getName());
        } else {
            rectorGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.rectorFemale");
            addParameter("rectorName",
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.presidentGrantFemale") + " "
                            + rectorIst.getName());
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

    protected String getCycleDescription() {

        final StringBuilder res = new StringBuilder();
        RegistryDiplomaRequest request = (RegistryDiplomaRequest) getDocumentRequest();
        CycleType cycle = request.getRequestedCycle();
        Degree degree = request.getDegree();
        final DegreeType degreeType = request.getDegreeType();
        if (degreeType.hasAnyCycleTypes()) {
            res.append(cycle.getDescription(getLocale()));
            res.append(" ").append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.of.both")).append(" ");
        }
        if (!degree.isEmpty()) {
            res.append(degreeType.getFilteredName(getLocale()));
        }
        return res.toString();
    }

    @Override
    protected String getDegreeDescription() {

        if (getDocumentRequest().isRequestForRegistration()) {
            final StringBuilder res = new StringBuilder();
            RegistryDiplomaRequest request = (RegistryDiplomaRequest) getDocumentRequest();
            CycleType cycle = request.getRequestedCycle();
            Degree degree = request.getDegree();
            final DegreeType degreeType = request.getDegreeType();
            if (degreeType.hasAnyCycleTypes()) {
                res.append(cycle.getDescription(getLocale()));
                res.append(" ").append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.of.both")).append(" ");
            }

            if (!degree.isEmpty()) {
                res.append(degreeType.getFilteredName(getLocale()));
                res.append(" ").append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.in")).append(" ");
            }

            res.append(degree.getNameI18N(request.getConclusionYear()).getContent(getLanguage()));
            return res.toString();
        } else if (getDocumentRequest().isRequestForPhd()) {
            PhdRegistryDiplomaRequest request = (PhdRegistryDiplomaRequest) getDocumentRequest();
            final StringBuilder res = new StringBuilder();
            res.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.doctoral.program.designation"));
            res.append(" ").append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.in"));
            res.append(" ").append(request.getPhdIndividualProgramProcess().getPhdProgram().getName().getContent(getLanguage()));

            return res.toString();
        }

        throw new DomainException("docs.academicAdministrativeOffice.RegistryDiploma.degreeDescription.invalid.request");
    }
}
