package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.places.campus.IPersistentCampus;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateBuilding implements IService {

    public void run(final String buildingName, final Integer campusID) throws ExcepcaoPersistencia, ExistingServiceException {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final List buildings = (List) persistentSupport.getIPersistentObject().readAll(Building.class);

        if (exists(buildings, buildingName)) {
            throw new ExistingServiceException();
        }

        final IPersistentCampus persistentCampus = persistentSupport.getIPersistentCampus();
        final Campus campus = (Campus) persistentCampus.readByOID(Campus.class, campusID);

        final Building building = DomainFactory.makeBuilding();
        building.setName(buildingName);
        building.setCampus(campus);
    }

    protected boolean exists(final List buildings, final String buildingName) {
        final Building building = (Building) CollectionUtils.find(buildings, new Predicate() {
            public boolean evaluate(Object arg0) {
                final Building building = (Building) arg0;
                return building.getName().equalsIgnoreCase(buildingName);
            }});

        return building != null;
    }

}