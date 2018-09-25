package org.fenixedu.academic.domain.accounting.events;

import java.util.Collections;
import java.util.Map;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.events.administrativeOfficeFee.IAdministrativeOfficeFeeEvent;
import org.fenixedu.academic.domain.accounting.postingRules.AdministrativeOfficeFeePR;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class AdministrativeOfficeFeeEvent extends AdministrativeOfficeFeeEvent_Base implements IAdministrativeOfficeFeeEvent {

    static {
        getRelationPersonAccountingEvent().addListener(new RelationAdapter<Party, Event>() {
            @Override public void beforeAdd(Party party, Event event) {
                if (event instanceof AdministrativeOfficeFeeEvent && party != null && party instanceof Person) {
                    Person person = (Person) party;
                    final AdministrativeOfficeFeeEvent administrativeOfficeFeeEvent =
                            (AdministrativeOfficeFeeEvent) event;
                    if (person.hasAdministrativeOfficeFeeInsuranceEventFor(
                            administrativeOfficeFeeEvent.getExecutionYear())) {
                        throw new DomainException(
                                "error.org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent.event.is.already.defined.for.execution.year");

                    }
                }
            }
        });
    }
    
    protected AdministrativeOfficeFeeEvent() {
        super();
    }

    public AdministrativeOfficeFeeEvent(AdministrativeOffice administrativeOffice, Person person,
            ExecutionYear executionYear) {
        this();
        init(administrativeOffice, EventType.ADMINISTRATIVE_OFFICE_FEE, person, executionYear);
    }

    @Override protected Account getFromAccount() {
        return getPerson().getExternalAccount();
    }

    @Override public Account getToAccount() {
        return getAdministrativeOffice().getUnit().getInternalAccount();
    }

    @Override public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), Bundle.ENUMERATION).appendLabel(" - ")
                .appendLabel(getExecutionYear().getYear());

        return labelFormatter;
    }

    @Override
    public Map<LocalDate, Money> getDueDateAmountMap(PostingRule postingRule, DateTime when) {
        if (postingRule instanceof AdministrativeOfficeFeePR) {
            LocalDate key = ((AdministrativeOfficeFeePR) postingRule).getWhenToApplyFixedAmountPenalty().toLocalDate();
            Money fixedAmount = ((AdministrativeOfficeFeePR) postingRule).getFixedAmount();
            return Collections.singletonMap(key, fixedAmount);
        }
        return super.getDueDateAmountMap(postingRule, when);
    }

    @Override protected AdministrativeOfficeServiceAgreementTemplate getServiceAgreementTemplate() {
        return getAdministrativeOffice().getServiceAgreementTemplate();
    }

}
