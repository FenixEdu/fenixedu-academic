package net.sourceforge.fenixedu.domain.phd.alert;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdCustomAlert extends PhdCustomAlert_Base {

    protected PhdCustomAlert() {
	super();
    }

    public PhdCustomAlert(PhdIndividualProgramProcess process, Group targetGroup, MultiLanguageString subject,
	    MultiLanguageString body, Boolean sendMail, LocalDate fireDate) {
	this();
	init(process, targetGroup, subject, body, sendMail, fireDate);
    }

    public PhdCustomAlert(PhdIndividualProgramProcess process, PhdCustomAlertBean bean) {
	this(process, bean.getTargetGroup(), new MultiLanguageString(Language.getDefaultLanguage(), bean.getSubject()),
		new MultiLanguageString(Language.getDefaultLanguage(), bean.getBody()), bean.isToSendEmail(), bean.getFireDate());
    }

    private void init(PhdIndividualProgramProcess process, Group targetGroup, MultiLanguageString subject,
	    MultiLanguageString body, Boolean sendEmail, LocalDate whenToFire) {

	super.init(process, subject, body);

	check(whenToFire, "error.net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert.whenToFire.cannot.be.null");
	check(targetGroup, "error.phd.alert.PhdAlert.targetGroup.cannot.be.null");
	check(sendEmail, "error.phd.alert.PhdAlert.sendEmail.cannot.be.null");
	super.setWhenToFire(whenToFire);
	super.setSendEmail(sendEmail);
	super.setTargetGroup(targetGroup);
    }

    @Override
    public boolean isToFire() {
	return !new LocalDate().isBefore(getWhenToFire());
    }

    @Override
    protected boolean isToDiscard() {
	return true;
    }

    @Override
    public boolean isCustomAlert() {
	return true;
    }

    @Override
    public String getDescription() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getDefaultLocale());

	return MessageFormat.format(bundle.getString("message.phd.alert.custom.description"), getTargetGroup().getName(),
		getWhenToFire().toString(DateFormatUtil.DEFAULT_DATE_FORMAT), getFormattedSubject(), getFormattedBody());
    }

    public void delete() {
	disconnect();

    }

    @Override
    protected void generateMessage() {

	for (final Person person : getTargetGroup().getElements()) {
	    new PhdAlertMessage(getProcess(), person, getFormattedSubject(), getFormattedBody());
	}

	if (isToSendMail()) {
	    new Message(getRootDomainObject().getSystemSender(), new Recipient(getTargetGroup().getElements()),
		    buildMailSubject(), buildMailBody());

	}

    }

    @Override
    public void setTargetGroup(Group targetGroup) {
	throw new DomainException("error.phd.alert.PhdAlert.cannot.modify.targetGroup");
    }

    @Override
    public void setSendEmail(Boolean sendEmail) {
	throw new DomainException("error.phd.alert.PhdAlert.cannot.modify.sendEmail");
    }

    @Override
    public boolean isToSendMail() {
	return getSendEmail().booleanValue();
    }

}
