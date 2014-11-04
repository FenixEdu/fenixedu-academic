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
package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatterBuilder;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class DurationRenderer extends OutputRenderer {
    private static final String SHORT = ".short";
    private static final String enumerationBundle = "ENUMERATION_RESOURCES";
    private static final String CLASS_NAME = DurationFieldType.class.getName() + ".";

    private boolean printZeroAlways = true;
    private String includedFields;

    private class DurationRendererLayout extends Layout {
        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            StringBuilder result = new StringBuilder();
            if (object != null) {
                Duration duration = (Duration) object;
                PeriodFormatterBuilder periodFormatterBuilder = new PeriodFormatterBuilder();

                PeriodType periodType = PeriodType.forFields(getDurationFieldTypes());

                if (printZeroAlways) {
                    periodFormatterBuilder.printZeroAlways();
                }
                if (duration.getMillis() < 0) {
                    result.append("-");
                }

                MutablePeriod period = new MutablePeriod(Math.abs(duration.getMillis()), periodType);

                if (period.isSupported(DurationFieldType.years())) {
                    periodFormatterBuilder.appendYears().appendSuffix(
                            RenderUtils.getResourceString(enumerationBundle, CLASS_NAME + DurationFieldType.years().getName()
                                    + SHORT));
                }
                if (period.isSupported(DurationFieldType.months())) {
                    periodFormatterBuilder.appendMonths().appendSuffix(
                            RenderUtils.getResourceString(enumerationBundle, CLASS_NAME + DurationFieldType.months().getName()
                                    + SHORT));
                }
                if (period.isSupported(DurationFieldType.weeks())) {
                    periodFormatterBuilder.appendWeeks().appendSuffix(
                            RenderUtils.getResourceString(enumerationBundle, CLASS_NAME + DurationFieldType.weeks().getName()
                                    + SHORT));
                }
                if (period.isSupported(DurationFieldType.days())) {
                    periodFormatterBuilder.appendDays().appendSuffix(
                            RenderUtils.getResourceString(enumerationBundle, CLASS_NAME + DurationFieldType.days().getName()
                                    + SHORT));
                }
                if (period.isSupported(DurationFieldType.hours())) {
                    periodFormatterBuilder.appendHours().appendSuffix(
                            RenderUtils.getResourceString(enumerationBundle, CLASS_NAME + DurationFieldType.hours().getName()
                                    + SHORT));
                }
                if (period.isSupported(DurationFieldType.minutes())) {
                    periodFormatterBuilder
                            .minimumPrintedDigits(2)
                            .appendMinutes()
                            .appendSuffix(
                                    RenderUtils.getResourceString(enumerationBundle, CLASS_NAME
                                            + DurationFieldType.minutes().getName() + SHORT));
                }
                if (period.isSupported(DurationFieldType.seconds())) {
                    periodFormatterBuilder
                            .minimumPrintedDigits(2)
                            .appendSeconds()
                            .appendSuffix(
                                    RenderUtils.getResourceString(enumerationBundle, CLASS_NAME
                                            + DurationFieldType.seconds().getName() + SHORT));
                }
                if (period.isSupported(DurationFieldType.millis())) {
                    periodFormatterBuilder
                            .minimumPrintedDigits(2)
                            .appendMillis()
                            .appendSuffix(
                                    RenderUtils.getResourceString(enumerationBundle, CLASS_NAME
                                            + DurationFieldType.millis().getName() + SHORT));
                }
                result.append(periodFormatterBuilder.toFormatter().print(period));
            }
            return new HtmlText(result.toString());
        }

    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new DurationRendererLayout();
    }

    /*
     * Standard - years, months, weeks, days, hours, minutes, seconds, millis
     * YearMonthDayTime - years, months, days, hours, minutes, seconds, millis
     * YearMonthDay - years, months, days YearWeekDayTime - years, weeks, days,
     * hours, minutes, seconds, millis YearWeekDay - years, weeks, days
     * YearDayTime - years, days, hours, minutes, seconds, millis YearDay
     * -years, days, hours DayTime - days, hours, minutes, seconds, millis Time
     * -hours, minutes, seconds, millis plus one for each single type
     */
    private DurationFieldType[] getDurationFieldTypes() {
        List<DurationFieldType> result = new ArrayList<DurationFieldType>();
        if (getIncludedFields() == null) {
            result.add(DurationFieldType.hours());
            result.add(DurationFieldType.minutes());
        } else {
            PeriodType standard = PeriodType.standard();
            for (int index = 0; index < standard.size(); index++) {
                if (getIncludedFields().contains(standard.getFieldType(index).getName())) {
                    result.add(standard.getFieldType(index));
                }
            }
        }
        return result.toArray(new DurationFieldType[] {});
    }

    public boolean isPrintZeroAlways() {
        return printZeroAlways;
    }

    public void setPrintZeroAlways(boolean printZeroAlways) {
        this.printZeroAlways = printZeroAlways;
    }

    public String getIncludedFields() {
        return includedFields;
    }

    public void setIncludedFields(String includedFields) {
        this.includedFields = includedFields;
    }

}
