package net.sourceforge.fenixedu.domain.accounting.events.dfa;

import net.sourceforge.fenixedu.domain.CandidacyPeriodInDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RegistrationPeriodInDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

public class DfaRegistrationEvent extends DfaRegistrationEvent_Base {

    private DfaRegistrationEvent() {
	super();
    }

    public DfaRegistrationEvent(AdministrativeOffice administrativeOffice, Person person,
	    Registration registration) {
	this();
	init(administrativeOffice, person, registration);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, Registration registration) {
	super.init(administrativeOffice, EventType.DFA_REGISTRATION, person);
	checkParameters(registration);
	super.setRegistration(registration);

    }

    private void checkParameters(Registration registration) {
	if (registration == null) {
	    throw new DomainException(
		    "error.accounting.events.dfa.DfaRegistrationEvent.registration.cannot.be.null");
	}
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" (").appendLabel(
		getDegree().getDegreeType().name(), "enum").appendLabel(" - ").appendLabel(
		getDegree().getName()).appendLabel(" - ").appendLabel(
		getExecutionDegree().getExecutionYear().getYear()).appendLabel(")");

	return labelFormatter;
    }

    private ExecutionDegree getExecutionDegree() {
	return getRegistration().getStudentCandidacy().getExecutionDegree();
    }

    private Degree getDegree() {
	return getExecutionDegree().getDegree();
    }

    @Override
    protected PostingRule getPostingRule(DateTime whenRegistered) {
	return getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(),
		whenRegistered);
    }

    private AdministrativeOfficeServiceAgreementTemplate getServiceAgreementTemplate() {
	return getAdministrativeOffice().getServiceAgreementTemplate();
    }

    @Override
    public Account getToAccount() {
	return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public void setRegistration(Registration registration) {
	throw new DomainException(
		"error.accounting.events.dfa.DfaRegistrationEvent.cannot.modify.registration");
    }

    public DateTime getRegistrationDate() {
	return getRegistration().getStartDate().toDateTimeAtMidnight();
    }

    public RegistrationPeriodInDegreeCurricularPlan getRegistrationPeriodInDegreeCurricularPlan() {
	return getExecutionDegree().getDegreeCurricularPlan().getRegistrationPeriod(getExecutionYear());
    }

    public boolean hasRegistrationPeriodInDegreeCurricularPlan() {
	return getExecutionDegree().getDegreeCurricularPlan().hasRegistrationPeriodFor(
		getExecutionYear());
    }

    private ExecutionYear getExecutionYear() {
	return getExecutionDegree().getExecutionYear();
    }

    public CandidacyPeriodInDegreeCurricularPlan getCandidacyPeriodInDegreeCurricularPlan() {
	return getExecutionDegree().getDegreeCurricularPlan().getCandidacyPeriod(getExecutionYear());
    }

    public boolean hasCandidacyPeriodInDegreeCurricularPlan() {
	return getExecutionDegree().getDegreeCurricularPlan().hasCandidacyPeriodFor(getExecutionYear());
    }

    private StudentCandidacy getCandidacy() {
	return getRegistration().getStudentCandidacy();
    }

    public DateTime getCandidacyDate() {
	return getCandidacy().getCandidacyDate();
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = super.getDescription();
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel(getDegree().getName()).appendLabel(" - ");
	labelFormatter.appendLabel(getExecutionYear().getYear());
	return labelFormatter;
    }

}
