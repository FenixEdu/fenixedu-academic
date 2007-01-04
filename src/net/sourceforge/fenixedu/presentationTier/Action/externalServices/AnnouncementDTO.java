/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 31, 2006,4:09:13 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.util.MultiLanguageString;


/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jul 31, 2006,4:09:13 PM
 * 
 */
public class AnnouncementDTO {

    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";

    private String creationDate;
    private String referedSubjectBegin;
    private String referedSubjectEnd;
    private String publicationBegin;
    private String publicationEnd;
    private String lastModification;
    private String subject;
    private String keywords;
    private String body;
    private String excerpt;
    private String author;
    private String authorEmail;
    private String place;
    private String visible;
    private String id;
    
    public AnnouncementDTO() {
    }

    public AnnouncementDTO(final Announcement announcement, final Language language) {
	
	setCreationDate(getFormattedDate(announcement.getCreationDate()));
	setLastModification(getFormattedDate(announcement.getLastModification()));

	setReferedSubjectBegin(getFormattedDate(announcement.getReferedSubjectBegin()));
	setReferedSubjectEnd(getFormattedDate(announcement.getReferedSubjectEnd()));
	setPublicationBegin(getFormattedDate(announcement.getPublicationBegin()));
	setPublicationEnd(getFormattedDate(announcement.getPublicationEnd()));

	setAuthor(announcement.getAuthor());
	setAuthorEmail(announcement.getAuthorEmail());

	setSubject(getContentFrom(announcement.getSubject(), language));
	setBody(getContentFrom(announcement.getBody(), language));
	setExcerpt(getContentFrom(announcement.getExcerpt(), language));
	setKeywords(getContentFrom(announcement.getKeywords(), language));

	setPlace(announcement.getPlace());

	setVisible(announcement.getVisible().toString());
	setId(announcement.getIdInternal().toString());
    }
    
    private String getFormattedDate(final DateTime dateTime) {
	return dateTime == null ? null : dateTime.toString(DATE_TIME_FORMAT);
    }
    
    private String getContentFrom(final MultiLanguageString multiLanguageString, final Language language) {
	if (multiLanguageString == null) { return null;	}
	return multiLanguageString.hasLanguage(language) ? multiLanguageString.getContent(language) : multiLanguageString.getContent();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getLastModification() {
        return lastModification;
    }

    public void setLastModification(String lastModification) {
        this.lastModification = lastModification;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPublicationBegin() {
        return publicationBegin;
    }

    public void setPublicationBegin(String publicationBegin) {
        this.publicationBegin = publicationBegin;
    }

    public String getPublicationEnd() {
        return publicationEnd;
    }

    public void setPublicationEnd(String publicationEnd) {
        this.publicationEnd = publicationEnd;
    }

    public String getReferedSubjectBegin() {
        return referedSubjectBegin;
    }

    public void setReferedSubjectBegin(String referedSubjectBegin) {
        this.referedSubjectBegin = referedSubjectBegin;
    }

    public String getReferedSubjectEnd() {
        return referedSubjectEnd;
    }

    public void setReferedSubjectEnd(String referedSubjectEnd) {
        this.referedSubjectEnd = referedSubjectEnd;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }
}
