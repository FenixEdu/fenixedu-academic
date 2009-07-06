package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

abstract public class PhdAlert extends PhdAlert_Base {

    protected PhdAlert() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setRootDomainObjectForActivePhdAlerts(RootDomainObject.getInstance());
    }

    protected void init(PhdIndividualProgramProcess process, Group targetGroup, MultiLanguageString subject,
	    MultiLanguageString body, Boolean sendEmail) {
	checkParameters(process, targetGroup, subject, body, sendEmail);
	super.setProcess(process);
	super.setTargetGroup(targetGroup);
	super.setSubject(subject);
	super.setBody(body);
	super.setSendEmail(sendEmail);
	super.setActive(Boolean.TRUE);
    }

    protected void checkParameters(PhdIndividualProgramProcess process, Group targetGroup, MultiLanguageString subject,
	    MultiLanguageString body, Boolean sendEmail) {
	check(process, "error.phd.alert.PhdAlert.process.cannot.be.null");
	check(subject, "error.phd.alert.PhdAlert.subject.cannot.be.null");
	check(body, "error.phd.alert.PhdAlert.body.cannot.be.null");
	check(sendEmail, "error.phd.alert.PhdAlert.sendEmail.cannot.be.null");
    }

    @Override
    public void setTargetGroup(Group targetGroup) {
	throw new DomainException("error.phd.alert.PhdAlert.cannot.modify.targetGroup");
    }

    @Override
    public void setSubject(MultiLanguageString subject) {
	throw new DomainException("error.phd.alert.PhdAlert.cannot.modify.subject");
    }

    @Override
    public void setBody(MultiLanguageString body) {
	throw new DomainException("error.phd.alert.PhdAlert.cannot.modify.body");
    }

    @Override
    public void setSendEmail(Boolean sendEmail) {
	throw new DomainException("error.phd.alert.PhdAlert.cannot.modify.sendEmail");
    }

    @Override
    public void setProcess(PhdIndividualProgramProcess process) {
	throw new DomainException("error.phd.alert.PhdAlert.cannot.modify.process");
    }

    @Override
    public void setActive(Boolean active) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlert.cannot.modify.active");
    }

    public boolean isToSendEmail() {
	return getSendEmail().booleanValue();
    }

    protected String buildMailBody() {
	final StringBuilder result = new StringBuilder();

	for (final String eachContent : getBody().getAllContents()) {
	    result.append(eachContent).append("\n").append(" ------------------------- ");
	}

	result.delete(result.lastIndexOf("\n") + 1, result.length());

	return result.toString();

    }

    protected String buildMailSubject() {
	final StringBuilder result = new StringBuilder();

	for (final String eachContent : getSubject().getAllContents()) {
	    result.append(eachContent).append(" / ");
	}

	if (result.toString().endsWith(" / ")) {
	    result.delete(result.length() - 3, result.length());
	}

	return result.toString();
    }

    public void fire() {
	if (!isToFire()) {
	    return;
	}

	generateMessage();
	super.setFireDate(new DateTime());

	if (isToDiscard()) {
	    discard();
	}
    }

    protected void generateMessage() {
	final Set<Person> toSendEmail = new HashSet<Person>();
	for (final Person person : getTargetGroup().getElements()) {
	    if (isToSendEmail()) {
		toSendEmail.add(person);
	    }

	    new PhdAlertMessage(getProcess(), person, getSubject(), getBody());
	}

	new Message(getRootDomainObject().getSystemSender(), new Recipient(toSendEmail), buildMailSubject(), buildMailBody());
    }

    public void discard() {
	super.setRootDomainObjectForActivePhdAlerts(null);
	super.setActive(false);
    }

    public boolean isSystemAlert() {
	return false;
    }

    public boolean isCustomAlert() {
	return false;
    }

    protected void disconnect() {
	super.setProcess(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean isActive() {
	return getActive().booleanValue();
    }

    public boolean hasFireDate() {
	return getFireDate() != null;
    }

    abstract public String getDescription();

    abstract protected boolean isToDiscard();

    abstract protected boolean isToFire();

}
