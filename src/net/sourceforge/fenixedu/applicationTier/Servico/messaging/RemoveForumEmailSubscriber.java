package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.Forum;

public class RemoveForumEmailSubscriber extends Service {

    public void run(Forum forum, Person person) {
	forum.removeEmailSubscriber(person);
    }

}
