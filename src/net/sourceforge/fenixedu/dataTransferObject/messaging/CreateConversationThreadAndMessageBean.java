package net.sourceforge.fenixedu.dataTransferObject.messaging;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.Forum;

public class CreateConversationThreadAndMessageBean implements Serializable {

    private String subject;

    private String body;

    private DomainReference<Person> creatorReference;

    private DomainReference<Forum> forumReference;

    public CreateConversationThreadAndMessageBean() {
        super();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Person getCreator() {
        return (this.creatorReference != null) ? this.creatorReference.getObject() : null;

    }

    public void setCreator(Person creator) {
        this.creatorReference = (creator != null) ? new DomainReference<Person>(creator) : null;
    }

    public Forum getForum() {
        return (this.forumReference != null) ? this.forumReference.getObject() : null;

    }

    public void setForum(Forum forum) {
        this.forumReference = (forum != null) ? new DomainReference<Forum>(forum) : null;
    }

}
