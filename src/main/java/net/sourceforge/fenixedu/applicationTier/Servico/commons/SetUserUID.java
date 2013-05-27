package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;

public class SetUserUID {

    @Service
    public static void run(final Person person) {
        person.hasIstUsername();
    }

}