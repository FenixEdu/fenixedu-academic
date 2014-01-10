package net.sourceforge.fenixedu.domain.research.activity;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.publication.ConferenceArticles;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class EventConferenceArticlesAssociation extends EventConferenceArticlesAssociation_Base {

    static {
        getRelationEventEditionEventConferenceArticlesAssociation().addListener(
                new RelationAdapter<EventEdition, EventConferenceArticlesAssociation>() {

                    @Override
                    public void afterRemove(EventEdition eventEdition, EventConferenceArticlesAssociation association) {
                        super.afterRemove(eventEdition, association);

                        if (eventEdition != null && association != null
                                && !eventEdition.hasAnyEventConferenceArticlesAssociations()
                                && !eventEdition.hasAnyParticipations() && !eventEdition.hasAnyAssociatedProjects()) {
                            eventEdition.delete();
                        }
                    }

                });
    }

    public EventConferenceArticlesAssociation(EventEdition eventEdition, ConferenceArticles article, Person person) {
        super();
        setRootDomainObject(Bennu.getInstance());
        this.setEventEdition(eventEdition);
        this.setConferenceArticle(article);
        this.setPerson(person);
    }

    public void delete() {
        setConferenceArticle(null);
        setPerson(null);
        setEventEdition(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEventEdition() {
        return getEventEdition() != null;
    }

    @Deprecated
    public boolean hasConferenceArticle() {
        return getConferenceArticle() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
