package net.sourceforge.fenixedu.domain.util.email;

import pt.ist.fenixframework.Atomic;

public class MessageDeleteService {

    @Atomic
    public static void delete(final Message message) {
        message.safeDelete();
    }

}
