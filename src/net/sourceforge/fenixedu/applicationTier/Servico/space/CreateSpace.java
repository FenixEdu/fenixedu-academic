package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainFactory;

public class CreateSpace extends Service {

    public void run() {
        DomainFactory.makeBuilding("Building " + System.currentTimeMillis());
    }

}
