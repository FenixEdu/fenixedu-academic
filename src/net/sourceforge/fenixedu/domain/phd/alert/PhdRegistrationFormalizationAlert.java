package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.MasterDegreeAdministrativeOfficeGroup;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCalendarUtil;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdRegistrationFormalizationAlert extends PhdRegistrationFormalizationAlert_Base {

    private static final int MAX_DAYS = 20;

    public PhdRegistrationFormalizationAlert(PhdIndividualProgramProcess process) {
	super();
	init(process);

    }

    private void init(PhdIndividualProgramProcess process) {
	super.init(process, buildSubject(), buildBody());
    }

    private MultiLanguageString buildBody() {
	final ResourceBundle bundle = getResourceBundle();
	return new MultiLanguageString(Language.getDefaultLanguage(), bundle
		.getString("message.phd.alert.registration.formalization.body"));
    }

    private MultiLanguageString buildSubject() {
	return new MultiLanguageString(Language.getDefaultLanguage(), getResourceBundle().getString(
		"message.phd.alert.registration.formalization.subject"));
    }

    @Override
    public String getDescription() {
	return getResourceBundle().getString("message.phd.alert.registration.formalization.description");
    }

    private ResourceBundle getResourceBundle() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getDefaultLocale());
	return bundle;
    }

    private LocalDate getWhenToFire() {
	return PhdProgramCalendarUtil.addWorkDaysTo(getProcess().getCandidacyProcess().getWhenRatified(), MAX_DAYS);
    }

    @Override
    protected boolean isToDiscard() {
	return getProcess().isRegistrationFormalized();
    }

    @Override
    protected boolean isToFire() {

	if (getProcess().isRegistrationFormalized()) {
	    return false;
	}

	return new LocalDate().isBefore(getWhenToFire());

    }

    @Override
    public boolean isSystemAlert() {
	return true;
    }

    @Override
    protected void generateMessage() {
	final Group academicOfficeGroup = new MasterDegreeAdministrativeOfficeGroup();

	new PhdAlertMessage(getProcess(), academicOfficeGroup.getElements(), getFormattedSubject(), getFormattedBody());

	new Message(getRootDomainObject().getSystemSender(), new Recipient(academicOfficeGroup), buildMailSubject(),
		buildMailBody());

    }

    @Override
    public boolean isToSendMail() {
	return true;
    }

}
