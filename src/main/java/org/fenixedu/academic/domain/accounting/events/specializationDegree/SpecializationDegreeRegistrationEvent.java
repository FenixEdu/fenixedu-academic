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
package org.fenixedu.academic.domain.accounting.events.specializationDegree;

import org.fenixedu.academic.domain.CandidacyPeriodInDegreeCurricularPlan;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.RegistrationPeriodInDegreeCurricularPlan;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountType;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.joda.time.DateTime;

public class SpecializationDegreeRegistrationEvent extends SpecializationDegreeRegistrationEvent_Base {

    private SpecializationDegreeRegistrationEvent() {
        super();
    }

    public SpecializationDegreeRegistrationEvent(AdministrativeOffice administrativeOffice, Person person,
            Registration registration) {
        this();
        init(administrativeOffice, person, registration);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, Registration registration) {
        super.init(administrativeOffice, EventType.SPECIALIZATION_DEGREE_REGISTRATION, person);
        checkParameters(registration);
        super.setRegistration(registration);
    }

    private void checkParameters(Registration registration) {
        if (registration == null) {
            throw new DomainException("error.accounting.events.dfa.DfaRegistrationEvent.registration.cannot.be.null");
        }

        if (registration.getDegreeType().isSpecializationDegree()) {
            throw new DomainException(
                    "error.accounting.events.specializationDegree.SpecializationDegreeRegistrationEvent.registrationType.incorrect");
        }
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), Bundle.ENUMERATION).appendLabel(" (")
                .appendLabel(getDegree().getDegreeType().getName().getContent()).appendLabel(" - ")
                .appendLabel(getDegree().getNameFor(getExecutionYear()).getContent()).appendLabel(" - ")
                .appendLabel(getExecutionDegree().getExecutionYear().getYear()).appendLabel(")");

        return labelFormatter;
    }

    private ExecutionDegree getExecutionDegree() {
        return getRegistration().getStudentCandidacy().getExecutionDegree();
    }

    private Degree getDegree() {
        return getExecutionDegree().getDegree();
    }

    @Override
    public PostingRule getPostingRule() {
        return getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(), getWhenOccured());
    }

    private AdministrativeOfficeServiceAgreementTemplate getServiceAgreementTemplate() {
        return getAdministrativeOffice().getServiceAgreementTemplate();
    }

    @Override
    public Account getToAccount() {
        return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    protected Account getFromAccount() {
        return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public void setRegistration(Registration registration) {
        throw new DomainException("error.accounting.events.dfa.SpecializationDegreeRegistrationEvent.cannot.modify.registration");
    }

    public DateTime getRegistrationDate() {
        return getRegistration().getStartDate().toDateTimeAtMidnight();
    }

    public RegistrationPeriodInDegreeCurricularPlan getRegistrationPeriodInDegreeCurricularPlan() {
        return getExecutionDegree().getDegreeCurricularPlan().getRegistrationPeriod(getExecutionYear());
    }

    public boolean hasRegistrationPeriodInDegreeCurricularPlan() {
        return getExecutionDegree().getDegreeCurricularPlan().hasRegistrationPeriodFor(getExecutionYear());
    }

    private ExecutionYear getExecutionYear() {
        return getExecutionDegree().getExecutionYear();
    }

    public CandidacyPeriodInDegreeCurricularPlan getCandidacyPeriodInDegreeCurricularPlan() {
        return getExecutionDegree().getDegreeCurricularPlan().getCandidacyPeriod(getExecutionYear());
    }

    public boolean hasCandidacyPeriodInDegreeCurricularPlan() {
        return getExecutionDegree().getDegreeCurricularPlan().hasCandidacyPeriodFor(getExecutionYear());
    }

    private StudentCandidacy getCandidacy() {
        return getRegistration().getStudentCandidacy();
    }

    public DateTime getCandidacyDate() {
        return getCandidacy().getCandidacyDate();
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = super.getDescription();
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel(getDegree().getDegreeType().getName().getContent()).appendLabel(" - ");
        labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent()).appendLabel(" - ");
        labelFormatter.appendLabel(getExecutionYear().getYear());
        return labelFormatter;
    }

    @Override
    public boolean isSpecializationDegreeRegistrationEvent() {
        return true;
    }

}
