package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class MicroPaymentEvent extends MicroPaymentEvent_Base {

    public static Comparator<MicroPaymentEvent> COMPARATOR_BY_DATE = new Comparator<MicroPaymentEvent>() {

        @Override
        public int compare(final MicroPaymentEvent e1, final MicroPaymentEvent e2) {
            final int i = e1.getWhenOccured().compareTo(e1.getWhenOccured());
            return i == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(e1, e2) : i;
        }

    };

    protected MicroPaymentEvent() {
        super();
    }

    protected MicroPaymentEvent(final Person person, final Unit destinationUnit, final Money amount, final String paymentTicket) {
        super();
        init(person, destinationUnit, amount);
        final InstitutionAffiliationEvent affiliationEvent = getAffiliationEvent();
        affiliationEvent.consumeTicket(paymentTicket, this);
    }

    protected void init(Person person, Unit destinationUnit, Money amount) {
        super.init(EventType.MICRO_PAYMENT, person);
        setAmount(amount);
        setDestinationUnit(destinationUnit);
        Set<? extends Event> affiliations = person.getEventsByEventType(EventType.INSTITUTION_AFFILIATION);
        for (Event event : affiliations) {
            InstitutionAffiliationEvent affiliation = (InstitutionAffiliationEvent) event;
            if (affiliation.isOpen()) {
                setAffiliationEvent(affiliation);
                return;
            }
        }
        throw new DomainException("error.accounting.events.MicroPaymentEvent.personHasNoInstitutionAffiliation");
    }

    @Override
    protected Account getFromAccount() {
        return getAffiliationEvent().getToAccount();
    }

    @Override
    public Account getToAccount() {
        return getDestinationUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter result = super.getDescription();
        result.appendLabel(" - ").appendLabel(getDestinationUnit().getPresentationName());
        return result;
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(LabelFormatter.ENUMERATION_RESOURCES, entryType.name(), getAmount().toString());
        return labelFormatter;
    }

    @Override
    public PostingRule getPostingRule() {
        return getAffiliationEvent().getInstitution().getUnitServiceAgreementTemplate()
                .findPostingRuleBy(getEventType(), getWhenOccured(), null);
    }

    @Atomic
    public static MicroPaymentEvent create(final User responsible, final Person person, final Unit destinationUnit,
            final Money amount, final String paymentTicket) {
        final MicroPaymentEvent event = new MicroPaymentEvent(person, destinationUnit, amount, paymentTicket);
        final AccountingTransactionDetailDTO dto =
                new AccountingTransactionDetailDTO(new DateTime(), PaymentMode.CREDIT_SPENDING);
        event.process(responsible, event.calculateEntries(), dto);
        return event;
    }

    @Override
    public Unit getOwnerUnit() {
        return getDestinationUnit();
    }

    @Deprecated
    public boolean hasAmount() {
        return getAmount() != null;
    }

    @Deprecated
    public boolean hasDestinationUnit() {
        return getDestinationUnit() != null;
    }

    @Deprecated
    public boolean hasAffiliationEvent() {
        return getAffiliationEvent() != null;
    }

}
