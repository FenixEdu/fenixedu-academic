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
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.EnrolmentDeclarationRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class EnrolmentDeclaration extends AdministrativeOfficeDocument {

    protected EnrolmentDeclaration(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected void fillReport() {
        super.fillReport();
        Registration registration = getDocumentRequest().getRegistration();
        final Person coordinator = getAdministrativeOffice().getCoordinator().getPerson();

        final int numberEnrolments = getDocumentRequest().getRegistration().getNumberOfTotalEnrolments(getExecutionYear());

        String coordinatorTitle = getCoordinatorGender(coordinator);

        String student, studentEnrolment;
        if (registration.getStudent().getPerson().isMale()) {
            student = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.theMaleStudent");
            studentEnrolment =
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                            "label.academicDocument.enrolment.declaration.maleEnrolment");
        } else {
            student = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.theFemaleStudent");
            studentEnrolment =
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                            "label.academicDocument.enrolment.declaration.femaleEnrolment");
        }

        addParameter("documentTitle",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.title.declaration"));
        addParameter("documentPurpose", getDocumentPurpose());
        fillFirstParagraph(coordinatorTitle, coordinator);
        fillSecondParagraph(registration, student);
        fillthirdthParagraph(registration, numberEnrolments, studentEnrolment);
        fillInstitutionAndStaffFields();
        setFooter(getDocumentRequest());
    }

    private void fillthirdthParagraph(Registration registration, Integer numberEnrolments, String studentEnrolment) {
        String situation = "";
        if (getDocumentRequest().getExecutionYear() != null) {
            situation =
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                            getExecutionYear().containsDate(new DateTime()) ? "label.is" : "label.was");
        }

        String executionYear =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "message.declaration.registration.execution.year.prefix");
        String stringTemplate1 =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "message.academicDocument.enrolment.declaration");
        addParameter("thirdParagraph", MessageFormat.format(stringTemplate1, situation, studentEnrolment, executionYear,
                getDocumentRequest().getExecutionYear().getYear().toString(), getCurricularYear(), getDegreeDescription(),
                numberEnrolments, getApprovementInfo()));

    }

    protected void fillFirstParagraph(String coordinatorTitle, Person coordinator) {

        String adminOfficeName = getI18NText(getAdministrativeOffice().getName());
        String institutionName = getInstitutionName();
        String universityName = getUniversityName(new DateTime());
        String stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.firstParagraph");

        addParameter(
                "firstParagraph",
                "     "
                        + MessageFormat.format(stringTemplate, coordinator.getName(), coordinatorTitle,
                                adminOfficeName.toUpperCase(), institutionName.toUpperCase(getLocale()),
                                universityName.toUpperCase(getLocale())));
    }

    protected void fillSecondParagraph(Registration registration, String student) {

        String stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.secondParagraph");
        addParameter("secondParagraph",
                "      " + MessageFormat.format(stringTemplate, student, registration.getNumber().toString()));
    }

    @Override
    protected String getDegreeDescription() {
        final Registration registration = getDocumentRequest().getRegistration();

        if (registration.getDegreeType().isComposite()) {
            return registration.getDegreeDescription(getDocumentRequest().getExecutionYear(), (ProgramConclusion) null,
                    getLocale());
        } else {
            final DegreeType degreeType = registration.getDegreeType();
            final CycleType cycleType =
                    degreeType.hasExactlyOneCycleType() ? degreeType.getCycleType() : registration
                            .getCycleType(getExecutionYear());
            return registration.getDegreeDescription(getExecutionYear(), cycleType, getLocale());
        }
    }

    @Override
    public DocumentRequest getDocumentRequest() {
        return (DocumentRequest) super.getDocumentRequest();
    }

    final private String getCurricularYear() {
        final StringBuilder result = new StringBuilder();

        final DegreeCurricularPlan degreeCurricularPlan =
                getDocumentRequest().getRegistration().getStudentCurricularPlan(getExecutionYear()).getDegreeCurricularPlan();

        if (degreeCurricularPlan.getDurationInYears() != 1) {
            final Integer curricularYear =
                    Integer.valueOf(getDocumentRequest().getRegistration().getCurricularYear(getExecutionYear()));

            result.append(BundleUtil.getString(Bundle.ENUMERATION, getLocale(), curricularYear.toString() + ".ordinal")
                    .toUpperCase());
            result.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                    "label.academicDocument.enrolment.declaration.curricularYear"));
        }

        return result.toString();
    }

    final private String getApprovementInfo() {
        final StringBuilder result = new StringBuilder();

        final EnrolmentDeclarationRequest enrolmentDeclarationRequest = (EnrolmentDeclarationRequest) getDocumentRequest();

        if (enrolmentDeclarationRequest.getDocumentPurposeType() == DocumentPurposeType.PPRE) {
            final Registration registration = getDocumentRequest().getRegistration();
            final ExecutionYear executionYear = enrolmentDeclarationRequest.getExecutionYear();
            final boolean transition = registration.isTransition(executionYear);

            if (registration.isFirstTime(executionYear) && !transition) {
                result.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                        "message.academicDocument.enrolment.declaration.approvement.firstTime"));
            } else {
                final Registration registrationToInspect = transition ? registration.getSourceRegistration() : registration;
                if (registrationToInspect.hasApprovement(executionYear.getPreviousExecutionYear())) {
                    result.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                            "message.academicDocument.enrolment.declaration.approvement.have")
                            + executionYear.getPreviousExecutionYear().getYear());
                } else {
                    result.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                            "message.academicDocument.enrolment.declaration.approvement.notHave")
                            + executionYear.getPreviousExecutionYear().getYear());
                }
            }
        }
        return result.toString();
    }

    final private String getDocumentPurpose() {
        final StringBuilder result = new StringBuilder();

        final EnrolmentDeclarationRequest enrolmentDeclarationRequest = (EnrolmentDeclarationRequest) getDocumentRequest();

        if (enrolmentDeclarationRequest.getDocumentPurposeType() != null) {
            result.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "documents.declaration.valid.purpose")).append(
                    SINGLE_SPACE);
            if (enrolmentDeclarationRequest.getDocumentPurposeType() == DocumentPurposeType.OTHER
                    && !StringUtils.isEmpty(enrolmentDeclarationRequest.getOtherDocumentPurposeTypeDescription())) {
                result.append(enrolmentDeclarationRequest.getOtherDocumentPurposeTypeDescription().toUpperCase());
            } else {
                result.append(BundleUtil.getString(Bundle.ENUMERATION, getLocale(),
                        enrolmentDeclarationRequest.getDocumentPurposeType().name()).toUpperCase());
            }
            result.append(".");
        }

        return result.toString();
    }

}
