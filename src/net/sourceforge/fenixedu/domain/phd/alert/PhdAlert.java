package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

abstract public class PhdAlert extends PhdAlert_Base {

    protected PhdAlert() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setRootDomainObjectForActivePhdAlerts(RootDomainObject.getInstance());
	setWhenCreated(new DateTime());
    }

    protected void init(final MultiLanguageString subject, final MultiLanguageString body) {
	checkParameters(subject, body);
	super.setSubject(subject);
	super.setBody(body);
	super.setActive(Boolean.TRUE);
    }

    protected void init(PhdIndividualProgramProcess process, MultiLanguageString subject, MultiLanguageString body) {
	check(process, "error.phd.alert.PhdAlert.process.cannot.be.null");
	super.setProcess(process);
	init(subject, body);
    }

    private void checkParameters(MultiLanguageString subject, MultiLanguageString body) {
	check(subject, "error.phd.alert.PhdAlert.subject.cannot.be.null");
	check(body, "error.phd.alert.PhdAlert.body.cannot.be.null");
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
    public void setProcess(PhdIndividualProgramProcess process) {
	throw new DomainException("error.phd.alert.PhdAlert.cannot.modify.process");
    }

    @Override
    public void setActive(Boolean active) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlert.cannot.modify.active");
    }

    protected String buildMailBody() {
	final StringBuilder result = new StringBuilder();

	for (final String eachContent : getFormattedBody().getAllContents()) {
	    result.append(eachContent).append("\n").append(" ------------------------- ");
	}

	result.delete(result.lastIndexOf("\n") + 1, result.length());

	return result.toString();

    }

    protected MultiLanguageString getFormattedBody() {
	return super.getBody();
    }

    protected String buildMailSubject() {
	final StringBuilder result = new StringBuilder();

	for (final String eachContent : getFormattedSubject().getAllContents()) {
	    result.append(eachContent).append(" / ");
	}

	if (result.toString().endsWith(" / ")) {
	    result.delete(result.length() - 3, result.length());
	}

	return result.toString();
    }

    protected MultiLanguageString getFormattedSubject() {
	return super.getSubject();
    }

    public void fire() {
	if (isToDiscard()) {
	    discard();
	    return;
	}

	if (!isToFire()) {
	    return;
	}

	generateMessage();
	super.setFireDate(new DateTime());

	// check again, because alert may be discarded after send messages
	if (isToDiscard()) {
	    discard();
	}
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
	removeRootDomainObjectForActivePhdAlerts();
	removeRootDomainObject();
    }

    public void delete() {
	disconnect();
	super.deleteDomainObject();
    }

    public boolean isActive() {
	return getActive().booleanValue();
    }

    public boolean hasFireDate() {
	return getFireDate() != null;
    }

    @Override
    public MultiLanguageString getBody() {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlert.use.getFormattedBody.instead");
    }

    @Override
    public MultiLanguageString getSubject() {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlert.use.getFormattedSubject.instead");
    }

    final protected ResourceBundle getResourceBundle() {
	return getResourceBundle(Language.getDefaultLocale());
    }

    final protected ResourceBundle getResourceBundle(final Locale locale) {
	return ResourceBundle.getBundle("resources.PhdResources", locale);
    }

    abstract public String getDescription();

    abstract protected boolean isToDiscard();

    abstract protected boolean isToFire();

    abstract protected void generateMessage();

    abstract public boolean isToSendMail();

}
