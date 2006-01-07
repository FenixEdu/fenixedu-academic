package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.Advisory;

/**
 * @author Nuno Nunes & Luis Cruz
 * 
 * /2003/08/26
 */

public class InfoAdvisory extends InfoObject {

    private String sender;

    private String subject;

    private String message;

    private Date created;

    private Date expires;

    private Boolean onlyShowOnce;

    public InfoAdvisory() {
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + getIdInternal() + "; ";
        result += "subject = " + this.subject + "; ";
        result += "sender = " + this.sender + "\n";
        result += "message = " + this.message + "\n";
        result += "created = " + this.created + "\n";
        result += "expires = " + this.expires + "\n";
        result += "onlyShowOnce = " + this.onlyShowOnce + "]\n";

        return result;
    }

    /**
     * @return
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @return
     */
    public Date getExpires() {
        return expires;
    }

    /**
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return
     */
    public Boolean getOnlyShowOnce() {
        return onlyShowOnce;
    }

    /**
     * @return
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param date
     */
    public void setCreated(Date date) {
        created = date;
    }

    /**
     * @param date
     */
    public void setExpires(Date date) {
        expires = date;
    }

    /**
     * @param string
     */
    public void setMessage(String string) {
        message = string;
    }

    /**
     * @param boolean1
     */
    public void setOnlyShowOnce(Boolean boolean1) {
        onlyShowOnce = boolean1;
    }

    /**
     * @param string
     */
    public void setSubject(String string) {
        subject = string;
    }

    /**
     * @return
     */
    public String getSender() {
        return sender;
    }

    /**
     * @param string
     */
    public void setSender(String string) {
        sender = string;
    }

    public void copyFromDomain(Advisory advisory) {
        super.copyFromDomain(advisory);
        if (advisory != null) {
            setCreated(advisory.getCreated());
            setExpires(advisory.getExpires());
            setMessage(advisory.getMessage());            
            setSender(advisory.getSender());
            setSubject(advisory.getSubject());
        }
    }

    public static InfoAdvisory newInfoFromDomain(Advisory advisory) {
        InfoAdvisory infoAdvisory = null;
        if (advisory != null) {
            infoAdvisory = new InfoAdvisory();
            infoAdvisory.copyFromDomain(advisory);
        }
        return infoAdvisory;
    }

}