package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;

public class SetUserUID extends FenixService {

    @Service
    public static void run(final Person person) {
        person.hasIstUsername();
    }

}