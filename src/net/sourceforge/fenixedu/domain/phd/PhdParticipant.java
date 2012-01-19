package net.sourceforge.fenixedu.domain.phd;

import static net.sourceforge.fenixedu.util.StringUtils.isEmpty;

import java.util.UUID;

import net.sourceforge.fenixedu.applicationTier.utils.GeneratePasswordBase;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessTypeList;
import net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestElement;
import net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

import org.joda.time.DateTime;

abstract public class PhdParticipant extends PhdParticipant_Base {

    protected PhdParticipant() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setWhenCreated(new DateTime());
	setAccessTypes(PhdProcessAccessTypeList.EMPTY);
    }

    protected void init(PhdIndividualProgramProcess individualProcess) {
	check(individualProcess, "error.PhdParticipant.individualProcess.cannot.be.null");
	super.setIndividualProcess(individualProcess);
    }

    private void delete() {
	disconnect();
	deleteDomainObject();
    }

    protected void disconnect() {
	removeIndividualProcess();
	removeProcessForGuiding();
	removeProcessForAssistantGuiding();
	removeRootDomainObject();
	removeAcceptanceLetter();
    }

    abstract public String getName();

    abstract public String getQualification();

    abstract public String getAddress();

    abstract public String getEmail();

    abstract public String getPhone();
    
    abstract public boolean isTeacher();

    public String getNameWithTitle() {
	return isEmpty(getTitle()) ? getName() : getTitle() + " " + getName();
    }

    public boolean isFor(Person person) {
	return false;
    }

    public boolean isInternal() {
	return false;
    }

    public boolean isFor(PhdIndividualProgramProcess process) {
	return getIndividualProcess().equals(process);
    }

    public void addAccessType(PhdProcessAccessType... types) {
	/*
	 * TODO: this method is not being invoked on every email we sent to
	 * external participants.
	 */
	ensureExternalAccess();
	setAccessTypes(getAccessTypes().addAccessTypes(types));
    }

    public void ensureExternalAccess() {
	if (isEmpty(getAccessHashCode())) {
	    super.setAccessHashCode(UUID.randomUUID().toString());
	    super.setPassword(new GeneratePasswordBase().generatePassword(null));
	}
    }

    public void checkAccessCredentials(String email, String password) {
	if (isEmpty(email) || isEmpty(password) || !hasAccessHashCode()) {
	    throw new DomainException("error.PhdParticipant.credential.not.valid");
	}

	if (!getEmail().equals(email) || !getPassword().equals(password)) {
	    throw new DomainException("error.PhdParticipant.credential.not.valid");
	}

    }

    private boolean hasAccessHashCode() {
	return !isEmpty(getAccessHashCode());
    }

    public void tryDelete() {
	if (canBeDeleted()) {
	    delete();
	}
    }

    protected boolean canBeDeleted() {
	return !(hasAnyThesisJuryElements() || hasProcessForGuiding() || hasProcessForAssistantGuiding()
		|| hasAnyCandidacyFeedbackRequestElements() || isParticipantCoordinator());
    }

    private boolean isParticipantCoordinator() {
	if (!isInternal()) {
	    return false;
	}

	InternalPhdParticipant internalParticipant = (InternalPhdParticipant) this;
	return getIndividualProcess().getPhdProgram().isCoordinatorFor(internalParticipant.getPerson(),
		getIndividualProcess().getExecutionYear());
    }

    public boolean isGuidingOrAssistantGuiding() {
	return hasProcessForGuiding() || hasProcessForAssistantGuiding();
    }

    /*
     * Actually each participant belongs to one process, so it will have only
     * thesis jury element, but assuming that we can share participants we have
     * several thesis jury elements
     */
    public ThesisJuryElement getThesisJuryElement(final PhdThesisProcess process) {
	for (final ThesisJuryElement element : getThesisJuryElementsSet()) {
	    if (element.isFor(process)) {
		return element;
	    }
	}
	return null;
    }

    public PhdCandidacyFeedbackRequestElement getPhdCandidacyFeedbackRequestElement(
	    final PhdCandidacyFeedbackRequestProcess process) {
	for (final PhdCandidacyFeedbackRequestElement element : getCandidacyFeedbackRequestElements()) {
	    if (element.isFor(process)) {
		return element;
	    }
	}
	return null;
    }

    private boolean hasAccessHashCode(final String hash) {
	return !isEmpty(getAccessHashCode()) && getAccessHashCode().equals(hash);
    }

    static public PhdParticipant getUpdatedOrCreate(final PhdIndividualProgramProcess process, final PhdParticipantBean bean) {

	if (bean.hasParticipant()) {
	    bean.getParticipant().updateTitleIfNecessary(bean);
	    return bean.getParticipant();
	}

	if (bean.isInternal()) {
	    return new InternalPhdParticipant(process, bean);
	} else {
	    return new ExternalPhdParticipant(process, bean);
	}
    }

    private void updateTitleIfNecessary(final PhdParticipantBean bean) {
	if (isEmpty(getTitle())) {
	    setTitle(bean.getTitle());
	}
    }

    public void edit(final PhdParticipantBean bean) {
	setTitle(bean.getTitle());
	setCategory(bean.getCategory());
	setWorkLocation(bean.getWorkLocation());
	setInstitution(bean.getInstitution());
    }

    static public PhdParticipant readByAccessHashCode(final String hash) {
	for (final PhdParticipant participant : RootDomainObject.getInstance().getPhdParticipants()) {
	    if (participant.hasAccessHashCode(hash)) {
		return participant;
	    }
	}

	return null;
    }

}
