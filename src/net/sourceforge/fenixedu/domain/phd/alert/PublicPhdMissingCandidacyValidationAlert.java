package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PublicPhdMissingCandidacyValidationAlert extends PublicPhdMissingCandidacyValidationAlert_Base {

    static private final int INTERVAL = 15; // number of days

    private PublicPhdMissingCandidacyValidationAlert() {
	super();
    }

    public PublicPhdMissingCandidacyValidationAlert(final PhdIndividualProgramProcess process) {
	this();
	init(process, generateSubject(process), generateBody(process));
    }

    private MultiLanguageString generateSubject(final PhdIndividualProgramProcess process) {
	return process.getCandidacyProcess().getPublicPhdCandidacyPeriod()
		.getEmailMessageSubjectForMissingCandidacyValidation(process);
    }

    private MultiLanguageString generateBody(final PhdIndividualProgramProcess process) {
	return process.getCandidacyProcess().getPublicPhdCandidacyPeriod()
		.getEmailMessageBodyForMissingCandidacyValidation(process);
    }

    @Override
    protected void generateMessage() {
	new Message(getSender(), null, Collections.<Recipient> emptyList(), buildMailSubject(), buildMailBody(), getEmail());
    }

    private Set<String> getEmail() {
	return Collections.singleton(getProcess().getPerson().getInstitutionalOrDefaultEmailAddressValue());
    }

    @Override
    public String getDescription() {
	final ResourceBundle bundle = getResourceBundle(Locale.ENGLISH);
	return bundle.getString(String.format("message.phd.missing.candidacy.validation.alert", INTERVAL));
    }

    @Override
    protected boolean isToFire() {
	int days = Days.daysBetween(calculateStartDate().toDateMidnight(), new LocalDate().toDateMidnight()).getDays();
	return days >= INTERVAL;
    }

    private LocalDate calculateStartDate() {
	return hasFireDate() ? getFireDate().toLocalDate() : getCandidacyProcess().getWhenCreated().toLocalDate();
    }

    private PhdProgramCandidacyProcess getCandidacyProcess() {
	return getProcess().getCandidacyProcess();
    }

    @Override
    protected boolean isToDiscard() {
	return getCandidacyProcess().isValidatedByCandidate() || candidacyPeriodIsOver();
    }

    /*
     * Must exist a candidacy period, otherwise candidacy hash code could not be
     * previously created
     */
    private boolean candidacyPeriodIsOver() {
	return new DateTime().isAfter(getCandidacyProcess().getPublicPhdCandidacyPeriod().getEnd());
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
