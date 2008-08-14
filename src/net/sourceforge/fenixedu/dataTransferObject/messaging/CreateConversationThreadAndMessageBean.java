package net.sourceforge.fenixedu.dataTransferObject.messaging;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateConversationThreadAndMessageBean implements Serializable {

    private MultiLanguageString subject;

    private MultiLanguageString body;

    private DomainReference<Person> creatorReference;

    private DomainReference<Forum> forumReference;

    public CreateConversationThreadAndMessageBean() {
	super();
	creatorReference = new DomainReference<Person>(null);
	forumReference = new DomainReference<Forum>(null);
    }

    public MultiLanguageString getSubject() {
	return subject;
    }

    public void setSubject(MultiLanguageString subject) {
	this.subject = subject;
    }

    public MultiLanguageString getBody() {
	return body;
    }

    public void setBody(MultiLanguageString body) {
	this.body = body;
    }

    public Person getCreator() {
	return this.creatorReference.getObject();

    }

    public void setCreator(Person creator) {
	this.creatorReference = new DomainReference<Person>(creator);
    }

    public Forum getForum() {
	return this.forumReference.getObject();

    }

    public void setForum(Forum forum) {
	this.forumReference = new DomainReference<Forum>(forum);
    }

}
