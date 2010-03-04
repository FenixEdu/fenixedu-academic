package net.sourceforge.fenixedu.domain.phd;

import java.util.UUID;

import net.sourceforge.fenixedu.applicationTier.utils.GeneratePasswordBase;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessTypeList;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;
import net.sourceforge.fenixedu.util.StringUtils;

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
    }

    abstract public String getName();

    abstract public String getQualification();

    abstract public String getCategory();

    abstract public String getAddress();

    abstract public String getEmail();

    abstract public String getPhone();

    public String getNameWithTitle() {
	return StringUtils.isEmpty(getTitle()) ? getName() : getTitle() + " " + getName();
    }

    public boolean isFor(Person person) {
	return false;
    }

    public boolean isInternal() {
	return false;
    }

    public void addAccessType(PhdProcessAccessType... types) {
	ensureExternalAccess();
	setAccessTypes(getAccessTypes().addAccessTypes(types));
    }

    private void ensureExternalAccess() {
	if (StringUtils.isEmpty(getAccessHashCode())) {
	    super.setAccessHashCode(UUID.randomUUID().toString());
	    super.setPassword(new GeneratePasswordBase().generatePassword(null));
	}
    }

    public void checkAccessCredentials(String email, String password) {
	if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password) || !hasAccessHashCode()) {
	    throw new DomainException("error.PhdParticipant.credential.not.valid");
	}

	if (!getEmail().equals(email) || !getPassword().equals(password)) {
	    throw new DomainException("error.PhdParticipant.credential.not.valid");
	}

    }

    private boolean hasAccessHashCode() {
	return !StringUtils.isEmpty(getAccessHashCode());
    }

    public void tryDelete() {
	if (canBeDeleted()) {
	    delete();
	}
    }

    protected boolean canBeDeleted() {
	return !(hasAnyThesisJuryElements() || hasProcessForGuiding() || hasProcessForAssistantGuiding() || hasAnyCandidacyFeedbackRequestElements());
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

    private boolean hasAccessHashCode(final String hash) {
	return !StringUtils.isEmpty(getAccessHashCode()) && getAccessHashCode().equals(hash);
    }

    static public PhdParticipant create(final PhdIndividualProgramProcess process, final PhdParticipantBean bean) {
	if (bean.isInternal()) {
	    return new InternalPhdParticipant(process, bean);
	} else {
	    return new ExternalPhdParticipant(process, bean);
	}
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
