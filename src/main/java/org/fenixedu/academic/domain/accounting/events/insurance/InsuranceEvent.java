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
package net.sourceforge.fenixedu.domain.accounting.events.insurance;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.UnitServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

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
        labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" - ").appendLabel(getExecutionYear().getYear());

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

    @Override
    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
        final EntryDTO entryDTO = calculateEntries(new DateTime()).iterator().next();
        getNonProcessedPaymentCodes().iterator().next()
                .update(new YearMonthDay(), calculatePaymentCodeEndDate(), entryDTO.getAmountToPay(), entryDTO.getAmountToPay());

        return getNonProcessedPaymentCodes();

    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
        final EntryDTO entryDTO = calculateEntries(new DateTime()).iterator().next();

        return Collections.singletonList(AccountingEventPaymentCode.create(PaymentCodeType.INSURANCE, new YearMonthDay(),
                calculatePaymentCodeEndDate(), this, entryDTO.getAmountToPay(), entryDTO.getAmountToPay(), getPerson()));
    }

    private YearMonthDay calculatePaymentCodeEndDate() {
        return calculateNextEndDate(new YearMonthDay());
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        final Set<EntryType> result = new HashSet<EntryType>();
        result.add(EntryType.INSURANCE_FEE);

        return result;
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode, Money amountToPay,
            SibsTransactionDetailDTO transactionDetail) {
        return internalProcess(responsibleUser,
                Collections.singletonList(new EntryDTO(EntryType.INSURANCE_FEE, this, amountToPay)), transactionDetail);
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

    public boolean hasInsuranceExemption() {
        return getInsuranceExemption() != null;
    }

    public Exemption getInsuranceExemption() {
        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption.isForInsurance()) {
                return exemption;
            }
        }

        return null;
    }

    @Override
    public boolean isInsuranceEvent() {
        return true;
    }
}
