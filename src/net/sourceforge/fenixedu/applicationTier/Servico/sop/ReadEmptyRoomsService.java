package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class ReadEmptyRoomsService implements IService {
    //SERVICO NAO USADO

    public ReadEmptyRoomsService() {
    }

    public Object run(InfoRoom infoRoom, InfoLesson infoLesson, InfoExecutionPeriod infoExecutionPeriod)
            throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IAulaPersistente lessonDAO = sp.getIAulaPersistente();
            ISalaPersistente roomDAO = sp.getISalaPersistente();

            // Check is time interval is valid

            if (!validTimeInterval(infoLesson)) {
                throw new InvalidTimeInterval();
            }

            // Read all Rooms with a capacity
            List roomList = roomDAO.readSalas(null, null, null, null,
            //					infoRoom.getCapacidadeNormal()
                    null, null);

            Iterator roomListIterator = roomList.iterator();

            List infoRoomList = new ArrayList();

            while (roomListIterator.hasNext()) {
                IRoom element = (IRoom) roomListIterator.next();
                try {
                    InfoRoom infoRoomElement = Cloner.copyRoom2InfoRoom(element);
                    infoRoomList.add(infoRoomElement);
                } catch (IllegalArgumentException e) {
                    // ignored
                }
            }
            // remove predicate
            infoRoomList = (List) CollectionUtils.select(infoRoomList, new RoomLessonPredicate());

            ILesson lesson = Cloner.copyInfoLesson2Lesson(infoLesson);

            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

            //List lessonList = lessonDAO.readLessonsInPeriod(lesson);
            List lessonList = lessonDAO.readLessonsInBroadPeriodInAnyRoom(lesson, executionPeriod);

            Iterator lessonIterator = lessonList.iterator();

            /* remove lesson's rooms from room list */
            while (lessonIterator.hasNext()) {
                ILesson lessonAux = (ILesson) lessonIterator.next();
                InfoLesson infoLessonAux = Cloner.copyILesson2InfoLesson(lessonAux);
                if (infoLesson.getIdInternal() != null
                        && !infoLesson.getIdInternal().equals(infoLessonAux.getIdInternal())) {
                    infoRoomList.remove(infoLessonAux.getInfoSala());
                } else if (infoLesson.getIdInternal() == null) {
                    infoRoomList.remove(infoLessonAux.getInfoSala());
                }
            }
            return infoRoomList;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private class RoomLessonPredicate implements Predicate {
        public RoomLessonPredicate() {
        }

        /**
         * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
         */
        public boolean evaluate(Object listElement) {
            InfoRoom infoRoom = (InfoRoom) listElement;
            return !infoRoom.getNome().endsWith(".");
        }
    }

    private boolean validTimeInterval(InfoLesson lesson) {
        boolean result = true;

        if (lesson.getInicio().getTime().getTime() >= lesson.getFim().getTime().getTime()) {
            result = false;
        }

        return result;
    }

    /**
     */
    public class InvalidTimeInterval extends FenixServiceException {

        /**
         *  
         */
        InvalidTimeInterval() {
            super();
        }

    }
}