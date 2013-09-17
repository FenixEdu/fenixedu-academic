package net.sourceforge.fenixedu.domain.research.activity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.ConferenceArticles;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ResearchEvent extends ResearchEvent_Base implements ParticipationsInterface {

    public ResearchEvent() {
        super();
        setStage(ResearchActivityStage.DRAFT);
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public ResearchEvent(String name, EventType type, ScopeType scope) {
        this();

        if (name == null) {
            throw new DomainException("errors.event.requiredAttributes");
        }

        setName(name);
        setEventType(type);
        setLocationType(scope);
    }

    public void delete() {
        for (; this.hasAnyParticipations(); getParticipations().iterator().next().delete()) {
            ;
        }
        for (; this.hasAnyEventEditions(); this.getEventEditions().iterator().next().setEvent(null)) {
            ;
        }
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    /**
     * This method is responsible for checking if the object still has active
     * connections if not, the object is deleted.
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

    @Override
    public List<EventParticipation> getParticipationsFor(Party party) {
        List<EventParticipation> participations = new ArrayList<EventParticipation>();
        for (EventParticipation participation : getParticipations()) {
            if (participation.getParty().equals(party)) {
                participations.add(participation);
            }
        }
        return participations;
    }

    @Override
    public boolean canBeEditedByUser(Person person) {
        for (EventEdition edition : getEventEditions()) {
            if (!edition.canBeEditedByUser(person)) {
                return false;
            }
        }
        return getParticipations().size() == getParticipationsFor(person).size();

    }

    @Override
    public boolean canBeEditedByCurrentUser() {
        return canBeEditedByUser(AccessControl.getPerson());
    }

    @Override
    public void addUniqueParticipation(Participation participation) {
        if (participation instanceof EventParticipation) {
            EventParticipation eventParticipation = (EventParticipation) participation;
            for (EventParticipation eventParticipation2 : getParticipationsSet()) {
                if (eventParticipation2.getParty().equals(eventParticipation.getParty())
                        && eventParticipation2.getRole().equals(eventParticipation.getRole())) {
                    return;
                }
            }
            addParticipations(eventParticipation);
        }

    }

    @Override
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.EventParticipation> getParticipations() {
        return getParticipationsSet();
    }

    @Deprecated
    public boolean hasAnyParticipations() {
        return !getParticipationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.EventEdition> getEventEditions() {
        return getEventEditionsSet();
    }

    @Deprecated
    public boolean hasAnyEventEditions() {
        return !getEventEditionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasUrl() {
        return getUrl() != null;
    }

    @Deprecated
    public boolean hasLocationType() {
        return getLocationType() != null;
    }

    @Deprecated
    public boolean hasEventType() {
        return getEventType() != null;
    }

    @Deprecated
    public boolean hasStage() {
        return getStage() != null;
    }

}
