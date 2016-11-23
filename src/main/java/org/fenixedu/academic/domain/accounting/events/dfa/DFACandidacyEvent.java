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
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountType;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacy.DFACandidacy;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.util.workflow.StateMachine;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.joda.time.DateTime;

public class DFACandidacyEvent extends DFACandidacyEvent_Base {

    private DFACandidacyEvent() {
        super();
    }

    public DFACandidacyEvent(AdministrativeOffice administrativeOffice, Person person, DFACandidacy candidacy) {
        this();
        init(administrativeOffice, person, candidacy);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, DFACandidacy candidacy) {
        init(administrativeOffice, EventType.CANDIDACY_ENROLMENT, person);
        checkParameters(candidacy);
        super.setCandidacy(candidacy);
    }

    private void checkParameters(Candidacy candidacy) {
        if (candidacy == null) {
            throw new DomainException("error.candidacy.dfaCandidacyEvent.invalid.candidacy");
        }
    }

    @Override
    public Account getToAccount() {
        return getUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    protected Account getFromAccount() {
        return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    private Unit getUnit() {
        return getCandidacy().getExecutionDegree().getDegreeCurricularPlan().getDegree().getUnit();
    }

    @Override
    public void setCandidacy(DFACandidacy candidacy) {
        throw new DomainException("error.candidacy.dfaCandidacyEvent.cannot.modify.candidacy");
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
        return getCandidacy().getExecutionDegree();

    }

    private Degree getDegree() {
        return getExecutionDegree().getDegreeCurricularPlan().getDegree();

    }

    @Override
    public void closeEvent() {
        StateMachine.execute(getCandidacy().getActiveCandidacySituation());

        super.closeEvent();
    }

    @Override
    public PostingRule getPostingRule() {
        return getExecutionDegree().getDegreeCurricularPlan().getServiceAgreementTemplate()
                .findPostingRuleByEventTypeAndDate(getEventType(), getWhenOccured());
    }

    public CandidacyPeriodInDegreeCurricularPlan getCandidacyPeriodInDegreeCurricularPlan() {
        return getExecutionDegree().getDegreeCurricularPlan().getCandidacyPeriod(getExecutionYear());
    }

    public boolean hasCandidacyPeriodInDegreeCurricularPlan() {
        return getExecutionDegree().getDegreeCurricularPlan().hasCandidacyPeriodFor(getExecutionYear());
    }

    private ExecutionYear getExecutionYear() {
        return getExecutionDegree().getExecutionYear();
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
    protected void disconnect() {
        super.setCandidacy(null);
        super.disconnect();
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

}
