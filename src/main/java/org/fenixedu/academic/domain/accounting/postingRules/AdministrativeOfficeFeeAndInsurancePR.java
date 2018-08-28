/**
 * Copyright © 2002 Instituto Superior Técnico
 * <p>
 * This file is part of FenixEdu Academic.
 * <p>
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accounting.postingRules;

import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.domain.accounting.events.AnnualEvent;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.UnitServiceAgreementTemplate;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AdministrativeOfficeFeeAndInsurancePR extends AdministrativeOfficeFeeAndInsurancePR_Base
        implements IAdministrativeOfficeFeeAndInsurancePR {

    protected AdministrativeOfficeFeeAndInsurancePR() {
        super();
    }

    public AdministrativeOfficeFeeAndInsurancePR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate) {
        this();
        super.init(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, startDate, endDate, serviceAgreementTemplate);
    }

    @Override protected Money doCalculationForAmountToPay(Event event, DateTime when) {
        throw new UnsupportedOperationException("use getAdministrativeOfficeFeeAmount and getInsuranceAmount");
    }

    @Override protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

        final Set<AccountingTransaction> createdAccountingTransactionSet = entryDTOs.stream()
                .map(entryDTO -> makeAccountingTransaction(user, event, fromAccount, toAccount, entryDTO.getEntryType(), entryDTO.getAmountToPay(), transactionDetail))
                .collect(Collectors.toSet());

        ((AdministrativeOfficeFeeAndInsuranceEvent) event)
                .changePaymentCodeState(transactionDetail.getWhenRegistered(), transactionDetail.getPaymentMode());

        return createdAccountingTransactionSet;
    }

    private FixedAmountPR getPostingRuleForInsurance(DateTime startDate, DateTime endDate) {
        return (FixedAmountPR) getServiceAgreementTemplateForInsurance()
                .findPostingRuleBy(EventType.INSURANCE, startDate, endDate);
    }

    private AdministrativeOfficeFeePR getPostingRuleForAdministrativeOfficeFee(DateTime startDate, DateTime endDate) {
        return (AdministrativeOfficeFeePR) getServiceAgreementTemplate()
                .findPostingRuleBy(EventType.ADMINISTRATIVE_OFFICE_FEE, startDate, endDate);
    }

    public UnitServiceAgreementTemplate getServiceAgreementTemplateForInsurance() {
        return Bennu.getInstance().getInstitutionUnit().getUnitServiceAgreementTemplate();
    }

    @Override public boolean isVisible() {
        return false;
    }

    public YearMonthDay getAdministrativeOfficeFeePaymentLimitDate(DateTime startDate, DateTime endDate) {
        return getPostingRuleForAdministrativeOfficeFee(startDate, endDate).getWhenToApplyFixedAmountPenalty();
    }

    public Money getInsuranceAmount(DateTime startDate, DateTime endDate) {
        return getPostingRuleForInsurance(startDate, endDate).getFixedAmount();
    }

    @Override public Money getAdministrativeOfficeFeeAmount(Event event, DateTime startDate, DateTime endDate) {
        return getPostingRuleForAdministrativeOfficeFee(startDate, endDate).getFixedAmount();
    }

    @Override public Money getAdministrativeOfficeFeePenaltyAmount(Event event, DateTime startDate, DateTime endDate) {
        return getPostingRuleForAdministrativeOfficeFee(startDate, endDate).getFixedAmountPenalty();
    }

    @Override public AccountingTransaction depositAmount(User responsibleUser, Event event, Account fromAccount, Account toAccount,
            Money amount, EntryType entryType, AccountingTransactionDetailDTO transactionDetailDTO) {
        return makeAccountingTransaction(responsibleUser, event, fromAccount, toAccount, entryType,  amount, transactionDetailDTO);
    }

    @Override protected EntryType getEntryType() {
        return EntryType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE;
    }

    @Override public Map<LocalDate, Money> getDueDatePenaltyAmountMap(Event event, DateTime when) {
        final AnnualEvent annualEvent = (AnnualEvent) event;
        return getPostingRuleForAdministrativeOfficeFee(annualEvent.getStartDate(), annualEvent.getEndDate())
                .getDueDatePenaltyAmountMap(annualEvent, when);
    }
}
