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
package org.fenixedu.academic.ui.renderers;

import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.Summary;
import org.fenixedu.academic.dto.SummariesManagementBean.SummaryType;
import org.fenixedu.academic.util.DateFormatUtil;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class SummaryPlainRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                if (object == null) {
                    return new HtmlText();
                }

                Summary summary = (Summary) object;
                StringBuilder builder = new StringBuilder();
                Lesson lesson = null;

                builder.append(summary.getSummaryDateYearMonthDay().getDayOfMonth()).append("/");
                builder.append(summary.getSummaryDateYearMonthDay().getMonthOfYear()).append("/");
                builder.append(summary.getSummaryDateYearMonthDay().getYear());
                builder.append(" - ").append(RenderUtils.getResourceString("DEFAULT", "label.lesson") + ": ");

                if (summary.isExtraSummary()) {

                    builder.append(RenderUtils.getEnumString(SummaryType.EXTRA_SUMMARY, null)).append(" ");
                    builder.append(" (").append(summary.getSummaryHourHourMinuteSecond().getHour());
                    builder.append(":").append(summary.getSummaryHourHourMinuteSecond().getMinuteOfHour()).append(") ");

                } else {

                    lesson = summary.getLesson();
                    if (lesson != null) {

                        builder.append(lesson.getDiaSemana().toString()).append(" (");
                        builder.append(DateFormatUtil.format("HH:mm", lesson.getInicio().getTime()));
                        builder.append("-").append(DateFormatUtil.format("HH:mm", lesson.getFim().getTime()));
                        builder.append(") ");
                    }
                }
                if (lesson != null && lesson.hasSala()) {
                    builder.append(lesson.getSala().getName());
                }

                return new HtmlText(builder.toString());
            }
        };
    }

}