package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.jfree.data.time.Month;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class PhDGratuityEvent extends PhDGratuityEvent_Base {

    public PhDGratuityEvent() {
	super();
    }

    public PhDGratuityEvent(AdministrativeOffice administrativeOffice, Person person,
	    StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {

	this();
	init(administrativeOffice, person, studentCurricularPlan, executionYear);
	setCreationDate(new YearMonthDay());
    }

    public boolean isFirstYearOfPhD(DateTime when) {
	return getRegistration().getRegistrationYear().equals(ExecutionYear.readByDateTime(when));
    }

    @Override
    public boolean isExemptionAppliable() {
	return true;
    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
	final List<AccountingEventPaymentCode> result = new ArrayList<AccountingEventPaymentCode>();
	for (final EntryDTO entryDTO : calculateEntries()) {
	    result.add(createAccountingEventPaymentCode(entryDTO, getPerson().getStudent()));
	}
	return result;
    }

    private AccountingEventPaymentCode createAccountingEventPaymentCode(final EntryDTO entryDTO,
	    final Student student) {
	return AccountingEventPaymentCode.create(PaymentCodeType.TOTAL_GRATUITY, new YearMonthDay(),
		calculateFullPaymentCodeEndDate(), this, entryDTO.getAmountToPay(), entryDTO
			.getAmountToPay(), student);
    }

    private YearMonthDay calculateFullPaymentCodeEndDate() {
	YearMonthDay today = new YearMonthDay();
	if (today.isAfter(new YearMonthDay(YearMonthDay.YEAR, 6, 30))) {
	    return new YearMonthDay(today.getYear() + 1, 2, 28);
	} else {
	    return new YearMonthDay(today.getYear(), 7, 31);
	}
    }

    @Override
    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
	final List<EntryDTO> entryDTOs = calculateEntries();
	final List<AccountingEventPaymentCode> result = new ArrayList<AccountingEventPaymentCode>();

	for (final AccountingEventPaymentCode paymentCode : getNonProcessedPaymentCodes()) {
	    final EntryDTO entryDTO = findEntryDTOForPaymentCode(entryDTOs, paymentCode);
	    if (entryDTO == null) {
		paymentCode.cancel();
		continue;
	    }

	    paymentCode.update(new YearMonthDay(), calculateFullPaymentCodeEndDate(), entryDTO
		    .getAmountToPay(), entryDTO.getAmountToPay());
	    result.add(paymentCode);
	}

	return result;
    }

    private EntryDTO findEntryDTOForPaymentCode(List<EntryDTO> entryDTOs,
	    AccountingEventPaymentCode paymentCode) {
	for (EntryDTO entryDto : entryDTOs) {
	    if (paymentCode.getAccountingEvent().equals(entryDto.getEvent())) {
		return entryDto;
	    }
	}
	return null;

    }

    public YearMonthDay hasToBePayedUntil() {

	YearMonthDay actualDate = getPaymentDeadLine(getCreationDate());

	while (true) {
	    List<Interval> interruptions = getInterruptions(actualDate.toDateTimeAtMidnight());
	    if (!interruptions.isEmpty()) {
		YearMonthDay lastEnd = interruptions.get(interruptions.size() - 1).getEnd()
			.toYearMonthDay();
		YearMonthDay extendedDate = getPaymentDeadLine(lastEnd);
		if (!extendedDate.equals(actualDate)) {
		    actualDate = extendedDate;
		} else {
		    return actualDate;
		}
	    } else {
		return actualDate;
	    }

	}
    }

    private YearMonthDay getPaymentDeadLine(YearMonthDay date) {
	YearMonthDay transition = new YearMonthDay(date.getYear(), Month.JUNE, 30);
	if (date.isBefore(transition)) {
	    return new YearMonthDay(date.getYear(), Month.AUGUST, 31);
	} else {
	    return new YearMonthDay(date.getYear() + 1, Month.FEBRUARY, 28);
	}
    }

    public List<Interval> getInterruptions(DateTime until) {
	List<Interval> interruptions = new ArrayList<Interval>();

	DateTime creationDate = getCreationDate().toDateTimeAtMidnight();
	for (RegistrationState registrationState : getRegistration().getRegistrationStates(
		RegistrationStateType.INTERRUPTED)) {
	    DateTime start = registrationState.getStateDate();
	    DateTime end = registrationState.getEndDate();
	    if (start.isAfter(creationDate) && (until == null || start.isBefore(until))) {
		interruptions.add(new Interval(start, (end == null ? new DateTime() : end)));
	    }
	}
	return interruptions;

    }

    public List<Interval> getInterruptions() {
	return getInterruptions(null);
    }

    public int getNumberOfInterruptedDaysInEvent(DateTime until) {

	int numberOfDays = 0;
	for (Interval interruption : getInterruptions(until)) {
	    numberOfDays += Days.daysIn(interruption).getDays();
	}

	return numberOfDays;
    }

    public static void generatePhDGratuities() {

	for (DegreeCurricularPlan degreeCurricularPlan : ExecutionYear.readCurrentExecutionYear()
		.getDegreeCurricularPlans()) {
	    if (degreeCurricularPlan.getDegreeType().equals(DegreeType.BOLONHA_PHD_PROGRAM)) {
		for (StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan
			.getStudentCurricularPlans()) {
		    if (studentCurricularPlan.isActive()) {
			process(studentCurricularPlan);
		    }
		}
	    }
	}
    }

    private static void process(StudentCurricularPlan studentCurricularPlan) {
	
	Registration registration = studentCurricularPlan.getRegistration();
	if(registration == null) {
	    return;
	}
	
	Person person = registration.getPerson();
	System.out.println("Processing " + person.getName());
	
	YearMonthDay start = registration.getStartDate();
	YearMonthDay today = new YearMonthDay();

	int totalDays = Days.daysBetween(start, today).getDays();

	Set<PhDGratuityEvent> gratuities = (Set<PhDGratuityEvent>) person.getEventsByEventTypeAndClass(
		EventType.GRATUITY, PhDGratuityEvent.class);

	for (PhDGratuityEvent event : gratuities) {
	    totalDays -= event.getNumberOfInterruptedDaysInEvent(today.toDateTimeAtMidnight());
	}

	int roundYears = totalDays / 365;
	if (roundYears >= gratuities.size()) {
	    System.out.println("Added new PhDGratuityEvent");
	    new PhDGratuityEvent(studentCurricularPlan.getAdministrativeOffice(), person, studentCurricularPlan,
		    ExecutionYear.readCurrentExecutionYear());
	}
    }
}
