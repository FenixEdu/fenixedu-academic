package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ana e Ricardo
 */
public class ReadAvailableRoomsForExam {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static List<InfoRoom> run(YearMonthDay startDate, YearMonthDay endDate, HourMinuteSecond startTimeHMS,
            HourMinuteSecond endTimeHMS, DiaSemana dayOfWeek, Integer normalCapacity, FrequencyType frequency, Boolean withLabs) {

        final Collection<AllocatableSpace> rooms = new HashSet<AllocatableSpace>();
        final List<AllocatableSpace> roomsToCheck = new ArrayList<AllocatableSpace>();

        if (normalCapacity != null) {
            roomsToCheck.addAll(AllocatableSpace.findActiveAllocatableSpacesForEducationWithNormalCapacity(normalCapacity));

        } else if (withLabs.booleanValue()) {
            roomsToCheck.addAll(AllocatableSpace.getAllActiveAllocatableSpacesForEducation());

        } else {
            roomsToCheck.addAll(AllocatableSpace.getAllActiveAllocatableSpacesExceptLaboratoriesForEducation());
        }

        for (AllocatableSpace allocatableSpace : roomsToCheck) {
            if (allocatableSpace.isFree(startDate, endDate, startTimeHMS, endTimeHMS, dayOfWeek, frequency, Boolean.TRUE,
                    Boolean.TRUE)) {
                rooms.add(allocatableSpace);
            }
        }

        final List<InfoRoom> availableInfoRooms = new ArrayList<InfoRoom>();
        for (final AllocatableSpace room : rooms) {
            if (room.containsIdentification()) {
                availableInfoRooms.add(InfoRoom.newInfoFromDomain(room));
            }
        }

        return availableInfoRooms;
    }
}