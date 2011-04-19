package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.util.phd.PhdProperties;

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
	// TODO: if collaboration type change, then message must be different
	final ResourceBundle bundle = getResourceBundle(Locale.ENGLISH);
	return MultiLanguageString.i18n().add("en", bundle.getString("message.phd.email.subject.missing.candidacy.validation"))
		.finish();
    }

    private MultiLanguageString generateBody(final PhdIndividualProgramProcess process) {
	// TODO: if collaboration type change, then message must be different
	final ResourceBundle bundle = getResourceBundle(Locale.ENGLISH);
	final String body = String.format(bundle.getString("message.phd.email.body.missing.candidacy.validation"), PhdProperties
		.getPublicCandidacyAccessLink(), process.getCandidacyProcess().getCandidacyHashCode().getValue());
	return MultiLanguageString.i18n().add("en", body).finish();
    }

    @Override
    protected void generateMessage() {
	new Message(getRootDomainObject().getSystemSender(), null, Collections.EMPTY_LIST, buildMailSubject(), buildMailBody(),
		getEmail());
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
