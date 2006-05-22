package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.Forum;

public class AddForumEmailSubscriber extends Service {

    public AddForumEmailSubscriber() {
        super();
    }

    public void run(Integer forumId, Integer personId) {
        Forum forum = rootDomainObject.readForumByOID(forumId);
        Person person = (Person) rootDomainObject.readPartyByOID(personId);

        forum.addEmailSubscriber(person);
    }

}
