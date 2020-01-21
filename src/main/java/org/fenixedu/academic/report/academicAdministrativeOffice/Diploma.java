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

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.IDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.academic.util.StringFormatter;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

public class Diploma extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 1L;

    protected Diploma(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    public IDiplomaRequest getDocumentRequest() {
        return (IDiplomaRequest) super.getDocumentRequest();
    }

    @Override
    public String getReportTemplateKey() {
        return DiplomaRequest.class.getName();
    }

    @Override
    protected void fillReport() {
        final Person principal = getPrincipal();
        final Person president = getPresident();
        final Person student = getDocumentRequest().getPerson();

        getPayload().addProperty("registryCode",
                getDocumentRequest().hasRegistryCode() ? getDocumentRequest().getRegistryCode().getCode() : null);
        getPayload()
                .addProperty("conclusionDate", getFormattedDate(getDocumentRequest().getConclusionDate()));
        getPayload().addProperty("date", getFormattedCurrentDate());
        getPayload().addProperty("graduateTitle", getDocumentRequest().getGraduateTitle(getLocale()));
        getPayload().addProperty("qualification", getQualification());
        getPayload().addProperty("conclusion", getConclusion());
        getPayload().addProperty("degreeAcronym", getDocumentRequest().getDegree().getSigla());

        if (hasAssociatedInstitutions()) {
            getPayload().addProperty("associatedInstitutions", getAssociatedInstitutions());
        }

        getPayload().addProperty("isUTL", isUTL());
        getPayload().addProperty("institutionName", getInstitutionName());
        getPayload().addProperty("universityName", getUniversityName(getDocumentRequest().getRequestDate()));
        getPayload().addProperty("principalGender", principal.getGender().toLocalizedString(LocaleUtils.EN).toLowerCase());
        getPayload().addProperty("presidentGender", president.getGender().toLocalizedString(LocaleUtils.EN).toLowerCase());
        getPayload().addProperty("principalName", principal.getValidatedName());
        getPayload().addProperty("presidentName", president.getValidatedName());
        getPayload().addProperty("studentName", StringFormatter.prettyPrint(student.getName()));

        if (!(getDocumentRequest() instanceof PhdDiplomaRequest)) {
            getPayload().addProperty("graduationLevel",
                    getDocumentRequest().getProgramConclusion().getGraduationLevel().getContent(getLocale()));
            getPayload().addProperty("extensiveGrade", getExtensiveGrade());
            getPayload().addProperty("grade", getGrade());
        }

        if (student.getCountry() != null) {
            getPayload().addProperty("studentNationality", getStudentNationality(student));
        } else {
            throw new DomainException("error.personWithoutParishOfBirth");
        }
    }

    protected String getConclusion() {
        final StringBuilder result = new StringBuilder();
        final Registration registration = getRegistration();

        if (registration.getDegreeType().isAdvancedFormationDiploma() || registration.getDegreeType()
                .isAdvancedSpecializationDiploma()) {
            forDFA(result, registration);
        } else {
            forOthers(result, registration);
        }

        return result.toString();
    }

    private void forOthers(StringBuilder result, final Registration registration) {
        final DegreeType degreeType = registration.getDegreeType();

        if (degreeType.hasAnyCycleTypes() && getDocumentRequest().getRequestedCycle() != null) {
            result.append(BundleUtil
                    .getString(Bundle.ENUMERATION, getLocale(), getDocumentRequest().getRequestedCycle().getQualifiedName()));
            result.append(SINGLE_SPACE).append(BundleUtil.getString(Bundle.APPLICATION, getLocale(), "of.masculine"))
                    .append(SINGLE_SPACE);
        }

        result.append(degreeType.getPrefix(getLocale()))
                .append(getDocumentRequest().getProgramConclusion().getDescription().getContent(getLocale()));
    }

    private void forDFA(StringBuilder result, final Registration registration) {
        final DegreeType degreeType = registration.getDegreeType();

        result.append(degreeType.getPrefix());
        result.append(degreeType.getName().getContent());

        if (degreeType.hasExactlyOneCycleType()) {
            result.append(" (")
                    .append(BundleUtil.getString(Bundle.ENUMERATION, getLocale(), degreeType.getCycleType().getQualifiedName()))
                    .append(")");
        }
    }

    private String getQualifiedAverageGrade() {
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

    private String getExtensiveGrade() {
        return BundleUtil.getString(Bundle.ENUMERATION, getLocale(), getGrade());
    }

    protected String getGrade() {
        Integer finalAverage = getDocumentRequest().getFinalAverage();

        if (finalAverage == null)
            return StringUtils.EMPTY;

        return finalAverage.toString();
    }

    protected String getQualification() {
        return BundleUtil.getString(Bundle.ACADEMIC, getLocale(), getQualifiedAverageGrade());
    }

    private boolean hasAssociatedInstitutions() {
        return getDocumentRequest().getAssociatedInstitutionsContent().isPresent();
    }

    protected String getAssociatedInstitutions() {
        final StringBuilder result = new StringBuilder();
        final Optional<String> associatedInstitutions = getDocumentRequest().getAssociatedInstitutionsContent();

        associatedInstitutions.ifPresent(result::append);

        return result.toString();
    }

    protected boolean isUTL() {
        return getUniversity(getDocumentRequest().getRequestDate()) != getUniversity(
                getDocumentRequest().getConclusionDate().toDateTimeAtCurrentTime());
    }

    private String getStudentNationality(final Person student) {
        final String country = student.getCountry().getCountryNationality().getContent(getLanguage()).toLowerCase();

        return BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "diploma.nationality", WordUtils.capitalize(country));
    }

    @Override
    protected String getDegreeDescription() {
        if (getRegistration() == null) {
            return super.getDegreeDescription();
        }
        return getRegistration()
                .getDegreeDescription(getRegistration().getStartExecutionYear(), getDocumentRequest().getProgramConclusion(),
                        getLocale());
    }

}
