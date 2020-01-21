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

import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.lang.WordUtils;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.IRegistryDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.academic.util.StringFormatter;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import com.google.common.base.Joiner;
import org.joda.time.LocalDate;

public class RegistryDiploma extends AdministrativeOfficeDocument {
    private static final long serialVersionUID = 7788392282506503345L;

    RegistryDiploma(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    public IRegistryDiplomaRequest getDocumentRequest() {
        return (IRegistryDiplomaRequest) super.getDocumentRequest();
    }

    @Override
    public String getReportTemplateKey() {
        return RegistryDiplomaRequest.class.getName();
    }

    @Override
    protected void fillReport() {
        final Person principal = getPrincipal();
        final Person president = getPresident();
        final Person student = getDocumentRequest().getPerson();

        getPayload().addProperty("degreeAcronym", getDocumentRequest().getDegree().getSigla());
        getPayload().addProperty("graduationLevel",
                getDocumentRequest().getProgramConclusion().getGraduationLevel().getContent(getLocale()));
        getPayload().addProperty("universityName", getUniversityName(getDocumentRequest().getRequestDate()));
        getPayload().addProperty("principalGender", principal.getGender().toLocalizedString(LocaleUtils.EN).toLowerCase());
        getPayload().addProperty("presidentGender", president.getGender().toLocalizedString(LocaleUtils.EN).toLowerCase());
        getPayload().addProperty("principalName", principal.getValidatedName());
        getPayload().addProperty("registryCode",
                getDocumentRequest().hasRegistryCode() ? getDocumentRequest().getRegistryCode().getCode() : null);
        getPayload().addProperty("studentName", StringFormatter.prettyPrint(student.getName()));
        getPayload().addProperty("studentGender", student.getGender().toLocalizedString(LocaleUtils.EN).toLowerCase());
        getPayload().addProperty("studentDocumentType", student.getIdDocumentType().getLocalizedName(getLocale()));
        getPayload().addProperty("studentDocumentNumber", student.getDocumentIdNumber());

        if (student.getCountry() != null) {
            getPayload().addProperty("studentNationality", getStudentNationality(student));
        } else {
            throw new DomainException("error.personWithoutParishOfBirth");
        }

        getPayload().addProperty("conclusion", getConclusion());
        getPayload().addProperty("presidentName", president.getValidatedName());
        getPayload().addProperty("institutionName", getInstitutionName());

        if (hasAssociatedInstitutions()) {
            getPayload().addProperty("associatedInstitutions", getAssociatedInstitutions());
        }

        getPayload().addProperty("conclusionDate", getFormattedDate(getDocumentRequest().getConclusionDate()));
        getPayload().addProperty("graduateTitle", getDocumentRequest().getGraduateTitle(getLocale()));
        getPayload().addProperty("isUTL", isUTL());

        if (!(getDocumentRequest() instanceof PhdRegistryDiplomaRequest)) {
            getPayload().addProperty("extensiveGrade", getExtensiveGrade());
            getPayload().addProperty("qualification", getQualification());
        }

        getPayload().addProperty("grade", getGrade());
        getPayload().addProperty("date", getFormattedCurrentDate());
    }

    protected String getConclusion() {
        final ProgramConclusion programConclusion = getDocumentRequest().getProgramConclusion();
        final ArrayList<String> result = new ArrayList<>();
        final ExecutionYear year = getDocumentRequest().getExecutionYear();

        result.add(programConclusion.getName().getContent(getLocale()));

        if (!programConclusion.getDescription().isEmpty()) {
            result.add(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.of.both"));
            result.add(programConclusion.getDescription().getContent(getLocale()));
        }

        result.add(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.in"));
        result.add(getDocumentRequest().getDegreeName(year));

        return Joiner.on(" ").join(result);
    }

    protected boolean isUTL() {
        return getUniversity(new DateTime()) != getUniversity(getDocumentRequest().getConclusionDate().toDateTimeAtCurrentTime());
    }

    private String getStudentNationality(Person student) {
        final String country = student.getCountry().getCountryNationality().getContent(getLanguage()).toLowerCase();

        return BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.registryDiploma.studentNationality",
                WordUtils.capitalize(country));
    }

    private String getAssociatedInstitutions() {
        final StringBuilder result = new StringBuilder();
        final Optional<String> associatedInstitutions = getDocumentRequest().getAssociatedInstitutionsContent();

        associatedInstitutions.ifPresent(result::append);

        return result.toString();
    }

    private boolean hasAssociatedInstitutions() {
        return getDocumentRequest().getAssociatedInstitutionsContent().isPresent();
    }

    private String getExtensiveGrade() {
        final String finalAverage = getDocumentRequest().getFinalAverage(getLocale());

        return BundleUtil.getString(Bundle.ENUMERATION, getLocale(), finalAverage);
    }

    private String getGrade() {
        return getDocumentRequest().getFinalAverage(getLocale());
    }

    protected String getQualification() {
        return BundleUtil.getString(Bundle.ACADEMIC, getLocale(), getDocumentRequest().getQualifiedAverageGrade(getLocale()));
    }

}
