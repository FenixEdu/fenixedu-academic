package net.sourceforge.fenixedu.domain.research.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.result.publication.ConferenceArticles;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class EventEdition extends EventEdition_Base implements ParticipationsInterface {

    static {
        getRelationEventEventEdition().addListener(new RelationAdapter<EventEdition, ResearchEvent>() {

            @Override
            public void afterRemove(EventEdition edition, ResearchEvent event) {
                super.afterRemove(edition, event);
                if (edition != null && event != null && !event.hasAnyEventEditions() && !event.hasAnyParticipations()) {
                    event.delete();
                }
            }
        });
    }

    public EventEdition(ResearchEvent event) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        this.setEvent(event);
    }

    public EventEdition(String name) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    @Override
    public void addEventConferenceArticlesAssociations(EventConferenceArticlesAssociation eventConferenceArticlesAssociations) {
        if (!containsArticle(eventConferenceArticlesAssociations.getConferenceArticle())) {
            super.addEventConferenceArticlesAssociations(eventConferenceArticlesAssociations);
        } else {
            throw new DomainException("error.articleAlreadyAssociated");
        }
    }

    public List<ConferenceArticles> getArticles() {
        List<ConferenceArticles> articles = new ArrayList<ConferenceArticles>();
        for (EventConferenceArticlesAssociation association : this.getEventConferenceArticlesAssociations()) {
            articles.add(association.getConferenceArticle());
        }
        return articles;
    }

    public boolean containsArticle(ConferenceArticles article) {
        for (EventConferenceArticlesAssociation association : this.getEventConferenceArticlesAssociations()) {
            if (association.getConferenceArticle().equals(article)) {
                return true;
            }
        }
        return false;
    }

    public String getFullName() {
        return this.getEdition();
        // return this.getEdition() + " " + this.getEvent().getName();
    }

    /**
     * This method is responsible for deleting the object and all its references
     */
    public void delete() {
        for (; !this.getEventConferenceArticlesAssociations().isEmpty(); this.getEventConferenceArticlesAssociations().iterator().next()
                .delete()) {
            ;
        }

        setEvent(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    /**
     * This method is responsible for checking if the object still has active
     * connections if not, the object is deleted.
     */
    public void sweep() {
        if (!hasAnyParticipations() && !hasAnyAssociatedProjects() && !hasAnyEventConferenceArticlesAssociations()) {
            this.delete();
        }
    }

    @Override
    public List<EventEditionParticipation> getParticipationsFor(Party party) {
        List<EventEditionParticipation> participations = new ArrayList<EventEditionParticipation>();
        for (EventEditionParticipation participation : getParticipations()) {
            if (participation.getParty().equals(party)) {
                participations.add(participation);
            }
        }
        return participations;
    }

    public ResearchActivityStage getStage() {
        return getEvent().getStage();
    }

    @Override
    public boolean canBeEditedByUser(Person person) {
        Set<Person> people = getPeopleWhoHaveAssociatedArticles();
        people.addAll(getPeopleWhoHaveParticipations());
        return people.size() == 1 && people.contains(person);
    }

    @Override
    public boolean canBeEditedByCurrentUser() {
        return canBeEditedByUser(AccessControl.getPerson());
    }

    public Set<Person> getPeopleWhoHaveAssociatedArticles() {
        Set<Person> people = new HashSet<Person>();
        for (EventConferenceArticlesAssociation association : getEventConferenceArticlesAssociations()) {
            people.add(association.getPerson());
        }
        return people;
    }

    public Set<Person> getPeopleWhoHaveParticipations() {
        Set<Person> people = new HashSet<Person>();
        for (EventEditionParticipation participation : getParticipations()) {
            if (participation.getParty().isPerson()) {
                people.add((Person) participation.getParty());
            }
        }
        return people;
    }

    public void setParticipations(List<EventEditionParticipation> participations) {
        getParticipations().clear();
        getParticipations().addAll(participations);

    }

    @Override
    public void addUniqueParticipation(Participation participation) {
        if (participation instanceof EventEditionParticipation) {
            EventEditionParticipation eventEditionParticipation = (EventEditionParticipation) participation;
            for (EventEditionParticipation eventEditionParticipation2 : getParticipationsSet()) {
                if (eventEditionParticipation2.getParty().equals(eventEditionParticipation.getParty())
                        && eventEditionParticipation2.getRole().equals(eventEditionParticipation.getRole())) {
                    return;
                }
            }
            addParticipations(eventEditionParticipation);
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation> getAssociatedProjects() {
        return getAssociatedProjectsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedProjects() {
        return !getAssociatedProjectsSet().isEmpty();
    }

    @Override
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.EventEditionParticipation> getParticipations() {
        return getParticipationsSet();
    }

    @Deprecated
    public boolean hasAnyParticipations() {
        return !getParticipationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.EventConferenceArticlesAssociation> getEventConferenceArticlesAssociations() {
        return getEventConferenceArticlesAssociationsSet();
    }

    @Deprecated
    public boolean hasAnyEventConferenceArticlesAssociations() {
        return !getEventConferenceArticlesAssociationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasEventLocation() {
        return getEventLocation() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEndDate() {
        return getEndDate() != null;
    }

    @Deprecated
    public boolean hasUrl() {
        return getUrl() != null;
    }

    @Deprecated
    public boolean hasStartDate() {
        return getStartDate() != null;
    }

    @Deprecated
    public boolean hasEdition() {
        return getEdition() != null;
    }

    @Deprecated
    public boolean hasEvent() {
        return getEvent() != null;
    }

    @Deprecated
    public boolean hasOrganization() {
        return getOrganization() != null;
    }

}
