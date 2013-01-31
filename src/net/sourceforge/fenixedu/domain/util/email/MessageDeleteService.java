package net.sourceforge.fenixedu.domain.util.email;

import pt.ist.fenixWebFramework.services.Service;

public class MessageDeleteService {

	@Service
	public static void delete(final Message message) {
		message.safeDelete();
	}

}
