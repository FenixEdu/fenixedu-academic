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
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IBuilding;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBuilding;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CriarSala implements IService {

    public Object run(InfoRoom infoSala) throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        final ISalaPersistente roomDAO = persistentSupport.getISalaPersistente();
        final IPersistentBuilding persistentBuilding = persistentSupport.getIPersistentBuilding();

        final IRoom existingRoom = roomDAO.readByName(infoSala.getNome());

        if (existingRoom != null) {
            throw new ExistingServiceException("Duplicate Entry: " + infoSala.getNome());
        }

        final IBuilding building = findBuilding(persistentBuilding, infoSala.getEdificio());

        final IRoom room = writeRoom(roomDAO, infoSala, building);

        return Cloner.copyRoom2InfoRoom(room);

    }

    protected IBuilding findBuilding(final IPersistentBuilding persistentBuilding, final String edificio)
            throws ExcepcaoPersistencia {
        final List buildings = persistentBuilding.readAll();
        return (IBuilding) CollectionUtils.find(buildings, new Predicate() {
            public boolean evaluate(Object arg0) {
                final IBuilding building = (IBuilding) arg0;
                return building.getName().equalsIgnoreCase(edificio);
            }
        });
    }

    protected IRoom writeRoom(final ISalaPersistente roomDAO, final InfoRoom infoRoom, final IBuilding building)
            throws ExcepcaoPersistencia {
        final IRoom room = new Room();
        roomDAO.simpleLockWrite(room);
        room.setCapacidadeExame(infoRoom.getCapacidadeExame());
        room.setCapacidadeNormal(infoRoom.getCapacidadeNormal());
        room.setEdificio(infoRoom.getEdificio());
        room.setNome(infoRoom.getNome());
        room.setPiso(infoRoom.getPiso());
        room.setRoomOccupations(new ArrayList());
        room.setTipo(infoRoom.getTipo());
        room.setBuilding(building);

        return room;
    }

}