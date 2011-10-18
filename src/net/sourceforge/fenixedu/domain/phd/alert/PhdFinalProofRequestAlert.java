package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCalendarUtil;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdFinalProofRequestAlert extends PhdFinalProofRequestAlert_Base {

    static private int MAX_DAYS = 5 * 365; // years * days

    private PhdFinalProofRequestAlert() {
	super();
    }

    public PhdFinalProofRequestAlert(final PhdIndividualProgramProcess process) {
	this();
	super.init(process, buildSubject(process), buildBody(process));
    }

    private MultiLanguageString buildSubject(final PhdIndividualProgramProcess process) {
	return new MultiLanguageString(Language.getDefaultLanguage(), AlertService.getSubjectPrefixed(process,
		"message.phd.alert.final.proof.request.subject"));
    }

    private MultiLanguageString buildBody(final PhdIndividualProgramProcess process) {
	return new MultiLanguageString(Language.getDefaultLanguage(), AlertService.getBodyText(process,
		"message.phd.alert.final.proof.request.body"));
    }

    @Override
    public String getDescription() {
	return getResourceBundle().getString("message.phd.alert.final.proof.request.description");
    }

    @Override
    protected boolean isToDiscard() {
	return getProcess().hasThesisProcess() || !getProcess().getActiveState().isPhdActive();
    }

    @Override
    protected boolean isToFire() {
	if (!hasFireDate()) {
	    return !new LocalDate()
		    .isBefore(PhdProgramCalendarUtil.addWorkDaysTo(getProcess().getWhenStartedStudies(), MAX_DAYS));
	}

	return !new LocalDate().isBefore(PhdProgramCalendarUtil.addWorkDaysTo(getFireDate().toLocalDate(), 25));
    }

    @Override
    protected void generateMessage() {
	// initialize subject and body again with correct values
	super.init(buildSubject(getProcess()), buildBody(getProcess()));
	
	// TODO: add missing elements (Coordinator, AcademicOffice?)
	new PhdAlertMessage(getProcess(), getProcess().getPerson(), getFormattedSubject(), getFormattedBody());
	new Message(getRootDomainObject().getSystemSender(), new Recipient(Collections.singletonList(getProcess().getPerson())),
		buildMailSubject(), buildMailBody());
    }

    @Override
    public boolean isToSendMail() {
	return true;
    }

    @Override
    public boolean isSystemAlert() {
	return true;
    }

}
