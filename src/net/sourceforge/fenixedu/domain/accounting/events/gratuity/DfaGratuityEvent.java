package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;

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
    public boolean canApplyExemption() {
	return !isCustomEnrolmentModel();
    }

    @Override
    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
	final EntryDTO entryDTO = calculateEntries(new DateTime()).get(0);
	getNonProcessedPaymentCodes().get(0).update(new YearMonthDay(), calculatePaymentCodeEndDate(),
		entryDTO.getAmountToPay(), entryDTO.getAmountToPay());

	return getNonProcessedPaymentCodes();

    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
	final EntryDTO entryDTO = calculateEntries(new DateTime()).get(0);

	return Collections.singletonList(AccountingEventPaymentCode.create(
		PaymentCodeType.TOTAL_GRATUITY, new YearMonthDay(), calculatePaymentCodeEndDate(), this,
		entryDTO.getAmountToPay(), entryDTO.getAmountToPay(), getStudent()));
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
    
}
