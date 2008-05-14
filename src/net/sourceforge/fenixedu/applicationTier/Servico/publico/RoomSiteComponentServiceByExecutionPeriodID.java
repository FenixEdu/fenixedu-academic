/*
 * Created on 6/Mai/2003
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.RoomSiteComponentBuilder;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

/**
 * @author Jo�o Mota
 * @author Fernanda Quit�rio
 * 
 * 
 */
public class RoomSiteComponentServiceByExecutionPeriodID extends Service {

    public static Object run(ISiteComponent bodyComponent, RoomKey roomKey, Calendar someDay, Integer executionPeriodID)
	    throws Exception {
	final Calendar day = findMonday(someDay);
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);
	return (executionSemester != null) ? runService(bodyComponent, roomKey, day, executionSemester)
		: RoomSiteComponentService.run(bodyComponent, roomKey, day);
    }

    private static Calendar findMonday(final Calendar someDay) {
	final DateTime dateTime = new DateTime(someDay.getTimeInMillis());
	return dateTime.withField(DateTimeFieldType.dayOfWeek(), 1).toCalendar(null);
    }

    public static Object runService(ISiteComponent bodyComponent, RoomKey roomKey, Calendar day,
	    ExecutionSemester executionSemester) throws Exception {
	SiteView siteView = null;

	AllocatableSpace room = AllocatableSpace.findAllocatableSpaceForEducationByName(roomKey.getNomeSala());
	RoomSiteComponentBuilder componentBuilder = RoomSiteComponentBuilder.getInstance();
	bodyComponent = componentBuilder.getComponent(bodyComponent, day, room, executionSemester);

	siteView = new SiteView(bodyComponent);

	return siteView;
    }
}