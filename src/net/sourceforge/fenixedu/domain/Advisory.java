package net.sourceforge.fenixedu.domain;

import java.util.Date;

/**
 * @author Nuno Nunes & Luis Cruz
 * 
 * /2003/08/26
 */
public class Advisory extends Advisory_Base implements IAdvisory {

    private String sender;

    private String subject;

    private String message;

    private Date created;

    private Date expires;

    private Boolean onlyShowOnce;

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IAdvisory) {
            IAdvisory advisory = (IAdvisory) obj;
            result = this.getIdInternal().equals(advisory.getIdInternal());
        }
        return result;
    }

    public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		stringBuilder.append(this.getClass().getName());
		stringBuilder.append(": ");
		stringBuilder.append("idInternal = ");
		stringBuilder.append(getIdInternal());
		stringBuilder.append("; ");
		stringBuilder.append("subject = ");
		stringBuilder.append(this.subject);
		stringBuilder.append("; ");
		stringBuilder.append("sender = ");
		stringBuilder.append(this.sender);
		stringBuilder.append("\n");
		stringBuilder.append("message = ");
		stringBuilder.append(this.message);
		stringBuilder.append("\n");
		stringBuilder.append("created = ");
		stringBuilder.append(this.created);
		stringBuilder.append("\n");
		stringBuilder.append("expires = ");
		stringBuilder.append(this.expires);
		stringBuilder.append("\n");
		stringBuilder.append("onlyShowOnce = ");
		stringBuilder.append(this.onlyShowOnce);
		stringBuilder.append("]\n");
		
        return stringBuilder.toString();
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

}