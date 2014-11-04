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
 * Created on 22/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.comparators;

import java.util.Calendar;
import java.util.Comparator;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 *         22/Jul/2003 fenix-head
 *         net.sourceforge.fenixedu.dataTransferObject.comparators
 * 
 */
public class CalendarHourComparator implements Comparator {

    /**
     *  
     */
    public CalendarHourComparator() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Object arg0, Object arg1) {
        Calendar calendar0 = (Calendar) arg0;
        Calendar calendar1 = (Calendar) arg1;
        if (calendar0.get(Calendar.HOUR_OF_DAY) < calendar1.get(Calendar.HOUR_OF_DAY)) {
            return -1;
        }
        if (calendar0.get(Calendar.HOUR_OF_DAY) > calendar1.get(Calendar.HOUR_OF_DAY)) {
            return 1;
        }
        if (calendar0.get(Calendar.MINUTE) < calendar1.get(Calendar.MINUTE)) {
            return -1;
        }
        if (calendar0.get(Calendar.MINUTE) > calendar1.get(Calendar.MINUTE)) {
            return 1;
        }

        return 0;
    }

}