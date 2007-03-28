package net.sourceforge.fenixedu.domain.research.activity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.result.publication.ConferenceArticles;

public class EventEdition extends EventEdition_Base {

    public EventEdition(Event event) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	this.setEvent(event);
    }

    // TODO Retirar este construtor, utilizado em ProjectEventAssociation
    public EventEdition(String name) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    @Override
    public void addEventConferenceArticlesAssociations(
	    EventConferenceArticlesAssociation eventConferenceArticlesAssociations) {
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
	    if (association.getConferenceArticle().equals(article))
		return true;
	}
	return false;
    }

    public String getFullName() {
	return this.getEdition();
	// return this.getEdition() + " " + this.getEvent().getName();
    }

    /**
         * This method is responsible for deleting the object and all its
         * references
         */
    public void delete() {
	for (; !this.getEventConferenceArticlesAssociations().isEmpty(); this
		.getEventConferenceArticlesAssociations().get(0).delete())
	    ;

	removeEvent();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    /**
         * This method is responsible for checking if the object still has
         * active connections if not, the object is deleted.
         */
    public void sweep() {
	if (!hasAnyParticipations() && !hasAnyAssociatedProjects()
		&& !hasAnyEventConferenceArticlesAssociations()) {
	    this.delete();
	}
    }
    
    public List<EventEditionParticipation> getParticipationsFor(Party party) {
	List<EventEditionParticipation> participations = new ArrayList<EventEditionParticipation>();
	for(EventEditionParticipation participation : getParticipations()) {
	    if(participation.getParty().equals(party)) {
		participations.add(participation);
	    }
	}
	return participations;
    }
    
    public ResearchActivityStage getStage() {
	return getEvent().getStage();
    }


}
