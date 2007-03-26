package net.sourceforge.fenixedu.domain.research.activity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.ConferenceArticles;

public class Event extends Event_Base {

    public Event() {
	super();
	setOjbConcreteClass(getClass().getName());
    }

    public Event(String name, EventType type, ResearchActivityLocationType scope) {
	super();
	setOjbConcreteClass(getClass().getName());

	if (name == null)
	    throw new DomainException("errors.event.requiredAttributes");

	setName(name);
	setEventType(type);
	setLocationType(scope);
    }

    @Override
    public void delete() {
	for (; this.hasAnyParticipations(); getParticipations().get(0).delete())
	    ;
	for (; this.hasAnyParty(); this.getParties().get(0).removeResearchActivities(this))
	    ;
	for (; this.hasAnyEventEditions(); this.getEventEditions().get(0).removeEvent())
	    ;
	super.delete();
    }

    /**
     * This method is responsible for checking if the object still has active connections
     *if not, the object is deleted.
     */
    public void sweep() {
	if (!(this.hasAnyParticipations() || this.hasAnyEventEditions())) {
	    delete();
	}
    }

    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles() {
	return ResearchActivityParticipationRole.getAllEventParticipationRoles();
    }

    public static List<Event> readAll() {
	List<Event> result = new ArrayList<Event>();
	for (ResearchActivity researchActivity : RootDomainObject.getInstance()
		.getResearchActivitiesSet()) {
	    if (researchActivity instanceof Event) {
		result.add((Event) researchActivity);
	    }
	}
	return result;
    }

    public List<ConferenceArticles> getArticles() {
	List<ConferenceArticles> articles = new ArrayList<ConferenceArticles>();
	for (EventEdition edition : getEventEditions()) {
	    articles.addAll(edition.getArticles());
	}
	return articles;
    }
}
