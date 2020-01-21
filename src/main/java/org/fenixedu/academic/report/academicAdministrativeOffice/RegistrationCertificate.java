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
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class RegistrationCertificate extends AdministrativeOfficeDocument {

    protected RegistrationCertificate(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected void fillReport() {
        super.fillReport();
    }

    @Override
    public DocumentRequest getDocumentRequest() {
        return (DocumentRequest) super.getDocumentRequest();
    }

    @Override
    protected String getDegreeDescription() {
        final Registration registration = getDocumentRequest().getRegistration();
        final DegreeType degreeType = registration.getDegreeType();
        final CycleType cycleType =
                degreeType.hasExactlyOneCycleType() ? degreeType.getCycleType() : registration.getCycleType(getExecutionYear());
        return registration.getDegreeDescription(getExecutionYear(), cycleType, getLocale());
    }

    @Override
    protected void setDocumentTitle() {
        addParameter("documentTitle",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.title.declaration"));
    }

    @Override
    protected void newFillReport() {
        Person coordinator = getAdministrativeOffice().getCoordinator().getPerson();
        Registration registration = getDocumentRequest().getRegistration();

        String coordinatorTitle = getCoordinatorGender(coordinator);

        String studentRegistered;
        if (registration.getStudent().getPerson().isMale()) {
            studentRegistered =
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.maleRegistered");
        } else {
            studentRegistered =
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.femaleRegistered");
        }

        fillFirstParagraph(coordinator, coordinatorTitle);

        fillSecondParagraph(registration);

        fillSeventhParagraph(registration, studentRegistered);

        fillInstitutionAndStaffFields();
        setFooter(getDocumentRequest());

    }

    protected void fillFirstParagraph(Person coordinator, String coordinatorTitle) {

        String adminOfficeName = getI18NText(getAdministrativeOffice().getName());
        String institutionName = getInstitutionName();
        String universityName = getUniversityName(new DateTime());

        String stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.firstParagraph");

        addParameter(
                "firstParagraph",
                "     "
                        + MessageFormat.format(stringTemplate, coordinator.getName(), coordinatorTitle,
                                adminOfficeName.toUpperCase(getLocale()), institutionName.toUpperCase(getLocale()),
                                universityName.toUpperCase(getLocale())));

        addParameter("certificate", BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                "label.academicDocument.standaloneEnrolmentCertificate.secondParagraph"));
    }

    protected void fillSecondParagraph(Registration registration) {
        String student;
        if (registration.getStudent().getPerson().isMale()) {
            student = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.theMaleStudent");
        } else {
            student = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.theFemaleStudent");
        }
        String stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.secondParagraph");
        addParameter("secondParagraph",
                "      " + MessageFormat.format(stringTemplate, student, registration.getNumber().toString()));
    }

    protected void fillSeventhParagraph(Registration registration, String studentRegistered) {

        String situation =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                        getExecutionYear().containsDate(new DateTime()) ? "label.is" : "label.was");

        String stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.seventhParagraph");
        addParameter("seventhParagraph", MessageFormat.format(stringTemplate, situation,
                studentRegistered.toUpperCase(getLocale()), getExecutionYear().getYear(), getDegreeDescription()));
    }

}
