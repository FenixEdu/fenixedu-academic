package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomEditor;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.space.OldBuilding;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class EditarSala extends Service {

	public void run(RoomKey salaAntiga, InfoRoomEditor salaNova) throws ExistingServiceException,
			ExcepcaoPersistencia {

        final OldRoom room = OldRoom.findOldRoomByName(salaAntiga.getNomeSala());
		if (room != null) {

			if (!room.getNome().equals(salaNova.getNome())) {
				OldRoom roomWithSameName = OldRoom.findOldRoomByName(salaNova.getNome());
				if (roomWithSameName != null) {
					throw new ExistingServiceException();
				}
			}

			final OldBuilding building = findBuilding(salaNova.getEdificio());
            room.setBuilding(building);
            
			room.setNome(salaNova.getNome());
			room.setPiso(salaNova.getPiso());
			room.setCapacidadeNormal(salaNova.getCapacidadeNormal());
			room.setCapacidadeExame(salaNova.getCapacidadeExame());
			room.setTipo(salaNova.getTipo());
		}
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

}
