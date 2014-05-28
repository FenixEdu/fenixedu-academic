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
package net.sourceforge.fenixedu.domain.accounting.events.specializationDegree;

import net.sourceforge.fenixedu.domain.CandidacyPeriodInDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RegistrationPeriodInDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

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

        if (registration.getDegreeType().equals(DegreeType.BOLONHA_SPECIALIZATION_DEGREE)) {
            throw new DomainException(
                    "error.accounting.events.specializationDegree.SpecializationDegreeRegistrationEvent.registrationType.incorrect");
        }
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" (")
                .appendLabel(getDegree().getDegreeType().name(), "enum").appendLabel(" - ")
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
        labelFormatter.appendLabel(getDegree().getDegreeType().name(), "enum").appendLabel(" - ");
        labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent()).appendLabel(" - ");
        labelFormatter.appendLabel(getExecutionYear().getYear());
        return labelFormatter;
    }

    @Override
    public boolean isSpecializationDegreeRegistrationEvent() {
        return true;
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

}
