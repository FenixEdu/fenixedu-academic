/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 6/Mai/2003
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Factory.RoomSiteComponentBuilder;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Jo�o Mota
 * @author Fernanda Quit�rio
 * 
 * 
 */
public class RoomSiteComponentServiceByExecutionPeriodID {

    @Atomic
    public static Object run(ISiteComponent bodyComponent, RoomKey roomKey, Calendar someDay, String executionPeriodID)
            throws Exception {
        final Calendar day = findMonday(someDay);
        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodID);
        return (executionSemester != null) ? runService(bodyComponent, roomKey, day, executionSemester) : RoomSiteComponentService
                .run(bodyComponent, roomKey, day);
    }

    private static Calendar findMonday(final Calendar someDay) {
        final DateTime dateTime = new DateTime(someDay.getTimeInMillis());
        return dateTime.withField(DateTimeFieldType.dayOfWeek(), 1).toCalendar(null);
    }

    public static Object runService(ISiteComponent bodyComponent, RoomKey roomKey, Calendar day,
            ExecutionSemester executionSemester) throws Exception {
        SiteView siteView = null;

        Space room = SpaceUtils.findAllocatableSpaceForEducationByName(roomKey.getNomeSala());
        RoomSiteComponentBuilder componentBuilder = RoomSiteComponentBuilder.getInstance();
        bodyComponent = componentBuilder.getComponent(bodyComponent, day, room, executionSemester);

        siteView = new SiteView(bodyComponent);

        return siteView;
    }
}