/*
 * ReadExamsByExecutionCourse.java
 *
 * Created on 2003/05/26
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewRoomSchedule;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadPavillionsRoomsLessons implements IService {

    private static ReadPavillionsRoomsLessons _servico = new ReadPavillionsRoomsLessons();

    /**
     * The singleton access method of this class.
     */
    public static ReadPavillionsRoomsLessons getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadPavillionsRoomsLessons() {
    }

    public List run(List pavillions, InfoExecutionPeriod infoExecutionPeriod) {

        final List infoViewRoomScheduleList = new ArrayList();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IExecutionPeriod executionPeriod = InfoExecutionPeriodWithInfoExecutionYear
                    .newDomainFromInfo(infoExecutionPeriod);

            ISalaPersistente roomDAO = sp.getISalaPersistente();
            IAulaPersistente lessonDAO = sp.getIAulaPersistente();
            // Read pavillions rooms
            List rooms = roomDAO.readByPavillions(pavillions);

            // Read rooms classes
            for (int i = 0; i < rooms.size(); i++) {
                InfoViewRoomSchedule infoViewRoomSchedule = new InfoViewRoomSchedule();
                IRoom room = (IRoom) rooms.get(i);
                List lessonList = lessonDAO.readByRoomAndExecutionPeriod(room, executionPeriod);
                Iterator iterator = lessonList.iterator();
                List infoLessonsList = new ArrayList();
                while (iterator.hasNext()) {
                    ILesson elem = (ILesson) iterator.next();
                    InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
                    IShift shift = elem.getShift();
                    if (shift == null) {
                        continue;
                    }
                    InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
                    infoLesson.setInfoShift(infoShift);

                    infoLessonsList.add(infoLesson);
                }

                infoViewRoomSchedule.setInfoRoom(Cloner.copyRoom2InfoRoom(room));
                infoViewRoomSchedule.setRoomLessons(infoLessonsList);
                infoViewRoomScheduleList.add(infoViewRoomSchedule);
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoViewRoomScheduleList;
    }
}