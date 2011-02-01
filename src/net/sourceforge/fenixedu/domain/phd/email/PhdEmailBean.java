package net.sourceforge.fenixedu.domain.phd.email;

public abstract class PhdEmailBean implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    protected String bccs;
    protected String subject;
    protected String message;

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
}



