package net.sourceforge.fenixedu.presentationTier.docs;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class IRSCustomDeclaration extends FenixReport {

    private static final int LINE_LENGTH = 64;

    private Person person;

    private List<Event> eventsForPerson;

    private int civilYear;

    private Employee employee;

    public IRSCustomDeclaration(final Person person, final List<Event> eventsForPerson,
	    final int civilYear, final Employee employee) {
	this.person = person;
	this.eventsForPerson = eventsForPerson;
	this.civilYear = civilYear;
	this.employee = employee;

	fillReport();
    }

    @Override
    protected void fillReport() {
	fillStudentParameters();
	fillCivilYearParameter();
	fillEntriesAndTotalParameters();
	fillLocationParameter();
	fillDateParameter();
    }

    private void fillDateParameter() {
	parameters.put("date", new YearMonthDay().toString("yyyy/MM/dd", LanguageUtils
		.getLocale()));
    }

    private void fillLocationParameter() {
	parameters.put("employeeLocation", getEmployee().getCurrentCampus().getLocation());
    }

    private Employee getEmployee() {
	return this.employee;
    }

    private void fillCivilYearParameter() {
	parameters.put("civilYear", String.valueOf(getCivilYear()));
    }

    private void fillStudentParameters() {
	parameters.put("person", getPerson());
	parameters.put("name", StringUtils.multipleLineRightPad(person.getName().toUpperCase(),
		LINE_LENGTH, '-'));
	parameters.put("studentNumber", StringUtils.multipleLineRightPad(person.getStudent().getNumber()
		.toString(), LINE_LENGTH - "aluno deste Instituto com o Número ".length(), '-'));
	parameters.put("documentIdNumber", StringUtils.multipleLineRightPad(person.getDocumentIdNumber()
		.toString(), LINE_LENGTH - "portador do Bilhete de Identidade ".length(), '-'));
    }

    private void fillEntriesAndTotalParameters() {
	final Map<EventType, Money> totalByEvent = calculateTotalsByEventType();
	final StringBuilder eventTypes = new StringBuilder();
	final StringBuilder payedAmounts = new StringBuilder();
	for (final Map.Entry<EventType, Money> entry : totalByEvent.entrySet()) {
	    eventTypes.append("- ").append(
		    enumerationBundle.getString(entry.getKey().getQualifiedName())).append("\n");
	    payedAmounts.append("*").append(entry.getValue().toPlainString()).append("Eur").append("\n");
	}

	parameters.put("eventTypes", eventTypes.toString());
	parameters.put("payedAmounts", payedAmounts.toString());
	parameters.put("totalPayedAmount", "*" + calculateTotal(totalByEvent).toPlainString() + "Eur");
    }

    private Money calculateTotal(final Map<EventType, Money> totalByEventType) {
	Money result = Money.ZERO;

	for (final Money money : totalByEventType.values()) {
	    result = result.add(money);
	}

	return result;

    }

    private Map<EventType, Money> calculateTotalsByEventType() {
	final Map<EventType, Money> result = new HashMap<EventType, Money>();
	for (final Event event : getEvents()) {
	    final Money totalForEventType = getTotalForEventType(result, event.getEventType());
	    result.put(event.getEventType(), totalForEventType.add(event.getPayedAmountFor(civilYear)));

	}

	return result;
    }

    private Money getTotalForEventType(final Map<EventType, Money> totalByEventType,
	    final EventType eventType) {
	final Money result = totalByEventType.get(eventType);

	return result != null ? result : Money.ZERO;

    }

    @Override
    public String getReportTemplateKey() {
	return getClass().getName();
    }

    @Override
    public String getReportFileName() {
	return MessageFormat.format("DECLARATION-{0}-{1}", getPerson().getIstUsername(), new DateTime()
		.toString("yyyyMMdd"));

    }

    private Person getPerson() {
	return this.person;
    }

    private List<Event> getEvents() {
	return this.eventsForPerson;
    }

    private int getCivilYear() {
	return this.civilYear;
    }

}
