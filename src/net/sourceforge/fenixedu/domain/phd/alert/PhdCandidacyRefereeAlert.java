package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.util.email.Message;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdCandidacyRefereeAlert extends PhdCandidacyRefereeAlert_Base {

    static private final int INTERVAL = 7; // number of days

    private PhdCandidacyRefereeAlert() {
	super();
    }

    public PhdCandidacyRefereeAlert(final PhdCandidacyReferee referee) {
	this();
	check(referee, "error.PhdCandidacyRefereeAlert.invalid.referee");
	init(referee.getIndividualProgramProcess(), generateSubject(referee), generateBody(referee));
	setReferee(referee);
    }

    private MultiLanguageString generateSubject(final PhdCandidacyReferee referee) {
	// TODO: if collaboration type change, then message must be different
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Locale.ENGLISH);
	final String subject = String.format(bundle.getString("message.phd.email.subject.referee"), referee
		.getIndividualProgramProcess().getPerson().getName());
	return MultiLanguageString.i18n().add("en", subject).finish();
    }

    private MultiLanguageString generateBody(final PhdCandidacyReferee referee) {
	// TODO: if collaboration type change, then message must be different
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Locale.ENGLISH);
	final String body = String.format(bundle.getString("message.phd.email.body.referee"), referee.getValue());
	return MultiLanguageString.i18n().add("en", body).finish();
    }

    @Override
    public String getDescription() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Locale.ENGLISH);
	return bundle.getString(String.format("message.phd.referee.alert", INTERVAL));
    }

    @Override
    protected boolean isToDiscard() {
	return getReferee().hasLetter() || candidacyPeriodIsOver();
    }

    private boolean candidacyPeriodIsOver() {
	final LocalDate candidacyDate = getReferee().getPhdProgramCandidacyProcess().getCandidacyDate();
	final PhdCandidacyPeriod period = PhdCandidacyPeriod.getCandidacyPeriod(candidacyDate.toDateTimeAtStartOfDay());
	return new DateTime().isAfter(period.getEnd());
    }

    @Override
    protected boolean isToFire() {
	if (isToDiscard()) {
	    return false;
	}
	int days = Days.daysBetween(calculateStartDate().toDateMidnight(), new LocalDate().toDateMidnight()).getDays();
	return days >= INTERVAL;
    }

    private LocalDate calculateStartDate() {
	return hasFireDate() ? getFireDate().toLocalDate() : getReferee().getPhdProgramCandidacyProcess().getCandidacyDate();
    }

    @Override
    public void fire() {
	if (!isToFire()) {
	    if (isToDiscard()) {
		discard();
	    }
	    return;
	}

	generateMessage();
	setFireDate(new DateTime());

	if (isToDiscard()) {
	    discard();
	}
    }

    @Override
    protected void generateMessage() {
	new Message(getRootDomainObject().getSystemSender(), null, Collections.EMPTY_LIST, buildMailSubject(), buildMailBody(),
		getEmail());
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

}
