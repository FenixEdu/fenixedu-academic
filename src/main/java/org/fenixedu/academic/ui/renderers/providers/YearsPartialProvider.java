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
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class YearsPartialProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        List<Partial> result = new ArrayList<Partial>();
        ExecutionYear firstExecutionYear = ExecutionYear.readFirstExecutionYear();

        if (firstExecutionYear != null) {
            ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
            int firstYear = firstExecutionYear.getBeginDateYearMonthDay().getYear();
            int lastYear = currentExecutionYear.getEndDateYearMonthDay().getYear();
            while (firstYear <= lastYear) {
                result.add(new Partial(DateTimeFieldType.year(), firstYear));
                firstYear++;
            }
        }

        return result;
    }

    @Override
    public Converter getConverter() {
        return null;
    }
}