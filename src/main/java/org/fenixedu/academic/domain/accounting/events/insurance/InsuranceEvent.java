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
package org.fenixedu.academic.domain.accounting.events.insurance;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountType;
import org.fenixedu.academic.domain.accounting.Entry;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.postingRules.InsurancePR;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.UnitServiceAgreementTemplate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.dto.accounting.SibsTransactionDetailDTO;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class InsuranceEvent extends InsuranceEvent_Base implements IInsuranceEvent {

    static {
        getRelationPersonAccountingEvent().addListener(new RelationAdapter<Party, Event>() {
            @Override
            public void beforeAdd(Party party, Event event) {
                if (event instanceof InsuranceEvent) {
                    Person person = (Person) party;
                    final InsuranceEvent insuranceEvent = ((InsuranceEvent) event);
                    if (person != null
                            && (person.hasAdministrativeOfficeFeeInsuranceEventFor(insuranceEvent.getExecutionYear()) || person
                                    .hasInsuranceEventFor(insuranceEvent.getExecutionYear()))) {
                        throw new DomainException(
                                "error.accounting.events.insurance.InsuranceEvent.person.already.has.insurance.event.for.execution.year");

                    }
                }
            }
        });
    }

    private InsuranceEvent() {
        super();
    }

    public InsuranceEvent(Person person, ExecutionYear executionYear) {
        this();
        init(EventType.INSURANCE, person, executionYear);
        setupDueDate();
    }

    private void setupDueDate() {
        final PostingRule postingRule = getPostingRule();
        if (postingRule instanceof InsurancePR) {
            setDueDate(getWhenOccured().plusDays(((InsurancePR) postingRule).getNumberOfDaysToCalculateDueDate()));
        }
    }

    @Override
    protected void init(final EventType eventType, final Person person, final ExecutionYear executionYear) {
        checkRulesToCreate(person, executionYear);
        super.init(eventType, person, executionYear);
    }

    private void checkRulesToCreate(final Person person, final ExecutionYear executionYear) {
        if (person.hasInsuranceEventOrAdministrativeOfficeFeeInsuranceEventFor(executionYear)) {
            throw new DomainException(
                    "error.accounting.events.insurance.InsuranceEvent.person.already.has.insurance.event.for.execution.year");
        }
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), Bundle.ENUMERATION).appendLabel(" - ")
                .appendLabel(getExecutionYear().getYear());

        return labelFormatter;
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = super.getDescription();
        labelFormatter.appendLabel(" - ").appendLabel(getExecutionYear().getYear());
        return labelFormatter;
    }

    @Override
    protected UnitServiceAgreementTemplate getServiceAgreementTemplate() {
        return getInstitutionUnit().getUnitServiceAgreementTemplate();
    }

    @Override
    public Account getToAccount() {
        return getInstitutionUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    protected Account getFromAccount() {
        return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    private Unit getInstitutionUnit() {
        return Bennu.getInstance().getInstitutionUnit();
    }

    private YearMonthDay calculatePaymentCodeEndDate() {
        return calculateNextEndDate(new YearMonthDay());
    }

    @Override public EntryType getEntryType() {
        return EntryType.INSURANCE_FEE;
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, PaymentCode paymentCode, Money amountToPay,
            SibsTransactionDetailDTO transactionDetail) {
        return internalProcess(responsibleUser,
                Collections.singletonList(new EntryDTO(EntryType.INSURANCE_FEE, this, amountToPay)), transactionDetail);
    }

    public boolean hasInsuranceExemption() {
        return getInsuranceExemption() != null;
    }

    public Exemption getInsuranceExemption() {
        return getExemptionsSet().stream().filter(Exemption::isForInsurance).findFirst().orElse(null);
    }

    @Override
    public boolean isInsuranceEvent() {
        return true;
    }

    @Override
    public DateTime getDueDateByPaymentCodes() {
        return Optional.ofNullable(getDueDate()).orElseGet(super::getDueDateByPaymentCodes);
    }
}
