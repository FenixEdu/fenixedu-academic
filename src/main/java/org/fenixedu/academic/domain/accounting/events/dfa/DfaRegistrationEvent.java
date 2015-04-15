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
package org.fenixedu.academic.domain.accounting.events.dfa;

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

public class DfaRegistrationEvent extends DfaRegistrationEvent_Base {

    private DfaRegistrationEvent() {
        super();
    }

    public DfaRegistrationEvent(AdministrativeOffice administrativeOffice, Person person, Registration registration) {
        this();
        ExecutionYear executionYear = registration.getStudentCandidacy().getExecutionDegree().getExecutionYear();
        init(administrativeOffice, person, registration, executionYear);
    }

    public DfaRegistrationEvent(AdministrativeOffice administrativeOffice, Person person, Registration registration,
            ExecutionYear executionYear) {
        this();
        init(administrativeOffice, person, registration, executionYear);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, Registration registration,
            ExecutionYear executionYear) {
        super.init(administrativeOffice, EventType.DFA_REGISTRATION, person);
        checkParameters(registration, executionYear);
        super.setRegistration(registration);
        super.setExecutionYear(executionYear);
    }

    private void checkParameters(Registration registration, ExecutionYear executionYear) {
        if (registration == null) {
            throw new DomainException("error.accounting.events.dfa.DfaRegistrationEvent.registration.cannot.be.null");
        }

        if (executionYear == null) {
            throw new DomainException("error.accounting.events.dfa.DfaRegistrationEvent.execution.year.cannot.be.null");
        }
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), Bundle.ENUMERATION).appendLabel(" (")
                .appendLabel(getDegree().getDegreeType().getName().getContent()).appendLabel(" - ")
                .appendLabel(getDegree().getNameFor(getExecutionYear()).getContent()).appendLabel(" - ")
                .appendLabel(getExecutionYear().getYear()).appendLabel(")");

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
        return getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(),
                getExecutionYear().getBeginDateYearMonthDay().toDateMidnight().toDateTime().plusSeconds(1));
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
        throw new DomainException("error.accounting.events.dfa.DfaRegistrationEvent.cannot.modify.registration");
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

    public ExecutionYear getObsoleteExecutionYear() {
        return getExecutionDegree().getExecutionYear();
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
    public boolean isDfaRegistrationEvent() {
        return true;
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

}
