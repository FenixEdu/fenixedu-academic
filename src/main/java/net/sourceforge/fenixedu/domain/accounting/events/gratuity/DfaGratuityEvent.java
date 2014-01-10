package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class DfaGratuityEvent extends DfaGratuityEvent_Base {

    protected DfaGratuityEvent() {
        super();
    }

    public DfaGratuityEvent(AdministrativeOffice administrativeOffice, Person person,
            StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {

        this();

        checkRulesToCreate(studentCurricularPlan);
        init(administrativeOffice, person, studentCurricularPlan, executionYear);
    }

    private void checkRulesToCreate(StudentCurricularPlan studentCurricularPlan) {
        if (studentCurricularPlan.getDegreeType() != DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent.invalid.degreeType");
        }
    }

    @Override
    public boolean canApplyExemption(final GratuityExemptionJustificationType justificationType) {
        if (isCustomEnrolmentModel()) {
            return justificationType == GratuityExemptionJustificationType.OTHER_INSTITUTION
                    || justificationType == GratuityExemptionJustificationType.DIRECTIVE_COUNCIL_AUTHORIZATION;

        }

        return true;
    }

    @Override
    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
        final EntryDTO entryDTO = calculateEntries(new DateTime()).iterator().next();

        if (!getNonProcessedPaymentCodes().isEmpty()) {
            getNonProcessedPaymentCodes()
                    .iterator()
                    .next()
                    .update(new YearMonthDay(), calculatePaymentCodeEndDate(), entryDTO.getAmountToPay(),
                            entryDTO.getAmountToPay());
        }

        return getNonProcessedPaymentCodes();

    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
        final EntryDTO entryDTO = calculateEntries(new DateTime()).iterator().next();

        return Collections.singletonList(AccountingEventPaymentCode.create(PaymentCodeType.TOTAL_GRATUITY, new YearMonthDay(),
                calculatePaymentCodeEndDate(), this, entryDTO.getAmountToPay(), entryDTO.getAmountToPay(), getStudent()
                        .getPerson()));
    }

    private Student getStudent() {
        return getStudentCurricularPlan().getRegistration().getStudent();
    }

    private YearMonthDay calculatePaymentCodeEndDate() {
        return calculateNextEndDate(new YearMonthDay());
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode, Money amountToPay,
            SibsTransactionDetailDTO transactionDetail) {
        return internalProcess(responsibleUser,
                Collections.singletonList(new EntryDTO(EntryType.GRATUITY_FEE, this, amountToPay)), transactionDetail);
    }

    @Override
    public boolean isOtherPartiesPaymentsSupported() {
        return true;
    }

    static public Set<AccountingTransaction> readPaymentsFor(final YearMonthDay startDate, final YearMonthDay endDate) {
        return readPaymentsFor(DfaGratuityEvent.class, startDate, endDate);

    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        return Collections.singleton(EntryType.GRATUITY_FEE);
    }

    @Override
    public boolean isDfaGratuityEvent() {
        return true;
    }
}
