package net.sourceforge.fenixedu.domain.research.activity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.ConferenceArticles;

public class Event extends Event_Base {

    public Event() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Event(String name, EventType type, ResearchActivityLocationType scope) {
	this();

	if (name == null)
	    throw new DomainException("errors.event.requiredAttributes");

	setName(name);
	setEventType(type);
	setLocationType(scope);
    }

    public void delete() {
	for (; this.hasAnyParticipations(); getParticipations().get(0).delete())
	    ;
	for (; this.hasAnyEventEditions(); this.getEventEditions().get(0).removeEvent())
	    ;
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    /**
         * This method is responsible for checking if the object still has
         * active connections if not, the object is deleted.
         */
    public void sweep() {
	if (!(this.hasAnyParticipations() || this.hasAnyEventEditions())) {
	    delete();
	}
    }

    public List<ResearchActivityParticipationRole> getAllowedRoles() {
	return ResearchActivityParticipationRole.getAllEventParticipationRoles();
    }

    public List<ConferenceArticles> getArticles() {
	List<ConferenceArticles> articles = new ArrayList<ConferenceArticles>();
	for (EventEdition edition : getEventEditions()) {
	    articles.addAll(edition.getArticles());
	}
	return articles;
    }

    public List<EventParticipation> getParticipationsFor(Party party) {
	List<EventParticipation> participations = new ArrayList<EventParticipation>();
	for(EventParticipation participation : getParticipations()) {
	    if(participation.getParty().equals(party)) {
		participations.add(participation);
	    }
	}
	return participations;
    }
}
