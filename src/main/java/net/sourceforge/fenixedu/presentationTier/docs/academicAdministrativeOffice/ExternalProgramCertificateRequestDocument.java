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
package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExternalCourseLoadRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExternalProgramCertificateRequest;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.LocalDate;

import pt.ist.utl.fenix.utils.NumberToWordsConverter;

public class ExternalProgramCertificateRequestDocument extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 10L;

    protected ExternalProgramCertificateRequestDocument(final ExternalProgramCertificateRequest externalProgramCertificateRequest) {
        super(externalProgramCertificateRequest);
    }

    @Override
    protected ExternalProgramCertificateRequest getDocumentRequest() {
        return (ExternalProgramCertificateRequest) super.getDocumentRequest();
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
        Unit adminOfficeUnit = administrativeOffice.getUnit();
        Person activeUnitCoordinator = adminOfficeUnit.getActiveUnitCoordinator();

        addParameter("administrativeOfficeCoordinatorName", activeUnitCoordinator.getName());
        addParameter("administrativeOfficeName", getMLSTextContent(adminOfficeUnit.getPartyName()));
        addParameter("institutionName", Bennu.getInstance().getInstitutionUnit().getName());
        addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getName());

        addParameter("day", new LocalDate().toString(DD_MMMM_YYYY, getLocale()));

        addParameter("numberOfPrograms", NumberToWordsConverter.convert(getDocumentRequest().getNumberOfPrograms()));
        addParameter("externalInstitutionName", getDocumentRequest().getInstitution().getName());
    }

    private String getStudentNumber() {
        final Registration registration = getDocumentRequest().getRegistration();
        if (ExternalCourseLoadRequest.FREE_PAYMENT_AGREEMENTS.contains(registration.getRegistrationAgreement())) {
            final String agreementInformation = registration.getAgreementInformation();
            if (!StringUtils.isEmpty(agreementInformation)) {
                return registration.getRegistrationAgreement().toString() + SINGLE_SPACE + agreementInformation;
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
