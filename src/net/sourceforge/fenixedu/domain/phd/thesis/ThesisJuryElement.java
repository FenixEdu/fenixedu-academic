package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.Collections;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class ThesisJuryElement extends ThesisJuryElement_Base {

    static public final Comparator<ThesisJuryElement> COMPARATOR_BY_ELEMENT_ORDER = new Comparator<ThesisJuryElement>() {
	@Override
	public int compare(ThesisJuryElement o1, ThesisJuryElement o2) {
	    return o1.getElementOrder().compareTo(o2.getElementOrder());
	}
    };

    protected ThesisJuryElement() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreationDate(new DateTime());
    }

    protected ThesisJuryElement init(final PhdThesisProcess process, PhdParticipant participant, PhdThesisJuryElementBean bean) {

	check(process, "error.ThesisJuryElement.invalid.process");
	checkParticipant(process, participant);

	setElementOrder(generateNextElementOrder(process));
	setProcess(process);
	setParticipant(participant);
	setReporter(bean.isReporter());

	return this;
    }

    private void checkParticipant(final PhdThesisProcess process, final PhdParticipant participant) {
	check(participant, "error.ThesisJuryElement.participant.cannot.be.null");

	/*
	 * Actually participant belongs to one process, so he can not
	 * participate in more than one process. But this test remain here
	 * assuring future modifications correctness
	 */
	for (final ThesisJuryElement element : participant.getThesisJuryElements()) {
	    if (element.getProcess().equals(process)) {
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
    void delete() {
	disconnect();
	deleteDomainObject();
    }

    protected void disconnect() {
	
	final PhdParticipant participant = getParticipant();
	removeParticipant();
	participant.tryDelete();
	
	removeProcess();
	removeRootDomainObject();
    }

    public boolean isInternal() {
	return getParticipant().isInternal();
    }

    public String getName() {
	return getParticipant().getName();
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
	final PhdParticipant participant = getOrCreateParticipant(process, bean);
	return new ThesisJuryElement().init(process, participant, bean);
    }

    private static PhdParticipant getOrCreateParticipant(final PhdThesisProcess process, final PhdThesisJuryElementBean bean) {
	final PhdParticipant participant = !bean.hasParticipant() ? PhdParticipant.create(process.getIndividualProgramProcess(),
		bean) : bean.getParticipant();
	return participant;
    }

    static public ThesisJuryElement createPresident(final PhdThesisProcess process, final PhdThesisJuryElementBean bean) {
	
	if (process.hasPresidentJuryElement()) {
	    throw new DomainException("error.ThesisJuryElement.president.already.exists");
	}
	
	final PhdParticipant participant = getOrCreateParticipant(process, bean);
	final ThesisJuryElement element = new ThesisJuryElement();
	
	element.checkParticipant(process, participant);
	element.setElementOrder(0);
	element.setProcessForPresidentJuryElement(process);
	element.setParticipant(participant);
	
	return element;
    }
}
