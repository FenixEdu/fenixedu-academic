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
 * Created on 10/Set/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.student.Registration;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

/**
 * @author Susana Fernandes
 */
public class StudentTestLog extends StudentTestLog_Base {

    public StudentTestLog(DistributedTest distributedTest, Registration student, String event) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setDistributedTest(distributedTest);
        setStudent(student);
        DateTime now = new DateTime();
        setDateDateTime(now);
        setEvent(event);
        setChecksum(getChecksum(distributedTest, student, now));
    }

    public static String getChecksum(DistributedTest distributedTest, Registration student, DateTime dateTime) {
        StringBuilder checksumCode = new StringBuilder();
        checksumCode.append(student.getNumber());
        checksumCode.append(distributedTest.getExternalId());
        final DateTime thisDateTime =
                new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), dateTime.getHourOfDay(),
                        dateTime.getMinuteOfHour(), dateTime.getSecondOfMinute(), 0);
        checksumCode.append(thisDateTime.getMillis());
        checksumCode.append(Bennu.getInstance().getTestChecksumsSet().iterator().next().getChecksumCode());
        return (Hashing.sha1().hashString(checksumCode.toString(), Charsets.UTF_8)).toString();
    }

    public void delete() {
        setStudent(null);
        setDistributedTest(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public String getDateFormatted() {
        return getDateDateTime().toString("dd/MM/yyyy HH:mm:ss");
    }

    public List<String> getEventList() {
        String[] eventTokens = getEvent().split(";");
        List<String> eventList = new ArrayList<String>();
        for (String eventToken : eventTokens) {
            eventList.add(eventToken);
        }
        return eventList;
    }

    @Deprecated
    public java.util.Date getDate() {
        org.joda.time.DateTime dt = getDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setDate(java.util.Date date) {
        if (date == null) {
            setDateDateTime(null);
        } else {
            setDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasChecksum() {
        return getChecksum() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDistributedTest() {
        return getDistributedTest() != null;
    }

    @Deprecated
    public boolean hasDateDateTime() {
        return getDateDateTime() != null;
    }

    @Deprecated
    public boolean hasEvent() {
        return getEvent() != null;
    }

}
