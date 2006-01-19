package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.applicationTier.Service;

public class EditarSala extends Service {

	public void run(RoomKey salaAntiga, InfoRoom salaNova) throws ExistingServiceException,
			ExcepcaoPersistencia {

		final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final Room room = sp.getISalaPersistente().readByName(salaAntiga.getNomeSala());
		if (room != null) {

			if (!room.getNome().equals(salaNova.getNome())) {
				Room roomWithSameName = sp.getISalaPersistente().readByName(salaNova.getNome());
				if (roomWithSameName != null) {
					throw new ExistingServiceException();
				}
			}

			final Building building = findBuilding(sp, salaNova.getEdificio());
            room.setBuilding(building);
            
			room.setNome(salaNova.getNome());
			room.setPiso(salaNova.getPiso());
			room.setCapacidadeNormal(salaNova.getCapacidadeNormal());
			room.setCapacidadeExame(salaNova.getCapacidadeExame());
			room.setTipo(salaNova.getTipo());
		}
	}

	protected Building findBuilding(ISuportePersistente sp, final String edificio)
			throws ExcepcaoPersistencia {
		final List<Building> buildings = (List<Building>) sp.getIPersistentObject().readAll(Building.class);
		return (Building) CollectionUtils.find(buildings, new Predicate() {
			public boolean evaluate(Object arg0) {
				final Building building = (Building) arg0;
				return building.getName().equalsIgnoreCase(edificio);
			}
		});
	}

}
