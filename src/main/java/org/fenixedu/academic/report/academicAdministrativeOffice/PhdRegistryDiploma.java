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

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.IRegistryDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class PhdRegistryDiploma extends RegistryDiploma {

    private static final long serialVersionUID = 1L;

    protected PhdRegistryDiploma(IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected PhdRegistryDiplomaRequest getDocumentRequest() {
        return (PhdRegistryDiplomaRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
        super.fillReport();

        IRegistryDiplomaRequest request = getDocumentRequest();
        Person person = request.getPerson();

        setHeader();

        addParameter("institution", getInstitutionName());

        setFirstParagraph(request);
        setSecondParagraph(person, request);

        addParameter("thirdParagraph",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.phdThirdParagraph"));

        String dateWord[] = getDateByWords(request.getConclusionDate());

        String fourthParagraph =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.phdfourthParagraph");

        addParameter("fourthParagraph", MessageFormat.format(fourthParagraph, dateWord[0], dateWord[1], dateWord[2]));

        String fifthParagraph;
        if (getUniversity(new DateTime()) != getUniversity(getDocumentRequest().getConclusionDate().toDateTimeAtCurrentTime())) {
            fifthParagraph =
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.phdFifthParagraph.UTL");
            addParameter("by", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.by.university"));
        } else {
            fifthParagraph = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.phdFifthParagraph");
            addParameter("university", "");
            addParameter("by", "");
        }

        addParameter("fifthParagraph", MessageFormat.format(fifthParagraph, getDocumentRequest().getFinalAverage(getLocale())));
        setFooter();
    }

    @Override
    protected void setHeader() {
        addParameter("thesisTitle", getThesisTitleI18N().getContent(getLanguage()));
        super.setHeader();
    }

    private MultiLanguageString getThesisTitleI18N() {
        return new MultiLanguageString(MultiLanguageString.pt, getDocumentRequest().getPhdIndividualProgramProcess()
                .getThesisTitle()).with(MultiLanguageString.en, getDocumentRequest().getPhdIndividualProgramProcess()
                .getThesisTitleEn());
    }

    @Override
    void setSecondParagraph(Person person, IRegistryDiplomaRequest request) {

        String studentGender;
        if (person.isMale()) {
            studentGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.studentHolderMale");
        } else {
            studentGender = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.studentHolderFemale");
        }

        String country;
        String countryUpperCase;
        if (person.getCountry() != null) {
            country = person.getCountry().getCountryNationality().getContent(getLanguage()).toLowerCase();
        } else {
            throw new DomainException("error.personWithoutParishOfBirth");
        }

        PhdRegistryDiplomaRequest phdRequest = getDocumentRequest();

        String secondParagraph =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.registryDiploma.phdSecondParagraph");
        addParameter(
                "secondParagraph",
                MessageFormat.format(secondParagraph, studentGender,
                        BundleUtil.getString(Bundle.ENUMERATION, getLocale(), person.getIdDocumentType().getName()),
                        person.getDocumentIdNumber(), country, phdRequest.getPhdIndividualProgramProcess().getPhdProgram()
                                .getName().getContent(getLanguage())));
    }

    @Override
    protected String getDegreeDescription() {
        return getDocumentRequest().getDegreeName(getDocumentRequest().getConclusionYear());
    }

}
