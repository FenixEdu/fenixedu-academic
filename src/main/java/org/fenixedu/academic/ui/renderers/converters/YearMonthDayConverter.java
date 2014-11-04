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
package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;

public class YearMonthDayConverter extends BiDirectionalConverter {

    @Override
    public Object convert(Class type, Object value) {

        if (value == null) {
            return null;
        }

        String valueString = (String) value;
        int year = Integer.parseInt(valueString.substring(0, 4));
        int month = Integer.parseInt(valueString.substring(5, 7));
        int day = Integer.parseInt(valueString.substring(8, 10));
        if (year == 0 || month == 0 || day == 0) {
            return null;
        }

        return new YearMonthDay(year, month, day);
    }

    @Override
    public String deserialize(Object object) {
        YearMonthDay date = (YearMonthDay) object;
        if (date != null) {
            return date.toString("yyyy-MM-dd");
        }
        return "";
    }
}
