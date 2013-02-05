package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PublicPhdMissingCandidacyAlert extends PublicPhdMissingCandidacyAlert_Base {

    static private final int INTERVAL = 15; // number of days

    private PublicPhdMissingCandidacyAlert() {
        super();
    }

    public PublicPhdMissingCandidacyAlert(final PhdProgramPublicCandidacyHashCode candidacyHashCode) {
        this();
        check(candidacyHashCode, "error.PublicPhdMissingCandidacyAlert.invalid.candidacy.hash.code");
        init(generateSubject(candidacyHashCode), generateBody(candidacyHashCode));
        setCandidacyHashCode(candidacyHashCode);
    }

    private MultiLanguageString generateSubject(final PhdProgramPublicCandidacyHashCode candidacyHashCode) {
        // TODO: if collaboration type change, then message must be different
        final ResourceBundle bundle = getResourceBundle(Locale.ENGLISH);
        return new MultiLanguageString().with(Language.en, bundle.getString("message.phd.email.subject.missing.candidacy"));
    }

    private MultiLanguageString generateBody(final PhdProgramPublicCandidacyHashCode hashCode) {
        // TODO: if collaboration type change, then message must be different
        String submissionAccessURL = PropertiesManager.getProperty("phd.public.candidacy.submission.link");
        final ResourceBundle bundle = getResourceBundle(Locale.ENGLISH);
        final String body =
                String.format(bundle.getString("message.phd.email.body.missing.candidacy"), submissionAccessURL,
                        hashCode.getValue());
        return new MultiLanguageString().with(Language.en, body);
    }

    @Override
    protected boolean isToFire() {
        int days = Days.daysBetween(calculateStartDate().toDateMidnight(), new LocalDate().toDateMidnight()).getDays();
        return days >= INTERVAL;
    }

    private LocalDate calculateStartDate() {
        return hasFireDate() ? getFireDate().toLocalDate() : getCandidacyHashCode().getWhenCreated().toLocalDate();
    }

    @Override
    protected boolean isToDiscard() {
        return getCandidacyHashCode().hasCandidacyProcess() || candidacyPeriodIsOver();
    }

    /*
     * Must exist a candidacy period, otherwise candidacy hash code could not be
     * previously created
     */
    private boolean candidacyPeriodIsOver() {
        return new DateTime().isAfter(getCandidacyHashCode().getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod()
                .getEnd());
    }

    @Override
    protected void generateMessage() {
        new Message(getSender(), null, Collections.<Recipient> emptyList(), buildMailSubject(), buildMailBody(), getEmail());
    }

    private Set<String> getEmail() {
        return Collections.singleton(getCandidacyHashCode().getEmail());
    }

    @Override
    public String getDescription() {
        final ResourceBundle bundle = getResourceBundle(Locale.ENGLISH);
        return bundle.getString(String.format("message.phd.missing.candidacy.alert", INTERVAL));
    }

    @Override
    public boolean isToSendMail() {
        return true;
    }

    @Override
    public boolean isSystemAlert() {
        return true;
    }

    @Override
    protected void disconnect() {
        removeCandidacyHashCode();
        super.disconnect();
    }
}
