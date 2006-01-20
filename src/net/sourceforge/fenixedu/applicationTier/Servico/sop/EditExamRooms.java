package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class EditExamRooms extends Service {

    public InfoExam run(InfoExam infoExam, final List<Integer> roomsForExam) throws ExcepcaoPersistencia,
            FenixServiceException {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ISalaPersistente persistentRoom = persistentSupport.getISalaPersistente();
        final IPersistentExam persistentExam = persistentSupport.getIPersistentExam();

        final List<Room> finalRoomList = new ArrayList<Room>();
        for (final Integer id : roomsForExam) {
        	finalRoomList.add((Room) persistentRoom.readByOID(Room.class, (Integer) id));
        }

        final Exam exam = (Exam) persistentExam.readByOID(Exam.class, infoExam.getIdInternal());
        if (exam == null) {
            throw new NonExistingServiceException();
        }
 
        // Remove all elements
        // TODO : Do this more intelegently.
        exam.getAssociatedRooms().clear();

        // Add all elements
        exam.getAssociatedRooms().addAll(finalRoomList);

        return InfoExam.newInfoFromDomain(exam);
    }

}