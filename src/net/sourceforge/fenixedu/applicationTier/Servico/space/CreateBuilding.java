package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateBuilding extends Service {

    public void run(final Integer suroundingSpaceID, final String name) throws ExcepcaoPersistencia {
        final Building building = DomainFactory.makeBuilding(name);
        final Space space = rootDomainObject.readSpaceByOID(suroundingSpaceID);
        building.setSuroundingSpace(space);
    }

}
