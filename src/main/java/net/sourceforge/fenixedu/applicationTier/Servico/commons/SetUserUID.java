package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;

public class SetUserUID {

    @Atomic
    public static void run(final Person person) {
        person.hasIstUsername();
    }

}