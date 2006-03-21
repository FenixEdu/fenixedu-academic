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
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewRoomSchedule;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadPavillionsRoomsLessons extends Service {

    public List run(List pavillions, InfoExecutionPeriod infoExecutionPeriod)
            throws ExcepcaoPersistencia {
    	final ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentObject
    			.readByOID(ExecutionPeriod.class, infoExecutionPeriod.getIdInternal());

        final Set<OldRoom> rooms = OldRoom.findOldRoomsByBuildingNames(pavillions);

        final List infoViewRoomScheduleList = new ArrayList();
        for (final OldRoom room : rooms) {
            final InfoViewRoomSchedule infoViewRoomSchedule = new InfoViewRoomSchedule();
            infoViewRoomScheduleList.add(infoViewRoomSchedule);

            final InfoRoom infoRoom = InfoRoom.newInfoFromDomain(room);
            infoViewRoomSchedule.setInfoRoom(infoRoom);

            final List lessons = room.findLessonsForExecutionPeriod(executionPeriod);
            final List infoLessons = new ArrayList(lessons.size());
            for (final Iterator iterator2 = lessons.iterator(); iterator2.hasNext();) {
                final Lesson lesson = (Lesson) iterator2.next();
                final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
                infoLessons.add(infoLesson);

                final RoomOccupation roomOccupation = lesson.getRoomOccupation();
                final InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation
                        .newInfoFromDomain(roomOccupation);
                infoLesson.setInfoRoomOccupation(infoRoomOccupation);

                infoRoomOccupation.setInfoRoom(infoRoom);
                infoLesson.setInfoSala(infoRoom);

                final OccupationPeriod period = roomOccupation.getPeriod();
                final InfoPeriod infoPeriod = InfoPeriod.newInfoFromDomain(period);
                infoRoomOccupation.setInfoPeriod(infoPeriod);

                final Shift shift = lesson.getShift();
                final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                infoLesson.setInfoShift(infoShift);
                infoLesson.setInfoShiftList(new ArrayList(1));
                infoLesson.getInfoShiftList().add(infoShift);

                final ExecutionCourse executionCourse = shift.getDisciplinaExecucao();
                final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                        .newInfoFromDomain(executionCourse);
                infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

                infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
            }
            infoViewRoomSchedule.setRoomLessons(infoLessons);
        }

        return infoViewRoomScheduleList;
    }

}
