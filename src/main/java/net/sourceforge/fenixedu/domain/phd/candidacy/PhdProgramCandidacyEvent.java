package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.IndividualCandidacyPaymentCode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.YearMonthDay;

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
        this(process.getIndividualProgramProcess().getAdministrativeOffice(), person, process);

        if (process.isPublicCandidacy()) {
            attachAvailablePaymentCode();
        }
    }

    protected void attachAvailablePaymentCode() {
        YearMonthDay candidacyDate = getCandidacyProcess().getCandidacyDate().toDateMidnight().toYearMonthDay();
        IndividualCandidacyPaymentCode paymentCode =
                IndividualCandidacyPaymentCode.getAvailablePaymentCodeAndUse(PaymentCodeType.PHD_PROGRAM_CANDIDACY_PROCESS,
                        candidacyDate, this, getPerson());
        if (paymentCode == null) {
            throw new DomainException("error.IndividualCandidacyEvent.invalid.payment.code");
        }
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, PhdProgramCandidacyProcess candidacyProcess) {
        super.init(administrativeOffice, EventType.CANDIDACY_ENROLMENT, person);
        String[] args = {};

        if (candidacyProcess == null) {
            throw new DomainException("error.phd.candidacy.PhdProgramCandidacyEvent.candidacyProcess.cannot.be.null", args);
        }
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
        return new LabelFormatter().appendLabel(AlertService.getMessageFromResource("label.phd.candidacy")).appendLabel(": ")
                .appendLabel(getPhdProgram().getPresentationName()).appendLabel(" - ").appendLabel(getExecutionYear().getYear());
    }

    private ExecutionYear getExecutionYear() {
        return getCandidacyProcess().getIndividualProgramProcess().getExecutionYear();
    }

    @Override
    protected PhdProgram getPhdProgram() {
        return getCandidacyProcess().getIndividualProgramProcess().getPhdProgram();
    }

    @Override
    protected void disconnect() {
        removeCandidacyProcess();
        super.disconnect();
    }

    public IndividualCandidacyPaymentCode getAssociatedPaymentCode() {
        if (super.getAllPaymentCodes().isEmpty()) {
            return null;
        }

        return (IndividualCandidacyPaymentCode) super.getAllPaymentCodes().get(0);
    }

    @Override
    public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
        return getCandidacyProcess().getIndividualProgramProcess();
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode, Money amountToPay,
            SibsTransactionDetailDTO transactionDetail) {
        return internalProcess(responsibleUser, Collections.singletonList(new EntryDTO(getEntryType(), this, amountToPay)),
                transactionDetail);
    }

    protected EntryType getEntryType() {
        return EntryType.CANDIDACY_ENROLMENT_FEE;
    }
}
