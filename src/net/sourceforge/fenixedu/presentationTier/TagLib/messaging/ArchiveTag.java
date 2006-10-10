/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 27, 2006,4:14:16 PM
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto.AnnouncementArchive;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto.MonthAnnouncementArchiveEntry;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto.YearAnnouncementArchiveEntry;
import net.sourceforge.fenixedu.util.Mes;

import org.apache.struts.taglib.TagUtils;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jul 27, 2006,4:14:16 PM
 * 
 */
public class ArchiveTag extends TagSupport {

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
        if (value == null)
            throw new JspTagException("Could not find the archive object (name,property,scope)=" + "("
                    + name + "," + "property" + "," + scope + ")");
        if (!(value instanceof AnnouncementArchive)) {
            throw new JspTagException("The specified object is not of the correct type. Is "
                    + value.getClass().getName() + " should be " + AnnouncementArchive.class.getName());
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
        StringBuffer buffer = new StringBuffer();

        Collection<Integer> availableYears = new ArrayList<Integer>();
        for (YearAnnouncementArchiveEntry year : archive.getEntries().values()) {
            buffer.append("<p><span>").append(year.getYear()).append("</span> ");
            buffer.append(this.renderYear(archive.getEntries().get(year.getYear())));
            buffer.append("</p>");
        }

        return buffer.toString();
    }

    private String renderYear(YearAnnouncementArchiveEntry year) {
        StringBuffer buffer = new StringBuffer();

        boolean first = true;
        for (int i = year.getFirstPostMonth(); i <= 12; i++) {
            MonthAnnouncementArchiveEntry month = year.getEntries().get(i);
            
            if (month != null && !first) {
                buffer.append(", ");
            }
            if (month != null && this.getTargetUrl() != null) {
                buffer.append("<a href=\"").append(this.getTargetUrl());
                buffer.append("selectedYear=").append(year.getYear());
                buffer.append("&selectedMonth=").append(month.getMonth());
                buffer.append("\">");
            }
            if (month != null) {
                buffer.append(new Mes(i).toAbbreviationString());
                buffer.append(" (");
                buffer.append(month.getAnnouncementCount());
                buffer.append(")");
                first=false;
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
