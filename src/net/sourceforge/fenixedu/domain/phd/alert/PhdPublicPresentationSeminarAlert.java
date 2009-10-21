package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.MasterDegreeAdministrativeOfficeGroup;
import net.sourceforge.fenixedu.domain.phd.InternalGuiding;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramGuiding;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdPublicPresentationSeminarAlert extends PhdPublicPresentationSeminarAlert_Base {

    private static final int FIRST_WARNING_DAYS = 30;

    private static int MAX_DAYS = 24 * 30; // months * days

    private PhdPublicPresentationSeminarAlert() {
	super();
    }

    public PhdPublicPresentationSeminarAlert(final PhdIndividualProgramProcess process) {
	this();
	super.init(process, buildSubject(), buildBody());
    }

    private MultiLanguageString buildSubject() {
	return new MultiLanguageString(Language.getDefaultLanguage(), getResourceBundle().getString(
		"message.phd.alert.public.presentation.seminar.subject"));
    }

    private MultiLanguageString buildBody() {
	return new MultiLanguageString(Language.getDefaultLanguage(), getResourceBundle().getString(
		"message.phd.alert.public.presentation.seminar.body"));
    }

    @Override
    public String getDescription() {
	return getResourceBundle().getString("message.phd.alert.public.presentation.seminar.description");
    }

    @Override
    protected boolean isToDiscard() {
	return getProcess().hasPublicPresentationSeminar();
    }

    @Override
    public boolean isToFire() {
	if (!new LocalDate().isBefore(getProcess().getWhenStartedStudies().plusDays(MAX_DAYS))) {
	    if (getFireDate() == null) {
		return true;
	    }

	    if (Days.daysBetween(getFireDate().toLocalDate(), new LocalDate()).getDays() > 0) {
		return true;
	    }
	}

	if (!new LocalDate().isBefore(getProcess().getWhenStartedStudies().plusDays(MAX_DAYS).minusDays(FIRST_WARNING_DAYS))
		&& getFireDate() == null) {
	    return true;
	}

	return false;

    }

    @Override
    protected void generateMessage() {
	generateMessageForCoodinator();
	generateMessageForAcademicOffice();
	generateMessageForStudent();
	generateMessageForGuiders();
    }

    private void generateMessageForCoodinator() {
	generateMessage(new FixedSetGroup(getProcess().getPhdProgram().getCoordinatorsFor(
		ExecutionYear.readCurrentExecutionYear())));

    }

    private void generateMessageForAcademicOffice() {
	generateMessage(new MasterDegreeAdministrativeOfficeGroup());
    }

    private void generateMessageForStudent() {
	generateMessage(new FixedSetGroup(getProcess().getPerson()));
    }

    private void generateMessageForGuiders() {
	for (final PhdProgramGuiding guiding : getProcess().getGuidings()) {
	    if (guiding.isInternal()) {
		generateMessage(new FixedSetGroup(((InternalGuiding) guiding).getPerson()));
	    } else {
		new Message(getRootDomainObject().getSystemSender(), Collections.EMPTY_LIST, Collections.EMPTY_LIST,
			buildMailSubject(), buildMailBody(), Collections.singleton(guiding.getEmail()));
	    }
	}
    }

    private void generateMessage(Group group) {
	new PhdAlertMessage(getProcess(), group.getElements(), getFormattedSubject(), getFormattedBody());
	new Message(getRootDomainObject().getSystemSender(), new Recipient("", group), buildMailSubject(), buildMailBody());
    }

    @Override
    public boolean isSystemAlert() {
	return true;
    }

    @Override
    public boolean isToSendMail() {
	return true;
    }

}
