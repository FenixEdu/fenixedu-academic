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
package org.fenixedu.academic.domain.accounting.report.events;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.phd.serviceRequests.PhdAcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.RegistrationAcademicServiceRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.MultiLanguageString;

public class AcademicServiceRequestEventWrapper implements Wrapper {

    private final AcademicServiceRequestEvent event;
    private final AcademicServiceRequest request;

    public AcademicServiceRequestEventWrapper(AcademicServiceRequestEvent event) {
        this.event = event;
        this.request = event.getAcademicServiceRequest();
    }

    @Override
    public String getStudentNumber() {
        if (request.getPerson().getStudent() != null) {
            return request.getPerson().getStudent().getNumber().toString();
        }

        return "--";
    }

    @Override
    public String getStudentName() {
        return request.getPerson().getName();
    }

    @Override
    public String getStudentEmail() {
        return event.getPerson().getDefaultEmailAddressValue();
    }

    @Override
    public String getRegistrationStartDate() {
        if (request.isRequestForRegistration()) {
            return ((RegistrationAcademicServiceRequest) request).getRegistration().getStartDate().toString("dd/MM/yyyy");
        }

        return "--";
    }

    @Override
    public String getExecutionYear() {
        return getForExecutionYear().getName();
    }

    @Override
    public String getDegreeName() {
        if (request.isRequestForRegistration()) {
            return ((RegistrationAcademicServiceRequest) request).getRegistration().getDegree().getNameI18N()
                    .getContent(MultiLanguageString.pt);
        }

        return "--";
    }

    @Override
    public String getDegreeType() {
        if (request.isRequestForRegistration()) {
            return ((RegistrationAcademicServiceRequest) request).getRegistration().getDegreeType().getName().getContent();
        }

        return "--";
    }

    @Override
    public String getPhdProgramName() {
        if (request.isRequestForPhd()) {
            return ((PhdAcademicServiceRequest) request).getPhdIndividualProgramProcess().getPhdProgram().getName()
                    .getContent(MultiLanguageString.pt);
        }

        return "--";
    }

    @Override
    public String getEnrolledECTS() {
        if (request.isRequestForRegistration()) {
            Registration registration = ((RegistrationAcademicServiceRequest) request).getRegistration();
            ExecutionYear executionYear = ExecutionYear.readByDateTime(request.getCreationDate());

            return new BigDecimal(registration.getEnrolmentsEcts(executionYear)).toString();
        }

        return "--";
    }

    @Override
    public String getRegime() {
        if (request.isRequestForRegistration()) {
            Registration registration = ((RegistrationAcademicServiceRequest) request).getRegistration();
            ExecutionYear executionYear = ExecutionYear.readByDateTime(request.getCreationDate());

            return registration.getRegimeType(executionYear).getLocalizedName();
        }

        return "--";
    }

    @Override
    public String getEnrolmentModel() {
        if (request.isRequestForRegistration()) {
            Registration registration = ((RegistrationAcademicServiceRequest) request).getRegistration();
            ExecutionYear executionYear = ExecutionYear.readByDateTime(request.getCreationDate());

            if (registration.getEnrolmentModelForExecutionYear(executionYear) != null) {
                return registration.getEnrolmentModelForExecutionYear(executionYear).getLocalizedName();
            }

        }

        return "--";
    }

    @Override
    public String getResidenceYear() {
        return "--";
    }

    @Override
    public String getResidenceMonth() {
        return "--";
    }

    @Override
    public String getStudiesType() {
        if (request.isRequestForRegistration()) {
            return Wrapper.REGISTRATION_STUDIES;
        } else if (request.isRequestForPhd()) {
            return Wrapper.PHD_PROGRAM_STUDIES;
        }

        return "--";
    }

    @Override
    public String getTotalDiscount() {
        return event.getTotalDiscount().toPlainString();
    }

    @Override
    public boolean isAfterOrEqualExecutionYear(ExecutionYear executionYear) {
        return !ExecutionYear.readByDateTime(request.getCreationDate()).isBefore(executionYear);
    }

    @Override
    public ExecutionYear getForExecutionYear() {
        return ExecutionYear.readByDateTime(request.getCreationDate());
    }

    @Override
    public AdministrativeOffice getRelatedAcademicOffice() {
        return event.getAdministrativeOffice();
    }

    @Override
    public String getRelatedEventExternalId() {
        return "--";
    }

    @Override
    public String getDebtorFiscalId() {
        return "--";
    }

    @Override
    public String getDebtorName() {
        return "--";
    }

}
