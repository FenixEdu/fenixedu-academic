package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import pt.ist.fenixWebFramework.services.Service;

public class RemoveForumEmailSubscriber extends FenixService {

    @Service
    public static void run(Forum forum, Person person) {
        forum.removeEmailSubscriber(person);
    }

}