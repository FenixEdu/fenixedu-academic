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
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.Date;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.util.MessageResources;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class AdvisoryBean {

    private static final MessageResources messages = MessageResources.getMessageResources(Bundle.GLOBAL);

    private static final Locale locale = new Locale("pt");

    final DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");

    private Integer externalId;
    private Date created;
    private String subject;
    private Date expires;
    private String message;
    private String sender;

    public AdvisoryBean(int id, Attends attends, Interval responseWeek) {
        externalId = new Integer(id);
        created = new Date();
        expires = created;
        sender = messages.getMessage(locale, "weekly.work.load.advisory.from");
        final ExecutionCourse executionCourse = attends.getExecutionCourse();
        subject = messages.getMessage(locale, "weekly.work.load.advisory.subject", executionCourse.getNome());
        final String intervalString = fmt.print(responseWeek.getStart()) + " - " + fmt.print(responseWeek.getEnd());
        message = messages.getMessage(locale, "weekly.work.load.advisory.message", executionCourse.getNome(), intervalString);
    }

    public Date getCreated() {
        return created;
    }

    public Date getExpires() {
        return expires;
    }

    public Integer getExternalId() {
        return externalId;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }

}
