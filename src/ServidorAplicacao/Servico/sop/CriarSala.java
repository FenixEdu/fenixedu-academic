/*
 * CriarSala.java
 *
 * Created on 25 de Outubro de 2002, ??:??
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço CriarSala.
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoRoom;
import DataBeans.util.Cloner;
import Dominio.IBuilding;
import Dominio.IRoom;
import Dominio.Room;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBuilding;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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