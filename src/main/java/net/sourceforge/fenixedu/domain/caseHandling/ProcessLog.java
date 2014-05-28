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
package net.sourceforge.fenixedu.domain.caseHandling;

import java.util.Comparator;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class ProcessLog extends ProcessLog_Base {

    public static Comparator<ProcessLog> COMPARATOR_BY_WHEN = new Comparator<ProcessLog>() {
        @Override
        public int compare(ProcessLog leftProcessLog, ProcessLog rightProcessLog) {
            int comparationResult = leftProcessLog.getWhenDateTime().compareTo(rightProcessLog.getWhenDateTime());
            return (comparationResult == 0) ? leftProcessLog.getExternalId().compareTo(rightProcessLog.getExternalId()) : comparationResult;
        }
    };

    public ProcessLog(Process process, User userView, Activity<?> activity) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setProcess(process);

        setUserName(userView != null ? userView.getUsername() : "PUBLICO");
        setActivity(activity.getClass().getName());
        setWhenDateTime(new DateTime());
    }

    public boolean isFor(final Class<? extends Activity> clazz) {
        return getActivity().equals(clazz.getName());
    }

    @Deprecated
    public java.util.Date getWhen() {
        org.joda.time.DateTime dt = getWhenDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setWhen(java.util.Date date) {
        if (date == null) {
            setWhenDateTime(null);
        } else {
            setWhenDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasWhenDateTime() {
        return getWhenDateTime() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasActivity() {
        return getActivity() != null;
    }

    @Deprecated
    public boolean hasProcess() {
        return getProcess() != null;
    }

    @Deprecated
    public boolean hasUserName() {
        return getUserName() != null;
    }

}
