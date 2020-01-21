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

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.ExternalCourseLoadRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.NumberToWordsConverter;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.LocalDate;

public class ExternalCourseLoadRequestDocument extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 10L;

    protected ExternalCourseLoadRequestDocument(final ExternalCourseLoadRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    public ExternalCourseLoadRequest getDocumentRequest() {
        return (ExternalCourseLoadRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
        setPersonFields();
        addParametersInformation();
    }

    private void addParametersInformation() {
        addParameter("studentNumber", getStudentNumber());
        addParameter("degreeDescription", getDegreeDescription());

        AdministrativeOffice administrativeOffice = getAdministrativeOffice();
        Person activeUnitCoordinator = administrativeOffice.getCoordinator().getPerson();

        addParameter("administrativeOfficeCoordinatorName", activeUnitCoordinator.getName());
        addParameter("administrativeOfficeName", getI18NText(administrativeOffice.getName()));
        addParameter("institutionName", Bennu.getInstance().getInstitutionUnit().getName());
        addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getName());

        addParameter("day", new LocalDate().toString(DD_MMMM_YYYY, getLocale()));

        addParameter("numberOfCourseLoads", NumberToWordsConverter.convert(getDocumentRequest().getNumberOfCourseLoads()));
        addParameter("externalInstitutionName", getDocumentRequest().getInstitution().getName());
    }

    private String getStudentNumber() {
        final Registration registration = getDocumentRequest().getRegistration();
        if (registration.getRegistrationProtocol().isMilitaryAgreement()) {
            final String agreementInformation = registration.getAgreementInformation();
            if (!StringUtils.isEmpty(agreementInformation)) {
                return registration.getRegistrationProtocol().getCode() + SINGLE_SPACE + agreementInformation;
            }
        }
        return registration.getStudent().getNumber().toString();
    }

    @Override
    protected boolean showPriceFields() {
        return false;
    }

    @Override
    protected void setPersonFields() {
        addParameter("name", getDocumentRequest().getPerson().getName());
    }
}
