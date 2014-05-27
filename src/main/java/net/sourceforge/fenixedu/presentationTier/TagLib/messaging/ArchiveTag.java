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
 * Author : Goncalo Luiz
 * Creation Date: Jul 27, 2006,4:14:16 PM
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.messaging;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto.AnnouncementArchive;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto.MonthAnnouncementArchiveEntry;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto.YearAnnouncementArchiveEntry;

import org.apache.struts.taglib.TagUtils;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.LocalDate;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jul 27, 2006,4:14:16 PM
 * 
 */
public class ArchiveTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private String property;
    private String name;
    private String scope;
    private String locale;
    private String bundle;
    private String targetUrl;

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public int doStartTag() throws JspException {

        final Object value = TagUtils.getInstance().lookup(pageContext, name, property, scope);
        if (value == null) {
            throw new JspTagException("Could not find the archive object (name,property,scope)=" + "(" + name + "," + "property"
                    + "," + scope + ")");
        }
        if (!(value instanceof AnnouncementArchive)) {
            throw new JspTagException("The specified object is not of the correct type. Is " + value.getClass().getName()
                    + " should be " + AnnouncementArchive.class.getName());
        } else {

            try {
                pageContext.getOut().write(this.renderArchive(value));

            } catch (IOException e) {
                throw new JspTagException("IO Error: " + e.getMessage());
            }
        }
        return EVAL_BODY_INCLUDE;
    }

    private String renderArchive(Object value) {
        AnnouncementArchive archive = (AnnouncementArchive) value;
        StringBuilder buffer = new StringBuilder();

        for (YearAnnouncementArchiveEntry year : archive.getEntries().values()) {
            buffer.append("<p><span>").append(year.getYear()).append("</span> ");
            buffer.append(this.renderYear(archive.getEntries().get(year.getYear())));
            buffer.append("</p>");
        }

        return buffer.toString();
    }

    private String renderYear(YearAnnouncementArchiveEntry year) {
        StringBuilder buffer = new StringBuilder();

        boolean first = true;
        for (int i = year.getFirstPostMonth(); i <= 12; i++) {
            MonthAnnouncementArchiveEntry month = year.getEntries().get(i);

            if (month != null && !first) {
                buffer.append(", ");
            }
            if (month != null && this.getTargetUrl() != null) {
                buffer.append("<a href=\"").append(this.getTargetUrl());
                buffer.append("selectedYear=").append(year.getYear());
                buffer.append("&amp;selectedMonth=").append(month.getMonth());
                buffer.append("\">");
            }
            if (month != null) {
                Locale locale = I18N.getLocale();
                LocalDate localDate = new LocalDate();
                localDate = localDate.withMonthOfYear(month.getMonth());

                buffer.append(localDate.toString("MMM.", locale));
                buffer.append(" (");
                buffer.append(month.getAnnouncementCount());
                buffer.append(")");
                first = false;
            }
            if (month != null && this.getTargetUrl() != null) {
                buffer.append("</a>");
            }
        }
        return buffer.toString();
    }

    @Override
    public void release() {
        property = null;
        name = null;
        locale = null;
        bundle = null;
        super.release();
    }

}
