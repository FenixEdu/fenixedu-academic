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
package org.fenixedu.academic.domain.accounting.events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventState;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public abstract class AnnualEvent extends AnnualEvent_Base {

    protected AnnualEvent() {
        super();
    }

    protected void init(EventType eventType, Person person, ExecutionYear executionYear) {
        init(null, eventType, person, executionYear);
    }

    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person, ExecutionYear executionYear) {
        super.init(administrativeOffice, eventType, person);
        checkParameters(executionYear);
        super.setExecutionYear(executionYear);
        initEventStartDate();
    }

    @Override
    protected void initEventStartDate() {
        if (getExecutionYear() != null) {
            setEventStartDate(getExecutionYear().getBeginLocalDate());
        }
    }

    private void checkParameters(ExecutionYear executionYear) {
        if (executionYear == null) {
            throw new DomainException("error.accounting.events.AnnualEvent.executionYear.cannot.be.null");
        }

    }

    public DateTime getStartDate() {
        return getExecutionYear().getBeginDateYearMonthDay().toDateTimeAtMidnight();

    }

    public DateTime getEndDate() {
        return getExecutionYear().getEndDateYearMonthDay().toDateTimeAtMidnight();
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
        throw new DomainException("error.accounting.events.AnnualEvent.cannot.modify.executionYear");
    }

    @Override
    public PostingRule getPostingRule() {
        return getServiceAgreementTemplate().findPostingRuleBy(getEventType(), getStartDate(), getEndDate());
    }

    public boolean isFor(final ExecutionYear executionYear) {
        return super.getExecutionYear() == executionYear;
    }

    @Override
    protected void disconnect() {
        super.setExecutionYear(null);
        super.disconnect();
    }

    @Override
    public boolean isAnnual() {
        return true;
    }

    static public Set<AccountingTransaction> readPaymentsFor(final Class<? extends AnnualEvent> eventClass,
            final YearMonthDay startDate, final YearMonthDay endDate) {
        final Set<AccountingTransaction> result = new HashSet<AccountingTransaction>();
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            for (final AnnualEvent each : executionYear.getAnnualEventsSet()) {
                if (eventClass.equals(each.getClass()) && !each.isCancelled()) {
                    for (final AccountingTransaction transaction : each.getNonAdjustingTransactions()) {
                        if (transaction.isInsidePeriod(startDate, endDate)) {
                            result.add(transaction);
                        }
                    }
                }
            }
        }

        return result;
    }

    abstract protected ServiceAgreementTemplate getServiceAgreementTemplate();

    public boolean isAdministrativeOfficeAndInsuranceEvent() {
        return false;
    }

    public boolean isInsuranceEvent() {
        return false;
    }

}
