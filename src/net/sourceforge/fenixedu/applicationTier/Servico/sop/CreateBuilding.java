package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.space.OldBuilding;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class CreateBuilding extends Service {

    public void run(final String buildingName, final Integer campusID) throws ExcepcaoPersistencia, ExistingServiceException {
        final Set<OldBuilding> buildings = OldBuilding.getOldBuildings();

        if (exists(buildings, buildingName)) {
            throw new ExistingServiceException();
        }

        final Campus campus = rootDomainObject.readCampusByOID(campusID);

        final OldBuilding building = new OldBuilding();
        building.setName(buildingName);
        building.setCampus(campus);
    }

    protected boolean exists(final Set buildings, final String buildingName) {
        final OldBuilding building = (OldBuilding) CollectionUtils.find(buildings, new Predicate() {
            public boolean evaluate(Object arg0) {
                final OldBuilding building = (OldBuilding) arg0;
                return building.getName().equalsIgnoreCase(buildingName);
            }});

        return building != null;
    }

}
