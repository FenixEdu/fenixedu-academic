package net.sourceforge.fenixedu.domain.phd.alert;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

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
	    MultiLanguageString body, Boolean sendEmail, LocalDate fireDate) {
	check(fireDate, "error.net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert.fireDate.cannot.be.null");
	super.init(process, targetGroup, subject, body, sendEmail);
	super.setWhenToFire(fireDate);
    }

    @Override
    protected void checkParameters(PhdIndividualProgramProcess process, Group targetGroup, MultiLanguageString subject,
	    MultiLanguageString body, Boolean sendEmail) {
	check(targetGroup, "error.phd.alert.PhdAlert.targetGroup.cannot.be.null");
	super.checkParameters(process, targetGroup, subject, body, sendEmail);
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
	return getWhenToFire().toString(DateFormatUtil.DEFAULT_DATE_FORMAT);
    }

    public void delete() {
	disconnect();
    }

}
