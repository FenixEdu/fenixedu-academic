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
package net.sourceforge.fenixedu.domain.phd.thesis.meeting;

import java.io.Serializable;

import org.joda.time.DateTime;

public class PhdEditMeetingBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DateTime scheduledDate;
    private String scheduledPlace;

    public PhdEditMeetingBean(final PhdMeeting phdMeeting) {
        setScheduledDate(phdMeeting.getMeetingDate());
        setScheduledPlace(phdMeeting.getMeetingPlace());
    }

    public DateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(DateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getScheduledPlace() {
        return scheduledPlace;
    }

    public void setScheduledPlace(String scheduledPlace) {
        this.scheduledPlace = scheduledPlace;
    }

}
