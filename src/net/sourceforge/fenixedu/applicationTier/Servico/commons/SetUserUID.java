package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;

public class SetUserUID extends Service {

    public void run(final Person person) {
	person.hasIstUsername();
    }

}
