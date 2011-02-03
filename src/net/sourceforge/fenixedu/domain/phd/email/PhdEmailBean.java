package net.sourceforge.fenixedu.domain.phd.email;

import net.sourceforge.fenixedu.domain.Person;

import org.joda.time.DateTime;

public abstract class PhdEmailBean implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    protected String bccs;
    protected String subject;
    protected String message;
    protected DateTime creationDate;
    protected Person creator;

    public String getBccs() {
	return bccs;
    }

    public void setBccs(String bccs) {
	this.bccs = bccs;
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public DateTime getCreationDate() {
	return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
	this.creationDate = creationDate;
    }

    public Person getCreator() {
	return creator;
    }

    public void setCreator(Person creator) {
	this.creator = creator;
    }
}



