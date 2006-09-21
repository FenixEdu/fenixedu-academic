package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

public class GratuityEvent extends GratuityEvent_Base {

    protected GratuityEvent() {
	super();
    }

    public GratuityEvent(AdministrativeOffice administrativeOffice, Person person,
	    Registration registration) {
	this();
	init(administrativeOffice, person, registration);
    }

    protected void init(AdministrativeOffice administrativeOffice, Person person,
	    Registration registration) {
	super.init(administrativeOffice, EventType.GRATUITY, person);
	checkParameters(registration);
	super.setRegistration(registration);
    }

    private void checkParameters(Registration registration) {
	if (registration == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.GratuityEvent.registration.cannot.be.null");
	}
    }

    @Override
    public Account getToAccount() {
	return getUnit().getAccountBy(AccountType.INTERNAL);
    }

    private Unit getUnit() {
	return getExecutionDegree().getDegreeCurricularPlan().getDegree().getUnit();
    }

    private ExecutionDegree getExecutionDegree() {
	return getRegistration().getStudentCandidacy().getExecutionDegree();
    }

    private Degree getDegree() {
	return getExecutionDegree().getDegree();
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

    @Override
    protected PostingRule getPostingRule(DateTime whenRegistered) {
	return getExecutionDegree().getDegreeCurricularPlan().getServiceAgreementTemplate()
		.findPostingRuleByEventTypeAndDate(getEventType(), whenRegistered);
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, List<EntryDTO> entryDTOs,
	    PaymentMode paymentMode, DateTime whenRegistered) {
	return getPostingRule(whenRegistered).process(responsibleUser, entryDTOs, paymentMode,
		whenRegistered, this, getPerson().getAccountBy(AccountType.EXTERNAL), getToAccount());
    }

}
