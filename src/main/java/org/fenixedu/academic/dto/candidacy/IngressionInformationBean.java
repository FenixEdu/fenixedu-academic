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
package org.fenixedu.academic.dto.candidacy;

import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.EventTemplate;
import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class IngressionInformationBean implements Serializable {

    private RegistrationProtocol registrationProtocol;

    private String agreementInformation;

    private IngressionType ingressionType;

    private EntryPhase entryPhase;

    private YearMonthDay studiesStartDate;

    private YearMonthDay homologationDate;

    private boolean requestAgreementInformation;

    private ExecutionYear executionYear;
    private Registration registration;
    private LocalDate reingressionDate;

    private EventTemplate eventTemplate;

    public IngressionInformationBean(Registration registration) {
        setEntryPhase(registration.getStudentCandidacy().getEntryPhase());
        setRegistration(registration);
        eventTemplate = registration == null ? null : registration.getEventTemplate();
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public Collection<ExecutionYear> getExecutionYearsSinceRegistrationStart() {
        return ExecutionYear.readExecutionYears(getRegistration().getStartExecutionYear(),
                ExecutionYear.readCurrentExecutionYear());
    }

    public LocalDate getReingressionDate() {
        return reingressionDate;
    }

    public void setReingressionDate(LocalDate reingressionDate) {
        this.reingressionDate = reingressionDate;
    }

    public IngressionInformationBean() {
        super();
        this.registrationProtocol = RegistrationProtocol.getDefault();
        requestAgreementInformation = false;
    }

    public RegistrationProtocol getRegistrationProtocol() {
        return registrationProtocol;
    }

    public void setRegistrationProtocol(RegistrationProtocol registrationProtocol) {
        this.registrationProtocol = registrationProtocol;
        this.requestAgreementInformation = registrationProtocol != null && registrationProtocol.isMilitaryAgreement();
    }

    public boolean hasRegistrationProtocol() {
        return getRegistrationProtocol() != null;
    }

    public String getAgreementInformation() {
        return agreementInformation;
    }

    public void setAgreementInformation(String agreementInformation) {
        this.agreementInformation = agreementInformation;
    }

    public boolean isRequestAgreementInformation() {
        return requestAgreementInformation;
    }

    public IngressionType getIngressionType() {
        return ingressionType;
    }

    public void setIngressionType(IngressionType ingressionType) {
        this.ingressionType = ingressionType;
    }

    public EntryPhase getEntryPhase() {
        return entryPhase;
    }

    public void setEntryPhase(EntryPhase entryPhase) {
        this.entryPhase = entryPhase;
    }

    public void clearIngressionAndEntryPhase() {
        this.ingressionType = null;
        this.entryPhase = null;
        this.studiesStartDate = null;
        this.homologationDate = null;
    }

    public void clearAgreement() {
        this.registrationProtocol = RegistrationProtocol.getDefault();
        this.agreementInformation = null;
    }

    public YearMonthDay getHomologationDate() {
        return homologationDate;
    }

    public void setHomologationDate(YearMonthDay homologationDate) {
        this.homologationDate = homologationDate;
    }

    public YearMonthDay getStudiesStartDate() {
        return studiesStartDate;
    }

    public void setStudiesStartDate(YearMonthDay studiesStartDate) {
        this.studiesStartDate = studiesStartDate;
    }

    public EventTemplate getEventTemplate() {
        return eventTemplate;
    }

    public void setEventTemplate(final EventTemplate eventTemplate) {
        this.eventTemplate = eventTemplate;
    }

}
