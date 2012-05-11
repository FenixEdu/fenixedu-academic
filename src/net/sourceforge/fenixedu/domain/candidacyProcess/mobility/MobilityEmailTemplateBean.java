package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import java.io.Serializable;

public class MobilityEmailTemplateBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private MobilityEmailTemplateType type;
    private String subject;
    private String body;

    public MobilityEmailTemplateBean() {

    }

    public MobilityEmailTemplateBean(final MobilityEmailTemplateType type) {
	setType(type);
    }

    public MobilityEmailTemplateType getType() {
	return type;
    }

    public void setType(MobilityEmailTemplateType type) {
	this.type = type;
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public String getBody() {
	return body;
    }

    public void setBody(String body) {
	this.body = body;
    }

}
