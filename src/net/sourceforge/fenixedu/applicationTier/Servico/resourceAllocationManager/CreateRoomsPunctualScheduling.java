package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.DateTimeFieldType;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateRoomsPunctualScheduling extends FenixService {

	@Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
	@Service
	public static void run(RoomsPunctualSchedulingBean bean) throws FenixServiceException {

		List<AllocatableSpace> selectedRooms = bean.getRooms();
		if (!selectedRooms.isEmpty()) {

			HourMinuteSecond beginTime =
					new HourMinuteSecond(bean.getBeginTime().get(DateTimeFieldType.hourOfDay()), bean.getBeginTime().get(
							DateTimeFieldType.minuteOfHour()), 0);
			HourMinuteSecond endTime =
					new HourMinuteSecond(bean.getEndTime().get(DateTimeFieldType.hourOfDay()), bean.getEndTime().get(
							DateTimeFieldType.minuteOfHour()), 0);

			final GenericEvent event =
					new GenericEvent(bean.getSmallDescription(), bean.getCompleteDescription(), selectedRooms, bean.getBegin(),
							bean.getEnd(), beginTime, endTime, bean.getFrequency(), bean.getRoomsReserveRequest(),
							bean.getMarkSaturday(), bean.getMarkSunday());
			final String emailsTo = bean.getEmailsTo();
			if (!emailsTo.trim().isEmpty()) {
				GOPSendMessageService.sendMessage(Collections.EMPTY_LIST, emailsTo, bean.getSmallDescription().getContent(), bean
						.getCompleteDescription().getContent());
			}
		}
	}
}