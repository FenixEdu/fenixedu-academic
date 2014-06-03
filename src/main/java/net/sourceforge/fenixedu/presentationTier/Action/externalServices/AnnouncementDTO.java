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
 * Creation Date: Jul 31, 2006,4:09:13 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementCategory;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
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
    private String photoUrl;
    private String campus;
    private String[] categories;
    private Boolean pressRelease;
    private Boolean sticky;
    private String priority;

    public AnnouncementDTO() {
    }

    public AnnouncementDTO(final Announcement announcement, final Locale language) {

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
        setId(announcement.getExternalId().toString());
        setPhotoUrl(announcement.getPhotoUrl());
        setCampus(announcement.getCampus());
        setCategoriesFromAnnouncement(announcement, language);
        setPressRelease(announcement.getPressRelease());
        setSticky(announcement.getSticky());
        setPriority(announcement.getPriority());
    }

    private String getFormattedDate(final DateTime dateTime) {
        return dateTime == null ? null : dateTime.toString(DATE_TIME_FORMAT);
    }

    private String getContentFrom(final MultiLanguageString multiLanguageString, final Locale language) {
        if (multiLanguageString == null) {
            return null;
        }
        return multiLanguageString.getContent(language);
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

    public String getPhotoUrl() {
        return this.photoUrl;
    }

    public void setPhotoUrl(String value) {
        this.photoUrl = value;
    }

    public String getCampus() {
        return this.campus;
    }

    public void setCampus(Space campus) {
        this.campus = campus == null ? "EXTERNAL" : campus.getName();
    }

    public String[] getCategories() {
        return this.categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    private void setCategoriesFromAnnouncement(Announcement announcement, final Locale language) {
        java.util.List<String> categories = new java.util.ArrayList<String>();

        for (AnnouncementCategory category : announcement.getCategories()) {
            if (category.getName().getContent(language) != null) {
                categories.add(category.getName().getContent(language));
            }
        }

        this.setCategories(categories.toArray(new String[0]));
    }

    public Boolean getPressRelease() {
        return this.pressRelease;
    }

    public void setPressRelease(Boolean value) {
        this.pressRelease = value;
    }

    public Boolean getSticky() {
        return sticky;
    }

    public void setSticky(Boolean sticky) {
        this.sticky = (sticky == null ? false : sticky);
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = (priority == null ? "-1" : priority.toString());
    }
}
