package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;

public class InstitutionAffiliationEventTicket extends InstitutionAffiliationEventTicket_Base {

    protected InstitutionAffiliationEventTicket() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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
        builder.append(user.getUserUId());
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

}
