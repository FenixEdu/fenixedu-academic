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
package org.fenixedu.academic.ui.faces.components.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.struts.util.MessageResources;
import org.fenixedu.academic.domain.Exam;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Project;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.WrittenTest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.DateFormatUtil;

public class CalendarLink {
    private Calendar objectOccurrence;
    private String objectLinkLabel;
    private Map<String, String> linkParameters = new HashMap<String, String>();

    private boolean asLink;

    public CalendarLink(boolean asLink) {
        setAsLink(asLink);
    }

    public CalendarLink() {
        this(true);
    }

    public CalendarLink(final ExecutionCourse executionCourse, final WrittenEvaluation writtenEvaluation, final Locale locale) {
        setObjectOccurrence(writtenEvaluation.getDay());
        setObjectLinkLabel(constructCalendarPresentation(executionCourse, writtenEvaluation, locale));
    }

    public CalendarLink(final ExecutionCourse executionCourse, final Project project, final Date date, final String tail,
            final Locale locale) {
        setObjectOccurrence(date);
        setObjectLinkLabel(constructCalendarPresentation(executionCourse, project, date, tail, locale));
    }

    public void setObjectOccurrence(Calendar objectOccurrence) {
        this.objectOccurrence = objectOccurrence;
    }

    public void setObjectOccurrence(Date objectOccurrence) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(objectOccurrence);
        this.objectOccurrence = calendar;
    }

    public Calendar getObjectOccurrence() {
        return this.objectOccurrence;
    }

    public void setObjectLinkLabel(String objectLinkLabel) {
        this.objectLinkLabel = objectLinkLabel;
    }

    public String getObjectLinkLabel() {
        return this.objectLinkLabel;
    }

    public void setLinkParameters(Map<String, String> linkParameters) {
        this.linkParameters = linkParameters;
    }

    public String giveLink(String editLinkPage) {
        final StringBuilder linkParameters = new StringBuilder();
        linkParameters.append(editLinkPage);

        if (this.linkParameters != null && !this.linkParameters.isEmpty()) {
            linkParameters.append(editLinkPage.indexOf('?') > 0 ? '&' : '?');
            for (final Iterator<Entry<String, String>> iterator = this.linkParameters.entrySet().iterator(); iterator.hasNext();) {
                final Entry<String, String> entry = iterator.next();
                linkParameters.append(entry.getKey());
                linkParameters.append('=');
                linkParameters.append(entry.getValue());

                if (iterator.hasNext()) {
                    linkParameters.append('&');
                }
            }
        }
        return linkParameters.toString();
    }

    public void addLinkParameter(final String key, final String value) {
        linkParameters.put(key, value);
    }

    private static final MessageResources messages = MessageResources.getMessageResources(Bundle.DEGREE);

    private String constructCalendarPresentation(final ExecutionCourse executionCourse,
            final WrittenEvaluation writtenEvaluation, final Locale locale) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (writtenEvaluation instanceof WrittenTest) {
            stringBuilder.append(messages.getMessage(locale, "label.evaluation.shortname.test"));
        } else if (writtenEvaluation instanceof Exam) {
            stringBuilder.append(messages.getMessage(locale, "label.evaluation.shortname.exam"));
        }
        stringBuilder.append(" ");
        stringBuilder.append(executionCourse.getSigla());
        stringBuilder.append(" (");
        stringBuilder.append(DateFormatUtil.format("HH:mm", writtenEvaluation.getBeginningDate()));
        stringBuilder.append("-");
        stringBuilder.append(DateFormatUtil.format("HH:mm", writtenEvaluation.getEndDate()));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private String constructCalendarPresentation(final ExecutionCourse executionCourse, final Project project, final Date time,
            final String tail, final Locale locale) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(messages.getMessage(locale, "label.evaluation.shortname.project"));
        stringBuilder.append(" ");
        stringBuilder.append(executionCourse.getSigla());
        stringBuilder.append(" (");
        stringBuilder.append(DateFormatUtil.format("HH:mm", time));
        stringBuilder.append(") ");
        stringBuilder.append(tail);
        return stringBuilder.toString();
    }

    public boolean isAsLink() {
        return asLink;
    }

    public void setAsLink(boolean asLink) {
        this.asLink = asLink;
    }

}
