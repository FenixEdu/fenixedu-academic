package net.sourceforge.fenixedu.domain.accounting.events.phd;

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
import net.sourceforge.fenixedu.domain.candidacy.PHDProgramCandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.workflow.StateMachine;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

import org.joda.time.DateTime;

public class PhDCandidacyEvent extends PhDCandidacyEvent_Base {
    
    private  PhDCandidacyEvent() {
        super();
    }

    public PhDCandidacyEvent(AdministrativeOffice administrativeOffice, Person person,
	    PHDProgramCandidacy candidacy) {
	this();
	init(administrativeOffice, person, candidacy);
    }
    
    private void init(AdministrativeOffice administrativeOffice, Person person, PHDProgramCandidacy candidacy) {
	init(administrativeOffice, EventType.CANDIDACY_ENROLMENT, person);
	super.setPhdCandidacy(candidacy);
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
	return getPhdCandidacy().getExecutionDegree().getDegreeCurricularPlan().getDegree().getUnit();
    }

    @Override
    public void setPhdCandidacy(PHDProgramCandidacy candidacy) {
	throw new DomainException("error.PHDProgramCandidacy.cannot.modify.candidacy");
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" (").appendLabel(
		getDegree().getDegreeType().name(), "enum").appendLabel(" - ").appendLabel(
			getDegree().getNameFor(getExecutionYear()).getContent()).appendLabel(" - ").appendLabel(getExecutionYear().getYear())
		.appendLabel(")");

	return labelFormatter;

    }

    private ExecutionDegree getExecutionDegree() {
	return getPhdCandidacy().getExecutionDegree();

    }

    private Degree getDegree() {
	return getExecutionDegree().getDegreeCurricularPlan().getDegree();

    }

    @Override
    public void closeEvent() {
	StateMachine.execute(getPhdCandidacy().getActiveCandidacySituation());

	super.closeEvent();
    }

    @Override
    public PostingRule getPostingRule() {
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
	return getPhdCandidacy().getCandidacyDate();
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = super.getDescription();
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel(getDegree().getDegreeType().name(), "enum").appendLabel(" - ");
	labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent()).appendLabel(" - ");
	labelFormatter.appendLabel(getExecutionYear().getYear());
	return labelFormatter;
    }

}
