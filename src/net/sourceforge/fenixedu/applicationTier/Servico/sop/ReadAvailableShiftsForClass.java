/*
 * 
 * Created on 2003/08/13
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadAvailableShiftsForClass extends Service {

    public Object run(InfoClass infoClass) throws ExcepcaoPersistencia {

        List infoShifts = null;

        SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID(infoClass.getIdInternal());
        Set<Shift> shifts = schoolClass.findAvailableShifts();

        infoShifts = (List) CollectionUtils.collect(shifts, new Transformer() {
            public Object transform(Object arg0) {
                Shift shift = (Shift) arg0;
                final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

                final ExecutionCourse executionCourse = shift.getDisciplinaExecucao();
                final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                        .newInfoFromDomain(executionCourse);
                infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

                final Collection lessons = shift.getAssociatedLessons();
                final List infoLessons = new ArrayList(lessons.size());
                infoShift.setInfoLessons(infoLessons);
                for (final Iterator iterator2 = lessons.iterator(); iterator2.hasNext();) {
                    final Lesson lesson = (Lesson) iterator2.next();
                    final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);

                    final RoomOccupation roomOccupation = lesson.getRoomOccupation();
                    final InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation
                            .newInfoFromDomain(roomOccupation);
                    infoLesson.setInfoRoomOccupation(infoRoomOccupation);

                    final OldRoom room = roomOccupation.getRoom();
                    final InfoRoom infoRoom = InfoRoom.newInfoFromDomain(room);
                    infoRoomOccupation.setInfoRoom(infoRoom);
                    infoLesson.setInfoSala(infoRoom);

                    final OccupationPeriod period = roomOccupation.getPeriod();
                    final InfoPeriod infoPeriod = InfoPeriod.newInfoFromDomain(period);
                    infoRoomOccupation.setInfoPeriod(infoPeriod);

                    infoLessons.add(infoLesson);
                }
                return infoShift;
            }
        });

        return infoShifts;
    }

}