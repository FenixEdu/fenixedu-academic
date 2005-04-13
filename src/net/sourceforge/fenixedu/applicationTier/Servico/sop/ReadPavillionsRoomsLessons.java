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

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewRoomSchedule;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadPavillionsRoomsLessons implements IService {

    public List run(List pavillions, InfoExecutionPeriod infoExecutionPeriod) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ISalaPersistente roomDAO = sp.getISalaPersistente();
        final IAulaPersistente lessonDAO = sp.getIAulaPersistente();

        final List infoViewRoomScheduleList = new ArrayList();
        
        final IExecutionPeriod executionPeriod = InfoExecutionPeriodWithInfoExecutionYear
                .newDomainFromInfo(infoExecutionPeriod);

        // Read pavillions rooms
        final List rooms = roomDAO.readByPavillions(pavillions);

        // Read rooms classes
        for (final Iterator iterator = rooms.iterator(); iterator.hasNext(); ) {
            final IRoom room = (IRoom) iterator.next();

            final InfoViewRoomSchedule infoViewRoomSchedule = new InfoViewRoomSchedule();
            infoViewRoomScheduleList.add(infoViewRoomSchedule);

            final InfoRoom infoRoom = InfoRoom.newInfoFromDomain(room);
            infoViewRoomSchedule.setInfoRoom(infoRoom);

            final List lessons = lessonDAO.readByRoomAndExecutionPeriod(room, executionPeriod);
            final List infoLessons = new ArrayList(lessons.size());
            for (final Iterator iterator2 = lessons.iterator(); iterator2.hasNext(); ) {
                final ILesson lesson = (ILesson) iterator2.next();
                final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
                infoLessons.add(infoLesson);

                final IRoomOccupation roomOccupation = lesson.getRoomOccupation();
                final InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(roomOccupation);
                infoLesson.setInfoRoomOccupation(infoRoomOccupation);

                infoRoomOccupation.setInfoRoom(infoRoom);
                infoLesson.setInfoSala(infoRoom);

                final IPeriod period = roomOccupation.getPeriod();
                final InfoPeriod infoPeriod = InfoPeriod.newInfoFromDomain(period);
                infoRoomOccupation.setInfoPeriod(infoPeriod);

                final IShift shift = lesson.getShift();
                final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                infoLesson.setInfoShift(infoShift);
                infoLesson.setInfoShiftList(new ArrayList(1));
                infoLesson.getInfoShiftList().add(infoShift);

                final IExecutionCourse executionCourse = shift.getDisciplinaExecucao();
                final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
                infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

                infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
            }
            infoViewRoomSchedule.setRoomLessons(infoLessons);
        }

        return infoViewRoomScheduleList;
    }
}