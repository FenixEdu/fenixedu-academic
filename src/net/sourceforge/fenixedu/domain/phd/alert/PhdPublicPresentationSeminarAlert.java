package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.Collections;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdPublicPresentationSeminarAlert extends PhdPublicPresentationSeminarAlert_Base {

    static private final int FIRST_WARNING_DAYS = 30;

    static private int MAX_DAYS = 30 * 24; // days * months

    static private int MAX_DAYS_AFTER_LIMIT_REACHED = 30 * 3;

    private PhdPublicPresentationSeminarAlert() {
	super();
    }

    public PhdPublicPresentationSeminarAlert(final PhdIndividualProgramProcess process) {
	this();
	super.init(process, buildSubject(process), buildBody(process));
    }

    private MultiLanguageString buildSubject(final PhdIndividualProgramProcess process) {
	return new MultiLanguageString(Language.getDefaultLanguage(), AlertService.getSubjectPrefixed(process,
		AlertMessage.create("message.phd.alert.public.presentation.seminar.subject")));
    }

    private MultiLanguageString buildBody(final PhdIndividualProgramProcess process) {
	int days = getDaysUntilNow(process.getWhenStartedStudies());
	return new MultiLanguageString(Language.getDefaultLanguage(), AlertService.getBodyText(process, AlertMessage.create(
		"message.phd.alert.public.presentation.seminar.body", process.getWhenStartedStudies().toString("dd/MM/yyyy"),
		String.valueOf(days < 1 ? 1 : days), getGuidersNames(process))));
    }

    private int getDaysUntilNow(final LocalDate begin) {
	return Days.daysBetween(begin, new LocalDate()).getDays();
    }

    private String getGuidersNames(final PhdIndividualProgramProcess process) {
	final StringBuilder builder = new StringBuilder();
	final Iterator<PhdParticipant> values = process.getGuidings().iterator();
	while (values.hasNext()) {
	    builder.append(values.next().getName()).append(values.hasNext() ? ", " : "");
	}

	if (process.hasAnyGuidings()) {
	    builder.insert(0, "(").insert(builder.length(), ")");
	}

	return builder.toString();
    }

    @Override
    public String getDescription() {
	return getResourceBundle().getString("message.phd.alert.public.presentation.seminar.description");
    }

    @Override
    protected boolean isToDiscard() {
	return !getProcess().getActiveState().isActive() || getProcess().hasSeminarProcess();
    }

    @Override
    public boolean isToFire() {

	if (hasExceedLimitDate()) {

	    if (getFireDate() == null) {
		return true;
	    }

	    if (getDaysUntilNow(getFireDate().toLocalDate()) > MAX_DAYS_AFTER_LIMIT_REACHED) {
		return true;
	    }
	}

	if (hasExceedFirstAlertDate() && getFireDate() == null) {
	    return true;
	}

	return false;
    }

    private boolean hasExceedFirstAlertDate() {
	return !new LocalDate().isBefore(getFirstAlertDate());
    }

    private LocalDate getFirstAlertDate() {
	return getLimitDate().minusDays(FIRST_WARNING_DAYS);
    }

    private boolean hasExceedLimitDate() {
	return !new LocalDate().isBefore(getLimitDate());
    }

    private LocalDate getLimitDate() {
	return getProcess().getWhenStartedStudies().plusDays(MAX_DAYS);
    }

    @Override
    protected void generateMessage() {
	// initialize subject and body again with correct values
	super.init(buildSubject(getProcess()), buildBody(getProcess()));

	generateMessageForStudent();

	generateMessageForCoodinator();
	generateMessageForAcademicOffice();
	generateMessageForGuiders();

    }

    private void generateMessageForCoodinator() {
	generateMessage(new FixedSetGroup(getProcess().getPhdProgram().getCoordinatorsFor(
		ExecutionYear.readCurrentExecutionYear())));

    }

    private void generateMessageForAcademicOffice() {
	generateMessage(new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_PHD_PROCESSES, this.getProcess()
		.getPhdProgram()));
    }

    private void generateMessageForStudent() {
	generateMessage(new FixedSetGroup(getProcess().getPerson()));
    }

    private void generateMessageForGuiders() {
	for (final PhdParticipant guiding : getProcess().getGuidings()) {
	    if (guiding.isInternal()) {
		generateMessage(new FixedSetGroup(((InternalPhdParticipant) guiding).getPerson()));
	    } else {
		new Message(getSender(), Collections.<ReplyTo> emptyList(), Collections.<Recipient> emptyList(),
			buildMailSubject(), buildMailBody(), Collections.singleton(guiding.getEmail()));
	    }
	}
    }

    private void generateMessage(Group group) {
	new PhdAlertMessage(getProcess(), group.getElements(), getFormattedSubject(), getFormattedBody());
	new Message(getSender(), new Recipient("", group), buildMailSubject(), buildMailBody());
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
