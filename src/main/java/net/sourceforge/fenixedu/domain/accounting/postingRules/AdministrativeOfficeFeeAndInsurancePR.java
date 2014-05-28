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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.UnitServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class AdministrativeOfficeFeeAndInsurancePR extends AdministrativeOfficeFeeAndInsurancePR_Base {

    protected AdministrativeOfficeFeeAndInsurancePR() {
        super();
    }

    public AdministrativeOfficeFeeAndInsurancePR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate) {
        this();
        super.init(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, startDate, endDate, serviceAgreementTemplate);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        final AnnualEvent annualEvent = (AnnualEvent) event;
        return getPostingRuleForAdministrativeOfficeFee(annualEvent.getStartDate(), annualEvent.getEndDate())
                .calculateTotalAmountToPay(event, when, applyDiscount).add(
                        getPostingRuleForInsurance(annualEvent.getStartDate(), annualEvent.getEndDate())
                                .calculateTotalAmountToPay(event, when, applyDiscount));
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        return amountToPay;
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {

        final List<EntryDTO> result = new ArrayList<EntryDTO>();
        final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent =
                (AdministrativeOfficeFeeAndInsuranceEvent) event;
        final AnnualEvent annualEvent = (AnnualEvent) event;
        if (administrativeOfficeFeeAndInsuranceEvent.hasToPayAdministrativeOfficeFee()) {
            result.addAll(getPostingRuleForAdministrativeOfficeFee(annualEvent.getStartDate(), annualEvent.getEndDate())
                    .calculateEntries(event, when));
        }
        if (administrativeOfficeFeeAndInsuranceEvent.hasToPayInsurance()) {
            result.addAll(getPostingRuleForInsurance(annualEvent.getStartDate(), annualEvent.getEndDate()).calculateEntries(
                    event, when));
        }

        return result;
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

        final Set<AccountingTransaction> result = new HashSet<AccountingTransaction>();
        final Set<Entry> createdEntries = new HashSet<Entry>();
        final AnnualEvent annualEvent = (AnnualEvent) event;
        for (final EntryDTO entryDTO : entryDTOs) {

            if (entryDTO.getEntryType() == EntryType.INSURANCE_FEE) {

                createdEntries.addAll(getPostingRuleForInsurance(annualEvent.getStartDate(), annualEvent.getEndDate()).process(
                        user, Collections.singletonList(entryDTO), event, fromAccount, toAccount, transactionDetail));

            } else if (entryDTO.getEntryType() == EntryType.ADMINISTRATIVE_OFFICE_FEE) {
                createdEntries.addAll(getPostingRuleForAdministrativeOfficeFee(annualEvent.getStartDate(),
                        annualEvent.getEndDate()).process(user, Collections.singletonList(entryDTO), event, fromAccount,
                        toAccount, transactionDetail));
            } else {
                throw new DomainException(
                        "error.accounting.postingRules.AdministrativeOfficeFeeAndInsurancePR.invalid.entry.type");
            }
        }

        ((AdministrativeOfficeFeeAndInsuranceEvent) event).changePaymentCodeState(transactionDetail.getWhenRegistered(),
                transactionDetail.getPaymentMode());

        for (final Entry entry : createdEntries) {
            result.add(entry.getAccountingTransaction());
        }

        return result;
    }

    private FixedAmountPR getPostingRuleForInsurance(DateTime startDate, DateTime endDate) {
        return (FixedAmountPR) getServiceAgreementTemplateForInsurance().findPostingRuleBy(EventType.INSURANCE, startDate,
                endDate);
    }

    private AdministrativeOfficeFeePR getPostingRuleForAdministrativeOfficeFee(DateTime startDate, DateTime endDate) {
        return (AdministrativeOfficeFeePR) getServiceAgreementTemplate().findPostingRuleBy(EventType.ADMINISTRATIVE_OFFICE_FEE,
                startDate, endDate);
    }

    public UnitServiceAgreementTemplate getServiceAgreementTemplateForInsurance() {
        return Bennu.getInstance().getInstitutionUnit().getUnitServiceAgreementTemplate();
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    public Money getAdministrativeOfficeFeeAmount(DateTime startDate, DateTime endDate) {
        return getPostingRuleForAdministrativeOfficeFee(startDate, endDate).getFixedAmount();
    }

    public YearMonthDay getAdministrativeOfficeFeePaymentLimitDate(DateTime startDate, DateTime endDate) {
        return getPostingRuleForAdministrativeOfficeFee(startDate, endDate).getWhenToApplyFixedAmountPenalty();
    }

    public Money getAdministrativeOfficeFeePenaltyAmount(DateTime startDate, DateTime endDate) {
        return getPostingRuleForAdministrativeOfficeFee(startDate, endDate).getFixedAmountPenalty();
    }

    public Money getInsuranceAmount(DateTime startDate, DateTime endDate) {
        return getPostingRuleForInsurance(startDate, endDate).getFixedAmount();
    }

    @Override
    public AccountingTransaction depositAmount(User responsibleUser, Event event, Account fromAcount, Account toAccount,
            Money amount, EntryType entryType, AccountingTransactionDetailDTO transactionDetailDTO) {

        final AnnualEvent annualEvent = (AnnualEvent) event;

        if (entryType == EntryType.INSURANCE_FEE) {
            return getPostingRuleForInsurance(annualEvent.getStartDate(), annualEvent.getEndDate()).depositAmount(
                    responsibleUser, event, fromAcount, toAccount, amount, transactionDetailDTO);

        } else if (entryType == EntryType.ADMINISTRATIVE_OFFICE_FEE) {
            return getPostingRuleForAdministrativeOfficeFee(annualEvent.getStartDate(), annualEvent.getEndDate()).depositAmount(
                    responsibleUser, event, fromAcount, toAccount, amount, transactionDetailDTO);

        } else {
            throw new DomainException("error.AdministrativeOfficeFeeAndInsurancePR.unsupported.entry.type");
        }
    }
}
