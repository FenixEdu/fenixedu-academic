/*
 * ApagarSala.java
 *
 * Created on 25 de Outubro de 2002, 15:36
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço ApagarSala.
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ApagarSala implements IService {

    public Object run(RoomKey keySala) throws FenixServiceException, ExcepcaoPersistencia {

        IRoom sala1 = null;
        boolean result = false;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        sala1 = sp.getISalaPersistente().readByName(keySala.getNomeSala());
        if (sala1 != null) {
            // sp.getISalaPersistente().delete(sala1);
            if (sala1.getAssociatedLessons().isEmpty() && sala1.getAssociatedSummaries().isEmpty()
                    && sala1.getRoomOccupations().isEmpty() && sala1.getExamStudentRooms().isEmpty()) {
                sala1.getBuilding().getRooms().remove(sala1);
                sala1.setBuilding(null);
                sp.getISalaPersistente().deleteByOID(Room.class, sala1.getIdInternal());

                result = true;

            }

        }

        return new Boolean(result);
    }

    public class NotAuthorizedServiceDeleteRoomException extends FenixServiceException {

        /**
         * 
         */
        private NotAuthorizedServiceDeleteRoomException() {
            super();
        }

        /**
         * @param cause
         */
        NotAuthorizedServiceDeleteRoomException(Throwable cause) {
            super(cause);
        }

    }

}