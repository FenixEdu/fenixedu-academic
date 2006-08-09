/*
 * CriarSala.java
 *
 * Created on 25 de Outubro de 2002, ??:??
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o CriarSala.
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho
 */
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.OldBuilding;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class CriarSala extends Service {

    public Object run(InfoRoom infoSala) throws FenixServiceException, ExcepcaoPersistencia {
        final OldRoom existingRoom = OldRoom.findOldRoomByName(infoSala.getNome());

        if (existingRoom != null) {
            throw new ExistingServiceException("Duplicate Entry: " + infoSala.getNome());
        }

        final OldBuilding building = findBuilding(infoSala.getEdificio());

        final OldRoom room = writeRoom(infoSala, building);

        return InfoRoom.newInfoFromDomain(room);

    }

    protected OldBuilding findBuilding(final String edificio)
            throws ExcepcaoPersistencia {
        final Set<OldBuilding> buildings = OldBuilding.getOldBuildings();
        return (OldBuilding) CollectionUtils.find(buildings, new Predicate() {
            public boolean evaluate(Object arg0) {
                final OldBuilding building = (OldBuilding) arg0;
                return building.getName().equalsIgnoreCase(edificio);
            }
        });
    }

    protected OldRoom writeRoom(final InfoRoom infoRoom, final OldBuilding building)
            throws ExcepcaoPersistencia {
        final OldRoom room = new OldRoom();
        room.setCapacidadeExame(infoRoom.getCapacidadeExame());
        room.setCapacidadeNormal(infoRoom.getCapacidadeNormal());
        room.setNome(infoRoom.getNome());
        room.setPiso(infoRoom.getPiso());
        room.setTipo(infoRoom.getTipo());
        room.setBuilding(building);

        return room;
    }

}
