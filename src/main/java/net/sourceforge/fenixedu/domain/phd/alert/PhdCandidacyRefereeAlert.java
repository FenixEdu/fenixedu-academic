package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdCandidacyRefereeAlert extends PhdCandidacyRefereeAlert_Base {

    static private final int INTERVAL = 7; // number of days

    private PhdCandidacyRefereeAlert() {
        super();
    }

    public PhdCandidacyRefereeAlert(final PhdCandidacyReferee referee) {
        this();
        String[] args = {};
        if (referee == null) {
            throw new DomainException("error.PhdCandidacyRefereeAlert.invalid.referee", args);
        }
        init(referee.getIndividualProgramProcess(), generateSubject(referee), generateBody(referee));
        setReferee(referee);
    }

    private MultiLanguageString generateSubject(final PhdCandidacyReferee referee) {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
        return new MultiLanguageString(String.format(bundle.getString("message.phd.email.subject.referee"), referee
                .getCandidatePerson().getName(), referee.getCandidatePerson().getName()));
    }

    private MultiLanguageString generateBody(final PhdCandidacyReferee referee) {
        return new MultiLanguageString(referee.getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod()
                .getEmailMessageBodyForRefereeForm(referee));
    }

    @Override
    public String getDescription() {
        final ResourceBundle bundle = getResourceBundle(Language.getLocale());
        return bundle.getString(String.format("message.phd.referee.alert", INTERVAL));
    }

    @Override
    protected boolean isToDiscard() {
        return getReferee().hasLetter() || candidacyPeriodIsOver();
    }

    private boolean candidacyPeriodIsOver() {
        final LocalDate candidacyDate = getReferee().getPhdProgramCandidacyProcess().getCandidacyDate();
        final PhdCandidacyPeriod period = getReferee().getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod();
        return new DateTime().isAfter(period.getEnd());
    }

    @Override
    protected boolean isToFire() {
        int days = Days.daysBetween(calculateStartDate().toDateMidnight(), new LocalDate().toDateMidnight()).getDays();
        return days >= INTERVAL;
    }

    private LocalDate calculateStartDate() {
        return hasFireDate() ? getFireDate().toLocalDate() : getReferee().getPhdProgramCandidacyProcess().getCandidacyDate();
    }

    @Override
    protected void generateMessage() {
        new Message(getSender(), null, Collections.<Recipient> emptyList(), buildMailSubject(), buildMailBody(), getEmail());
    }

    private Set<String> getEmail() {
        return Collections.singleton(getReferee().getEmail());
    }

    @Override
    protected void disconnect() {
        removeReferee();
        super.disconnect();
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
