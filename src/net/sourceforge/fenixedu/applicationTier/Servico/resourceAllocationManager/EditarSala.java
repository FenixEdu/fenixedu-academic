package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomEditor;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditarSala extends Service {

    public void run(InfoRoomEditor salaNova) throws ExistingServiceException, ExcepcaoPersistencia {
	if(salaNova != null) {
	    AllocatableSpace room = salaNova.getRoom();
            if(room != null) {        	
        	room.editCapacities(salaNova.getCapacidadeNormal(), salaNova.getCapacidadeExame());
            }	
	}
    }
}
