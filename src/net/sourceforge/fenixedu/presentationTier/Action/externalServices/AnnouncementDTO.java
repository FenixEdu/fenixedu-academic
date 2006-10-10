/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 31, 2006,4:09:13 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.externalServices;


/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jul 31, 2006,4:09:13 PM
 * 
 */
public class AnnouncementDTO {

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

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Returns the author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author
     *            The author to set.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return Returns the authorEmail.
     */
    public String getAuthorEmail() {
        return authorEmail;
    }

    /**
     * @param authorEmail
     *            The authorEmail to set.
     */
    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    /**
     * @return Returns the body.
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body
     *            The body to set.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return Returns the creationDate.
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate
     *            The creationDate to set.
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return Returns the excerpt.
     */
    public String getExcerpt() {
        return excerpt;
    }

    /**
     * @param excerpt
     *            The excerpt to set.
     */
    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    /**
     * @return Returns the keywords.
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * @param keywords
     *            The keywords to set.
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * @return Returns the lastModification.
     */
    public String getLastModification() {
        return lastModification;
    }

    /**
     * @param lastModification
     *            The lastModification to set.
     */
    public void setLastModification(String lastModification) {
        this.lastModification = lastModification;
    }

    /**
     * @return Returns the place.
     */
    public String getPlace() {
        return place;
    }

    /**
     * @param place
     *            The place to set.
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * @return Returns the publicationBegin.
     */
    public String getPublicationBegin() {
        return publicationBegin;
    }

    /**
     * @param publicationBegin
     *            The publicationBegin to set.
     */
    public void setPublicationBegin(String publicationBegin) {
        this.publicationBegin = publicationBegin;
    }

    /**
     * @return Returns the publicationEnd.
     */
    public String getPublicationEnd() {
        return publicationEnd;
    }

    /**
     * @param publicationEnd
     *            The publicationEnd to set.
     */
    public void setPublicationEnd(String publicationEnd) {
        this.publicationEnd = publicationEnd;
    }

    /**
     * @return Returns the referedSubjectBegin.
     */
    public String getReferedSubjectBegin() {
        return referedSubjectBegin;
    }

    /**
     * @param referedSubjectBegin
     *            The referedSubjectBegin to set.
     */
    public void setReferedSubjectBegin(String referedSubjectBegin) {
        this.referedSubjectBegin = referedSubjectBegin;
    }

    /**
     * @return Returns the referedSubjectEnd.
     */
    public String getReferedSubjectEnd() {
        return referedSubjectEnd;
    }

    /**
     * @param referedSubjectEnd
     *            The referedSubjectEnd to set.
     */
    public void setReferedSubjectEnd(String referedSubjectEnd) {
        this.referedSubjectEnd = referedSubjectEnd;
    }

    /**
     * @return Returns the subject.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject
     *            The subject to set.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return Returns the visible.
     */
    public String getVisible() {
        return visible;
    }

    /**
     * @param visible
     *            The visible to set.
     */
    public void setVisible(String visible) {
        this.visible = visible;
    }
}
