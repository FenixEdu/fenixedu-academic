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
package net.sourceforge.fenixedu.domain.accounting.postingRules;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class PartialRegistrationRegimeRequestPR extends PartialRegistrationRegimeRequestPR_Base {

    protected PartialRegistrationRegimeRequestPR() {
        super();
    }

    public PartialRegistrationRegimeRequestPR(ExecutionYear executionYear, ServiceAgreementTemplate serviceAgreementTemplate,
            Money fixedAmount) {
        this();
        init(EntryType.PARTIAL_REGISTRATION_REGIME_REQUEST_FEE, EventType.PARTIAL_REGISTRATION_REGIME_REQUEST, executionYear,
                executionYear.getBeginDateYearMonthDay().toDateTimeAtMidnight(), executionYear.getEndDateYearMonthDay()
                        .toDateTimeAtMidnight(), serviceAgreementTemplate, fixedAmount);
    }

    private void checkParameters(ExecutionYear executionYear) {
        if (executionYear == null) {
            throw new DomainException(
                    "error.accounting.postingRules.PartialRegistrationRegimeRequestPR.executionYear.cannot.be.null");
        }
    }

    protected void init(EntryType entryType, EventType eventType, ExecutionYear executionYear, DateTime startDate,
            DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount) {
        checkParameters(executionYear);
        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, fixedAmount);
        setExecutionYear(executionYear);
    }

    @Override
    public boolean isVisible() {
        return this == readMostRecentPostingRule();
    }

    @Override
    public boolean isActive() {
        // TODO Auto-generated method stub
        return super.isActive();
    }

    @Override
    public boolean isActiveForDate(DateTime when) {
        ExecutionYear executionYear = ExecutionYear.readByDateTime(when);
        return this == readMostRecentPostingRuleForExecutionYear(executionYear);
    }

    @Override
    public boolean overlaps(EventType eventType, DateTime startDate, DateTime endDate) {
        return false;
    }

    public static PartialRegistrationRegimeRequestPR readMostRecentPostingRule() {
        for (ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear(); executionYear != null; executionYear =
                executionYear.getPreviousExecutionYear()) {
            PartialRegistrationRegimeRequestPR mostRecent = readMostRecentPostingRuleForExecutionYear(executionYear);

            if (mostRecent != null) {
                return mostRecent;
            }
        }

        return null;
    }

    public static PartialRegistrationRegimeRequestPR readMostRecentPostingRuleForExecutionYear(ExecutionYear executionYear) {
        PartialRegistrationRegimeRequestPR mostRecent = null;

        for (PartialRegistrationRegimeRequestPR postingRule : executionYear.getPartialRegistrationRegimeRequestPostingRules()) {
            if (mostRecent == null) {
                mostRecent = postingRule;
                continue;
            }

            if (postingRule.getCreationDate().isAfter(mostRecent.getCreationDate())) {
                mostRecent = postingRule;
            }
        }

        return mostRecent;
    }

    @Override
    public PartialRegistrationRegimeRequestPR edit(final Money fixedAmount) {
        return new PartialRegistrationRegimeRequestPR(getExecutionYear(), getServiceAgreementTemplate(), fixedAmount);
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
