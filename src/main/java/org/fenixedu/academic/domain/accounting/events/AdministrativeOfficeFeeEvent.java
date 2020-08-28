package org.fenixedu.academic.domain.accounting.events;

import java.util.Collections;
import java.util.Map;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.events.administrativeOfficeFee.IAdministrativeOfficeFeeEvent;
import org.fenixedu.academic.domain.accounting.postingRules.AdministrativeOfficeFeePR;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.joda.time.LocalDate;

public class AdministrativeOfficeFeeEvent extends AdministrativeOfficeFeeEvent_Base implements IAdministrativeOfficeFeeEvent {

    protected AdministrativeOfficeFeeEvent() {
        super();
    }

    public AdministrativeOfficeFeeEvent(AdministrativeOffice administrativeOffice, Person person,
            ExecutionYear executionYear) {
        this();
        init(administrativeOffice, EventType.ADMINISTRATIVE_OFFICE_FEE, person, executionYear);
        persistDueDateAmountMap();
    }                                                        

    @Override protected Account getFromAccount() {
        return getPerson().getExternalAccount();
    }

    @Override public Account getToAccount() {
        return getAdministrativeOffice().getUnit().getInternalAccount();
    }

    @Override protected LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();

        labelFormatter.appendLabel(entryType.name(), Bundle.ENUMERATION);

        if (getAdministrativeOffice() != null) {
            labelFormatter.appendLabel(" (");
            labelFormatter.appendLabel(getAdministrativeOffice().getName().getContent());
            labelFormatter.appendLabel(")");
        }
        
        labelFormatter.appendLabel(" - ").appendLabel(getExecutionYear().getYear());

        return labelFormatter;
    }

    @Override
    public Map<LocalDate, Money> calculateDueDateAmountMap() {
        final PostingRule postingRule = getPostingRule();
        if (postingRule instanceof AdministrativeOfficeFeePR) {
            LocalDate key = ((AdministrativeOfficeFeePR) postingRule).getWhenToApplyFixedAmountPenalty().toLocalDate();
            Money fixedAmount = ((AdministrativeOfficeFeePR) postingRule).getFixedAmount();
            return Collections.singletonMap(key, fixedAmount);
        }
        return super.calculateDueDateAmountMap();
    }

    @Override protected AdministrativeOfficeServiceAgreementTemplate getServiceAgreementTemplate() {
        return getAdministrativeOffice().getServiceAgreementTemplate();
    }

}
