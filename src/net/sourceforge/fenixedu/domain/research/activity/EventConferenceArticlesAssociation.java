package net.sourceforge.fenixedu.domain.research.activity;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.result.publication.ConferenceArticles;
import dml.runtime.RelationAdapter;

public class EventConferenceArticlesAssociation extends EventConferenceArticlesAssociation_Base {
    
	static {
		EventEditionEventConferenceArticlesAssociation.addListener(new RelationAdapter<EventConferenceArticlesAssociation, EventEdition>() {

		    @Override
		    public void afterRemove(EventConferenceArticlesAssociation association, EventEdition eventEdition) {
				super.afterRemove(association, eventEdition);
				
				if (eventEdition != null && association != null && !eventEdition.hasAnyEventConferenceArticlesAssociations()) {
					eventEdition.delete();
				}
		    }
		    
		});
	}
    
    public  EventConferenceArticlesAssociation(EventEdition eventEdition, ConferenceArticles article, Person person) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        this.setEventEdition(eventEdition);
        this.setConferenceArticle(article);
        this.setPerson(person);
    }
    
    public void delete() {
    	removeConferenceArticle();
    	removePerson();
    	removeEventEdition();
    	removeRootDomainObject();
    	super.deleteDomainObject();
    }
    
}
