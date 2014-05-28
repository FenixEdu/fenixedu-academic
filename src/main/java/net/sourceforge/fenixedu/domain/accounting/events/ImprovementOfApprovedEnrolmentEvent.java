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
package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class ImprovementOfApprovedEnrolmentEvent extends ImprovementOfApprovedEnrolmentEvent_Base {

    protected ImprovementOfApprovedEnrolmentEvent() {
        super();
    }

    public ImprovementOfApprovedEnrolmentEvent(final AdministrativeOffice administrativeOffice, final Person person,
            final Collection<EnrolmentEvaluation> enrolmentEvaluations) {
        this();
        init(administrativeOffice, EventType.IMPROVEMENT_OF_APPROVED_ENROLMENT, person, enrolmentEvaluations);
    }

    protected void init(final AdministrativeOffice administrativeOffice, final EventType eventType, final Person person,
            final Collection<EnrolmentEvaluation> enrolmentEvaluations) {
        checkParameters(enrolmentEvaluations);
        getImprovementEnrolmentEvaluations().addAll(enrolmentEvaluations);
        super.init(administrativeOffice, eventType, person);
    }

    private void checkParameters(final Collection<EnrolmentEvaluation> enrolmentEvaluations) {
        if (enrolmentEvaluations == null || enrolmentEvaluations.isEmpty()) {
            throw new DomainException(
                    "error.accounting.events.EnrolmentInSpecialSeasonEvaluationEvent.enrolmentEvaluations.cannot.be.null");
        }
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = super.getDescription();

        getDetailedDescription(labelFormatter);

        return labelFormatter;
    }

    private void getDetailedDescription(final LabelFormatter labelFormatter) {
        labelFormatter.appendLabel(" (").appendLabel(getImprovementEnrolmentsDescription()).appendLabel(")");
    }

    private String getImprovementEnrolmentsDescription() {
        final StringBuilder result = new StringBuilder();
        for (final EnrolmentEvaluation enrolmentEvaluation : getImprovementEnrolmentEvaluations()) {
            result.append(enrolmentEvaluation.getEnrolment().getName().getContent()).append(", ");
        }

        if (result.toString().endsWith(", ")) {
            result.delete(result.length() - 2, result.length());
        }

        return result.toString();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();

        labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
        getDetailedDescription(labelFormatter);

        return labelFormatter;
    }

    @Override
    protected Account getFromAccount() {
        return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public Account getToAccount() {
        return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    public PostingRule getPostingRule() {
        return getAdministrativeOffice().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(),
                getWhenOccured());
    }

    public boolean hasImprovementOfApprovedEnrolmentPenaltyExemption() {
        return getImprovementOfApprovedEnrolmentPenaltyExemption() != null;
    }

    public ImprovementOfApprovedEnrolmentPenaltyExemption getImprovementOfApprovedEnrolmentPenaltyExemption() {
        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption instanceof ImprovementOfApprovedEnrolmentPenaltyExemption) {
                return (ImprovementOfApprovedEnrolmentPenaltyExemption) exemption;
            }
        }

        return null;
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

    @Override
    public void removeImprovementEnrolmentEvaluations(EnrolmentEvaluation improvementEnrolmentEvaluations) {
        super.removeImprovementEnrolmentEvaluations(improvementEnrolmentEvaluations);
        if (getImprovementEnrolmentEvaluationsSet().isEmpty() && getNonAdjustingTransactions().isEmpty()
                && getAllAdjustedAccountingTransactions().isEmpty()) {
            this.delete();
        }

    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EnrolmentEvaluation> getImprovementEnrolmentEvaluations() {
        return getImprovementEnrolmentEvaluationsSet();
    }

    @Deprecated
    public boolean hasAnyImprovementEnrolmentEvaluations() {
        return !getImprovementEnrolmentEvaluationsSet().isEmpty();
    }

}
