/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
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

import org.fenixedu.bennu.core.domain.User;
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
        setCandidacyProcess(null);
        super.disconnect();
    }

    public IndividualCandidacyPaymentCode getAssociatedPaymentCode() {
        if (super.getAllPaymentCodes().isEmpty()) {
            return null;
        }

        return (IndividualCandidacyPaymentCode) super.getAllPaymentCodes().iterator().next();
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

    @Deprecated
    public boolean hasCandidacyProcess() {
        return getCandidacyProcess() != null;
    }

}
