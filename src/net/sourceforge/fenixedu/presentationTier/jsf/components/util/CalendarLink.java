package net.sourceforge.fenixedu.presentationTier.jsf.components.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProject;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.apache.struts.util.MessageResources;

public class CalendarLink {
    private Calendar objectOccurrence;
    private String objectLinkLabel;
    private Map<String,String> linkParameters = new HashMap<String, String>();

    public CalendarLink() {
    }

    public CalendarLink(final IExecutionCourse executionCourse, final IWrittenEvaluation writtenEvaluation, final Locale locale) {
        setObjectOccurrence(writtenEvaluation.getDay());
        setObjectLinkLabel(constructCalendarPresentation(executionCourse, writtenEvaluation, locale));
    }

	public CalendarLink(final IExecutionCourse executionCourse, final IProject project, final Date date, final String tail, final Locale locale) {
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
    
    public void setLinkParameters(Map<String,String> linkParameters) {
        this.linkParameters = linkParameters;
    }

    public String giveLink(String editLinkPage) {
        final StringBuilder linkParameters = new StringBuilder();
        linkParameters.append(editLinkPage);

        if (this.linkParameters != null && !this.linkParameters.isEmpty()) {
            linkParameters.append('?');
            for (final Iterator<Entry<String, String>> iterator = this.linkParameters.entrySet().iterator();
                    iterator.hasNext(); ) {
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

	private static final MessageResources messages = MessageResources.getMessageResources("ServidorApresentacao/PublicDegreeInformation");

    private String constructCalendarPresentation(final IExecutionCourse executionCourse, final IWrittenEvaluation writtenEvaluation, final Locale locale) {
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
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private String constructCalendarPresentation(final IExecutionCourse executionCourse, final IProject project, final Date time, final String tail, final Locale locale) {
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

}
