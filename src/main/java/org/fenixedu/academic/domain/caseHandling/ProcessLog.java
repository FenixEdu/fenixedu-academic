/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.caseHandling;

import java.util.Comparator;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
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

    public String getUserPresentationName() {
        String userName = getUserName();
        User user = User.findByUsername(userName);
        return user == null ? userName : user.getPerson().getPresentationName();
    }

    public String getActivityId() {
        String activity = getActivity();
        String errorResult = '!' + activity + '!';
        String activityDescription = BundleUtil.getString(Bundle.CASE_HANDLEING, activity);

        if (activityDescription.equals(errorResult)) {
            String activityType = activity.substring(activity.lastIndexOf(".") + 1, activity.lastIndexOf("$"));
            String activitySpecialization = activity.substring(activity.lastIndexOf("$") + 1);

            activityDescription = activityType + " - " + activitySpecialization;
        }

        return activityDescription;
    }

}
