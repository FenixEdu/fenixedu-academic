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
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

/**
 * @author Jo�o Mota
 * @author Fernanda Quit�rio
 * 
 * 
 */
public class RoomSiteComponentServiceByExecutionPeriodID extends Service {

    public static Object run(ISiteComponent bodyComponent, RoomKey roomKey, Calendar someDay, Integer executionPeriodID) throws Exception {
	final Calendar day = findMonday(someDay);
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
        return (executionPeriod != null) ? runService(bodyComponent, roomKey, day, executionPeriod) : RoomSiteComponentService.run(bodyComponent, roomKey, day);
    }

    private static Calendar findMonday(final Calendar someDay) {
	final DateTime dateTime = new DateTime(someDay.getTimeInMillis());
	return dateTime.withField(DateTimeFieldType.dayOfWeek(), 1).toCalendar(null);
    }

    public static Object runService(ISiteComponent bodyComponent, RoomKey roomKey, Calendar day,
            ExecutionPeriod executionPeriod) throws Exception {
        SiteView siteView = null;

        OldRoom room = OldRoom.findOldRoomByName(roomKey.getNomeSala());
        RoomSiteComponentBuilder componentBuilder = RoomSiteComponentBuilder.getInstance();
        bodyComponent = componentBuilder.getComponent(bodyComponent, day, room, executionPeriod);

        siteView = new SiteView(bodyComponent);

        return siteView;
    }
}