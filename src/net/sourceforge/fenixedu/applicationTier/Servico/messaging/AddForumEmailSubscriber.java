package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import pt.ist.fenixWebFramework.services.Service;

public class AddForumEmailSubscriber extends FenixService {

	public AddForumEmailSubscriber() {
		super();
	}

	@Service
	public static void run(Forum forum, Person person) {

		forum.addEmailSubscriber(person);
	}

}