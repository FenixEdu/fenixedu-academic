package net.sourceforge.fenixedu.domain.accounting.events.dfa;

import net.sourceforge.fenixedu.domain.CandidacyPeriodInDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.workflow.StateMachine;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

public class DFACandidacyEvent extends DFACandidacyEvent_Base {

    private DFACandidacyEvent() {
	super();
    }

    public DFACandidacyEvent(AdministrativeOffice administrativeOffice, Person person,
	    DFACandidacy candidacy) {
	this();
	init(administrativeOffice, person, candidacy);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, DFACandidacy candidacy) {
	init(administrativeOffice, EventType.CANDIDACY_ENROLMENT, person);
	checkParameters(candidacy);
	super.setCandidacy(candidacy);
    }

    private void checkParameters(Candidacy candidacy) {
	if (candidacy == null) {
	    throw new DomainException("error.candidacy.dfaCandidacyEvent.invalid.candidacy");
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
	return getCandidacy().getExecutionDegree().getDegreeCurricularPlan().getDegree().getUnit();
    }

    @Override
    public void setCandidacy(DFACandidacy candidacy) {
	throw new DomainException("error.candidacy.dfaCandidacyEvent.cannot.modify.candidacy");
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

    private ExecutionDegree getExecutionDegree() {
	return getCandidacy().getExecutionDegree();

    }

    private Degree getDegree() {
	return getExecutionDegree().getDegreeCurricularPlan().getDegree();

    }

    @Override
    public void closeEvent() {
	StateMachine.execute(getCandidacy().getActiveCandidacySituation());

	super.closeEvent();
    }

    @Override
    protected PostingRule getPostingRule() {
	return getExecutionDegree().getDegreeCurricularPlan().getServiceAgreementTemplate()
		.findPostingRuleByEventTypeAndDate(getEventType(), getWhenOccured());
    }

    public CandidacyPeriodInDegreeCurricularPlan getCandidacyPeriodInDegreeCurricularPlan() {
	return getExecutionDegree().getDegreeCurricularPlan().getCandidacyPeriod(getExecutionYear());
    }

    public boolean hasCandidacyPeriodInDegreeCurricularPlan() {
	return getExecutionDegree().getDegreeCurricularPlan().hasCandidacyPeriodFor(getExecutionYear());
    }

    private ExecutionYear getExecutionYear() {
	return getExecutionDegree().getExecutionYear();
    }

    public DateTime getCandidacyDate() {
	return getCandidacy().getCandidacyDate();
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = super.getDescription();
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel(getDegree().getDegreeType().name(), "enum").appendLabel(" - ");
	labelFormatter.appendLabel(getDegree().getName()).appendLabel(" - ");
	labelFormatter.appendLabel(getExecutionYear().getYear());
	return labelFormatter;
    }

}
