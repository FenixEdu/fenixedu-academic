package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.IndividualCandidacyPaymentCode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public abstract class IndividualCandidacyEvent extends IndividualCandidacyEvent_Base {

    protected IndividualCandidacyEvent() {
	super();
    }

    protected void init(final IndividualCandidacy candidacy, final EventType eventType, final Person person) {
	final AdministrativeOffice administrativeOffice = readAdministrativeOffice();
	checkParameters(candidacy, administrativeOffice);
	super.init(administrativeOffice, eventType, person);
	setIndividualCandidacy(candidacy);
    }

    protected void attachAvailablePaymentCode(final Person person) {
	YearMonthDay candidacyDate = getIndividualCandidacy().getCandidacyDate().toDateTimeAtStartOfDay().toYearMonthDay();
	IndividualCandidacyPaymentCode paymentCode = IndividualCandidacyPaymentCode.getAvailablePaymentCodeAndUse(
		getPaymentCodeType(), candidacyDate, this, person);
	if (paymentCode == null) {
	    throw new DomainException("error.IndividualCandidacyEvent.invalid.payment.code");
	}
    }

    protected void checkParameters(final IndividualCandidacy candidacy, final AdministrativeOffice administrativeOffice) {
	if (candidacy == null) {
	    throw new DomainException("error.IndividualCandidacyEvent.invalid.candidacy");
	}
	if (administrativeOffice == null) {
	    throw new DomainException("error.IndividualCandidacyEvent.invalid.administrativeOffice");
	}
    }

    abstract protected AdministrativeOffice readAdministrativeOffice();

    public PaymentCodeType getPaymentCodeType() {
	PostingRule postingRule = getAdministrativeOffice().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(
		getEventType(), getWhenOccured());

	return postingRule.calculatePaymentCodeTypeFromEvent(this, getWhenOccured(), false);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	return new LabelFormatter().appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getExternalAccount();
    }

    @Override
    public PostingRule getPostingRule() {
	return getAdministrativeOffice().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(),
		getWhenOccured());
    }

    @Override
    public Account getToAccount() {
	return getAdministrativeOffice().getUnit().getInternalAccount();
    }

    public Student getCandidacyStudent() {
	return getIndividualCandidacy().getStudent();
    }

    public boolean hasCandidacyStudent() {
	return getIndividualCandidacy().hasStudent();
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode, Money amountToPay,
	    SibsTransactionDetailDTO transactionDetail) {
	return internalProcess(responsibleUser, Collections.singletonList(new EntryDTO(getEntryType(), this, amountToPay)),
		transactionDetail);
    }

    protected abstract EntryType getEntryType();

    @Override
    public boolean isIndividualCandidacyEvent() {
	return true;
    }
}
