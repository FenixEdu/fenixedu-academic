/*
 * CriarSala.java
 *
 * Created on 25 de Outubro de 2002, ??:??
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço CriarSala.
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBuilding;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CriarSala implements IService {

    public Object run(InfoRoom infoSala) throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ISalaPersistente roomDAO = persistentSupport.getISalaPersistente();
        final IPersistentBuilding persistentBuilding = persistentSupport.getIPersistentBuilding();

        final Room existingRoom = roomDAO.readByName(infoSala.getNome());

        if (existingRoom != null) {
            throw new ExistingServiceException("Duplicate Entry: " + infoSala.getNome());
        }

        final Building building = findBuilding(persistentBuilding, infoSala.getEdificio());

        final Room room = writeRoom(roomDAO, infoSala, building);

        return InfoRoom.newInfoFromDomain(room);

    }

    protected Building findBuilding(final IPersistentBuilding persistentBuilding, final String edificio)
            throws ExcepcaoPersistencia {
        final List buildings = persistentBuilding.readAll();
        return (Building) CollectionUtils.find(buildings, new Predicate() {
            public boolean evaluate(Object arg0) {
                final Building building = (Building) arg0;
                return building.getName().equalsIgnoreCase(edificio);
            }
        });
    }

    protected Room writeRoom(final ISalaPersistente roomDAO, final InfoRoom infoRoom, final Building building)
            throws ExcepcaoPersistencia {
        final Room room = DomainFactory.makeRoom();
        room.setCapacidadeExame(infoRoom.getCapacidadeExame());
        room.setCapacidadeNormal(infoRoom.getCapacidadeNormal());
        room.setNome(infoRoom.getNome());
        room.setPiso(infoRoom.getPiso());
        room.setTipo(infoRoom.getTipo());
        room.setBuilding(building);

        return room;
    }

}
