package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

public class InfoAdvisoryEditor extends InfoObject {

    private String sender;

    private String subject;

    private String message;

    private Date created;

    private Date expires;

    public InfoAdvisoryEditor() {
    }

    public Date getCreated() {
        return created;
    }

    public Date getExpires() {
        return expires;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }

    public void setCreated(Date date) {
        created = date;
    }

    public void setExpires(Date date) {
        expires = date;
    }

    public void setMessage(String string) {
        message = string;
    }

    public void setSubject(String string) {
        subject = string;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String string) {
        sender = string;
    }

}