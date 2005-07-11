package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class EditExamRooms implements IService {

    ISuportePersistente sp = null;

    ISalaPersistente persistentRoom = null;

    IPersistentExam persistentExam = null;

    public InfoExam run(InfoExam infoExam, final List roomsForExam) throws FenixServiceException {
        ServiceSetUp();

        List finalRoomList = new ArrayList();
        CollectionUtils.collect(roomsForExam, TRANSFORM_ROOMID_TO_ROOM, finalRoomList);

        try {

            final IExam exam = (IExam) persistentRoom
                    .readByOID(Exam.class, infoExam.getIdInternal(), true);
            if (exam == null) {
                throw new NonExistingServiceException();
            }
            persistentExam.simpleLockWrite(exam);

            // Remove all elements
            // TODO : Do this more intelegently.
            exam.getAssociatedRooms().clear();

            // Add all elements
            CollectionUtils.forAllDo(finalRoomList, new Closure() {
                public void execute(Object objRoom) {
                    exam.getAssociatedRooms().add(objRoom);
                }
            });

            return Cloner.copyIExam2InfoExam(exam);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

    private void ServiceSetUp() throws FenixServiceException {
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        } catch (ExcepcaoPersistencia e1) {
            throw new FenixServiceException();
        }
        persistentRoom = sp.getISalaPersistente();
        persistentExam = sp.getIPersistentExam();
    }

    private Transformer TRANSFORM_ROOMID_TO_ROOM = new Transformer() {
        public Object transform(Object id) {

            try {
                return persistentRoom.readByOID(Room.class, (Integer) id);
            } catch (ExcepcaoPersistencia e) {
                return null;
            }
        }
    };

}