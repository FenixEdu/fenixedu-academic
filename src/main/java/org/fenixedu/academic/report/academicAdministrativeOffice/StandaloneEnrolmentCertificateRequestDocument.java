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
import java.util.Collection;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.Locality;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.StandaloneEnrolmentCertificateRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.FenixStringTools;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class StandaloneEnrolmentCertificateRequestDocument extends AdministrativeOfficeDocument {

    protected StandaloneEnrolmentCertificateRequestDocument(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public StandaloneEnrolmentCertificateRequest getDocumentRequest() {
        return (StandaloneEnrolmentCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
        super.fillReport();
        addParameter("enrolmentsInfo", getEnrolmentsInfo());
    }

    @Override
    protected String getDegreeDescription() {
        return getDocumentRequest().getRegistration().getDegreeDescription(null, getLocale());
    }

    @Override
    protected void newFillReport() {
        Registration registration = getDocumentRequest().getRegistration();

        fillFirstParagraph();

        fillSecondParagraph();

        fillSeventhParagraph();

        fillTrailer(registration);

        fillPriceTags();
    }

    protected void fillFirstParagraph() {

        Person coordinator = getAdministrativeOffice().getCoordinator().getPerson();
        String adminOfficeName = getI18NText(getAdministrativeOffice().getName());
        String institutionName = getInstitutionName();
        String universityName = getUniversityName(new DateTime());

        String stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.firstParagraph");
        String coordinatorTitle = getCoordinatorGender(coordinator);

        addParameter(
                "firstParagraph",
                "     "
                        + MessageFormat.format(stringTemplate, coordinator.getName(), coordinatorTitle,
                                adminOfficeName.toUpperCase(getLocale()), institutionName.toUpperCase(getLocale()),
                                universityName.toUpperCase(getLocale())));
    }

    protected void fillSecondParagraph() {
        addParameter(
                "secondParagraph",
                "      "
                        + BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                                "label.academicDocument.standaloneEnrolmentCertificate.secondParagraph"));
    }

    protected void fillSeventhParagraph() {
        String stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                        "label.academicDocument.standaloneEnrolmentCertificate.seventhParagraph");
        addParameter("seventhParagraph", MessageFormat.format(stringTemplate, getDegreeDescription()));
    }

    final private String getEnrolmentsInfo() {
        final StringBuilder result = new StringBuilder();
        StandaloneEnrolmentCertificateRequest request = getDocumentRequest();

        final Collection<Enrolment> enrolments = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID);
        enrolments.addAll(request.getEnrolmentsSet());

        for (final Enrolment enrolment : enrolments) {
            result.append(
                    FenixStringTools.multipleLineRightPadWithSuffix(getPresentationNameFor(enrolment).toUpperCase(), LINE_LENGTH,
                            END_CHAR, getCreditsAndGradeInfo(enrolment, enrolment.getExecutionYear()))).append(LINE_BREAK);
        }

        result.append(generateEndLine());

        return result.toString();
    }

    protected void fillTrailer(Registration registration) {

        Person coordinator = getAdministrativeOffice().getCoordinator().getPerson();
        String adminOfficeName = getI18NText(getAdministrativeOffice().getName());
        String institutionName = getInstitutionName();
        final Locality locality = getAdministrativeOffice().getCampus().getLocality();
        String location = locality != null ? locality.getName() : null;
        String dateDD = new LocalDate().toString("dd", getLocale());
        String dateMMMM = new LocalDate().toString("MMMM", getLocale());
        String dateYYYY = new LocalDate().toString("yyyy", getLocale());
        String student;

        if (registration.getStudent().getPerson().isMale()) {
            student = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.maleStudent");
        } else {
            student = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.femaleStudent");
        }
        String coordinatorTitle = getCoordinatorGender(coordinator);

        String stringTemplate = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.signer");
        addParameter("signer", MessageFormat.format(stringTemplate, coordinatorTitle, adminOfficeName));

        stringTemplate = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.signerLocation");
        addParameter("signerLocation",
                MessageFormat.format(stringTemplate, institutionName, location, dateDD, dateMMMM, dateYYYY));

        stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.footer.studentNumber");
        addParameter("studentNumber", MessageFormat.format(stringTemplate, student, registration.getNumber().toString()));

        stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.footer.documentNumber");
        addParameter("documentNumber",
                MessageFormat.format(stringTemplate, getDocumentRequest().getServiceRequestNumber().toString().trim()));

        addParameter("page", BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.footer.page"));
        addParameter("pageOf",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.footer.pageOf"));

        addParameter("checkedBy", BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                "label.academicDocument.standaloneEnrolmentCertificate.checkedBy"));
    }

    protected void fillPriceTags() {
        addParameter("priceTagsPrinting",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.certificate.printingPriceLabel"));
        addParameter("priceTagsIssuing",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.certificate.issuingPriceLabel"));
        addParameter("priceTagsFastDelivery",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.certificate.fastDeliveryPriceLabel"));
        addParameter("priceTagsTotal",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.certificate.totalsPriceLabel"));
    }

}
