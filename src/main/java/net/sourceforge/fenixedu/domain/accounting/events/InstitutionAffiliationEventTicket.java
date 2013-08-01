package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.User;

public class InstitutionAffiliationEventTicket extends InstitutionAffiliationEventTicket_Base {

    protected InstitutionAffiliationEventTicket() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setGenerated(new DateTime());
    }

    public InstitutionAffiliationEventTicket(final InstitutionAffiliationEvent institutionAffiliationEvent) {
        this();
        institutionAffiliationEvent.invalidateExistingTickets();
        setInstitutionAffiliationEvent(institutionAffiliationEvent);
        setTicket(generateTicket());
    }

    private String generateTicket() {
        final StringBuilder builder = new StringBuilder();

        builder.append(getExternalId());
        builder.append(hashCode());

        final InstitutionAffiliationEvent event = getInstitutionAffiliationEvent();
        final Person person = event.getPerson();
        final User user = person.getUser();
        builder.append(user.getUsername());
        builder.append(user.getExternalId());

        final DateTime instant = getGenerated();
        builder.append(instant.toString("yyyy-MM-dd HH:mm:ss"));

        return DigestUtils.shaHex(builder.toString());
    }

    public void consume(final Event event) {
        if (getConsumed() != null || getConsumerEvent() != null) {
            throw new DomainException("error.payment.ticket.already.used");
        }
        setConsumed(new DateTime());
        setConsumerEvent(event);
    }

    public void invalidate() {
        if (getConsumed() == null) {
            setConsumed(new DateTime());
        }
    }

    @Deprecated
    public boolean hasTicket() {
        return getTicket() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasConsumerEvent() {
        return getConsumerEvent() != null;
    }

    @Deprecated
    public boolean hasInstitutionAffiliationEvent() {
        return getInstitutionAffiliationEvent() != null;
    }

    @Deprecated
    public boolean hasConsumed() {
        return getConsumed() != null;
    }

    @Deprecated
    public boolean hasGenerated() {
        return getGenerated() != null;
    }

}
