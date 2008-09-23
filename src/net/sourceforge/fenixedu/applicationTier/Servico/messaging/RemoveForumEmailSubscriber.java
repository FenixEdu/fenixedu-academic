package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.Forum;

public class RemoveForumEmailSubscriber extends FenixService {

    public void run(Forum forum, Person person) {
	forum.removeEmailSubscriber(person);
    }

}
