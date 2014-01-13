package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;

public class CreateUserIfNecessary {

    @Atomic
    public static void run(final Person person) {
        if (person.getUser() == null) {
            person.createUser();
        }
    }

}