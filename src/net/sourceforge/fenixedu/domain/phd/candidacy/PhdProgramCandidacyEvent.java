package net.sourceforge.fenixedu.domain.phd.candidacy;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramUnit;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PhdProgramCandidacyEvent extends PhdProgramCandidacyEvent_Base {

    protected PhdProgramCandidacyEvent() {
	super();
    }

    public PhdProgramCandidacyEvent(AdministrativeOffice administrativeOffice, Person person,
	    PhdProgramCandidacyProcess candidacyProcess) {
	this();
	init(administrativeOffice, person, candidacyProcess);
    }

    public PhdProgramCandidacyEvent(final Person person, final PhdProgramCandidacyProcess process) {
	this(AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE), person, process);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, PhdProgramCandidacyProcess candidacyProcess) {
	super.init(administrativeOffice, EventType.CANDIDACY_ENROLMENT, person);

	check(candidacyProcess, "error.phd.candidacy.PhdProgramCandidacyEvent.candidacyProcess.cannot.be.null");

	super.setCandidacyProcess(candidacyProcess);
    }

    @Override
    public void setCandidacyProcess(PhdProgramCandidacyProcess candidacyProcess) {
	throw new DomainException("error.phd.candidacy.PhdProgramCandidacyEvent.cannot.modify.candidacyProcess");
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" (").appendLabel(getPhdProgram().getPresentationName())
		.appendLabel(" - ").appendLabel(getExecutionYear().getYear()).appendLabel(")");

	return labelFormatter;

    }

    @Override
    public LabelFormatter getDescription() {
	return new LabelFormatter().appendLabel(" ").appendLabel(getPhdProgram().getPresentationName()).appendLabel(" - ")
		.appendLabel(getExecutionYear().getYear());
    }

    private ExecutionYear getExecutionYear() {
	return getCandidacyProcess().getIndividualProgramProcess().getExecutionYear();
    }

    @Override
    public PostingRule getPostingRule() {
	return getPhdProgram().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(), getWhenOccured());
    }

    private PhdProgram getPhdProgram() {
	return getCandidacyProcess().getIndividualProgramProcess().getPhdProgram();
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public Account getToAccount() {
	return getUnit().getAccountBy(AccountType.INTERNAL);
    }

    private PhdProgramUnit getUnit() {
	return getPhdProgram().getPhdProgramUnit();
    }

    @Override
    protected void disconnect() {
	removeCandidacyProcess();
	super.disconnect();
    }

}
