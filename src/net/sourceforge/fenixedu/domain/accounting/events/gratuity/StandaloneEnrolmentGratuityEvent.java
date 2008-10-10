package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class StandaloneEnrolmentGratuityEvent extends StandaloneEnrolmentGratuityEvent_Base {

    protected StandaloneEnrolmentGratuityEvent() {
	super();
    }

    public StandaloneEnrolmentGratuityEvent(AdministrativeOffice administrativeOffice, Person person,
	    StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {

	this();

	init(administrativeOffice, EventType.STANDALONE_ENROLMENT_GRATUITY, person, studentCurricularPlan, executionYear);
    }

    @Override
    public Account getToAccount() {
	return getAdministrativeOffice().getUnit().getInternalAccount();
    }

    @Override
    public boolean isOpen() {
	if (isCancelled()) {
	    return false;
	}

	return calculateAmountToPay(new DateTime()).greaterThan(Money.ZERO);
    }

    @Override
    public boolean isClosed() {
	if (isCancelled()) {
	    return false;
	}

	return calculateAmountToPay(new DateTime()).lessOrEqualThan(Money.ZERO);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" - ").appendLabel(getExecutionYear().getYear());

	return labelFormatter;
    }

    @Override
    public boolean isDepositSupported() {
	return true;
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
	return Collections.singleton(EntryType.STANDALONE_ENROLMENT_GRATUITY_FEE);
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter result = new LabelFormatter();
	result.appendLabel(getEventType().getQualifiedName(), "enum").appendLabel(" - ")
		.appendLabel(getExecutionYear().getYear());

	return result;
    }
}
