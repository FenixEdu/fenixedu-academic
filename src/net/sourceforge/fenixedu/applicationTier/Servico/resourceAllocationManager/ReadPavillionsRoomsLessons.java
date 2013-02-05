/*
 * ReadExamsByExecutionCourse.java
 *
 * Created on 2003/05/26
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewRoomSchedule;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadPavillionsRoomsLessons extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static List run(List<String> pavillions, AcademicInterval academicInterval) {

        final List<Building> buildings = Building.getActiveBuildingsByNames(pavillions);
        List<AllocatableSpace> rooms = new ArrayList<AllocatableSpace>();
        for (Building building : buildings) {
            rooms.addAll(building.getAllActiveSubRoomsForEducation());
        }

        final List infoViewRoomScheduleList = new ArrayList();
        for (final AllocatableSpace room : rooms) {
            if (room.containsIdentification()) {

                final InfoViewRoomSchedule infoViewRoomSchedule = new InfoViewRoomSchedule();
                infoViewRoomScheduleList.add(infoViewRoomSchedule);

                final InfoRoom infoRoom = InfoRoom.newInfoFromDomain(room);
                infoViewRoomSchedule.setInfoRoom(infoRoom);

                final List lessons = room.getAssociatedLessons(academicInterval);
                final List infoLessons = new ArrayList(lessons.size());
                for (final Iterator iterator2 = lessons.iterator(); iterator2.hasNext();) {
                    final Lesson lesson = (Lesson) iterator2.next();
                    infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
                }
                infoViewRoomSchedule.setRoomLessons(infoLessons);
            }
        }

        return infoViewRoomScheduleList;
    }

}