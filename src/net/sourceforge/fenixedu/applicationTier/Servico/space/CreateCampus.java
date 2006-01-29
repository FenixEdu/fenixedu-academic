package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainFactory;

public class CreateCampus extends Service {

    public void run(final String name) {
        DomainFactory.makeCampus(name);
    }

}
