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
/*
 * Created on 3/Dez/2003
 *
 */
package org.fenixedu.academic.util;

import java.util.Calendar;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author Ana e Ricardo
 * @author Barbosa e Pica
 * 
 */
public class CalendarUtil {

    public CalendarUtil() {
    }

    public static boolean intersectDates(Calendar startDate1, Calendar endDate1, Calendar startDate2, Calendar endDate2) {
        String startDate1String = date2string(startDate1);
        String endDate1String = date2string(endDate1);
        String startDate2String = date2string(startDate2);
        String endDate2String = date2string(endDate2);

        boolean doesNotIntersect =
                (endDate2String.compareTo(startDate1String) < 0) || (startDate2String.compareTo(endDate1String) > 0);

        return !doesNotIntersect;
    }

    public static String date2string(Calendar date) {
        return DateFormatUtils.format(date.getTime(), "yyyyMMdd");
    }

}