package ServidorAplicacao.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExam;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.Room;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class EditExamRooms implements IService {

    ISuportePersistente sp = null;

    ISalaPersistente persistentRoom = null;

    IPersistentExam persistentExam = null;

    /**
     * The actor of this class.
     */
    public EditExamRooms() {
    }

    public InfoExam run(InfoExam infoExam, final List roomsForExam) throws FenixServiceException {

        ServiceSetUp();

        List finalRoomList = new ArrayList();
        CollectionUtils.collect(roomsForExam, TRANSFORM_ROOMID_TO_ROOM, finalRoomList);

        try {

            final Exam exam = (Exam) persistentRoom
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
            sp = SuportePersistenteOJB.getInstance();
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