package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.PhdThesisReportFeedbackDocument;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ThesisJuryElement extends ThesisJuryElement_Base {

    static public final Comparator<ThesisJuryElement> COMPARATOR_BY_ELEMENT_ORDER = new Comparator<ThesisJuryElement>() {
	@Override
	public int compare(ThesisJuryElement o1, ThesisJuryElement o2) {
	    return o1.getElementOrder().compareTo(o2.getElementOrder());
	}
    };

    static public ThesisJuryElement createPresident(final PhdThesisProcess process, final PhdThesisJuryElementBean bean) {

	if (process.hasPresidentJuryElement()) {
	    throw new DomainException("error.ThesisJuryElement.president.already.exists");
	}

	final PhdParticipant participant = PhdParticipant.getUpdatedOrCreate(process.getIndividualProgramProcess(), bean);
	final ThesisJuryElement element = new ThesisJuryElement();

	element.checkParticipant(process, participant, true);
	element.setElementOrder(0);
	element.setProcessForPresidentJuryElement(process);
	element.setParticipant(participant);
	element.setReporter(false);

	return element;
    }

    protected ThesisJuryElement() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreationDate(new DateTime());
    }

    protected ThesisJuryElement init(final PhdThesisProcess process, PhdParticipant participant, PhdThesisJuryElementBean bean) {

	check(process, "error.ThesisJuryElement.invalid.process");
	checkParticipant(process, participant, false);

	setElementOrder(generateNextElementOrder(process));
	setProcess(process);
	setParticipant(participant);
	setReporter(bean.isReporter());

	return this;
    }

    private void checkParticipant(final PhdThesisProcess process, final PhdParticipant participant, boolean isPresident) {
	check(participant, "error.ThesisJuryElement.participant.cannot.be.null");

	/*
	 * Can not have more than one jury element for the same process, but
	 * allow to be President and Jury Member at the same time
	 */
	for (final ThesisJuryElement element : participant.getThesisJuryElements()) {
	    if (element.hasProcess() && element.getProcess().equals(process) && !isPresident && !element.isPresident()) {
		throw new DomainException("error.ThesisJuryElement.participant.already.has.jury.element.in.process");
	    }
	}
    }

    private Integer generateNextElementOrder(final PhdThesisProcess process) {
	if (!process.hasAnyThesisJuryElements()) {
	    return Integer.valueOf(1);
	}
	return Integer.valueOf(Collections.max(process.getThesisJuryElements(), ThesisJuryElement.COMPARATOR_BY_ELEMENT_ORDER)
		.getElementOrder().intValue() + 1);
    }

    @Service
    public void delete() {
	checkIfCanBeDeleted();
	disconnect();
	deleteDomainObject();
    }

    private void checkIfCanBeDeleted() {
	if (hasAnyFeedbackDocuments()) {
	    throw new DomainException("error.ThesisJuryElement.has.feedback.documents");
	}
    }

    protected void disconnect() {

	final PhdParticipant participant = getParticipant();
	removeParticipant();
	participant.tryDelete();

	removeProcess();
	removeProcessForPresidentJuryElement();
	removeRootDomainObject();
    }

    public boolean isInternal() {
	return getParticipant().isInternal();
    }

    public String getName() {
	return getParticipant().getName();
    }

    public String getNameWithTitle() {
	return getParticipant().getNameWithTitle();
    }

    public String getNameWithTitleAndRoleOnProcess() {
	StringBuilder stringBuilder = new StringBuilder(getNameWithTitle());
	ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
	if (getProcess().getIndividualProgramProcess().isGuider(getParticipant())) {
	    stringBuilder.append(" (").append(resourceBundle.getString("label.phd.guiding")).append(")");
	}

	if (getProcess().getIndividualProgramProcess().isAssistantGuider(getParticipant())) {
	    stringBuilder.append(" (").append(resourceBundle.getString("label.phd.assistant.guiding")).append(")");
	}

	return stringBuilder.toString();
    }

    public String getQualification() {
	return getParticipant().getQualification();
    }

    public String getCategory() {
	return getParticipant().getCategory();
    }

    public String getWorkLocation() {
	return getParticipant().getWorkLocation();
    }

    public String getInstitution() {
	return getParticipant().getInstitution();
    }

    public String getAddress() {
	return getParticipant().getAddress();
    }

    public String getPhone() {
	return getParticipant().getPhone();
    }

    public String getEmail() {
	return getParticipant().getEmail();
    }

    public String getTitle() {
	return getParticipant().getTitle();
    }

    public boolean isTopElement() {
	return getElementOrder().intValue() == 1;
    }

    public boolean isBottomElement() {
	return getElementOrder().intValue() == getProcess().getThesisJuryElementsCount();
    }

    static public ThesisJuryElement create(final PhdThesisProcess process, final PhdThesisJuryElementBean bean) {
	return new ThesisJuryElement().init(process, PhdParticipant.getUpdatedOrCreate(process.getIndividualProgramProcess(),
		bean), bean);
    }

    public boolean isGuidingOrAssistantGuiding() {
	return getParticipant().isGuidingOrAssistantGuiding();
    }

    public boolean isMainGuiding() {
	return getParticipant().hasProcessForGuiding();
    }

    public boolean isAssistantGuiding() {
	return getParticipant().hasProcessForAssistantGuiding();
    }

    public boolean isFor(final PhdThesisProcess process) {
	return getProcess().equals(process);
    }

    public PhdThesisReportFeedbackDocument getLastFeedbackDocument() {
	return hasAnyFeedbackDocuments() ? Collections.max(getFeedbackDocumentsSet(),
		PhdProgramProcessDocument.COMPARATOR_BY_UPLOAD_TIME) : null;
    }

    public boolean isFor(final Person person) {
	return getParticipant().isFor(person);
    }

    public boolean isDocumentValidated() {
	final PhdThesisReportFeedbackDocument document = getLastFeedbackDocument();
	return document != null && document.isAssignedToProcess();
    }

    public void edit(final PhdThesisJuryElementBean bean) {
	getParticipant().edit(bean);
	setReporter(bean.isReporter());
    }

    public boolean isJuryValidated() {
	return getProcess().isJuryValidated();
    }

    public boolean isPresident() {
	return hasProcessForPresidentJuryElement();
    }

}
