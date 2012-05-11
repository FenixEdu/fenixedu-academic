package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ErasmusAlert extends ErasmusAlert_Base {

    public static final Comparator<ErasmusAlert> WHEN_CREATED_COMPARATOR = new Comparator<ErasmusAlert>() {

	@Override
	public int compare(ErasmusAlert o1, ErasmusAlert o2) {
	    return o1.getWhenCreated().compareTo(o2.getWhenCreated());
	}

    };

    public ErasmusAlert() {
	super();
    }

    public ErasmusAlert(MobilityIndividualApplicationProcess process, Boolean sendEmail, LocalDate whenToFire,
	    final MultiLanguageString subject, final MultiLanguageString body, ErasmusAlertEntityType entity) {
	this();
	init(process, sendEmail, whenToFire, subject, body, entity);
    }

    protected void init(MobilityIndividualApplicationProcess process, Boolean sendEmail, LocalDate whenToFire,
	    final MultiLanguageString subject, final MultiLanguageString body, ErasmusAlertEntityType entity) {
	super.init(subject, body);

	check(whenToFire,
		"net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess.fireDate.cannot.be.null");

	super.setSendEmail(sendEmail);
	super.setWhenToFire(whenToFire);
	super.setEntity(entity);
	super.setProcess(process);

	super.setWhoCreated(AccessControl.getPerson());
    }

    @Override
    protected void generateMessage() {
	// new Message(getRootDomainObject().getSystemSender(), null,
	// Collections.EMPTY_LIST, buildMailSubject(), buildMailBody(),
	// getProcess().getPersonalDetails().getEmail());
    }

    protected String buildMailBody() {
	final StringBuilder result = new StringBuilder();

	for (final String eachContent : getFormattedBody().getAllContents()) {
	    result.append(eachContent).append("\n").append(" ------------------------- ");
	}

	result.delete(result.lastIndexOf("\n") + 1, result.length());

	return result.toString();

    }

    protected String buildMailSubject() {
	final StringBuilder result = new StringBuilder();

	for (final String eachContent : getFormattedSubject().getAllContents()) {
	    result.append(eachContent).append(" / ");
	}

	if (result.toString().endsWith(" / ")) {
	    result.delete(result.length() - 3, result.length());
	}

	return result.toString();
    }

    @Override
    public String getDescription() {
	return null;
    }

    @Override
    protected boolean isToDiscard() {
	return !isToSendMail();
    }

    @Override
    public boolean isToFire() {
	return getFireDate() == null;
    }

    @Override
    public boolean isToSendMail() {
	return getSendEmail() && getProcess().getCandidacyProcess().hasOpenCandidacyPeriod();
    }
}
