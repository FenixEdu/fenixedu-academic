package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

public class GratuityEvent extends GratuityEvent_Base {

    protected GratuityEvent() {
	super();
    }

    public GratuityEvent(AdministrativeOffice administrativeOffice, Person person,
	    Registration registration, ExecutionYear executionYear) {
	this();
	init(administrativeOffice, person, registration, executionYear);
    }

    protected void init(AdministrativeOffice administrativeOffice, Person person,
	    Registration registration, ExecutionYear executionYear) {
	super.init(administrativeOffice, EventType.GRATUITY, person);
	checkParameters(registration, executionYear);
	super.setRegistrationForGratuityEvent(registration);
	super.setExecutionYear(executionYear);
    }

    private void checkParameters(Registration registration, ExecutionYear executionYear) {
	if (registration == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.GratuityEvent.registration.cannot.be.null");
	}

	if (executionYear == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.GratuityEvent.executionYear.cannot.be.null");
	}

    }

    @Override
    public Account getToAccount() {
	return getUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    private Unit getUnit() {
	return getExecutionDegree().getDegreeCurricularPlan().getDegree().getUnit();
    }

    private ExecutionDegree getExecutionDegree() {
	return getRegistration().getStudentCandidacy().getExecutionDegree();
    }

    private Degree getDegree() {
	return getExecutionDegree().getDegree();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" (").appendLabel(
		getDegree().getDegreeType().name(), "enum").appendLabel(" - ").appendLabel(
		getDegree().getName()).appendLabel(" - ").appendLabel(getExecutionYear().getYear())
		.appendLabel(")");

	return labelFormatter;
    }

    @Override
    protected PostingRule getPostingRule(DateTime whenRegistered) {
	return getExecutionDegree().getDegreeCurricularPlan().getServiceAgreementTemplate()
		.findPostingRuleByEventTypeAndDate(getEventType(), whenRegistered);
    }

    @Override
    public void setRegistrationForGratuityEvent(Registration registration) {
	throw new DomainException(
		"error.accounting.events.gratuity.GratuityEvent.cannot.modify.registration");
    }

    public Registration getRegistration() {
	return getRegistrationForGratuityEvent();
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException(
		"error.accounting.events.gratuity.GratuityEvent.cannot.modify.executionYear");
    }

    public boolean isCompleteEnrolmentModel() {
	return getRegistration().isCompleteEnrolmentModel(getExecutionYear());
    }

    public boolean isCustomEnrolmentModel() {
	return getRegistration().isCustomEnrolmentModel(getExecutionYear());
    }

    public BigDecimal getTotalEctsCreditsForRegistration() {
	return getRegistration().getTotalEctsCredits(getExecutionYear());
    }

}
