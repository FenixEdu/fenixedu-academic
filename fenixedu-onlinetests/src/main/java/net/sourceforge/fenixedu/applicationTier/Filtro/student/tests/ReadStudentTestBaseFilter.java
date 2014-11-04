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
 * Created on Dec 14, 2003 by jpvl
 * 
 */
package org.fenixedu.academic.service.filter.student.tests;

import java.util.Calendar;

import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.domain.onlineTests.DistributedTest;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author jpvl
 */
public abstract class ReadStudentTestBaseFilter {

    final public void execute(String testId) throws NotAuthorizedException {

        DistributedTest distributedTest = FenixFramework.getDomainObject(testId);

        if (distributedTest != null) {
            Calendar now = Calendar.getInstance();

            Calendar beginDate = distributedTest.getBeginDate();
            Calendar beginHour = distributedTest.getBeginHour();
            getFullCalendar(beginDate, beginHour);

            Calendar endDate = distributedTest.getEndDate();
            Calendar endHour = distributedTest.getEndHour();
            getFullCalendar(endDate, endHour);

            if (!canReadTest(now, beginDate, endDate)) {
                throw new NotAuthorizedException();
            }

        }

    }

    abstract protected boolean canReadTest(Calendar now, Calendar beginDate, Calendar endDate);

    private void getFullCalendar(Calendar beginDate, Calendar beginHour) {
        beginDate.set(Calendar.HOUR_OF_DAY, beginHour.get(Calendar.HOUR_OF_DAY));
        beginDate.set(Calendar.MINUTE, beginHour.get(Calendar.MINUTE));
        beginDate.set(Calendar.SECOND, 0);
        beginDate.set(Calendar.MILLISECOND, 0);
    }

}