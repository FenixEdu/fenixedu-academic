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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.ChangeTutorshipByEntryYearBean.ChangeTutorshipBean;
import net.sourceforge.fenixedu.domain.Tutorship;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.Period;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class MonthYearsProviderTutorshipManagement implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        List<String> result = new ArrayList<String>();

        ChangeTutorshipBean tutorshipBean = (ChangeTutorshipBean) source;

        Partial startMonthYear = tutorshipBean.getTutorship().getStartDate();
        startMonthYear = startMonthYear.plus(Period.years(2));

        Partial endMonthYear = startMonthYear.plus(Period.years(Tutorship.TUTORSHIP_MAX_PERIOD));

        while (startMonthYear.compareTo(endMonthYear) < 0) {
            String line =
                    tutorshipBean.generateMonthYearOption(startMonthYear.get(DateTimeFieldType.monthOfYear()),
                            startMonthYear.get(DateTimeFieldType.year()));
            result.add(line);

            startMonthYear = startMonthYear.plus(Period.months(1));
        }

        return result;
    }

    @Override
    public Converter getConverter() {
        return null;
    }
}
