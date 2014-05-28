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
package net.sourceforge.fenixedu.domain.accounting.report.events;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.events.EnrolmentOutOfPeriodEvent;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import net.sourceforge.fenixedu.domain.accounting.events.specializationDegree.SpecializationDegreeRegistrationEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.student.EnrolmentModel;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EventWrapper implements Wrapper {
    private final Event event;

    public EventWrapper(Event event) {
        this.event = event;
    }

    @Override
    public String getStudentNumber() {
        if (event.getParty().isPerson()) {
            if (event.getPerson().hasStudent()) {
                return event.getPerson().getStudent().getNumber().toString();
            }
        }

        return "--";
    }

    @Override
    public String getStudentName() {
        return event.getParty().getName();
    }

    @Override
    public String getStudentEmail() {
        return event.getParty().getDefaultEmailAddressValue();
    }

    @Override
    public String getRegistrationStartDate() {
        return "--";
    }

    @Override
    public String getExecutionYear() {
        ExecutionYear executionYear = getForExecutionYear();
        return executionYear.getName();
    }

    @Override
    public ExecutionYear getForExecutionYear() {

        ExecutionYear executionYear = null;
        if (event.isDfaRegistrationEvent()) {
            executionYear = ((DfaRegistrationEvent) event).getExecutionYear();
        } else if (event.isEnrolmentOutOfPeriod()) {
            executionYear = ((EnrolmentOutOfPeriodEvent) event).getExecutionPeriod().getExecutionYear();
        } else if (event.isAnnual()) {
            executionYear = ((AnnualEvent) event).getExecutionYear();
        } else {
            executionYear = ExecutionYear.readByDateTime(event.getWhenOccured());
        }

        return executionYear;
    }

    @Override
    public String getDegreeName() {
        if (hasRegistration()) {
            return getRegistration().getDegree().getNameI18N().getContent(MultiLanguageString.pt);
        }

        return "--";
    }

    @Override
    public String getDegreeType() {
        if (hasRegistration()) {
            return getRegistration().getDegreeType().getLocalizedName();
        }

        return "--";
    }

    @Override
    public String getPhdProgramName() {
        return "--";
    }

    @Override
    public String getEnrolledECTS() {
        if (hasRegistration()) {
            return new BigDecimal(getRegistration().getLastStudentCurricularPlan()
                    .getEnrolmentsEctsCredits(getForExecutionYear())).toString();
        }

        return "--";
    }

    @Override
    public String getRegime() {
        if (hasRegistration()) {
            return getRegistration().getRegimeType(getForExecutionYear()).getLocalizedName();
        }

        return "--";
    }

    @Override
    public String getEnrolmentModel() {
        if (!hasRegistration()) {
            return "--";
        }

        EnrolmentModel enrolmentModelForExecutionYear =
                getRegistration().getEnrolmentModelForExecutionYear(getForExecutionYear());
        if (enrolmentModelForExecutionYear != null) {
            return enrolmentModelForExecutionYear.getLocalizedName();
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
        if (hasRegistration()) {
            return "Curso";
        }

        return "--";
    }

    private Registration getRegistration() {
        if (event.isDfaRegistrationEvent()) {
            return ((DfaRegistrationEvent) event).getRegistration();
        } else if (event.isEnrolmentOutOfPeriod()) {
            return ((EnrolmentOutOfPeriodEvent) event).getStudentCurricularPlan().getRegistration();
        } else if (event.isSpecializationDegreeRegistrationEvent()) {
            return ((SpecializationDegreeRegistrationEvent) event).getRegistration();
        }

        return null;
    }

    private boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Override
    public String getTotalDiscount() {
        return event.getTotalDiscount().toPlainString();
    }

    @Override
    public boolean isAfterOrEqualExecutionYear(ExecutionYear executionYear) {
        return !getForExecutionYear().isBefore(executionYear);
    }

    @Override
    public AdministrativeOffice getRelatedAcademicOffice() {
        Registration registration = getRegistration();

        if (registration == null) {
            return null;
        }

        return registration.getDegree().getAdministrativeOffice();
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
