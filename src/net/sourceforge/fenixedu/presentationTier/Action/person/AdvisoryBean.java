package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.Date;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

import org.apache.struts.util.MessageResources;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class AdvisoryBean {

    private static final MessageResources messages = MessageResources.getMessageResources("resources/GlobalResources");

    private static final Locale locale = new Locale("pt");

    final DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");

    private Integer idInternal;
    private Date created;
    private String subject;
    private Date expires;
    private String message;
    private String sender;
    public AdvisoryBean(int id, Attends attends, Interval responseWeek) {
        idInternal = new Integer(id);
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
    public Integer getIdInternal() {
        return idInternal;
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
