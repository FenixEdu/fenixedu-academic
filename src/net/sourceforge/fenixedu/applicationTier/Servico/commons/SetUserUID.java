package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;

public class SetUserUID extends FenixService {

    public void run(final Person person) {
	person.hasIstUsername();
    }

}
